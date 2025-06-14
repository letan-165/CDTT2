const allQuizzes = {
  1: [ // Buổi 1: C++ cơ bản
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
  ],
  2: [ // Buổi 2: C++ nâng cao
    {
      question: "Câu 1: Trong C++, từ khóa nào được dùng để kế thừa lớp?",
      code: null,
      options: ["A. inherit", "B. extends", "C. public", "D. class"],
    },
    {
      question: "Câu 2: Đâu là cú pháp đúng để tạo đối tượng từ class?",
      code: `class MyClass {
                  public:
                    void sayHello() {
                      cout << "Hello";
                    }
                };
                _____
                obj.sayHello();`,
      options: ["A. MyClass obj;", "B. obj = MyClass();", "C. MyClass->obj;", "D. new MyClass obj;"],
    }
  ],
  3: [ // Buổi 3: Con trỏ & Class
    {
      question: "Câu 1: Con trỏ là gì trong C++?",
      code: null,
      options: [
        "A. Biến chứa địa chỉ ô nhớ",
        "B. Biến chứa giá trị",
        "C. Hàm đặc biệt",
        "D. Không phải biến"
      ],
    },
    {
      question: "Câu 2: Kết quả của đoạn mã sau là gì?",
      code: `int a = 10;
            int* p = &a;
            *p = *p + 5;
            printf("%d", a);`,
      options: ["A. 10", "B. 15", "C. 5", "D. Lỗi biên dịch"],
    }
  ]
};

let currentQuestion = 0;
let questions = [];
let userAnswers = {}; // key: index, value: answer option text (e.g., "A. 10")

function renderQuestion(index) {
  const container = document.getElementById("question-box");
  const q = questions[index];
  if (!q) return;

  let html = `<p><strong>${q.question}</strong></p>`;
  if (q.code) {
    html += `<pre><code class="code-block">${q.code}</code></pre>`;
  }

  html += `<div class="answers">`;
  q.options.forEach((opt, i) => {
    const checked = userAnswers[index] === opt ? "checked" : "";
    html += `<label>
      <input type="radio" name="q${index}" data-index="${index}" data-value="${opt}" ${checked} /> ${opt}
    </label><br />`;
  });
  html += `
  <div style="margin-top: 10px;">
    ${index > 0 ? `<button class="prev-btn">Câu trước</button>` : ""}
    ${index < questions.length - 1 
      ? `<button class="next-btn">Chọn tiếp</button>` 
      : `<button class="done-btn">Làm xong</button>`
    }
  </div>
`;

  container.innerHTML = html;
  highlightButton(index);

  // Bắt sự kiện chọn đáp án
  document.querySelectorAll(`input[name="q${index}"]`).forEach((input) => {
    input.addEventListener("change", function () {
      const qIndex = parseInt(this.dataset.index);
      const selected = this.dataset.value;
      userAnswers[qIndex] = selected;
      localStorage.setItem("userAnswers", JSON.stringify(userAnswers));
    });
  });

  // Chuyển sang câu tiếp theo
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
    // Lưu và chuyển sang giao diện review
    localStorage.setItem("questions", JSON.stringify(questions));
    localStorage.setItem("userAnswers", JSON.stringify(userAnswers));
    window.location.href = "quiz-save.html";
  });
}

  // Nộp bài
  document.getElementById("submit-btn").addEventListener("click", () => {
    const correctAnswers = questions.map(q =>
      q.options.find(opt => opt.startsWith("B.")) // ví dụ nếu đáp án đúng bắt đầu bằng "B."
    );

    localStorage.setItem("correctAnswers", JSON.stringify(correctAnswers));
    localStorage.setItem("questions", JSON.stringify(questions));
    localStorage.setItem("userAnswers", JSON.stringify(userAnswers));

    window.location.href = "quiz-answer-score.html";
  });
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



