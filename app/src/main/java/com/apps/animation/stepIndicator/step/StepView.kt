package com.apps.animation.stepIndicator.step

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout


class StepView(context: Context): FrameLayout(context) {

    var colorNotStarted = Color.YELLOW
    var colorCompleted = Color.BLUE

    var animDuration = 200L

    private val viewCompleted: View

    init {
        addView(
            View(context).apply {
                layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                setBackgroundColor(colorNotStarted)
            }
        )
        addView(
            View(context).apply {
                layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                setBackgroundColor(colorCompleted)
                scaleX = 0f
            }.also {
                viewCompleted = it
            }
        )
    }

    private var animating = false

    fun stepUp(onAnimationEnd: () -> Unit = {}) {
        if (animating) return

        viewCompleted.scaleX = 1f

        animating = true

        viewCompleted.startAnimation(
            ScaleAnimation(0f, 1f, 1f, 1f).apply {
                fillAfter = true
                duration = animDuration
                setAnimationListener(object : AnimationListener {
                    override fun onAnimationEnd(animation: Animation) {
                        animating = false
                        onAnimationEnd()
                    }
                })
            }
        )
    }

    fun stepDown(onAnimationEnd: () -> Unit = {}) {
        if (animating) return

        animating = true

        viewCompleted.startAnimation(
            ScaleAnimation(1f, 0f, 1f, 1f).apply {
                fillAfter = true
                duration = animDuration
                setAnimationListener(object : AnimationListener {
                    override fun onAnimationEnd(animation: Animation) {
                        animating = false
                        onAnimationEnd()
                    }
                })
            }
        )
    }

    interface AnimationListener: Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {}
        override fun onAnimationEnd(animation: Animation) {}
        override fun onAnimationRepeat(animation: Animation) {}
    }
}