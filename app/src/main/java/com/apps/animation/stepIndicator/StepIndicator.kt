package com.apps.animation

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import com.apps.animation.stepIndicator.*
import com.apps.animation.stepIndicator.indicator.IndicatorsViews
import com.apps.animation.stepIndicator.step.StepViews

class StepIndicator(context: Context, attrs: AttributeSet? = null): LinearLayout(context, attrs) {

    private val steps = 5
    private val indicatorSize = 60
    private val stepHeight = 10

    private val indicatorViews = IndicatorsViews(indicatorSize)
    private val stepViews = StepViews()

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        post {
            addViews()
            indicatorViews.setState()
        }
    }

    fun stepUp() {
        indicatorViews.stepUp()
        stepViews.stepUp()
    }

    fun stepDown() {
        indicatorViews.stepDown()
        stepViews.stepDown()
    }

    fun stepTo(step: Int) {
        indicatorViews.stepTo(step - 1)
        stepViews.stepTo(step - 2)
    }

    private fun addViews() {

        removeAllViews()
        indicatorViews.clear()
        stepViews.clear()

        val viewWidth = width - paddingStart - paddingEnd
        val stepWidth = (viewWidth / steps) - indicatorSize

        repeat(steps) {
            addView(indicatorViews.create(context))

            if (it == steps - 1) return@repeat

            addView(stepViews.create(context, stepWidth, stepHeight))
        }
    }
}


