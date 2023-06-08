package fxc.dev.app.ui.demo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import fxc.dev.app.R
import fxc.dev.app.adapter.DemoAdapter
import fxc.dev.app.databinding.ActivityDemoBinding
import fxc.dev.app.navigator.Navigator
import fxc.dev.app.ui.demo_room.HandleUserState
import fxc.dev.base.constants.Transition
import fxc.dev.base.core.BaseActivity
import fxc.dev.common.extension.flow.collectIn
import fxc.dev.common.extension.resourceColor
import fxc.dev.common.extension.setBackground
import fxc.dev.common.extension.showShortToast
import fxc.dev.core.domain.model.User
import org.koin.core.component.inject

/**
 *
 * Created by tamle on 07/06/2023
 *
 */

class DemoActivity : BaseActivity<DemoVM, ActivityDemoBinding>(R.layout.activity_demo) {
    override val viewModel: DemoVM by viewModels()
    override val transition: Transition
        get() = Transition.SLIDE_LEFT

    private val navigator: Navigator by inject()

    private var page = 0

    private val demoAdapter = DemoAdapter(
        context = this,
        onClickItem = ::onUserClick,
        onLoadMore = ::onLoadMore
    )

    override fun getVB(inflater: LayoutInflater): ActivityDemoBinding =
        ActivityDemoBinding.inflate(inflater)

    override fun initialize(savedInstanceState: Bundle?) {
        refreshListData()
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
        binding.refreshLayout.setOnRefreshListener {
            refreshListData()
        }
    }

    override fun bindViewModel() {
        viewModel.userState
            .collectIn(this) {
                when (it) {
                    FetchUserState.Init -> {}
                    FetchUserState.Start -> {
                        showLoading(true)
                    }

                    FetchUserState.Failure -> {
                        showLoading(false)
                    }

                    is FetchUserState.Success -> {
                        showLoading(false)
                        demoAdapter.submitList(it.data)
                        demoAdapter.setEnableLoadMore(it.enableLoadMore)
                    }
                }
            }

        viewModel.handleUserState
            .collectIn(this) {
                when (it) {
                    HandleUserState.Failure -> {
                        showShortToast("Save user failed")
                    }
                    HandleUserState.Success -> {
                        showShortToast("Save user successfully")
                    }
                }
            }
    }

    private fun onUserClick(user: User) {
        viewModel.saveUser(user)
    }

    private fun onLoadMore() {
        page += 1
        viewModel.fetchUsers(page)
    }

    private fun refreshListData() {
        page = 0
        viewModel.fetchUsers(page)
        binding.refreshLayout.isRefreshing = false
    }

    companion object {
        fun getIntent(activity: Activity): Intent {
            return Intent(activity, DemoActivity::class.java)
        }
    }
}