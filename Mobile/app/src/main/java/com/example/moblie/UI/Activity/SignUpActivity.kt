package com.example.moblie.UI.Activity

import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moblie.Data.Model.ApiResponse
import com.example.moblie.Data.Model.DTO.Request.SignUpRequest
import com.example.moblie.Data.Model.DTO.Response.UserResponse
import com.example.moblie.Data.Service.UserService
import com.example.moblie.UI.Util.handleTypePassword
import com.example.moblie.UI.Util.showToast
import com.example.moblie.R

class SignUpActivity : AppCompatActivity() {
    private lateinit var edit_text_name: EditText
    private lateinit var edit_text_email: EditText
    private lateinit var edit_text_phone: EditText
    private lateinit var edit_text_password: EditText
    private lateinit var edit_text_confirm_password: EditText
    private lateinit var button_signup: Button
    private lateinit var icon_password: ImageView
    private lateinit var icon_confirm_password: ImageView
    private lateinit var icon_hat: ImageView
    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)
        edit_text_name = findViewById(R.id.edit_text_name)
        edit_text_email = findViewById(R.id.edit_text_email)
        edit_text_phone = findViewById(R.id.edit_text_phone)
        edit_text_password = findViewById(R.id.edit_text_password)
        edit_text_confirm_password = findViewById(R.id.edit_text_confirm_password)
        button_signup = findViewById(R.id.button_signup)
        icon_password = findViewById(R.id.icon_password)
        icon_confirm_password = findViewById(R.id.icon_confirm_password)
        icon_hat = findViewById(R.id.icon_hat)

        icon_password.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            handleTypePassword(edit_text_password,isPasswordVisible)
        }

        icon_confirm_password.setOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            handleTypePassword(edit_text_confirm_password,isConfirmPasswordVisible)
        }

        icon_hat.setOnClickListener {
            finish()
        }

        //Nhấn nút gửi
        button_signup.setOnClickListener {
            handleSignUp();
        }
    }


    fun handleSignUp(){
            val name = edit_text_name.text.toString()
            val email = edit_text_email.text.toString()
            val phone = edit_text_phone.text.toString()
            val password = edit_text_password.text.toString()
            val confirm_password = edit_text_confirm_password.text.toString()
            //Check rỗng
            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirm_password.isEmpty()) {
                showToast("Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT)
                return
            }

            //Check mật khẩu
            if (password != confirm_password) {
                showToast("Mật khẩu không khớp", Toast.LENGTH_SHORT)
                return
            }

            //Gửi request
            var request = SignUpRequest(name, password, phone, email, "STUDENT");
            UserService.signUp(
                request,
                onSuccess = { user ->
                    showToast("Đăng kí thành công", Toast.LENGTH_SHORT)
                },
                onError = { error ->
                    showToast(error, Toast.LENGTH_SHORT)
                }
            )
    }

}

