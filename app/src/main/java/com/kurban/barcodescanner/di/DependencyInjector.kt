package com.kurban.barcodescanner.di

import com.kurban.barcodescanner.components.LogHelper

interface DependencyInjector {

    fun logHelper(): LogHelper
}