package fxc.dev.app.ui.demo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import fxc.dev.app.R
import fxc.dev.app.databinding.DemoActivityBinding
import fxc.dev.base.constants.Transition
import fxc.dev.base.core.BaseActivity
import fxc.dev.common.extension.flow.collectIn
import fxc.dev.common.extension.nav.safeNavigate
import fxc.dev.common.extension.safeClickListener
import fxc.dev.common.extension.nav.setupWithNavController
import kotlinx.coroutines.flow.MutableStateFlow

/**
 *
 * Created by tamle on 07/06/2023
 *
 */

class DemoActivity : BaseActivity<DemoVM, DemoActivityBinding>(R.layout.demo_activity) {
    override val viewModel: DemoVM by viewModels()
    override val transition: Transition
        get() = Transition.SLIDE_LEFT

    private var currentNavController: MutableStateFlow<NavController>? = null

    override fun getVB(inflater: LayoutInflater): DemoActivityBinding =
        DemoActivityBinding.inflate(inflater)

    override fun initialize(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    override fun initViews() {

    }

    override fun addListenerForViews() {

    }

    override fun bindViewModel() {

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    private fun setupBottomNavigationBar() {
        Log.d("ttt", "Start setup")
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

        val mainDestinations = listOf(
            R.navigation.demo_list_nav,
            R.navigation.demo_grid_nav
        )

        currentNavController = bottomNavigationView.setupWithNavController(
            navGraphIds = mainDestinations,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        currentNavController!!.collectIn(this) {
            onCurrentDestinationChange(it.currentDestination?.id)
            it.addOnDestinationChangedListener { _, destination, _ ->
                onCurrentDestinationChange(destination.id)
            }
        }
    }

    private fun onCurrentDestinationChange(id: Int?) {
        if (id != null) {
            val mainDestinations = listOf(
                R.id.frg_grid,
                R.id.frg_list
            )
            onBackPressedCallback.isEnabled = mainDestinations.contains(id)
        }
    }

    companion object {
        fun getIntent(activity: Activity): Intent {
            return Intent(activity, DemoActivity::class.java)
        }
    }
}