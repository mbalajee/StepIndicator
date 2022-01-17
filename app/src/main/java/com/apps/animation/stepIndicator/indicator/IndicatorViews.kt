package com.apps.animation.stepIndicator.indicator

import android.content.Context
import android.widget.LinearLayout

class IndicatorsViews(private val indicatorSize: Int) {

    private val indicatorViews = mutableListOf<IndicatorView>()
    private var currentIndicator = 0

    fun create(context: Context): IndicatorView = IndicatorView(context).apply {
        layoutParams = LinearLayout.LayoutParams(indicatorSize, indicatorSize)
        value = "${indicatorViews.size + 1}"
    }.also { indicatorViews.add(it) }

    fun clear() {
        indicatorViews.clear()
    }

    fun setState() {
        indicatorViews.forEachIndexed { index, indicatorView ->
            indicatorView.state = when {
                index < currentIndicator  -> IndicatorState.Past
                index == currentIndicator -> IndicatorState.Present
                index > currentIndicator  -> IndicatorState.Future
                else -> throw RuntimeException("Invalid current indicator index")
            }
        }
    }

    fun stepUp() {
        if (currentIndicator == indicatorViews.size - 1) return

        currentIndicator++
        indicatorViews[currentIndicator - 1].state = IndicatorState.Past
        indicatorViews[currentIndicator].state = IndicatorState.Present
    }

    fun stepDown() {
        if (currentIndicator == 0) return

        indicatorViews[currentIndicator].state = IndicatorState.Future
        indicatorViews[currentIndicator - 1].state = IndicatorState.Present
        currentIndicator--
    }

    fun stepTo(step: Int) {
        if (step < 0 || step >= indicatorViews.size) return
        when {
            step == currentIndicator -> return
            step > currentIndicator -> stepUp()
            step < currentIndicator -> stepDown()
        }
        stepTo(step)
    }
}