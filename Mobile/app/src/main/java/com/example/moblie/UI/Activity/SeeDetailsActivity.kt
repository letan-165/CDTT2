package com.example.moblie.UI.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.moblie.UI.Activity.MainActivity

import com.example.moblie.R
class SeeDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_details) 

        // Các nút điều hướng dưới thanh navigation bar
        val navHome: ImageView = findViewById(R.id.nav_home)
        val navSettings: ImageView = findViewById(R.id.nav_settings)
        val navChat: ImageView = findViewById(R.id.nav_chat)
        val navProfile: ImageView = findViewById(R.id.nav_profile)

        navHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        navSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        navChat.setOnClickListener {
            val intent = Intent(this, MainChatActivity::class.java)
            startActivity(intent)
        }

        navProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}
