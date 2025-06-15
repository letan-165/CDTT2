package com.example.moblie.UI.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout // Sử dụng RelativeLayout cho mỗi item chat
import androidx.appcompat.app.AppCompatActivity
import com.example.moblie.R
import com.google.android.material.imageview.ShapeableImageView

class MainChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_chat) 

        // 16. từ trang activity_main_chat bấm vô avatar bất kỳ sang trang activity_chat
        // Giả sử mỗi item chat là một RelativeLayout có thể click được (hoặc bạn có thể dùng CardView/ConstraintLayout)
        val chatItemLeTan: ShapeableImageView = findViewById(R.id.avatar_le_tan) // Đảm bảo ID này tồn tại trong layout của bạn
        val chatItemNgoThinh: ShapeableImageView = findViewById(R.id.avatar_ngo_thinh) // Ví dụ cho các item khác

        chatItemLeTan.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java) // ChatActivity = activity_chat
            startActivity(intent)
        }
        // Thêm các onClickListener tương tự cho các chatItem khác (Ngô Thịnh, Quốc Dũng, Hoàng Phú)

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
            // Đã ở MainChatActivity
        }

        navProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}
