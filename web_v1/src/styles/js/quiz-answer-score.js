window.onload = function () {
  const userAnswers = JSON.parse(localStorage.getItem("userAnswers")) || {};
  const correctAnswers = JSON.parse(localStorage.getItem("correctAnswers")) || [];
  const questions = JSON.parse(localStorage.getItem("questions")) || [];

  const resultBox = document.getElementById("result-box");
  const scoreSummary = document.getElementById("score-summary");
  const perQuestionResult = document.getElementById("per-question-result");

  let score = 0;

  questions.forEach((q, index) => {
    const userAnswer = userAnswers[index] || "Chưa chọn";
    const correctAnswer = correctAnswers[index];
    const isCorrect = userAnswer === correctAnswer;

    if (isCorrect) score++;

    // Vùng hiển thị nội dung từng câu
    const div = document.createElement("div");
    div.className = "question-box";
    div.innerHTML = `
      <h3>Câu ${index + 1}: ${q.question}</h3>
      ${q.code ? `<pre><code>${q.code}</code></pre>` : ""}
      <p><strong>Đáp án của bạn:</strong> ${userAnswer} ${isCorrect ? "✅" : "❌"}</p>
      <p><strong>Đáp án đúng:</strong> ${correctAnswer}</p>
    `;
    resultBox.appendChild(div);

    // Vùng tóm tắt bên phải
    const resultText = document.createElement("p");
    resultText.textContent = `Câu ${index + 1}: Bạn đã ${isCorrect ? "trả lời đúng ✅" : "trả lời sai ❌"}`;
    resultText.style.color = isCorrect ? "green" : "red";
    perQuestionResult.appendChild(resultText);
  });

  // Tính điểm theo thang 10
  const totalQuestions = questions.length;
  const scoreOutOfTen = ((score / totalQuestions) * 10).toFixed(1);

  // Hiển thị tổng kết điểm
  scoreSummary.innerHTML = `
    <p>Số câu trả lời đúng: <strong>${score}/${totalQuestions}</strong></p>
    <p>Điểm số đạt được: <strong>${scoreOutOfTen} điểm</strong></p>
  `;

  // Lưu kết quả vào quizResults
  const quizId = parseInt(new URLSearchParams(window.location.search).get("id"));
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
};
