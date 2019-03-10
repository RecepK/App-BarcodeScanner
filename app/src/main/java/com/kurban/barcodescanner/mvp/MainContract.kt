package com.kurban.barcodescanner.mvp

interface MainContract {

    interface MvpView {

        fun initUI()

        fun layoutControl(result: String, value: Boolean)
    }

    interface Presenter {

        fun init(): MainPresenter

        fun scan()

        fun resetResult()

        fun openMarket(value: Boolean)
    }
}