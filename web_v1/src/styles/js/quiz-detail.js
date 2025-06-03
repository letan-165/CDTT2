document.addEventListener("DOMContentLoaded", function () {
  const params = new URLSearchParams(window.location.search);
  const quizId = parseInt(params.get("id"));

  const quizData = {
    1: {
      title: "Buổi 1. 08/05/2025: C++ cơ bản",
      teacher: "Lê Tân",
      time: "10 phút • 75%",
      content: "Đọc tài liệu về C++ cơ bản và làm bài trắc nghiệm kiểm tra kiến thức.",
      start: "08/05/2025",
      end: "10/05/2025"
    },
    2: {
      title: "Buổi 2. 10/05/2025: C++ nâng cao",
      teacher: "Nguyễn Văn A",
      time: "15 phút • 80%",
      content: "Nắm vững kiến thức nâng cao về C++ và thực hành.",
      start: "10/05/2025",
      end: "12/05/2025"
    }
  };

  const quiz = quizData[quizId];
  const container = document.getElementById("quiz-detail");

  if (quiz && container) {
    container.innerHTML = `
      <div class="quiz-header">
        <strong>${quiz.title}</strong>
        <p>GV: ${quiz.teacher} • <a href="#">Liên hệ GV</a></p>
        <span>⏰ ${quiz.time}</span>
      </div>
      <div class="quiz-body">
        <strong>${quiz.title}</strong>
        <p><strong>Nội dung buổi học:</strong></p>
        <p>${quiz.content}</p>
        <p><strong>Bắt đầu vào ngày:</strong> ${quiz.start}</p>
        <p><strong>Kết thúc vào ngày:</strong> ${quiz.end}</p>
        <button class="start-btn">Bắt đầu làm bài</button>
      </div>
    `;
  } else {
    if (container) container.innerHTML = "<p>Không tìm thấy bài học này.</p>";
  }
});
