package com.example.moblie.UI.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.moblie.UI.Activity.MainActivity

import com.example.moblie.R
class QuizListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_list) 

        // Giả sử nút "Bắt đầu làm bài" có ID là btn_start_quiz hoặc tương tự
        val btnStartQuiz: Button = findViewById(R.id.btn_start_quiz) // Hoặc ID khác trong layout của bạn

        // 7. từ trang activity_quiz_list bấm bắt đầu làm bài thì sang trang activity_quiz_questions1
        btnStartQuiz.setOnClickListener {
            val intent = Intent(this, QuizQuestions1Activity::class.java)
            startActivity(intent)
        }

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
