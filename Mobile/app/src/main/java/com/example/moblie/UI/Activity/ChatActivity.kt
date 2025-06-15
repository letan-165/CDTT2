package com.example.moblie.UI.Activity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

import com.example.moblie.R
class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat) 

        val backArrow: ImageView = findViewById(R.id.back_arrow)

        // Xử lý nút quay lại trên top bar của màn hình chat
        backArrow.setOnClickListener {
            finish() // Kết thúc ChatActivity để quay lại MainChatActivity
        }

        // Các nút gọi, video call, tìm kiếm trên top bar 
        val callIcon: ImageView = findViewById(R.id.call_icon)
        val videoCallIcon: ImageView = findViewById(R.id.video_call_icon)
        val searchIconChat: ImageView = findViewById(R.id.search_icon_chat)
        val emojiButton: ImageView = findViewById(R.id.emoji_button)
        val likeButton: ImageView = findViewById(R.id.like_button)


        callIcon.setOnClickListener {
            // Xử lý gọi thoại
        }

        videoCallIcon.setOnClickListener {
            // Xử lý gọi video
        }

        searchIconChat.setOnClickListener {
            // Xử lý tìm kiếm trong cuộc trò chuyện
        }

        emojiButton.setOnClickListener {
            // Xử lý nút emoji
        }

        likeButton.setOnClickListener {
            // Xử lý nút like
        }
    }
}
