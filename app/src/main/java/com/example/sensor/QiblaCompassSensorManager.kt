package com.example.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.abs

class QiblaCompassSensorManager(context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val rotationVectorSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    private val accelerometerSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val magneticSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private val _azimuth = MutableStateFlow(0f)
    val azimuth: StateFlow<Float> = _azimuth.asStateFlow()

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    private var gravityValues: FloatArray? = null
    private var geomagneticValues: FloatArray? = null
    private var currentFilteredAzimuth = 0f

    fun startListening() {
        if (rotationVectorSensor != null) {
            sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            accelerometerSensor?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
            }
            magneticSensor?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
            }
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            SensorManager.getOrientation(rotationMatrix, orientationAngles)
            var azimuthDeg = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
            azimuthDeg = (azimuthDeg + 360f) % 360f
            smoothAndUpdate(azimuthDeg)
        } else if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            gravityValues = event.values.clone()
            calculateFallbackOrientation()
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagneticValues = event.values.clone()
            calculateFallbackOrientation()
        }
    }

    private fun calculateFallbackOrientation() {
        val g = gravityValues
        val m = geomagneticValues
        if (g != null && m != null) {
            val r = FloatArray(9)
            val i = FloatArray(9)
            if (SensorManager.getRotationMatrix(r, i, g, m)) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(r, orientation)
                var azimuthDeg = Math.toDegrees(orientation[0].toDouble()).toFloat()
                azimuthDeg = (azimuthDeg + 360f) % 360f
                smoothAndUpdate(azimuthDeg)
            }
        }
    }

    private fun smoothAndUpdate(targetAzimuth: Float) {
        // Handle wrap-around at 0/360 degrees
        var diff = targetAzimuth - currentFilteredAzimuth
        if (diff > 180f) diff -= 360f
        if (diff < -180f) diff += 360f

        val alpha = 0.2f // low-pass smoothing factor
        currentFilteredAzimuth = (currentFilteredAzimuth + alpha * diff + 360f) % 360f
        _azimuth.value = currentFilteredAzimuth
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No-op
    }
}
