package com.example.moblie.UI.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

import com.example.moblie.R
class LoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading) 

        // Chuyển sang DangNhapActivity sau 3 giây
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Kết thúc LoadingActivity để người dùng không thể quay lại
        }, 3000) // 3000 milliseconds = 3 giây
    }
}