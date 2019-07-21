package dev.into.ominous.beeping.app.services

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.IBinder
import android.hardware.SensorManager
import android.util.FloatMath
import android.util.TypedValue
import androidx.core.widget.TextViewCompat
import dev.into.ominous.beeping.app.Beeper
import dev.into.ominous.beeping.app.FullscreenActivity
import dev.into.ominous.beeping.app.logWith
import kotlin.math.sqrt


class MovementDetectionService : Service(), SensorEventListener{

    // Start with some variables
    lateinit var sensorMan: SensorManager
    lateinit var accelerometer: Sensor

    lateinit var mGravity: FloatArray
    var mAccel: Float = 0.toFloat()
    var mAccelCurrent: Float = 0.toFloat()
    var mAccelLast: Float = 0.toFloat()

    override fun onCreate() {
        sensorMan = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mAccel = 0.00f
        mAccelCurrent = SensorManager.GRAVITY_EARTH
        mAccelLast = SensorManager.GRAVITY_EARTH
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        sensorMan.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_UI)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        sensorMan.unregisterListener(this)
        Beeper.mp.pause()
        super.onDestroy()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            mGravity = event.values.clone()
            // Shake detection
            val x = mGravity[0]
            val y = mGravity[1]
            val z = mGravity[2]
            mAccelLast = mAccelCurrent
            mAccelCurrent = sqrt(x * x + y * y + z * z)
            val delta = mAccelCurrent - mAccelLast
            mAccel = mAccel * 0.9f + delta
            // Make this higher or lower according to how much
            // motion you want to detect
            if (mAccel > 6) {
                "shaken" logWith "MovementService"
                if(Beeper.detectShakes && !Beeper.foreground) {
                    try {
                        val intent = Intent(this,FullscreenActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}
