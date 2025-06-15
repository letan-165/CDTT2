package com.example.moblie.UI.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

import com.example.moblie.R

class SeeScoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_score) 

        val btnViewAllQuizzes: Button = findViewById(R.id.btn_view_all_quizzes)
        val btnRedoQuiz: Button = findViewById(R.id.btn_redo_quiz) // Nút Làm lại bài

        // 14. từ trang activity_see_score bấm xem điểm các bài trắc nghiệm thì sang trang activity_see_details
        btnViewAllQuizzes.setOnClickListener {
            val intent = Intent(this, SeeDetailsActivity::class.java) // SeeDetailsActivity = activity_see_details
            startActivity(intent)
        }

        // 14. ...bấm làm lại bài thì quay lại trang activity_quiz_list
        btnRedoQuiz.setOnClickListener {
            val intent = Intent(this, QuizListActivity::class.java)
            startActivity(intent)
            finish() // Kết thúc SeeScoreActivity để người dùng bắt đầu lại quiz
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
