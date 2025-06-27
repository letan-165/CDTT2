import { submitQuiz } from '../../api/QuizService.js';

let indexPage = localStorage.getItem("indexPage");
let quiz = JSON.parse(localStorage.getItem("quiz"));
const questions = quiz.questions;
let isSubmitting = false; // ✅ dùng để bỏ qua cảnh báo khi nộp bài

let currentQuestion = (indexPage === null) ? 0 : Number(indexPage);

function renderQuestion(index) {
  let resultRes = JSON.parse(localStorage.getItem("resultRes"));
  let userAnswers = resultRes.questions;

  const container = document.getElementById("question-box");
  const q = questions[index];
  if (!q) return;

  const content = q.content.replace(/=@=/g, "____");
  let html = `<p><strong>${content}</strong></p><div class="answers">`;

  if (q.type === "ENTER") {
    const currentAnswer = userAnswers[index].answers.join("\n") || "";
    html += `
      <label>Nhập câu trả lời:</label><br>
      <textarea id="input-${index}" rows="5" style="width: 100%; border-radius: 10px;">${currentAnswer}</textarea>
    `;
  } else if (q.type === "SELECT" && q.corrects.length > 1) {
    const selectedOptions = userAnswers[index].answers || [];
    q.options.forEach((opt) => {
      const checked = selectedOptions.includes(opt) ? "checked" : "";
      html += `<label><input type="checkbox" name="q${index}" data-index="${index}" data-value="${opt}" ${checked}> ${opt}</label><br>`;
    });
  } else {
    q.options.forEach((opt) => {
      const checked = userAnswers[index].answers.includes(opt) ? "checked" : "";
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

  // Sự kiện nhập ENTER
  if (q.type === "ENTER") {
    const input = document.getElementById(`input-${index}`);
    input.addEventListener("input", function () {
      userAnswers[index].answers = this.value
        .split("\n")
        .map(line => line.trim())
        .filter(line => line);
    });
  }

  // Checkbox nhiều đáp án
  if (q.type === "SELECT" && q.corrects.length > 1) {
    document.querySelectorAll(`input[name="q${index}"]`).forEach(input => {
      input.addEventListener("change", () => {
        const checked = document.querySelectorAll(`input[name="q${index}"]:checked`);
        userAnswers[index].answers = Array.from(checked).map(i => i.dataset.value);
      });
    });
  }

  // Radio một đáp án
  if (q.type === "SELECT" && q.corrects.length == 1) {
    document.querySelectorAll(`input[name="q${index}"]`).forEach(input => {
      input.addEventListener("change", () => {
        userAnswers[index].answers = [input.dataset.value];
      });
    });
  }

  const data = {
    resultID: resultRes.resultID,
    questions: userAnswers
  };

  if (document.querySelector(".prev-btn")) {
    document.querySelector(".prev-btn").addEventListener("click", () => {
      currentQuestion--;
      getSubmitQuiz(data, currentQuestion);
    });
  }

  if (document.querySelector(".next-btn")) {
    document.querySelector(".next-btn").addEventListener("click", () => {
      currentQuestion++;
      getSubmitQuiz(data, currentQuestion);
    });
  }

  if (document.querySelector(".done-btn")) {
    document.querySelector(".done-btn").addEventListener("click", async () => {
      showLoading();
      isSubmitting = true; // ✅ báo là đang nộp bài
      await getSubmitQuiz(data); // nộp bài
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
  localStorage.setItem("indexPage", index);
}

window.onload = () => {
  if (questions.length === 0) {
    document.getElementById("question-box").innerHTML = "<p>Không có câu hỏi cho bài học này.</p>";
    return;
  }

  renderButtons();
  renderQuestion(currentQuestion);
  startCountdown();
};

function getSubmitQuiz(data, targetIndex) {
  return submitQuiz(data).then(res => {
    const result = res.result;
    localStorage.setItem("resultRes", JSON.stringify(result));

    if (typeof targetIndex === "number") {
      renderQuestion(targetIndex);
    } else {
      window.location.href = `quiz-save.html?quizId=${quiz.quizID}`;
    }
  }).catch(err => {
    console.error(err);
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
      alert("⏰ Hết giờ làm bài. Bài sẽ được tự động nộp.");
      document.querySelector(".done-btn")?.click();
      return;
    }

    const minutes = Math.floor(remaining / 1000 / 60);
    const seconds = Math.floor((remaining / 1000) % 60);
    timerElement.textContent = `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
  }, 1000);
}

function showLoading() {
  const overlay = document.getElementById("loading-overlay");
  if (overlay) overlay.style.display = "flex";
}

// ✅ Hiển thị tên người dùng
document.addEventListener("DOMContentLoaded", () => {
  const username = localStorage.getItem("username");
  if (username) {
    const welcomeEl = document.getElementById("welcome-name");
    if (welcomeEl) {
      welcomeEl.textContent = `Xin chào ${username}`;
    }
  }
});

// ✅ Cảnh báo khi thoát trang (chỉ khi chưa nộp)
window.addEventListener("beforeunload", function (e) {
  if (isSubmitting) return; // không cảnh báo nếu đang nộp bài

  e.preventDefault();
  e.returnValue = "Hành động này sẽ khiến tiến trình làm bài của bạn không được lưu.";
});
