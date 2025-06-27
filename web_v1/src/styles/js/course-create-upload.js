import { getQuiz, createQuiz } from '../../api/QuizService.js';

document.addEventListener("DOMContentLoaded", async () => {
  const titleInput = document.getElementById("quiz-title");
  const durationInput = document.getElementById("quiz-duration");
  const endInput = document.getElementById("quiz-end");
  const startInput = document.getElementById("quiz-start");
  const topicInput = document.getElementById("quiz-topic");
  const descriptionInput = document.getElementById("quiz-description");

  const queryParams = new URLSearchParams(window.location.search);
  const quizID = queryParams.get("id") || queryParams.get("quizId");

  if (!quizID) {
    alert("Không có quizID trong URL!");
    return;
  }

  console.log("Quiz ID từ URL:", quizID);

  try {
    const response = await getQuiz(quizID);
    const quiz = response.result;

    if (!quiz) {
      alert("Không tìm thấy quiz từ server!");
      return;
    }

    console.log("Nội dung quiz từ server:", quiz);

    // Gán dữ liệu vào form
    titleInput.value = quiz.title || "";

    durationInput.value = String(quiz.duration ?? "");

    endInput.value = quiz.endTime?.slice(0, 16) || "";
    startInput.value = quiz.startTime?.slice(0, 16) || "";
    topicInput.value = Array.isArray(quiz.topics) ? quiz.topics.join(", ") : "";
    descriptionInput.value = quiz.description || "";

    // Khi bấm nút lưu
    document.getElementById("save-quiz").addEventListener("click", async () => {
      const updatedQuiz = {
        quizID: quiz.quizID,
        teacherID: quiz.teacherID || localStorage.getItem("userID"),
        title: titleInput.value.trim(),
        topics: topicInput.value.split(",").map(t => t.trim()),
        description: descriptionInput.value.trim(),
        startTime: new Date(startInput.value.trim()).toISOString(),
        endTime: new Date(endInput.value.trim()).toISOString(),
        duration: parseInt(durationInput.value.trim()),
        questions: quiz.questions || []
      };

      console.log("Quiz chuẩn bị gửi cập nhật:", updatedQuiz);

      try {
        const res = await createQuiz(updatedQuiz);
        console.log("Cập nhật thành công:", res);
        alert("Đã cập nhật bài Quizz thành công!");
        window.location.href = `add-question-repair.html?id=${quizID}`;
      } catch (err) {
        console.error("Lỗi khi cập nhật quiz:", err);
        alert("Cập nhật thất bại!");
      }
    });
  } catch (err) {
    console.error("Lỗi khi lấy quiz từ server:", err);
    alert("Không thể tải thông tin quiz.");
  }
});
