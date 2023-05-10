package fxc.dev.base.extensions

import android.app.Activity
import fxc.dev.base.R
import fxc.dev.base.constants.Transition

/**
 *
 * Created by tamle on 19/04/2023
 *
 */

/**
 * Transition for open new activity
 *
 * @param transition
 */
fun Activity.applyTransitionIn(transition: Transition) {
    when (transition) {
        Transition.SLIDE_RIGHT -> overridePendingTransition(
            R.anim.slide_left_in,
            R.anim.short_slide_right_out
        )
        Transition.SLIDE_LEFT -> overridePendingTransition(
            R.anim.slide_right_in,
            R.anim.short_slide_left_out
        )
        Transition.SLIDE_DOWN -> overridePendingTransition(
            R.anim.slide_top_in,
            R.anim.slide_none
        )
        Transition.SLIDE_UP -> overridePendingTransition(
            R.anim.slide_bottom_in,
            R.anim.slide_none
        )
        else -> Transition.NONE
    }
}

fun Activity.applyTransitionOut(transition: Transition) {
    when (transition) {
        Transition.SLIDE_RIGHT -> overridePendingTransition(
            R.anim.short_slide_right_in,
            R.anim.slide_left_out
        )
        Transition.SLIDE_LEFT -> overridePendingTransition(
            R.anim.short_slide_left_in,
            R.anim.slide_right_out
        )
        Transition.SLIDE_DOWN -> overridePendingTransition(
            R.anim.slide_none,
            R.anim.slide_top_out
        )
        Transition.SLIDE_UP -> overridePendingTransition(
            R.anim.slide_none,
            R.anim.slide_bottom_out
        )
        else -> Transition.NONE
    }
}