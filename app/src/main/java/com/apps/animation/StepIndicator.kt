package com.apps.animation

import android.animation.*
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.ColorStateListDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.PaintDrawable
import android.graphics.drawable.ShapeDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

import android.animation.ValueAnimator.AnimatorUpdateListener
import android.view.animation.*
import com.google.android.material.animation.AnimationUtils
import kotlin.math.abs


class StepIndicator(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    private var currentStep = -1

    private val steps = 5
    private val indicatorSize = context.resources.getDimensionPixelSize(R.dimen.dimen_50)
    private val dashHeight = 20
    private val indicatorViews = mutableListOf<TextView>()
    private val dashViews = mutableListOf<View>()

    private val stepWidth: Int get() = width / steps
    private val dashWidth: Int get() = stepWidth - indicatorSize

    private var dashAnimDuration = 250L

    private var animating = false

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
    }

    fun draw() {

        repeat(steps) { step ->

            addView(
                TextView(context).apply {
                    layoutParams = LayoutParams(indicatorSize, indicatorSize)
                    text = "$step"
                    textSize = 14f
                    textAlignment = TEXT_ALIGNMENT_CENTER
                    gravity = Gravity.CENTER
                    background = GradientDrawable().apply {
                        cornerRadius = indicatorSize / 2f
                        setColor(if (step == 0) Color.GREEN else Color.RED)
                    }
                }.also {
                    indicatorViews.add(it)
                }
            )

            if (step == steps - 1) return

            addView(
                FrameLayout(context).apply {
                    layoutParams = LayoutParams(dashWidth, dashHeight)

                    addView(
                        View(context).apply {
                            layoutParams =
                                LayoutParams(dashWidth, FrameLayout.LayoutParams.MATCH_PARENT)
                            setBackgroundColor(Color.RED)
                        }
                    )

                    addView(
                        View(context).apply {
                            layoutParams =
                                LayoutParams(dashWidth, FrameLayout.LayoutParams.MATCH_PARENT)
                            setBackgroundColor(Color.GREEN)
                            scaleX = 0f
                        }.also {
                            dashViews.add(it)
                        }
                    )
                }
            )
        }
    }

    fun stepUp() {
        if (animating || currentStep == steps - 2) return
        stepUpBy(1, dashAnimDuration)
    }

    fun stepDown() {
        if (animating || currentStep < 0) return
        stepDownBy(1, dashAnimDuration)
    }

    fun stepTo(stepNo: Int) {

        if (stepNo == currentStep + 2) return

        val animDuration = dashAnimDuration / abs(currentStep + 2 - stepNo).let { if (it == 0) 1 else it }

        if (stepNo > currentStep + 2) {
            stepUpBy(abs(currentStep + 2 - stepNo), animDuration)
        } else {
            stepDownBy(abs(currentStep + 2 - stepNo), animDuration)
        }
    }

    private fun stepDownBy(count: Int, animDuration: Long) {

        if (animating || count == 0 || currentStep < 0) return

        val currentDash = dashViews[currentStep--]

        animating = true

        ValueAnimator.ofObject(ArgbEvaluator(), Color.GREEN, Color.RED).apply {
            duration = animDuration
            addUpdateListener { updateIndicatorColor(currentStep + 2, it.animatedValue as Int) }
            addListener(object : AnimatorListener {
                override fun onAnimationEnd(animation: Animator) {
                    currentDash.startAnimation(
                        ScaleAnimation(1f, 0f, 1f, 1f).apply {
                            fillAfter = true
                            duration = animDuration
                            setAnimationListener(object : AnimationListener {
                                override fun onAnimationEnd(animation: Animation) {
                                    animating = false
                                    stepDownBy(count - 1, animDuration)
                                }
                            })
                        }
                    )
                }
            })
        }.start()
    }

    private fun stepUpBy(count: Int, animDuration: Long) {

        if (animating || count == 0 || currentStep == steps - 2) return

        val currentDash = dashViews[++currentStep]
        currentDash.scaleX = 1f

        animating = true

        currentDash.startAnimation(
            ScaleAnimation(0f, 1f, 1f, 1f).apply {
                fillAfter = true
                duration = animDuration
                setAnimationListener(object : AnimationListener {
                    override fun onAnimationEnd(animation: Animation) {
                        ValueAnimator.ofObject(ArgbEvaluator(), Color.RED, Color.GREEN).apply {
                            duration = animDuration
                            addUpdateListener { updateIndicatorColor(currentStep + 1, it.animatedValue as Int) }
                            addListener(object : AnimatorListener {
                                override fun onAnimationEnd(animation: Animator) {
                                    animating = false
                                    stepUpBy(count - 1, animDuration)
                                }
                            })
                        }.start()
                    }
                })
            }
        )
    }

    private fun updateIndicatorColor(indicatorIdx: Int, color: Int) {
        with(indicatorViews[indicatorIdx].background as GradientDrawable) { setColor(color) }
    }

    interface AnimatorListener: Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {}
        override fun onAnimationEnd(animation: Animator) {}
        override fun onAnimationCancel(animation: Animator) {}
        override fun onAnimationRepeat(animation: Animator) {}
    }

    interface AnimationListener: Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {}
        override fun onAnimationEnd(animation: Animation) {}
        override fun onAnimationRepeat(animation: Animation) {}
    }
}