package fxc.dev.app.ui.demo.list

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import fxc.dev.app.R
import fxc.dev.app.adapter.DemoAdapter
import fxc.dev.app.databinding.DemoListFragmentBinding
import fxc.dev.app.navigator.Navigator
import fxc.dev.app.state.FetchApiState
import fxc.dev.app.state.SimpleHandleState
import fxc.dev.app.ui.demo.room.DemoRoomFragment
import fxc.dev.base.core.BaseFragment
import fxc.dev.common.extension.flow.collectInViewLifecycle
import fxc.dev.common.extension.resourceColor
import fxc.dev.common.extension.safeClickListener
import fxc.dev.common.extension.setBackground
import fxc.dev.common.extension.showShortToast
import fxc.dev.core.domain.model.User
import org.koin.core.component.inject

/**
 *
 * Created by tamle on 09/06/2023
 *
 */

class DemoListFragment: BaseFragment<DemoListVM, DemoListFragmentBinding>() {

    override val viewModel: DemoListVM by viewModels()

    private val navigator: Navigator by inject()

    private var page = 0

    private val demoAdapter by lazy {
        DemoAdapter (
            context = requireContext(),
            onClickItem = ::onUserClick,
            onLoadMore = ::onLoadMore
        )
    }

    override fun getVB(inflater: LayoutInflater) = DemoListFragmentBinding.inflate(inflater)

    override fun initialize(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            refreshListData()
        }
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

        binding.btRoom.safeClickListener {
            navigator.navigateToRoomDemo(this, DemoRoomFragment.LIST)
        }

        loadBannerAds(binding.flAdView, getString(R.string.ads_banner_id))
    }

    override fun bindViewModel() {
        viewModel.userState
            .collectInViewLifecycle(this) {
                when (it) {
                    FetchApiState.Init -> {}
                    FetchApiState.Start -> {
                        showLoading(true)
                    }

                    is FetchApiState.Failure -> {
                        showLoading(false)
                        showShortToast("Load data failed")
                    }

                    is FetchApiState.Success -> {
                        showLoading(false)
                        demoAdapter.submitList(it.data)
                        demoAdapter.setEnableLoadMore(it.enableLoadMore)
                    }
                }
            }

        viewModel.handleUserState
            .collectInViewLifecycle(this) {
                when (it) {
                    SimpleHandleState.Failure -> {
                        showShortToast("Save user failed")
                    }
                    SimpleHandleState.Success -> {
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
}