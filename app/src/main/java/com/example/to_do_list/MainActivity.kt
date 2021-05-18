package com.example.to_do_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.setOnNavigationItemSelectedListener(onBottomNavListener)

        var frHome = supportFragmentManager.beginTransaction()
        frHome.add(R.id.frag_container, HomeFragment())
        frHome.commit()

    }

    private val onBottomNavListener = BottomNavigationView.OnNavigationItemSelectedListener { i ->
        var selectedFr: Fragment = HomeFragment()

        when(i.itemId){
            R.id.mn_home -> {
                selectedFr = HomeFragment()
            }
            R.id.mn_add -> {
                selectedFr = AddFragment()
            }
            R.id.mn_setting -> {
                selectedFr = SettingFragment()
            }
        }

        var fr = supportFragmentManager.beginTransaction()
        fr.replace(R.id.frag_container, selectedFr)
        fr.commit()

        true
    }


}