package fxc.dev.base.core

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.markodevcic.peko.ActivityRotatingException
import fxc.dev.common.bus.BusProvider
import fxc.dev.common.utils.PrefUtils
import fxc.dev.common.wrapper.AppContextWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

abstract class BaseFragment<VM : BaseVM, VB : ViewBinding>
protected constructor() : Fragment(), CoroutineScope, BaseComponent<VB>, KoinComponent {

    val mainJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + mainJob

    abstract val viewModel: VM

    protected val bus: BusProvider by inject()
    protected val appContextWrapper: AppContextWrapper by inject()

    protected lateinit var binding: VB

    override fun onAttach(context: Context) {
        super.onAttach(appContextWrapper.wrap(context, PrefUtils.language));
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = getVB(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize(savedInstanceState)
        initViews()
        addListenerForViews()
        bindViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        bus.unregister(this)
        if (activity?.isChangingConfigurations == true) {
            mainJob.completeExceptionally(ActivityRotatingException())
        } else {
            mainJob.cancel()
        }
    }

    override fun showLoading(isShow: Boolean) {
        (activity as? BaseActivity<*, *>)?.run {
            showLoading(isShow)
        }
    }
}