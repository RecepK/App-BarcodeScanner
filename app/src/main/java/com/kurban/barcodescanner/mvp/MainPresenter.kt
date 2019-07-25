package com.kurban.barcodescanner.mvp

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.view.SurfaceHolder
import android.webkit.URLUtil
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.kurban.barcodescanner.components.LogHelper
import com.kurban.barcodescanner.di.DependencyInjector
import com.kurban.barcodescanner.ui.MainActivity
import com.kurban.barcodescanner.ui.SplashActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainPresenter(
    private val view: MainContract.MvpView,
    private var dependencyInjector: DependencyInjector
) : MainContract.Presenter {

    private var lock: Boolean = true;
    private lateinit var activity: MainActivity
    private lateinit var logHelper: LogHelper

    override fun init(): MainPresenter {
        logHelper = dependencyInjector.logHelper()
        activity = view as MainActivity
        view.initUI()
        return this
    }

    override fun scan() {
        val barcodeDetector = BarcodeDetector
            .Builder(activity)
            .setBarcodeFormats(Barcode.QR_CODE)
            .build()

        val cameraSource = CameraSource
            .Builder(activity, barcodeDetector)
            .setAutoFocusEnabled(true)
            .build()

        activity.surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
                if (ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) return

                try {
                    cameraSource.start(surfaceHolder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {}

            override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
                cameraSource.stop()
            }
        })

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val qrCodes = detections.detectedItems
                if (qrCodes.size() != 0) {
                    activity.runOnUiThread { setResultLayout(qrCodes.valueAt(0).displayValue) }
                }
            }
        })
    }

    override fun resetResult() {
        view.layoutControl("", false)
        lock = true
    }

    private fun setResultLayout(result: String) {
        if (result.isNotEmpty() && lock) {
            view.layoutControl("", false)
            openWebURL(result)
            lock = false
        }
    }

    private fun openWebURL(url: String) {
        var url = url
        if (!URLUtil.isNetworkUrl(url)) {
            logHelper.toast("Link formatında değil, Google ile arama yapılıyor..")
            view.layoutControl(url, true)
            url = "https://www.google.com/search?q=$url"
            return
        } else {
            logHelper.toast(url)
        }


        resetResult()

        Handler().postDelayed({
            val browse = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            (view as Context).startActivity(browse)
            activity.finish()
        }, SplashActivity.SPLASH_TIME)
    }

    // Market
    override fun openMarket(value: Boolean) {
        val appPackageUrl =
            if (value) {
                "developer?id=" + "Recep+KURBAN"
            } else {
                "details?id=" + activity.packageName
            }

        try {
            activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://$appPackageUrl")))
        } catch (e: ActivityNotFoundException) {
            activity.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/$appPackageUrl"))
            )
        }
    }
}
