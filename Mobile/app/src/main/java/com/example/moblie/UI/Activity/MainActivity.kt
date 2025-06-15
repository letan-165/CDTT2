package com.example.moblie.UI.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.moblie.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 6. từ trang activity_main bấm nút xem bất kì thì sang trang activity_quiz_list
        // Giả sử các nút "Xem" có ID như sau (bạn cần cập nhật theo layout thực tế của mình)
        val btn_view_quiz_1: Button = findViewById(R.id.btn_view_quiz_1)// Nút "Tiếp tục" cho Quiz C++

        val navHome: ImageView = findViewById(R.id.nav_home)
        val navSettings: ImageView = findViewById(R.id.nav_settings)
        val navChat: ImageView = findViewById(R.id.nav_chat)
        val navProfile: ImageView = findViewById(R.id.nav_profile)


        btn_view_quiz_1.setOnClickListener {
            val intent = Intent(
                this,
                QuizListActivity::class.java
            ) // QuizListActivity = activity_quiz_list
            startActivity(intent)
        }


        // 15. các nút dưới thanh điều hướng
        navHome.setOnClickListener {
            // Đã ở MainActivity, không cần làm gì hoặc tải lại chính nó nếu muốn
            // val intent = Intent(this, MainActivity::class.java)
            // startActivity(intent)
        }

        navSettings.setOnClickListener {
            val intent =
                Intent(this, SettingsActivity::class.java) // SettingsActivity = activity_settings
            startActivity(intent)
        }

        navChat.setOnClickListener {
            val intent =
                Intent(this, MainChatActivity::class.java) // MainChatActivity = activity_main_chat
            startActivity(intent)
        }

        navProfile.setOnClickListener {
            val intent =
                Intent(this, ProfileActivity::class.java) // ProfileActivity = activity_profile
            startActivity(intent)
        }
    }
}