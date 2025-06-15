package com.example.moblie.UI.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moblie.Data.Model.DTO.Request.SendOtpRequest
import com.example.moblie.Data.Service.OtpService
import com.example.moblie.R
import com.example.moblie.UI.Util.handleTypePassword
import com.example.moblie.UI.Util.showToast

class ForgotPasswordActivity :AppCompatActivity() {
    private lateinit var edit_text_email: EditText
    private lateinit var button_send_otp: Button
    private lateinit var icon_hat: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_password)
        edit_text_email = findViewById(R.id.edit_text_email)
        button_send_otp = findViewById(R.id.button_send_otp)
        icon_hat = findViewById(R.id.icon_hat)

        //Nhấn nút gửi
        button_send_otp.setOnClickListener {
            handleSendOtp();
        }

        icon_hat.setOnClickListener {
            finish()
        }
    }
    fun handleSendOtp() {
        val email = edit_text_email.text.toString()
        if(email.isEmpty()){
            edit_text_email.error = "Vui lòng nhập email"
            return
        }
        val request = SendOtpRequest(email)
        OtpService.sendOtp(request,
            onSuccess = {
                val intent = Intent(this, UpdatePasswordActivity::class.java)
                intent.putExtra("email", email)
                startActivity(intent)
            },
            onError = { error ->
                showToast(error, Toast.LENGTH_SHORT)
            }
        )
    }
}