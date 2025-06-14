window.onload = function () {
  const questions = JSON.parse(localStorage.getItem("questions")) || [];
  const userAnswers = JSON.parse(localStorage.getItem("userAnswers")) || {};

  const quizReview = document.getElementById("quiz-review");
  const buttonContainer = document.getElementById("question-buttons");

  questions.forEach((q, index) => {
    const userAnswer = userAnswers[index] || "Chưa chọn";

    const questionDiv = document.createElement("div");
    questionDiv.className = "question-box";
    questionDiv.innerHTML = `
      <h3>Câu ${index + 1}: ${q.question}</h3>
      ${q.code ? `<pre><code>${q.code}</code></pre>` : ""}
      <p><strong>Câu trả lời của bạn:</strong> ${userAnswer}</p>
    `;

    quizReview.appendChild(questionDiv);

    // Tạo nút điều hướng câu hỏi
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

  document.getElementById("submit-btn").addEventListener("click", () => {
    window.location.href = "quiz-answer-score.html";
  });
};
