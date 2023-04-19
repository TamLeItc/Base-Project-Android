package fxc.dev.common.extension

import android.animation.ObjectAnimator
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.transition.Slide
import androidx.transition.TransitionManager

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.setSize(@DimenRes height: Int? = null, @DimenRes width: Int? = null) {
    val params: ViewGroup.LayoutParams = this.layoutParams
    if (height != null) {
        params.height = height
    }
    if (width != null) {
        params.width = width
    }
    this.layoutParams = params
}

fun ViewGroup.slideFromLeft() {
    val enterTransition = Slide(Gravity.START)
    TransitionManager.beginDelayedTransition(this, enterTransition)
}

fun ViewGroup.slideFromRight() {
    val enterTransition = Slide(Gravity.END)
    TransitionManager.beginDelayedTransition(this, enterTransition)
}

fun View.rotation(degree: Float) {
    val f = this.rotation
    val animator = ObjectAnimator.ofFloat(this, "rotation", f, degree)
    animator.start()
}

/**
 * This function to click view for safe
 */
fun View.safeClickListener(safeClickListener: (view: View) -> Unit) {
    this.setOnClickListener {
        if (!SingleClick.isBlockingClick()) {
            safeClickListener(it)
        }
    }
}

fun View.setBackground(startColor: Int, endColor: Int, cornerRadius: Float) {
    val gradientDrawable = GradientDrawable(
        GradientDrawable.Orientation.TOP_BOTTOM,
        intArrayOf(startColor, endColor)
    );
    gradientDrawable.cornerRadius = cornerRadius;
    background = gradientDrawable
}

object SingleClick {
    private const val MIN_CLICK_INTERVAL = 200
    private var sLastClickTime: Long = 0

    fun isBlockingClick(): Boolean = isBlockingClick(MIN_CLICK_INTERVAL.toLong())

    private fun isBlockingClick(minClickInterval: Long): Boolean {
        val isBlocking: Boolean
        val currentTime = System.currentTimeMillis()
        isBlocking = Math.abs(currentTime - sLastClickTime) < minClickInterval
        if (!isBlocking) {
            sLastClickTime = currentTime
        }
        return isBlocking
    }
}

