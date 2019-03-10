package com.kurban.barcodescanner.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.kurban.barcodescanner.R
import com.kurban.barcodescanner.di.DependencyInjectorImpl
import com.kurban.barcodescanner.mvp.MainContract
import com.kurban.barcodescanner.mvp.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.result_layout.*

class MainActivity : AppCompatActivity(), MainContract.MvpView {

    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenter(this, DependencyInjectorImpl(this)).init()
        presenter.scan();
    }


    override fun onResume() {
        super.onResume()
        if (presenter != null)
            presenter.resetResult()
    }

    override fun initUI() {
        buttonReset.setOnClickListener { presenter.resetResult() }
    }

    override fun layoutControl(result: String, value: Boolean) {
        tvResult.text = result

        if (value) resultLayout.visibility = View.VISIBLE
        else resultLayout.visibility = View.INVISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu0 -> presenter.openMarket(false)
            R.id.menu1 -> presenter.openMarket(true)
            R.id.menu2 -> finish()
        }
        return true
    }
}
