package fxc.dev.common.widgets.dialog.alert

/**
 * Created by Tam Le on 01/04/2022.
 */

data class TAlertAction(
    var title: String,
    var style: TAlertActionStyle,
    var onClick: ((String?) -> Unit)? = null
)