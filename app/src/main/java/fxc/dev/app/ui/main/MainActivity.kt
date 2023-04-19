package fxc.dev.app.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import fxc.dev.app.R
import fxc.dev.base.core.BaseActivity
import fxc.dev.app.databinding.ActivityMainBinding
import fxc.dev.app.navigator.Navigator
import fxc.dev.common.constants.Transaction
import org.koin.core.component.inject

/**
 *
 * Created by tamle on 14/04/2023
 *
 */

class MainActivity : BaseActivity<MainVM, ActivityMainBinding>(R.layout.activity_main) {
    override val viewModel: MainVM by viewModels()
    override val transaction: Transaction
        get() = Transaction.NONE

    override fun getVB(inflater: LayoutInflater) = ActivityMainBinding.inflate(inflater)

    override fun initialize(savedInstanceState: Bundle?) {
        showLoading(true)
    }

    override fun initViews() {

    }

    override fun addListenerForViews() {

    }

    override fun bindViewModel() {

    }

    companion object {
        fun getIntent(activity: Activity): Intent {
            return Intent(activity, MainActivity::class.java)
        }
    }
}