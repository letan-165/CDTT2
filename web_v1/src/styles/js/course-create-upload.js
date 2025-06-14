document.addEventListener("DOMContentLoaded", () => {
  const titleInput = document.getElementById("quiz-title");
  const durationInput = document.getElementById("quiz-duration");
  const endInput = document.getElementById("quiz-end");
  const startInput = document.getElementById("quiz-start");
  const topicInput = document.getElementById("quiz-topic");
  const descriptionInput = document.getElementById("quiz-description");

  const queryParams = new URLSearchParams(window.location.search);
  const quizId = parseInt(queryParams.get("quizId"));

  if (isNaN(quizId)) {
    alert("ID bài Quizz không hợp lệ.");
    return;
  }

  let quizList = JSON.parse(localStorage.getItem("quizList")) || [];
  let quizIndex = quizList.findIndex(q => q.quizId === quizId);

  if (quizIndex === -1) {
    alert("Không tìm thấy bài Quizz!");
    return;
  }

  const quiz = quizList[quizIndex];

  // Hiển thị dữ liệu lên form
  titleInput.value = quiz.title || "";
  durationInput.value = quiz.duration || "";
  endInput.value = quiz.end || "";
  startInput.value = quiz.start || "";
  topicInput.value = quiz.topic || "";
  descriptionInput.value = quiz.description || "";

  // Khi người dùng bấm lưu cập nhật
  document.getElementById("save-quiz").addEventListener("click", () => {
    const updatedQuiz = {
      ...quiz, // giữ lại quizId, teacher, img, rating
      title: titleInput.value.trim(),
      duration: durationInput.value.trim(),
      end: endInput.value.trim(),
      start: startInput.value.trim(),
      topic: topicInput.value.trim(),
      description: descriptionInput.value.trim(),
      time: durationInput.value.trim()
    };

    quizList[quizIndex] = updatedQuiz;
    localStorage.setItem("quizList", JSON.stringify(quizList));
    alert("Đã cập nhật bài Quizz thành công!");

    // Quay lại trang add-question-repair của quiz hiện tại
    window.location.href = `add-question-repair.html?id=${quizId}`;
  });
});
