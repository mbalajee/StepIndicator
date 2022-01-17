package com.apps.animation.stepIndicator.indicator

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat

class IndicatorView(context: Context, attrs: AttributeSet? = null): AppCompatTextView(context, attrs) {

    lateinit var value: String

    init {
        gravity = Gravity.CENTER
    }

    var state = IndicatorState.Present
        set(value) {
            field = value
            updateState()
        }

    private fun updateState() {
        background = ContextCompat.getDrawable(context, state.drawable)
        text = if (state == IndicatorState.Past) "" else value
    }
}