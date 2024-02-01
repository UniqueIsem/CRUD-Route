package com.example.crud_route

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView

class Inclination : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null

    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)

    private val rotationMatrix = FloatArray(9)
    private val orientationValues = FloatArray(3)

    private lateinit var pitchText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inclination)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        pitchText = findViewById(R.id.pitch)

        if (accelerometer != null && magnetometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            // Handle the case where sensors are not available on the device
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do nothing here
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
            Sensor.TYPE_MAGNETIC_FIELD -> System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
        }

        updateOrientation()
    }

    private fun updateOrientation() {
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)
        SensorManager.getOrientation(rotationMatrix, orientationValues)

        // orientationValues[0] -> Azimuth (Ángulo de dirección)
        // orientationValues[1] -> Pitch (Ángulo de inclinación hacia adelante o hacia atrás)
        // orientationValues[2] -> Roll (Ángulo de inclinación lateral)

        val pitch = Math.toDegrees(orientationValues[1].toDouble()).toFloat()

        updateBackgroundColor(pitch)
        pitchText.text = "$pitch degrees"
    }

    private fun updateBackgroundColor(pitch: Float) {
        val inclinationBackground = findViewById<LinearLayout>(R.id.inclinationBackground)

        val badInclination = -10.0f
        val mediumInclination = -20.0f
        val goodInclination = -40.0f

        when {
            pitch >= mediumInclination && pitch <= badInclination -> inclinationBackground.setBackgroundColor(resources.getColor(R.color.red))
            pitch >= goodInclination && pitch <= mediumInclination -> inclinationBackground.setBackgroundColor(resources.getColor(R.color.yellow))
            pitch <= goodInclination -> inclinationBackground.setBackgroundColor(resources.getColor(R.color.green))
        }
        inclinationBackground.invalidate()
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        if (accelerometer != null && magnetometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
}