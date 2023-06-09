package fxc.dev.app.ui.demo_room

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import fxc.dev.app.R
import fxc.dev.app.adapter.DemoAdapter
import fxc.dev.app.databinding.ActivityDemoRoomBinding
import fxc.dev.app.navigator.Navigator
import fxc.dev.base.constants.Transition
import fxc.dev.base.core.BaseActivity
import fxc.dev.common.extension.resourceColor
import fxc.dev.common.extension.safeClickListener
import fxc.dev.common.extension.setBackground
import fxc.dev.common.extension.showShortToast
import fxc.dev.core.domain.model.User
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject

/**
 *
 * Created by tamle on 08/06/2023
 *
 */

class DemoRoomActivity :
    BaseActivity<DemoRoomVM, ActivityDemoRoomBinding>(R.layout.activity_demo_room) {
    override val viewModel: DemoRoomVM by viewModels()
    override val transition: Transition
        get() = Transition.SLIDE_LEFT

    private val navigator: Navigator by inject()

    private val demoAdapter = DemoAdapter(
        context = this,
        onClickItem = ::onUserClick
    )

    override fun getVB(inflater: LayoutInflater) = ActivityDemoRoomBinding.inflate(inflater)

    override fun initialize(savedInstanceState: Bundle?) {

    }

    override fun initViews() {
        binding.root.setBackground(
            startColor = resourceColor(R.color.backgroundSecondary),
            endColor = resourceColor(R.color.backgroundPrimary),
            cornerRadius = 0f
        )

        binding.rvUser.run {
            adapter = demoAdapter
        }
    }

    override fun addListenerForViews() {
        binding.btAddMore.safeClickListener {
            navigator.navigateToDemo(this)
        }
    }

    override fun bindViewModel() {
        viewModel.userState
            .onEach {
                when (it) {
                    LoadUserState.Init -> {}
                    LoadUserState.Failure -> {}
                    is LoadUserState.Success -> {
                        demoAdapter.submitList(it.data)
                    }
                }
            }.launchIn(lifecycleScope)

        viewModel.handleUserState
            .onEach {
                when(it){
                    HandleUserState.Failure -> {
                        showShortToast("Delete user failed")
                    }
                    HandleUserState.Success -> {
                        showShortToast("Delete user successfully")
                    }
                }
            }.launchIn(lifecycleScope)
    }

    private fun onUserClick(user: User) {
        viewModel.deleteUser(user)
    }

    companion object {
        fun getIntent(activity: Activity): Intent {
            return Intent(activity, DemoRoomActivity::class.java)
        }
    }
}