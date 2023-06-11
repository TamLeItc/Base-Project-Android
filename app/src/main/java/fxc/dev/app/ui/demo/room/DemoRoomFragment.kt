package fxc.dev.app.ui.demo.room

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import fxc.dev.app.R
import fxc.dev.app.adapter.DemoAdapter
import fxc.dev.app.databinding.DemoRoomFragmentBinding
import fxc.dev.app.navigator.Navigator
import fxc.dev.app.state.LoadUserState
import fxc.dev.app.state.SimpleHandleState
import fxc.dev.base.core.BaseFragment
import fxc.dev.common.extension.resourceColor
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

class DemoRoomFragment :
    BaseFragment<DemoRoomVM, DemoRoomFragmentBinding>() {
    override val viewModel: DemoRoomVM by viewModels()

    private val navigator: Navigator by inject()

    private var type = LIST

    private val demoAdapter by lazy {
        DemoAdapter (
            context = requireContext(),
            onClickItem = ::onUserClick
        )
    }

    override fun getVB(inflater: LayoutInflater) = DemoRoomFragmentBinding.inflate(inflater)

    override fun initialize(savedInstanceState: Bundle?) {
        type = arguments?.getInt(TYPE) ?: 0
    }

    override fun initViews() {
        binding.root.setBackground(
            startColor = resourceColor(R.color.backgroundSecondary),
            endColor = resourceColor(R.color.backgroundPrimary),
            cornerRadius = 0f
        )

        binding.rvUser.run {
            if (type == GRID) {
                layoutManager = GridLayoutManager(requireContext(), 2)
            }
            adapter = demoAdapter
        }

        loadBannerAds(binding.flAdView, getString(R.string.ads_banner_id))
    }

    override fun addListenerForViews() {

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
                    SimpleHandleState.Failure -> {
                        showShortToast("Delete user failed")
                    }
                    SimpleHandleState.Success -> {
                        showShortToast("Delete user successfully")
                    }
                }
            }.launchIn(lifecycleScope)
    }

    private fun onUserClick(user: User) {
        viewModel.deleteUser(user)
    }

    companion object {
        const val TYPE = "type"
        const val GRID = 1
        const val LIST = 0
    }
}