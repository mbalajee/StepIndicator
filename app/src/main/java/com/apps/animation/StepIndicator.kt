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


class StepIndicator(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    private var currentStep = -1

    private val steps = 5
    private val indicatorSize = context.resources.getDimensionPixelSize(R.dimen.dimen_50)
    private val dashHeight = 20
    private val indicatorViews = mutableListOf<TextView>()
    private val dashViews = mutableListOf<View>()

    private val stepWidth: Int get() = width / steps
    private val dashWidth: Int get() = stepWidth - indicatorSize

    private val dashAnimDuration = 500L

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

        val currentDash = dashViews[++currentStep]
        currentDash.scaleX = 1f

        currentDash.startAnimation(
            ScaleAnimation(0f, 1f, 1f, 1f).apply {
                fillAfter = true
                duration = dashAnimDuration
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {
                        animating = true
                    }

                    override fun onAnimationEnd(animation: Animation) {
                        ValueAnimator.ofObject(ArgbEvaluator(), Color.RED, Color.GREEN).apply {
                            duration = dashAnimDuration / 2
                            addUpdateListener {
                                with(indicatorViews[currentStep + 1].background as GradientDrawable) {
                                    setColor(it.animatedValue as Int)
                                }
                            }
                        }.start()
                    }

                    override fun onAnimationRepeat(animation: Animation) {
                        animating = true
                    }
                })
            }
        )
    }

    fun stepDown() {

        if (animating || currentStep < 0) return

        val currentDash = dashViews[currentStep--]

        ValueAnimator.ofObject(ArgbEvaluator(), Color.GREEN, Color.RED).apply {
            duration = dashAnimDuration / 2
            addUpdateListener {
                with(indicatorViews[currentStep + 2].background as GradientDrawable) {
                    setColor(it.animatedValue as Int)
                }
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    currentDash.startAnimation(
                        ScaleAnimation(
                            1f,
                            0f,
                            1f,
                            1f
                        ).apply {
                            fillAfter = true
                            duration = dashAnimDuration
                            setAnimationListener(object : Animation.AnimationListener {
                                override fun onAnimationStart(animation: Animation) {
                                    animating = true
                                }

                                override fun onAnimationEnd(animation: Animation) {
                                    animating = false
                                }

                                override fun onAnimationRepeat(animation: Animation) {
                                    animating = true
                                }
                            })
                        }
                    )
                }

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {}
            })
        }.start()
    }

}