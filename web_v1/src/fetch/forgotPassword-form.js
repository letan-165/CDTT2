import { forgotPassword } from '../api/AccountService.js';

document.addEventListener("DOMContentLoaded", () => {
  const btn = document.getElementById("ForgotPasswordBtn");
  if (btn) {
    btn.addEventListener("click", handleForgotPassword);
  }
});

function handleForgotPassword() {
  const email = document.getElementById('email').value.trim();
  const otp = document.getElementById('otp').value.trim();

  if (!email || !otp) {
    alert("Vui lòng nhập đầy đủ tên đăng nhập và mã OTP!");
    return;
  }

  const data = {
    email,
    otp
  };

  forgotPassword(data)
    .then(res => {
      alert("Xác thực OTP thành công!");
      console.log("Response:", res);
     
    })
    .catch(err => {
      alert("OTP không hợp lệ hoặc tên đăng nhập sai!");
      console.error("Forgot password error:", err);
    });
}

window.handleForgotPassword = handleForgotPassword;
