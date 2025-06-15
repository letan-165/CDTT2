package com.example.moblie.UI.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.moblie.UI.Activity.MainActivity

import com.example.moblie.R
class QuizQuestions2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions2) 

        val btnNextQuestion: Button = findViewById(R.id.btn_next_question)
        val btnPreviousQuestion: Button = findViewById(R.id.btn_previous_question)

        // 9. từ trang activity_quiz_questions2 bấm câu tiếp thì sang trang activity_quiz_questions3
        btnNextQuestion.setOnClickListener {
            val intent = Intent(this, QuizQuestions3Activity::class.java)
            startActivity(intent)
        }

        // 9. ...bấm câu trước thì quay lại trang activity_quiz_questions1
        btnPreviousQuestion.setOnClickListener {
            finish() // Đơn giản là kết thúc activity hiện tại để quay lại activity_quiz_questions1
        }

        // Các nút điều hướng dưới thanh navigation bar
        val navHome: ImageView = findViewById(R.id.nav_home)
        val navSettings: ImageView = findViewById(R.id.nav_settings)
        val navChat: ImageView = findViewById(R.id.nav_chat)
        val navProfile: ImageView = findViewById(R.id.nav_profile)
        val doneText: TextView = findViewById(R.id.done_text) // Nút Làm xong ở trên

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

        // Chuyển sang AnswerSavedActivity khi bấm "Làm xong"
        doneText.setOnClickListener {
            val intent = Intent(this, AnswerSavedActivity::class.java)
            startActivity(intent)
        }
    }
}
