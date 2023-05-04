package fxc.dev.base.extensions

import android.app.Activity
import fxc.dev.base.R
import fxc.dev.base.constants.Transaction

/**
 *
 * Created by tamle on 19/04/2023
 *
 */

/**
 * Transition for open new activity
 *
 * @param transaction
 */
fun Activity.applyTransitionIn(transaction: Transaction) {
    when (transaction) {
        Transaction.SLIDE_RIGHT -> overridePendingTransition(
            R.anim.slide_left_in,
            R.anim.short_slide_right_out
        )
        Transaction.SLIDE_LEFT -> overridePendingTransition(
            R.anim.slide_right_in,
            R.anim.short_slide_left_out
        )
        Transaction.SLIDE_DOWN -> overridePendingTransition(
            R.anim.slide_top_in,
            R.anim.slide_none
        )
        Transaction.SLIDE_UP -> overridePendingTransition(
            R.anim.slide_bottom_in,
            R.anim.slide_none
        )
        else -> Transaction.NONE
    }
}

fun Activity.applyTransitionOut(transaction: Transaction) {
    when (transaction) {
        Transaction.SLIDE_RIGHT -> overridePendingTransition(
            R.anim.short_slide_right_in,
            R.anim.slide_left_out
        )
        Transaction.SLIDE_LEFT -> overridePendingTransition(
            R.anim.short_slide_left_in,
            R.anim.slide_right_out
        )
        Transaction.SLIDE_DOWN -> overridePendingTransition(
            R.anim.slide_none,
            R.anim.slide_top_out
        )
        Transaction.SLIDE_UP -> overridePendingTransition(
            R.anim.slide_none,
            R.anim.slide_bottom_out
        )
        else -> Transaction.NONE
    }
}