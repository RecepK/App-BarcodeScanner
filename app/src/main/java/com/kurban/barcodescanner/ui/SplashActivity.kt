package com.kurban.barcodescanner.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.kurban.barcodescanner.BuildConfig
import com.kurban.barcodescanner.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private var lock: Boolean = false

    companion object {
        const val SPLASH_TIME: Long = 1000 * 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        tvVersion.text = getString(R.string.app_name) + "\n" + BuildConfig.VERSION_NAME

        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.INTERNET
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        Handler().postDelayed({
                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                            finish()
                        }, SPLASH_TIME)
                    }

                    if (report.isAnyPermissionPermanentlyDenied) {
                        if (!lock) {
                            Toast.makeText(
                                this@SplashActivity,
                                "Uygulamaya gerekli izinleri veriniz.",
                                Toast.LENGTH_SHORT
                            ).show()
                            lock = true
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .onSameThread()
            .check()
    }
}