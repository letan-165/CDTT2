document.addEventListener("DOMContentLoaded", () => {
  const queryParams = new URLSearchParams(window.location.search);
  const quizId = parseInt(queryParams.get("quizId"));

  const questionList = document.getElementById("question-list");
  const quizFormsContainer = document.getElementById("quiz-forms-container");
  const addQuestionBtn = document.getElementById("add-question-btn");
  const saveAllBtn = document.getElementById("save-all-btn");

  let currentFormId = null;
  let questionData = JSON.parse(localStorage.getItem(`questions-${quizId}`)) || [];

  function renderQuestionItem(question, index) {
    const form = createQuizForm(question.id, question);
    quizFormsContainer.appendChild(form);

    const li = document.createElement("li");
    li.id = `question-item-${question.id}`;
    li.innerHTML = `
      <span class="question-text">Câu ${index + 1}: ${question.title || "(chưa có tiêu đề)"}</span>
      <button class="remove-btn">\u2716</button>
    `;

    li.addEventListener("click", () => {
      hideAllForms();
      form.style.display = "block";
      currentFormId = question.id;
    });

    li.querySelector(".remove-btn").addEventListener("click", (e) => {
      e.stopPropagation();
      li.remove();
      form.remove();
      questionData = questionData.filter(q => q.id !== question.id);
      localStorage.setItem(`questions-${quizId}`, JSON.stringify(questionData));
      location.reload();
    });

    questionList.appendChild(li);
  }

  questionData.forEach((q, index) => {
    renderQuestionItem(q, index);
  });

  addQuestionBtn.addEventListener("click", () => {
    const uniqueId = Date.now();
    const newQuestion = {
      id: uniqueId,
      title: "",
      answer: "",
      correct: "",
      type: "input" // default
    };

    questionData.push(newQuestion);
    localStorage.setItem(`questions-${quizId}`, JSON.stringify(questionData));
    renderQuestionItem(newQuestion, questionData.length - 1);

    hideAllForms();
    document.getElementById(`quiz-form-${uniqueId}`).style.display = "block";
    currentFormId = uniqueId;
  });

  saveAllBtn.addEventListener("click", () => {
    const forms = document.querySelectorAll(".quiz-form");
    const updatedData = [];

    forms.forEach((form) => {
      const id = parseInt(form.id.replace("quiz-form-", ""));
      const title = form.querySelector(`input[id^='question-title']`).value.trim();
      const answer = form.querySelector(`textarea[id^='answer']`)?.value.trim() || "";
      const correct = form.querySelector(`textarea[id^='correct']`).value.trim();
      const typeRadio = form.querySelector(`input[name='type-${id}']:checked`);
      const type = typeRadio ? typeRadio.value : "input";

      updatedData.push({ id, title, answer, correct, type });

      const liText = document.querySelector(`#question-item-${id} .question-text`);
      if (liText) liText.textContent = `Câu: ${title || "(chưa có tiêu đề)"}`;
    });

    questionData = updatedData;
    localStorage.setItem(`questions-${quizId}`, JSON.stringify(questionData));
    alert("✅ Đã lưu câu hỏi!");
  });

  function hideAllForms() {
    document.querySelectorAll(".quiz-form").forEach(form => form.style.display = "none");
  }

function createQuizForm(id, data = {}) {
  const form = document.createElement("div");
  form.className = "quiz-form";
  form.id = `quiz-form-${id}`;
  form.style.display = "none";

  form.innerHTML = `
    <label>Tiêu đề:</label>
    <input type="text" id="question-title-${id}" value="${data.title || ""}" placeholder="Nhập tiêu đề..." style="border-radius: 25px">

    <label>Loại:</label>
    <div class="question-type">
      <label><input type="radio" name="type-${id}" value="multi" ${data.type === "multi" ? "checked" : ""}> Chọn nhiều đáp án</label>
      <label><input type="radio" name="type-${id}" value="single" ${data.type === "single" ? "checked" : ""}> Chọn 1 đáp án</label>
      <label><input type="radio" name="type-${id}" value="input" ${!data.type || data.type === "input" ? "checked" : ""}> Nhập đáp án</label>
    </div>

    <div class="answer-group" id="answer-group-${id}" style="margin-top:10px">
      <label>Các câu trả lời:</label>
      <textarea id="answer-${id}" placeholder="Nhập các câu trả lời..." style="height:100px; border-radius: 15px">${data.answer || ""}</textarea>
    </div>

    <div class="correct-group" id="correct-group-${id}" style="margin-top:10px">
      <label id="correct-label-${id}">Câu trả lời đúng:</label>
      <textarea id="correct-textarea-${id}" placeholder="Nhập câu trả lời đúng..." style="height:100px; border-radius: 15px">${data.correct || ""}</textarea>
      <input type="text" id="correct-input-${id}" placeholder="Nhập câu trả lời đúng..." value="${data.correct || ""}" style="display:none; border-radius: 15px">
    </div>
  `;

  const radioInputs = form.querySelectorAll(`input[name='type-${id}']`);
  const answerGroup = form.querySelector(`#answer-group-${id}`);
  const correctGroup = form.querySelector(`#correct-group-${id}`);
  const correctLabel = form.querySelector(`#correct-label-${id}`);
  const correctTextarea = form.querySelector(`#correct-textarea-${id}`);
  const correctInput = form.querySelector(`#correct-input-${id}`);

  function updateVisibility(type) {
    if (type === "multi") {
      answerGroup.style.display = "block";
      correctLabel.textContent = "Các câu trả lời đúng:";
      correctTextarea.style.display = "block";
      correctInput.style.display = "none";
    } else if (type === "single") {
      answerGroup.style.display = "block";
      correctLabel.textContent = "Câu trả lời đúng:";
      correctTextarea.style.display = "none";
      correctInput.style.display = "block";
    } else if (type === "input") {
      answerGroup.style.display = "none";
      correctLabel.textContent = "Câu trả lời đúng:";
      correctTextarea.style.display = "block";
      correctInput.style.display = "none";
    }
  }

  radioInputs.forEach(radio => {
    radio.addEventListener("change", () => updateVisibility(radio.value));
    if (radio.checked) updateVisibility(radio.value);
  });

  return form;
}
});
