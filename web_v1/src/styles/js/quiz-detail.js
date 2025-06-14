// File: quiz-detail.js
document.addEventListener("DOMContentLoaded", function () {
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
    },
    3: {
      title: "Buổi 3. 12/05/2025: Con trỏ & Class",
      teacher: "Trần Thị B",
      time: "20 phút • 70%",
      content: "Hiểu rõ về con trỏ trong C++ và cách làm việc với class.",
      start: "12/05/2025",
      end: "14/05/2025"
    }
    // Thêm các bài khác nếu cần
  };

  const container = document.getElementById("quiz-detail");

  function renderQuiz(id) {
    const quiz = quizData[id];
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
     const startBtn = container.querySelector(".start-btn");
    if (startBtn) {
      startBtn.addEventListener("click", function () {
        window.location.href = "quiz.html?id=" + id;
      });
    }
  } else if (container) {
    container.innerHTML = `<p>Không tìm thấy bài học này.</p>`;
  }
}

  // Gắn sự kiện click cho từng link
  const quizLinks = document.querySelectorAll(".related-quizzes a");
  quizLinks.forEach(link => {
    link.addEventListener("click", function (e) {
      e.preventDefault();

      const url = new URL(link.href);
      const id = parseInt(url.searchParams.get("id"));

      // Lưu vào localStorage
      localStorage.setItem("selectedQuizId", id);

      renderQuiz(id);
    });
  });

  // Khi tải trang, kiểm tra xem có id đã được chọn không
  const savedId = localStorage.getItem("selectedQuizId");

  if (savedId && quizData[savedId]) {
    renderQuiz(savedId);
  } else {
    // Nếu chưa chọn bài nào trước đó, mặc định hiển thị quiz id = 1
    renderQuiz(1);
    localStorage.setItem("selectedQuizId", 1);
  }
});


