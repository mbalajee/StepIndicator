package com.apps.animation.stepIndicator.step

import android.content.Context
import android.widget.LinearLayout
import kotlin.math.abs


class StepViews {
    private val views = mutableListOf<StepView>()
    private var stepToAnimate = -1
    private val stepAnimation = 250L
    private var animationDurationPerStep: Long? = null

    fun create(context: Context, width: Int, height: Int) = StepView(context).apply {
        layoutParams = LinearLayout.LayoutParams(width, height)
    }.also { views.add(it) }

    fun clear() {
        views.clear()
    }

    fun stepUp(onAnimationEnd: () -> Unit = {}) {
        if (stepToAnimate >= views.size - 1) return
        views[++stepToAnimate].apply {
            animDuration = animationDurationPerStep ?: stepAnimation
            this.stepUp(onAnimationEnd)
        }
    }

    fun stepDown(onAnimationEnd: () -> Unit = {}) {
        if (stepToAnimate < 0) return
        views[stepToAnimate--].apply {
            animDuration = animationDurationPerStep ?: stepAnimation
            this.stepDown(onAnimationEnd)
        }
    }

    fun stepTo(step: Int) {

        if (step < -1 || step > views.size - 1) return

        if (animationDurationPerStep == null) {
            animationDurationPerStep = stepAnimation / abs(stepToAnimate - step).let { if (it == 0) 1 else it }
        }
        when {
            step == stepToAnimate -> {
                animationDurationPerStep = null
                return
            }
            step > stepToAnimate -> stepUp { stepTo(step) }
            step < stepToAnimate -> stepDown { stepTo(step) }
        }
    }
}