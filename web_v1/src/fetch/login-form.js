import { login } from '../api/AuthService.js';

document.addEventListener("DOMContentLoaded", () => {
  const btn = document.getElementById("LoginBtn");
  if (btn) {
    btn.addEventListener("click", handleLogin);
  }
});

function handleLogin() {
  const username = document.getElementById('username').value.trim();
  const password = document.getElementById('password').value;

  if (!username || !password) {
    alert("Vui lòng nhập đầy đủ email và mật khẩu!");
    return;
  }

  const data = {
    username,
    password
  };

  login(data)
    .then(res => {
      alert("Đăng nhập thành công!");
      console.log("Response:", res);

    })
    .catch(err => {
      alert("Đăng nhập thất bại! Vui lòng kiểm tra lại thông tin.");
      console.error("Login error:", err);
    });
}

window.handleLogin = handleLogin; 
