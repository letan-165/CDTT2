document.addEventListener("DOMContentLoaded", () => {
  const queryParams = new URLSearchParams(window.location.search);
  const quizId = parseInt(queryParams.get("id")); // id truyền trên URL là ?id=123

  const quizList = JSON.parse(localStorage.getItem("quizList")) || [];
  const quiz = quizList.find(q => q.quizId === quizId);

  if (!quiz) {
    document.querySelector(".course-create-detail").innerHTML = "<p>Không tìm thấy bài Quizz.</p>";
    return;
  }

  // Hiển thị thông tin của quiz
  document.getElementById("quiz-title-heading").innerHTML =
    `<i class="fas fa-clipboard-list"></i> ${quiz.title}`;
  document.getElementById("quiz-description").textContent = quiz.description;
  document.getElementById("quiz-start").textContent = quiz.start;
  document.getElementById("quiz-end").textContent = quiz.end;
  document.getElementById("quiz-duration").textContent = quiz.duration;

  // Gắn sự kiện cho nút chỉnh sửa
  const editButton = document.querySelector(".button-wrapper button");
  if (editButton) {
    editButton.addEventListener("click", () => {
      window.location.href = `course-create-upload.html?quizId=${quiz.quizId}`;
    });
  }
   const addQuestionBtn = document.getElementById("add-question-btn");
  if (addQuestionBtn) {
    addQuestionBtn.addEventListener("click", () => {
      window.location.href = `add-question.html?quizId=${quiz.quizId}`;
    });
  }

  
});

document.addEventListener("DOMContentLoaded", () => {
  const queryParams = new URLSearchParams(window.location.search);
  const quizId = parseInt(queryParams.get("quizId"));
  const questionListEl = document.getElementById("question-list");

  const questionData = JSON.parse(localStorage.getItem(`questions-${quizId}`)) || [];

  if (questionData.length === 0) {
    questionListEl.innerHTML = "<li>Chưa có câu hỏi nào được thêm.</li>";
    return;
  }

  questionData.forEach((q, index) => {
    const li = document.createElement("li");
    li.innerHTML = `
      Câu ${index + 1}: ${q.title || "(chưa có tiêu đề)"}
      <button class="remove-btn" onclick="removeQuestion(${index})">✖</button>
    `;
    questionListEl.appendChild(li);
  });

  const h4 = document.querySelector(".quiz-summary h4");
  if (h4) {
    h4.innerHTML = `Danh sách câu hỏi trắc nghiệm: <strong>${questionData.length} câu</strong>`;
  }

  // Nút chuyển đến add-question.html
  document.getElementById("add-question-btn").addEventListener("click", () => {
    window.location.href = `add-question.html?quizId=${quizId}`;
  });
});

// Hàm xoá câu hỏi khỏi danh sách
function removeQuestion(index) {
  const queryParams = new URLSearchParams(window.location.search);
  const quizId = parseInt(queryParams.get("quizId"));

  const questionData = JSON.parse(localStorage.getItem(`questions-${quizId}`)) || [];
  questionData.splice(index, 1);
  localStorage.setItem(`questions-${quizId}`, JSON.stringify(questionData));

  // Reload lại trang sau khi xoá
  location.reload();
}
