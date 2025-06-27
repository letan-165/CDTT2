import { login } from '../api/AuthService.js';
import { findbyname } from '../api/AccountService.js';

document.addEventListener("DOMContentLoaded", () => {
  const btn = document.getElementById("LoginBtn");
  if (btn) {
    btn.addEventListener("click", handleLogin);
  }
});

function getuser(name) {
  return findbyname(name)
    .then(res => res.result)
    .catch(err => {
      console.error("Lỗi khi lấy thông tin người dùng:", err);
      return null;
    });
}

async function handleLogin() {
  const username = document.getElementById('username').value.trim();
  const password = document.getElementById('password').value;

  if (!username || !password) {
    alert("Vui lòng nhập đầy đủ email và mật khẩu!");
    return;
  }

  const data = { username, password };

  await login(data)
    .then(async res => {
      alert("Đăng nhập thành công!");
      console.log("Response:", res);

      const user = await getuser(username);
      if (!user) {
        alert("Không thể lấy thông tin người dùng!");
        return;
      }

      localStorage.setItem("userID", user.userID);
      localStorage.setItem("username", user.name);
      localStorage.setItem("email", user.email);
      localStorage.setItem("phone", user.phone);
      localStorage.setItem("role", user.role);
      localStorage.setItem("token", res.result); 



      if (user.role === "STUDENT") {
        window.location.href = "main_quizz_course-form.html";
      } else if (user.role === "TEACHER") {
        window.location.href = "teacher-page.html";
      } else {
        alert("Vai trò không xác định!");
      }
    })
    .catch(err => {
      alert("Đăng nhập thất bại! Vui lòng kiểm tra lại thông tin.");
      console.error("Login error:", err);
    });
}

window.handleLogin = handleLogin;
