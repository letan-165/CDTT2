import { startQuiz } from '../api/QuizService.js';

document.addEventListener("DOMContentLoaded", () => {
  const btn = document.getElementById("start-btn");
  if (btn) {
    btn.addEventListener("click", handlestart);
  }
});

function handlestart() {
  const studentID = document.getElementById('studentID').value.trim();
  const quizID = document.getElementById('quizID').value;

  if (!studentID || !quizID) {
    alert("Vui lòng nhập đầy đủ email và mật khẩu!");
    return;
  }

  const data = {
    studentID,
    quizID
  };

  startQuiz(data)
    .then(res => {
      alert("Bắt đầu làm bài!");
      console.log("Response:", res);

    })
    .catch(err => {
      alert("Bạn chưa đăng nhập vào hệ thống để thực hiện bào làm");
      console.error("Login error:", err);
    });
}



window.handleLogin = handlestart; 
