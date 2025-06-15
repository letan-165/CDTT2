const allQuizzes = {
  1: [
    {
      question: "Câu 1: Kết quả của đoạn mã sau là gì?",
      code: `#include <stdio.h>\nint main() {\n  int a = 5;\n  int b = ++a * 2;\n  printf("%d", b);\n  return 0;\n}`,
      answer: "A. 10\nB. 12\nC. 11\nD. Không in ra gì",
      correct: "B. 12",
      type: "single"
    },
    {
      question: "Câu 2: Kết quả của đoạn mã sau là gì?",
      code: `int a = 3, b = 4;\nprintf("%d", a++ + ++b);`,
      answer: "A. 7\nB. 8\nC. 9\nD. 10",
      correct: "C. 9",
      type: "single"
    },
    {
      question: "Câu 3: Hàm nào dùng để nhập dữ liệu trong C?",
      code: null,
      answer: "A. printf\nB. scanf\nC. gets\nD. puts",
      correct: "B. scanf",
      type: "single"
    },
    {
      question: "Câu 4: Nhập đoạn mô tả ngắn về khái niệm biến trong C.",
      code: null,
      answer: "",
      correct: "Biến là một vùng nhớ có tên dùng để lưu trữ dữ liệu trong chương trình.",
      type: "input"
    },
    {
      question: "Câu 5: Những kiểu dữ liệu nào sau đây là kiểu số nguyên trong C?",
      code: null,
      answer: "A. int\nB. float\nC. long\nD. char",
      correct: "A. int\nC. long",
      type: "multi"
    }
  ]
};

let currentQuestion = 0;
let questions = [];
let userAnswers = {};

function renderQuestion(index) {
  const container = document.getElementById("question-box");
  const q = questions[index];
  if (!q) return;

  let html = `<p><strong>${q.question}</strong></p>`;
  if (q.code) {
    html += `<pre><code class="code-block">${q.code}</code></pre>`;
  }

  html += `<div class="answers">`;

  if (q.type === "input") {
    const currentAnswer = userAnswers[index] || "";
    html += `
      <label>Nhập câu trả lời:</label><br>
      <textarea id="input-${index}" rows="5" style="width: 100%; border-radius: 10px;">${currentAnswer}</textarea>
    `;
  } else if (q.type === "multi") {
    const selectedOptions = userAnswers[index] || [];
    q.answer.split("\n").forEach((opt) => {
      const checked = selectedOptions.includes(opt) ? "checked" : "";
      html += `<label><input type="checkbox" name="q${index}" data-index="${index}" data-value="${opt}" ${checked}> ${opt}</label><br>`;
    });
  } else {
    q.answer.split("\n").forEach((opt) => {
      const checked = userAnswers[index] === opt ? "checked" : "";
      html += `<label><input type="radio" name="q${index}" data-index="${index}" data-value="${opt}" ${checked}> ${opt}</label><br>`;
    });
  }

  html += `
    <div class="fu-btn" style="margin-top: 100px;">
      ${index > 0 ? `<button class="prev-btn">Câu trước</button>` : ""}
      ${index < questions.length - 1
        ? `<button class="next-btn">Chọn tiếp</button>`
        : `<button class="done-btn">Làm xong</button>`}
    </div>
  `;

  container.innerHTML = html;
  highlightButton(index);

  // Sự kiện cho input nhập
  if (q.type === "input") {
    const input = document.getElementById(`input-${index}`);
    input.addEventListener("input", function () {
      userAnswers[index] = this.value.trim();
      localStorage.setItem("userAnswers", JSON.stringify(userAnswers));
    });
  }

  // Sự kiện cho chọn nhiều đáp án
  if (q.type === "multi") {
    document.querySelectorAll(`input[name="q${index}"]`).forEach((input) => {
      input.addEventListener("change", function () {
        const checkedInputs = document.querySelectorAll(`input[name="q${index}"]:checked`);
        userAnswers[index] = Array.from(checkedInputs).map(i => i.dataset.value);
        localStorage.setItem("userAnswers", JSON.stringify(userAnswers));
      });
    });
  }

  // Sự kiện cho chọn 1 đáp án
  if (q.type === "single") {
    document.querySelectorAll(`input[name="q${index}"]`).forEach((input) => {
      input.addEventListener("change", function () {
        userAnswers[index] = this.dataset.value;
        localStorage.setItem("userAnswers", JSON.stringify(userAnswers));
      });
    });
  }

  if (document.querySelector(".prev-btn")) {
    document.querySelector(".prev-btn").addEventListener("click", () => {
      currentQuestion--;
      renderQuestion(currentQuestion);
    });
  }

  if (document.querySelector(".next-btn")) {
    document.querySelector(".next-btn").addEventListener("click", () => {
      currentQuestion++;
      renderQuestion(currentQuestion);
    });
  }

  if (document.querySelector(".done-btn")) {
    document.querySelector(".done-btn").addEventListener("click", () => {
      const correctAnswers = questions.map(q => q.correct);
      localStorage.setItem("correctAnswers", JSON.stringify(correctAnswers));
      localStorage.setItem("questions", JSON.stringify(questions));
      localStorage.setItem("userAnswers", JSON.stringify(userAnswers));

      const params = new URLSearchParams(window.location.search);
      const quizId = parseInt(params.get("id"));
      const quizStatus = JSON.parse(localStorage.getItem("quizStatus")) || {};
      quizStatus[quizId] = "done";
      localStorage.setItem("quizStatus", JSON.stringify(quizStatus));

      window.location.href = "quiz-save.html";
    });
  }
}

function renderButtons() {
  const buttonContainer = document.getElementById("question-buttons");
  buttonContainer.innerHTML = "";
  for (let i = 0; i < questions.length; i++) {
    const btn = document.createElement("button");
    btn.textContent = i + 1;
    btn.addEventListener("click", () => {
      currentQuestion = i;
      renderQuestion(i);
    });
    buttonContainer.appendChild(btn);
  }
}

function highlightButton(index) {
  const buttons = document.querySelectorAll("#question-buttons button");
  buttons.forEach((btn, i) => {
    btn.classList.toggle("active", i === index);
  });
}

window.onload = () => {
  const params = new URLSearchParams(window.location.search);
  const quizId = parseInt(params.get("id")) || 1;
  questions = allQuizzes[quizId] || [];

  if (questions.length === 0) {
    document.getElementById("question-box").innerHTML = "<p>Không có câu hỏi cho bài học này.</p>";
    return;
  }

  userAnswers = JSON.parse(localStorage.getItem("userAnswers")) || {};
  renderButtons();
  renderQuestion(currentQuestion);
};
