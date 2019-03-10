package com.kurban.barcodescanner.di

import android.support.v7.app.AppCompatActivity
import com.kurban.barcodescanner.components.LogHelper

class DependencyInjectorImpl(private val activity: AppCompatActivity) : DependencyInjector {

    override fun logHelper(): LogHelper {
        return LogHelper(activity)
    }
}