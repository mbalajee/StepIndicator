package com.apps.animation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val stepIndicator = findViewById<StepIndicator>(R.id.stepIndicator)

        findViewById<Button>(R.id.draw).setOnClickListener {
            stepIndicator.draw()
        }

        findViewById<Button>(R.id.animateUp).setOnClickListener {
            stepIndicator.stepUp()
        }

        findViewById<Button>(R.id.animateDown).setOnClickListener {
            stepIndicator.stepDown()
        }
    }
}