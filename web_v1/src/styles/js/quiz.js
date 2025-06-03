const questions = [
  {
    question: "Câu 1: Kết quả của đoạn mã sau là gì?",
    code: `#include <stdio.h>
int main() {
  int a = 5;
  int b = ++a * 2;
  printf("%d", b);
  return 0;
}`,
    options: ["A. 10", "B. 12", "C. 11", "D. Không in ra gì"],
  },
  {
    question: "Câu 2: Kết quả của đoạn mã sau là gì?",
    code: `int a = 3, b = 4;
printf("%d", a++ + ++b);`,
    options: ["A. 7", "B. 8", "C. 9", "D. 10"],
  },
  {
    question: "Câu 3: Hàm nào dùng để nhập dữ liệu trong C?",
    code: null,
    options: ["A. printf", "B. scanf", "C. gets", "D. puts"],
  }
];

let currentQuestion = 0;

function renderQuestion(index) {
  const container = document.getElementById("question-box");
  const q = questions[index];
  let html = `<p><strong>${q.question}</strong></p>`;
  if (q.code) {
    html += `<pre><code class="code-block">${q.code}</code></pre>`;
  }

  html += `<div class="answers">`;
  q.options.forEach((opt, i) => {
    html += `<label><input type="radio" name="q${index}" /> ${opt}</label><br />`;
  });
  html += `</div><button class="submit-btn">Chọn tiếp</button>`;

  container.innerHTML = html;
  highlightButton(index);
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
  renderButtons();
  renderQuestion(currentQuestion);
};
