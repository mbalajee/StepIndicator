package com.apps.animation.stepIndicator.indicator

import androidx.annotation.DrawableRes
import com.apps.animation.R


enum class IndicatorState {
    Past {
        override val drawable: Int get() = R.drawable.ic_indicator_past
    },
    Present {
        override val drawable: Int get() = R.drawable.drawable_indicator_present
    },
    Future {
        override val drawable: Int get() = R.drawable.drawable_indicator_future
    };

    @get:DrawableRes
    abstract val drawable: Int
}