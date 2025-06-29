import { finishQuiz } from '../../api/QuizService.js';

window.onload = function () {
  let quiz = JSON.parse(localStorage.getItem("quiz"));
  let resultRes = JSON.parse(localStorage.getItem("resultRes"));
  let userAnswers = resultRes.questions;

  const quizReview = document.getElementById("quiz-review");
  const buttonContainer = document.getElementById("question-buttons");

  // Hiển thị từng câu hỏi + đáp án
  quiz.questions.forEach((q, index) => {
    const content = q.content.replace(/=@=/g, "____");
    const userAnswer = userAnswers[index].answers || "Chưa chọn";

    const questionDiv = document.createElement("div");
    questionDiv.className = "question-box";
    questionDiv.innerHTML = `
      <h3>Câu ${index + 1}: ${content}</h3>
      <p><strong>Câu trả lời của bạn:</strong> ${Array.isArray(userAnswer) ? userAnswer.join(", ") : userAnswer}</p>
    `;
    quizReview.appendChild(questionDiv);

    // Tạo nút chuyển nhanh đến từng câu hỏi
    const btn = document.createElement("button");
    btn.textContent = index + 1;
    btn.addEventListener("click", () => {
      window.scrollTo({
        top: questionDiv.offsetTop - 60,
        behavior: "smooth"
      });
    });
    buttonContainer.appendChild(btn);
  });

  // Nút xác nhận nộp bài
  const submitBtn = document.getElementById("submit-btn");
  if (submitBtn) {
    submitBtn.addEventListener("click", () => {
      showLoading();
      geFinishQuiz(resultRes.resultID);
    });
  }

  startCountdown();
};

function geFinishQuiz(resultID) {
  finishQuiz(resultID).then(res => {
    localStorage.setItem("result", JSON.stringify(res.result));

    const quizId = res.result.quiz.quizID;

    // Đánh dấu đã làm bài
    const quizStatus = JSON.parse(localStorage.getItem("quizStatus")) || {};
    quizStatus[quizId] = "done";
    localStorage.setItem("quizStatus", JSON.stringify(quizStatus));

    // Tăng số lần làm bài
    const attempts = JSON.parse(localStorage.getItem("quizAttempts")) || {};
    attempts[quizId] = (attempts[quizId] || 0) + 1;
    localStorage.setItem("quizAttempts", JSON.stringify(attempts));

    // Chuyển đến trang kết quả
    window.location.href = `quiz-answer-score.html?quizId=${quizId}`;
  }).catch(err => {
    console.error(err);
    hideLoading(); // Ẩn loading nếu có lỗi
    alert("❌ Có lỗi xảy ra khi nộp bài. Vui lòng thử lại.");
  });
}

function startCountdown() {
  const timerElement = document.getElementById("timer");
  const endTime = Number(localStorage.getItem("quizEndTime"));

  if (!endTime || !timerElement) return;

  const interval = setInterval(() => {
    const now = Date.now();
    const remaining = endTime - now;

    if (remaining <= 0) {
      clearInterval(interval);
      timerElement.textContent = "00:00";

      // Ẩn nút quay lại
      const backBtn = document.querySelector(".buton-save button[onclick]");
      if (backBtn) {
        backBtn.disabled = true;
        backBtn.textContent = "Hết giờ";
        backBtn.style.opacity = "0.6";
        backBtn.style.cursor = "not-allowed";
      }

      // Tự động nộp bài khi hết giờ
      const resultRes = JSON.parse(localStorage.getItem("resultRes"));
      if (resultRes?.resultID) {
        alert("⏰ Hết giờ! Bài sẽ được nộp tự động.");
        showLoading();
        geFinishQuiz(resultRes.resultID);
      }
    } else {
      const minutes = Math.floor(remaining / 1000 / 60);
      const seconds = Math.floor((remaining / 1000) % 60);
      timerElement.textContent = `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
    }
  }, 1000);
}

// 
function showLoading() {
  const overlay = document.getElementById("loading-overlay");
  if (overlay) overlay.style.display = "flex";
}

function hideLoading() {
  const overlay = document.getElementById("loading-overlay");
  if (overlay) overlay.style.display = "none";
}


document.addEventListener("DOMContentLoaded", () => {
  const username = localStorage.getItem("username");
  if (username) {
    const welcomeEl = document.getElementById("welcome-name");
    if (welcomeEl) {
      welcomeEl.textContent = `Xin chào ${username}`;
    }
  }
});