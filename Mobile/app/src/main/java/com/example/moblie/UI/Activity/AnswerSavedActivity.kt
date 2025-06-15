package com.example.moblie.UI.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.moblie.R

class AnswerSavedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer_saved) 

        val btnBackToQuiz: Button = findViewById(R.id.btn_back_to_quiz)
        val btnConfirmSubmit: Button = findViewById(R.id.btn_confirm_submit)

        // 12. từ trang activity_answer_saved bấm quay lại bài làm thì quay lại trang activity_quiz_questions4
        btnBackToQuiz.setOnClickListener {
            finish() // Kết thúc AnswerSavedActivity để quay lại activity_quiz_questions4
        }

        // 12. ...bấm xác nhận nộp bài thì sang trang activity_save
        btnConfirmSubmit.setOnClickListener {
            val intent = Intent(this, SaveActivity::class.java) // SaveActivity = activity_save
            startActivity(intent)
            finish() // Kết thúc AnswerSavedActivity
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
