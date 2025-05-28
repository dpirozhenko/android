package com.example.lab5

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var compassImage: ImageView
    private lateinit var azimuthText: TextView

    private var currentAzimuth = 0f
    private var accelerometer = FloatArray(3)
    private var magnetometer = FloatArray(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        compassImage = findViewById(R.id.compassImage)
        azimuthText = findViewById(R.id.azimuthText)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> accelerometer = event.values.clone()
            Sensor.TYPE_MAGNETIC_FIELD -> magnetometer = event.values.clone()
        }

        val R = FloatArray(9)
        val I = FloatArray(9)
        if (SensorManager.getRotationMatrix(R, I, accelerometer, magnetometer)) {
            val orientation = FloatArray(3)
            SensorManager.getOrientation(R, orientation)

            val azimuthRad = orientation[0]
            val azimuthDeg = ((Math.toDegrees(azimuthRad.toDouble()) + 360) % 360).toFloat()

            azimuthText.text = "Azimuth: ${azimuthDeg.roundToInt()}°"

            val rotate = RotateAnimation(
                currentAzimuth,
                -azimuthDeg,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f
            ).apply {
                duration = 250
                fillAfter = true
            }

            compassImage.startAnimation(rotate)
            currentAzimuth = -azimuthDeg
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}
