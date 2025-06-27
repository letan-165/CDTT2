import { forgotPassword } from '../api/AccountService.js';

document.addEventListener("DOMContentLoaded", () => {
  const btn = document.getElementById("ForgotPasswordBtn");
  if (btn) {
    btn.addEventListener("click", handleForgotPassword);
  }
});

async function handleForgotPassword() {
  const username = document.getElementById("username").value.trim();
  const password = document.getElementById("password").value.trim();
  const otp = document.getElementById("otp").value.trim();
  const email = localStorage.getItem("forgot_email");

  if (!username || !password || !otp || !email) {
    alert("Vui lòng nhập đầy đủ thông tin!");
    return;
  }

  const data = {
    username,
    password,
    otp,
    email
  };

  try {
    const res = await forgotPassword(data);
    alert("Đặt lại mật khẩu thành công!");
    console.log("Server response:", res);

    localStorage.removeItem("forgot_email");
  } catch (err) {
    console.error("Lỗi đặt lại mật khẩu:", err);
    alert("Thông tin không chính xác hoặc OTP không hợp lệ!");
  }
}

window.handleForgotPassword = handleForgotPassword;
