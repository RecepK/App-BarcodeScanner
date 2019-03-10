package com.kurban.barcodescanner.components

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.kurban.barcodescanner.BuildConfig
import com.kurban.barcodescanner.R

class LogHelper(private val context: Context) {

    fun toast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun toastLong(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }


    fun log(msg: String) {
        if (BuildConfig.DEBUG)
            Log.d("RKRK", context.getString(R.string.app_name) + ":" + msg);
    }
}
