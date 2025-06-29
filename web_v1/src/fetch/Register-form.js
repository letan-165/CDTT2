import { signUp } from '../api/AccountService.js';

document.addEventListener("DOMContentLoaded", () => {
  const btn = document.getElementById("registerBtn");
  if (btn) {
    btn.addEventListener("click", handlerRegister);
  }
});

function handlerRegister() {
  const name = document.getElementById('name').value.trim();
  const email = document.getElementById('email').value.trim();
  const phone = document.getElementById('phone').value.trim();
  const password = document.getElementById('password').value;
  const confirmPassword = document.getElementById('confirm-password').value;

  //  Kiểm tra định dạng email
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(email)) {
    alert("Email không hợp lệ!");
    return;
  }

  //  Kiểm tra số điện thoại có đúng 10 chữ số
  const phoneRegex = /^\d{10}$/;
  if (!phoneRegex.test(phone)) {
    alert("Số điện thoại phải gồm đúng 10 chữ số!");
    return;
  }

  //  Kiểm tra mật khẩu
  if (password !== confirmPassword) {
    alert("Mật khẩu không khớp!");
    return;
  }

  const selectedRole = document.querySelector('input[name="role"]:checked');
  if (!selectedRole) {
    alert("Vui lòng chọn vai trò!");
    return;
  }

  const role = selectedRole.value === 'giaovien' ? 'TEACHER' : 'STUDENT';

  const data = {
    name,
    email,
    phone,
    password,
    role
  };

  //  Hiện overlay loading
  document.getElementById("loading-overlay").style.display = "flex";

  signUp(data)
    .then(res => {
      alert("Đăng ký thành công!");
      console.log(res);
      window.location.href = "login-form.html";
    })
    .catch(err => {
      alert("Có lỗi xảy ra!");
      console.error(err);
    })
    .finally(() => {
      //  Tắt overlay loading
      document.getElementById("loading-overlay").style.display = "none";
    });
}

window.handlerRegister = handlerRegister;
