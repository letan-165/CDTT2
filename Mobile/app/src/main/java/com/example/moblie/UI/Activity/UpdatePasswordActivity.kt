package com.example.moblie.UI.Activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moblie.Data.Model.DTO.Request.ForgotPasswordRequest
import com.example.moblie.Data.Service.UserService
import com.example.moblie.UI.Util.handleTypePassword
import com.example.moblie.UI.Util.showToast
import com.example.moblie.R


class UpdatePasswordActivity:AppCompatActivity() {
    private lateinit var edit_text_account: EditText
    private lateinit var edit_text_password: EditText
    private lateinit var edit_text_otp: EditText
    private lateinit var button_update_password: Button
    private lateinit var icon_password: ImageView
    private lateinit var icon_hat: ImageView
    private var isPasswordVisible = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_password)
        edit_text_account = findViewById(R.id.edit_text_account)
        edit_text_password = findViewById(R.id.edit_text_password)
        edit_text_otp = findViewById(R.id.edit_text_otp)
        button_update_password = findViewById(R.id.button_update_password)
        icon_password = findViewById(R.id.icon_password)
        icon_hat = findViewById(R.id.icon_hat)

        icon_password.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            handleTypePassword(edit_text_password,isPasswordVisible)
        }

        icon_hat.setOnClickListener {
            finish()
        }

        //Nhấn nút gửi
        button_update_password.setOnClickListener {
            handleUpdatePassword();
        }
    }
    fun handleUpdatePassword(){
        val name = edit_text_account.text.toString()
        val password = edit_text_password.text.toString()
        val otp = edit_text_otp.text.toString().toInt()
        val email = intent.getStringExtra("email").toString()
        //Check rỗng
        if (name.isEmpty() || password.isEmpty() || otp.toString().isEmpty()) {
            showToast("Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT)
            return
        }
        val request = ForgotPasswordRequest(name,email,password,otp)
        UserService.forgotPassword(request,
            onSuccess = { user ->
                showToast("Cập nhật thành công ${user.name}", Toast.LENGTH_SHORT)
            },
            onError = { error ->
                showToast(error, Toast.LENGTH_SHORT)
            }
        )
    }
}