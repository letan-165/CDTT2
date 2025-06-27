import { requestOtp } from '../api/AccountService.js'; 

document.addEventListener("DOMContentLoaded", () => {
  const btn = document.getElementById("RequestOtpBtn");
  if (btn) {
    btn.addEventListener("click", handleRequestOtp);
  }
});

function handleRequestOtp() {
  const email = document.getElementById('email').value.trim();

  if (!email) {
    alert("Vui lòng nhập email!");
    return;
  }

  const data = {
    email
  };

  requestOtp(data)
    .then(res => {
      alert("Mã OTP đã được gửi đến email!");
      console.log("OTP Response:", res);

      localStorage.setItem("forgot_email", email);

      window.location.href = "./Reset-Pass-form.html";
    })
    .catch(err => {
      alert("Không thể gửi mã OTP! Vui lòng kiểm tra lại email.");
      console.error("OTP request error:", err);
    });
}

window.handleRequestOtp = handleRequestOtp;
