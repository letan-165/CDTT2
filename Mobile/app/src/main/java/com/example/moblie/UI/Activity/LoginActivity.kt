package com.example.moblie.UI.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moblie.Data.Model.DTO.Request.ForgotPasswordRequest
import com.example.moblie.Data.Model.DTO.Request.LoginRequest
import com.example.moblie.Data.Model.DTO.Request.SignUpRequest
import com.example.moblie.Data.Service.AuthService
import com.example.moblie.Data.Service.UserService
import com.example.moblie.R
import com.example.moblie.UI.Util.handleTypePassword
import com.example.moblie.UI.Util.showToast

class LoginActivity : AppCompatActivity() {
    private lateinit var edit_text_account: EditText
    private lateinit var edit_text_password: EditText
    private lateinit var button_login: Button
    private lateinit var icon_password: ImageView
    private lateinit var text_create_account: TextView
    private lateinit var text_forgot_password: TextView
    private var isPasswordVisible = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        edit_text_account = findViewById(R.id.edit_text_account)
        edit_text_password = findViewById(R.id.edit_text_password)
        button_login = findViewById(R.id.button_login)
        icon_password = findViewById(R.id.icon_password)
        text_create_account = findViewById(R.id.text_create_account)
        text_forgot_password = findViewById(R.id.text_forgot_password)

        icon_password.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            handleTypePassword(edit_text_password, isPasswordVisible)
        }

        //Nhấn nút gửi
        button_login.setOnClickListener {
            handleLogin();
        }
        text_create_account.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        text_forgot_password.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }
    fun handleLogin(){
        val name = edit_text_account.text.toString()
        val password = edit_text_password.text.toString()

        //Check rỗng
        if (name.isEmpty() || password.isEmpty()) {
            showToast("Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT)
            return
        }

        //Gửi request
        var request = LoginRequest(name, password);

        AuthService.login(
            request,
            onSuccess = { user ->
                showToast("Đăng nhập thành công", Toast.LENGTH_SHORT)
            },
            onError = { error ->
                showToast(error, Toast.LENGTH_SHORT)
            }
        )
    }

}

