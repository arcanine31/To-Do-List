package com.example.to_do_list

import android.app.AlertDialog
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*


class MainActivity : AppCompatActivity(), SensorEventListener{

    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setUpSensor()


        bottom_navigation.setOnNavigationItemSelectedListener(onBottomNavListener)

        val frHome = supportFragmentManager.beginTransaction()
        frHome.add(R.id.frag_container, HomeFragment())
        frHome.commit()
    }

    private val onBottomNavListener = BottomNavigationView.OnNavigationItemSelectedListener { i ->
        var selectedFr: Fragment = HomeFragment()
        val fr = supportFragmentManager.beginTransaction()

        when(i.itemId){
            R.id.mn_home -> {
                selectedFr = HomeFragment()
                fr.replace(R.id.frag_container, selectedFr)
                fr.commit()
            }
            R.id.mn_add -> {
                selectedFr = AddFragment()
                fr.replace(R.id.frag_container, selectedFr)
                fr.commit()
            }
            R.id.mn_setting -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }



        true
    }

    private fun setUpSensor(){
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        if (lightSensor == null) {
            Toast.makeText(this, "The device has no light sensor !", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val light1 = event.values[0].toInt()
            Log.i("BrightnessChange", "onSensorChanged: ${light1} ")

            if(light1 < 2){
                val alertdg = AlertDialog.Builder(this)
                alertdg.setTitle("Environment brigtness is too low")
                alertdg.setMessage("Care for your eyes")

                val layout = LinearLayout(this)

                alertdg.setView(layout)

                alertdg.setPositiveButton("Ok") { dialog, which ->

                }
                alertdg.show()

            }

        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    override fun onResume() {
        super.onResume()
        // Register a listener for the sensor.
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }


    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}

//    private fun brightness(brightness: Float): String {
//
//        return when (brightness.toInt()) {
//            0 -> "Pitch black"
//            in 1..10 -> "Dark"
//            in 11..50 -> "Grey"
//            in 51..5000 -> "Normal"
//            in 5001..25000 -> "Incredibly bright"
//            else -> "This light will blind you"
//        }
//    }

//}