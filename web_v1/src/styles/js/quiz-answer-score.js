window.onload = function () {
  showLoading();

  setTimeout(() => {
    let result = JSON.parse(localStorage.getItem("result"));
    const questions = result.quiz.questions;

    const resultBox = document.getElementById("result-box");
    const scoreSummary = document.getElementById("score-summary");
    const perQuestionResult = document.getElementById("per-question-result");

    let score = 0;
    const totalQuestions = questions.length;

    questions.forEach((q, index) => {
      const userAnswer = q.answers;
      const correctAnswer = q.corrects;

      const isCorrect = arraysEqual(userAnswer, correctAnswer);
      if (isCorrect) score++;

      const div = document.createElement("div");
      div.className = "question-box";
      const content = q.content.replace(/=@=/g, "____");

      div.innerHTML = `
        <h3>Câu ${index + 1}: ${content}</h3>
        <p><strong>Đáp án của bạn:</strong> ${Array.isArray(userAnswer) ? userAnswer.join(", ") : userAnswer} ${isCorrect ? "" : ""}</p>
        <p><strong>Đáp án đúng:</strong> ${Array.isArray(correctAnswer) ? correctAnswer.join(", ") : correctAnswer}</p>
      `;
      resultBox.appendChild(div);

      const resultText = document.createElement("p");
      resultText.textContent = `Câu ${index + 1}: Bạn đã ${isCorrect ? "trả lời đúng " : "trả lời sai "}`;
      resultText.style.color = isCorrect ? "green" : "red";
      perQuestionResult.appendChild(resultText);
    });

    const scoreOutOfTen = ((score / totalQuestions) * 10).toFixed(1);

    scoreSummary.innerHTML = `
      <p>Số câu trả lời đúng: <strong>${score}/${totalQuestions}</strong></p>
      <p>Điểm số đạt được: <strong>${scoreOutOfTen} điểm</strong></p>
    `;

    const quizId = parseInt(new URLSearchParams(window.location.search).get("quizId"));
    const quizResults = JSON.parse(localStorage.getItem("quizResults")) || {};

    if (!quizResults[quizId]) {
      quizResults[quizId] = [];
    }

    quizResults[quizId].push({
      score: scoreOutOfTen,
      correct: score,
      total: totalQuestions,
      timestamp: new Date().toISOString()
    });

    localStorage.setItem("quizResults", JSON.stringify(quizResults));
    hideLoading();
  }, 300); // delay nhẹ để hiển thị overlay
};

function arraysEqual(a, b) {
  if (!Array.isArray(a) || !Array.isArray(b)) return false;
  if (a.length !== b.length) return false;

  const sortedA = [...a].sort();
  const sortedB = [...b].sort();

  return sortedA.every((val, index) => val === sortedB[index]);
}


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