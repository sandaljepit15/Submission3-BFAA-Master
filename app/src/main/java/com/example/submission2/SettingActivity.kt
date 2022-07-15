package com.example.submission2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import submission2.R



class SettingActivity : AppCompatActivity() {

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_activity)
       supportFragmentManager
           .beginTransaction()
           .replace(R.id.idFrameLayout,SettingFragment() )
           .commit()
       supportActionBar?.apply {
           title = resources.getString(R.string.lable_setting)
       }
    }
 }


















