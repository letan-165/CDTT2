package com.example.moblie.UI.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.moblie.UI.Activity.MainActivity

import com.example.moblie.R
class QuizQuestions4Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions4) 

        val btnFinishQuiz: Button = findViewById(R.id.btn_finish_quiz) // Nút "Làm xong" dưới cùng
        val btnPreviousQuestion: Button = findViewById(R.id.btn_previous_question)

        // 11. từ trang activity_quiz_questions4 bấm làm xong thì sang trang activity_answer_saved
        btnFinishQuiz.setOnClickListener {
            val intent = Intent(this, AnswerSavedActivity::class.java)
            startActivity(intent)
            finish() // Kết thúc QuizQuestions4Activity nếu không muốn quay lại đây
        }

        // 11. ...bấm câu trước thì quay lại trang activity_quiz_questions3
        btnPreviousQuestion.setOnClickListener {
            finish() // Kết thúc activity hiện tại để quay lại activity_quiz_questions3
        }

        // Các nút điều hướng dưới thanh navigation bar
        val navHome: ImageView = findViewById(R.id.nav_home)
        val navSettings: ImageView = findViewById(R.id.nav_settings)
        val navChat: ImageView = findViewById(R.id.nav_chat)
        val navProfile: ImageView = findViewById(R.id.nav_profile)
        val doneText: TextView = findViewById(R.id.done_text) // Nút Làm xong ở trên (giả định có trong layout)

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

        // Chuyển sang AnswerSavedActivity khi bấm "Làm xong" (nút trên cùng)
        // Nếu layout có nút "Làm xong" ở cả trên và dưới, bạn cần đặt ID khác nhau
        // hoặc kiểm tra lại ID chính xác của nút "Làm xong" bạn muốn xử lý.
        doneText.setOnClickListener {
            val intent = Intent(this, AnswerSavedActivity::class.java)
            startActivity(intent)
        }
    }
}
