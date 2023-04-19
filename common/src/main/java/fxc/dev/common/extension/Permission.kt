package tam.le.baseproject.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.markodevcic.peko.Peko
import com.markodevcic.peko.PermissionResult
import fxc.dev.common.R
import fxc.dev.common.widgets.dialog.alert.TAlertAction
import fxc.dev.common.widgets.dialog.alert.TAlertActionStyle
import fxc.dev.common.widgets.dialog.alert.TAlertDialog

/**
 * Created by Tam Le on 13/01/2022.
 */

suspend fun FragmentActivity.requestPermission(
    vararg permissions: String,
    listener: PermissionListener? = null
) {
    if (Peko.isRequestInProgress()) {
        return
    }
    val result = Peko.requestPermissionsAsync(this, *permissions)
    when (result) {
        is PermissionResult.Granted -> {
            listener?.onPermissionGrated(result.grantedPermissions)
        }
        is PermissionResult.Denied.JustDenied -> {
            listener?.onPermissionDenied(result.deniedPermissions)
            showDialogRequirePermission(this)
        }
        is PermissionResult.Denied.NeedsRationale -> {
            listener?.onPermissionDenied(result.deniedPermissions)
            showDialogRequirePermission(this)
        }
        is PermissionResult.Denied.DeniedPermanently -> {
            listener?.onPermissionDenied(result.deniedPermissions)
            showDialogRequirePermission(this)
        }
        PermissionResult.Cancelled -> {
            listener?.onPermissionCancel()
        }
    }
}

suspend fun Fragment.requestPermission(
    vararg permissions: String,
    listener: PermissionListener? = null
) {
    if (Peko.isRequestInProgress()) {
        return
    }
    val result = Peko.requestPermissionsAsync(requireContext(), *permissions)
    when (result) {
        is PermissionResult.Granted -> {
            listener?.onPermissionGrated(result.grantedPermissions)
        }
        is PermissionResult.Denied.JustDenied -> {
            listener?.onPermissionDenied(result.deniedPermissions)
            showDialogRequirePermission(requireActivity())
        }
        is PermissionResult.Denied.NeedsRationale -> {
            listener?.onPermissionDenied(result.deniedPermissions)
            showDialogRequirePermission(requireActivity())
        }
        is PermissionResult.Denied.DeniedPermanently -> {
            listener?.onPermissionDenied(result.deniedPermissions)
            showDialogRequirePermission(requireActivity())
        }
        PermissionResult.Cancelled -> {
            listener?.onPermissionCancel()
        }
    }
}

private fun showDialogRequirePermission(activity: FragmentActivity) {
    TAlertDialog.Builder(activity)
        .setTitle(R.string.permission_failed_title)
        .setMessage(R.string.permission_failed_message)
        .setLeftAction(TAlertAction(activity.getString(R.string.cancel), TAlertActionStyle.CANCEL))
        .setRightAction(TAlertAction(activity.getString(R.string.ok), TAlertActionStyle.CONFIRM) {

        })
        .build().show(activity.supportFragmentManager)
}

abstract class PermissionListener {
    open fun onPermissionGrated(grantedPermissions: Collection<String>) {}
    open fun onPermissionDenied(deniedPermissions: Collection<String>) {}
    open fun onPermissionCancel() {}
}