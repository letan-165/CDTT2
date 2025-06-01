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

  if (password !== confirmPassword) {
    alert("Mật khẩu không khớp!");
    return;
  }
  const role ="STUDENT";

  const data = {
    name,
    email,
    phone,
    password,
    role 
  };


  signUp(data)
    .then(res => {
      alert("Đăng ký thành công!");
      console.log(res);
    })
    .catch(err => {
      alert("Có lỗi xảy ra!");
      console.error(err);
    });
}

window.handlerRegister = handlerRegister; // cần để gọi được từ onclick
