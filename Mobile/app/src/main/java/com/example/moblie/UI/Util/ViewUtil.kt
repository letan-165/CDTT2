package com.example.moblie.UI.Util;

import android.content.Context
import android.text.InputType
import android.widget.EditText
import android.widget.Toast


fun handleTypePassword(editText: EditText, isVisible: Boolean) {
    if (isVisible) {
        editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
    } else {
        editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }
    // Giữ con trỏ ở cuối
    editText.setSelection(editText.text.length)
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}
