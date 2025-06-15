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
  };

  const container = document.getElementById("quiz-detail");

  // Kiểm tra nếu là lần đầu vào trang trong phiên trình duyệt này => reset trạng thái
  if (!sessionStorage.getItem("hasVisited")) {
    localStorage.removeItem("quizStatus");
    sessionStorage.setItem("hasVisited", "true");
  }

  function renderQuiz(id) {
    const quiz = quizData[id];
    const quizStatus = JSON.parse(localStorage.getItem("quizStatus")) || {};

    if (quiz && container) {
      if (quizStatus[id] === "done") {
        // Đã làm => hiển thị form kết quả
        container.innerHTML = `
        <div class="quiz-header">
            <strong>${quiz.title}</strong>
            <p>GV: ${quiz.teacher} • <a href="#">Liên hệ GV</a></p>
            <span>⏰ ${quiz.time}</span>
          </div>
          <div class="quiz-result-form">
            <strong>${quiz.title}</strong>
            <p><strong>Nội dung buổi học:</strong><br>
            ${quiz.content}</p>
            <p><strong>Bắt đầu vào ngày:</strong> ${quiz.start}</p>
            <p><strong>Kết thúc vào ngày:</strong> ${quiz.end}</p>
            <p style="color: green; font-weight: bold;">Bạn đã hoàn thành bài trắc nghiệm này 1 lần</p>
            <button class="retake-btn">Làm lại bài trắc nghiệm</button>
            <button class="score-btn">Xem điểm</button>
          </div>
        `;

        container.querySelector(".retake-btn").addEventListener("click", () => {
          window.location.href = "quiz.html?id=" + id;
        });

        container.querySelector(".score-btn").addEventListener("click", () => {
          window.location.href = "quiz-answer-score.html?id=" + id;
        });

      } else {
        // Chưa làm => hiển thị form bắt đầu
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
        startBtn.addEventListener("click", function () {
          const updatedStatus = JSON.parse(localStorage.getItem("quizStatus")) || {};
          updatedStatus[id] = "done";
          localStorage.setItem("quizStatus", JSON.stringify(updatedStatus));
          window.location.href = "quiz.html?id=" + id;
        });
      }
    } else {
      container.innerHTML = `<p>Không tìm thấy bài học này.</p>`;
    }
  }

  // Xử lý link các bài quiz khác
  const quizLinks = document.querySelectorAll(".related-quizzes a");
  quizLinks.forEach(link => {
    link.addEventListener("click", function (e) {
      e.preventDefault();
      const url = new URL(link.href);
      const id = parseInt(url.searchParams.get("id"));
      localStorage.setItem("selectedQuizId", id);
      renderQuiz(id);
    });
  });

  // Khi tải trang, hiển thị bài đã chọn
  const savedId = localStorage.getItem("selectedQuizId");
  if (savedId && quizData[savedId]) {
    renderQuiz(savedId);
  } else {
    renderQuiz(1);
    localStorage.setItem("selectedQuizId", 1);
  }

  
});


