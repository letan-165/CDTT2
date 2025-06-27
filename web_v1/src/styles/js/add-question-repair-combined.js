import { SaveQuestion, DeleteQuestion } from "../../api/quiz.js";
import { getQuiz } from "../../api/QuizService.js";

function showLoading() {
  const overlay = document.getElementById("loading-overlay");
  if (overlay) overlay.style.display = "flex";
}

function hideLoading() {
  const overlay = document.getElementById("loading-overlay");
  if (overlay) overlay.style.display = "none";
}

document.addEventListener("DOMContentLoaded", async () => {
  showLoading(); 
  const queryParams = new URLSearchParams(window.location.search);
  const quizId = queryParams.get("id");
  const quiz = await getQuizFun(quizId);
  hideLoading(); 

  const detailContainer = document.getElementById("quiz-detail");
  const formContainer = document.getElementById("quiz-form-container");
  const quizForm = document.getElementById("quiz-form");
  const questionList = document.getElementById("question-list");
  const saveAllBtn = document.getElementById("save-all-btn");

  let questionData = quiz?.questions || [];

  if (!quiz) {
    detailContainer.innerHTML = "<p>Không tìm thấy bài Quizz.</p>";
    return;
  }

  // Hiển thị thông tin quiz
  document.getElementById("quiz-title-heading").innerHTML =
    `<i class="fas fa-clipboard-list"></i> ${quiz.title || "Không có tiêu đề"}`;
  document.getElementById("quiz-ID").textContent = quiz.quizID || "không có quizID";
  document.getElementById("quiz-description").textContent = quiz.description || "Không có mô tả";
  document.getElementById("quiz-start").textContent = quiz.startTime || "Không rõ";
  document.getElementById("quiz-end").textContent = quiz.endTime || "Không rõ";
  document.getElementById("quiz-duration").textContent = quiz.duration ? `${quiz.duration} phút` : "Không rõ";

  document.querySelector(".quiz-summary h4").innerHTML =
    `Danh sách câu hỏi trắc nghiệm: <strong>${questionData.length} câu</strong>`;

  // Nút chỉnh sửa quiz
  document.getElementById("edit-quiz-btn")?.addEventListener("click", () => {
    window.location.href = `course-create-upload.html?quizId=${quiz.quizID}`;
  });

  document.getElementById("score-view-btn")?.addEventListener("click", () => {
    window.location.href = `teacher-quiz-results.html?quizId=${quiz.quizID}`;
  });

  // Hiển thị danh sách câu hỏi
  questionData.forEach((q, index) => renderQuestionItem(q, index));

  // Thêm câu hỏi
  document.getElementById("add-question-btn")?.addEventListener("click", () => {
    loadFormWithQuestion(null);
    formContainer.style.display = "block";
    detailContainer.style.display = "none";
    quizForm.dataset.questionIndex = "-1"; // chỉ số -1 nghĩa là câu mới
  });

  // Quay lại
  document.getElementById("back-btn")?.addEventListener("click", () => {
    formContainer.style.display = "none";
    detailContainer.style.display = "block";
  });

  // Lưu tất cả
  saveAllBtn.addEventListener("click", async () => {
    const index = parseInt(quizForm.dataset.questionIndex, 10);
    const content = document.getElementById("question-title").value.trim();
    const typeRadio = quizForm.querySelector("input[name='type-question']:checked");
    const type = typeRadio?.value || "ENTER";
    const answer = document.getElementById("answer").value.trim();
    const correctTextarea = document.getElementById("correct-textarea").value.trim();
    const correctInput = document.getElementById("correct-input").value.trim();

    let options = [];
    let corrects = [];

    if (type === "SELECT-MULTI") {
      options = answer.split("\n").map(opt => opt.trim()).filter(Boolean);
      corrects = correctTextarea.split("\n").map(opt => opt.trim()).filter(Boolean);
    } else if (type === "SELECT-SINGLE") {
      options = answer.split("\n").map(opt => opt.trim()).filter(Boolean);
      if (correctInput) corrects = [correctInput];
    } else if (type === "ENTER") {
      corrects = correctTextarea.split("\n").map(opt => opt.trim()).filter(Boolean);
    }

    const question = {
      content,
      type: type.startsWith("SELECT") ? "SELECT" : "ENTER",
      options,
      corrects
    };
    

    if (index >= 0) {
      questionData[index] = { ...questionData[index], ...question };
    } else {
      questionData.push(question);
    }
    console.log("question", question);
    
    try {
      const payload = { quizID: quizId, questions: [question] };
      const result = await SaveQuestion(payload);
      alert("Đã lưu câu hỏi lên hệ thống!");
      formContainer.style.display = "none";
      detailContainer.style.display = "block";
      questionList.innerHTML = ""; // Xoá danh sách cũ
      questionData.forEach((q, index) => renderQuestionItem(q, index)); // Hiển thị lại danh sách mới
      document.querySelector(".quiz-summary h4").innerHTML =
        `Danh sách câu hỏi trắc nghiệm: <strong>${questionData.length} câu</strong>`;
    } catch (err) {
      console.error("Lỗi khi gửi API:", err);
      alert("Lỗi khi lưu câu hỏi.");
    }
  });

  function renderQuestionItem(question, index) {
    const li = document.createElement("li");
    li.id = `question-item-${question.questionID || index}`;
    li.innerHTML = `
      <span class="question-text">Câu ${index + 1}: ${question.content}</span>
      <button class="remove-btn">✖</button>
    `;

    li.addEventListener("click", () => {
      loadFormWithQuestion(question);
      formContainer.style.display = "block";
      detailContainer.style.display = "none";
      quizForm.dataset.questionIndex = index;
    });

    li.querySelector(".remove-btn").addEventListener("click", async (e) => {
      e.stopPropagation();
      const confirmDelete = confirm("Bạn có chắc muốn xóa câu hỏi này?");
      if (!confirmDelete) return;

      try {
        await DeleteQuestion({
          quizID: quizId,
          questions: [question.questionID]
        });
        alert("Đã xoá câu hỏi khỏi hệ thống.");
        location.reload();
      } catch (err) {
        console.error("Lỗi xoá câu hỏi:", err);
        alert("Lỗi khi xoá câu hỏi.");
      }
    });

    questionList.appendChild(li);
  }

  function loadFormWithQuestion(question = null) {
    document.getElementById("question-title").value = question?.content || "";

    let type = "SELECT-MULTI";
    if (question?.type === "ENTER") {
      type = "ENTER";
    } else if (question?.type === "SELECT" && question?.corrects?.length === 1) {
      type = "SELECT-SINGLE";
    }

    quizForm.querySelectorAll("input[name='type-question']").forEach(radio => {
      radio.checked = radio.value === type;
      radio.dispatchEvent(new Event("change"));
    });

    const answerGroup = document.getElementById("answer-group");
    const correctLabel = document.getElementById("correct-label");
    const correctTextarea = document.getElementById("correct-textarea");
    const correctInput = document.getElementById("correct-input");

    if (type === "SELECT-MULTI") {
      answerGroup.style.display = "block";
      correctTextarea.style.display = "block";
      correctInput.style.display = "none";
      correctLabel.textContent = "Các đáp án đúng:";
      document.getElementById("answer").value = question?.options?.join("\n") || "";
      correctTextarea.value = question?.corrects?.join("\n") || "";
      correctInput.value = "";
    } else if (type === "SELECT-SINGLE") {
      answerGroup.style.display = "block";
      correctTextarea.style.display = "none";
      correctInput.style.display = "block";
      correctLabel.textContent = "Đáp án đúng:";
      document.getElementById("answer").value = question?.options?.join("\n") || "";
      correctInput.value = question?.corrects?.[0] || "";
      correctTextarea.value = "";
    } else if (type === "ENTER") {
      answerGroup.style.display = "none";
      correctTextarea.style.display = "block";
      correctInput.style.display = "none";
      correctLabel.textContent = "Đáp án đúng:";
      correctTextarea.value = question?.corrects?.join("\n") || "";
      correctInput.value = "";
      document.getElementById("answer").value = "";
    }
  }

  // Ánh xạ sự kiện khi đổi loại câu hỏi
  quizForm.querySelectorAll("input[name='type-question']").forEach(radio => {
    radio.addEventListener("change", () => {
      const type = radio.value;
      const answerGroup = document.getElementById("answer-group");
      const correctLabel = document.getElementById("correct-label");
      const correctTextarea = document.getElementById("correct-textarea");
      const correctInput = document.getElementById("correct-input");

      if (type === "SELECT-MULTI") {
        answerGroup.style.display = "block";
        correctTextarea.style.display = "block";
        correctInput.style.display = "none";
        correctLabel.textContent = "Các đáp án đúng:";
      } else if (type === "SELECT-SINGLE") {
        answerGroup.style.display = "block";
        correctTextarea.style.display = "none";
        correctInput.style.display = "block";
        correctLabel.textContent = "Đáp án đúng:";
      } else if (type === "ENTER") {
        answerGroup.style.display = "none";
        correctTextarea.style.display = "block";
        correctInput.style.display = "none";
        correctLabel.textContent = "Đáp án đúng:";
      }
    });
  });
});

// Gọi API quiz
function getQuizFun(data) {
  return getQuiz(data)
    .then(res => res.result)
    .catch(err => {
      console.error("Lỗi khi tải quiz:", err);
      return null;
    });
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


