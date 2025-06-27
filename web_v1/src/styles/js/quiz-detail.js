import { getQuizPublics, joinQuiz, StatisticsResultStudents } from '../../api/QuizService.js';
import { createChats } from '../../api/ChatService.js'; // 👈 bạn đã tạo hàm này

document.addEventListener("DOMContentLoaded", async function () {
  const quizData = await getQuizs();
  const container = document.getElementById("quiz-detail");
  const id = new URL(window.location.href).searchParams.get("id");

  renderQuiz(id);

  // Xóa localStorage quiz nếu là lần đầu
  if (!sessionStorage.getItem("hasVisited")) {
    Object.keys(localStorage).forEach((key) => {
      if (key.startsWith("quiz_") || key === "quizStatus" || key === "quizScoresData") {
        localStorage.removeItem(key);
      }
    });
    sessionStorage.setItem("hasVisited", "true");
  }

  async function renderQuiz() {
    const quiz = quizData.find(q => q.quizID === id);
    const quizStatus = JSON.parse(localStorage.getItem("quizStatus")) || {};

    const quiz_detail = `
       <div class="quiz-title-row">
    <img src="../styles/img/logo-icon.png" alt="quiz icon" class="icon-title" />
    <strong>${quiz.title}</strong>
  </div>
  <div class="quiz-content">
    <p class="teacher-info">
      GV: ${quiz.teacherName} • 
      <a href="#" id="contact-link" data-teacher="${quiz.teacherName}">Liên hệ GV</a>
    </p>
    <span class="quiz-duration">
      <img src="../styles/img/clock.png" alt="clock" class="icon-duration" />
      ${quiz.duration} phút
    </span>
      </div>
      </div>
      <div class="quiz-body">
        <strong>${quiz.title}</strong>
        <p><strong>Nội dung bài quiz:</strong></p>
        <p>${quiz.description}</p>
        <p><strong>Bắt đầu vào ngày:</strong> ${quiz.startTime}</p>
        <p><strong>Kết thúc vào ngày:</strong> ${quiz.endTime}</p>
    `;

    if (quiz && container) {
      if (quizStatus[id] === "done") {
        const attempts = JSON.parse(localStorage.getItem("quizAttempts")) || {};
        const attemptCount = attempts[id] || 1; // mặc định là 1 nếu không có dữ liệu

        container.innerHTML = `
          <div class="quiz-header">
            ${quiz_detail}
            <p style="color: green; font-weight: bold;">
              Bạn đã hoàn thành bài trắc nghiệm này ${attemptCount} lần
            </p>
            <button class="retake-btn">Làm lại bài trắc nghiệm</button>
            <button class="score-btn">Xem điểm</button>
          </div>
        `;


        container.querySelector(".retake-btn").addEventListener("click", () => {
          showLoading();
          const data = {
            studentID: localStorage.getItem("userID"),
            quizID: quiz.quizID
          };
          getJoinQuiz(data, quiz);
        });

        container.querySelector(".score-btn").addEventListener("click", () => {
          window.location.href = "quiz-answer-score.html?quizId=" + id;
        });

      } else {
        container.innerHTML = `
          <div class="quiz-header">
            ${quiz_detail}
            <button class="start-btn" id="start-btn">Bắt đầu làm bài</button>
          </div>
        `;

        const startBtn = container.querySelector(".start-btn");
        startBtn.addEventListener("click", function () {
          showLoading();
          const data = {
            studentID: localStorage.getItem("userID"),
            quizID: quiz.quizID
          };
          getJoinQuiz(data, quiz);
        });
      }

      // Gắn sự kiện click cho "Liên hệ GV"
      const contactLink = document.getElementById("contact-link");
      if (contactLink) {
        contactLink.addEventListener("click", async function (e) {
          e.preventDefault();

          const teacherName = contactLink.dataset.teacher;
          const studentName = localStorage.getItem("username"); // Lưu trong login

          if (!studentName || !teacherName) {
            alert("Không thể liên hệ giáo viên. Vui lòng đăng nhập lại.");
            return;
          }

          await createCourseChat(studentName, teacherName);
        });
      }

    } else {
      container.innerHTML = `<p>Không tìm thấy bài học này.</p>`;
    }
  }

  const viewAllScoresBtn = document.getElementById("view-all-scores-btn");
  if (viewAllScoresBtn) {
    viewAllScoresBtn.addEventListener("click", async () => {
      const studentID = localStorage.getItem("userID");
      const quizResults = await fetchStudentQuizResults(studentID);
      const container = document.getElementById("quiz-detail");
      container.innerHTML = "";

      const title = document.createElement("h3");
      title.textContent = "Kết quả các bài trắc nghiệm";
      container.appendChild(title);

      const scoreListContainer = document.createElement("div");
      scoreListContainer.className = "quiz-score-list";
      container.appendChild(scoreListContainer);

      quizResults.forEach(result => {
        const quiz = result.quiz;
        const quizDiv = document.createElement("div");
        quizDiv.className = "quiz-score-box";

        quizDiv.innerHTML = `
          <div class="quiz-score-title"><strong>Buổi ${quiz.startTime}: ${quiz.title}</strong></div>
          <p>Làm vào ngày: ${new Date(result.startTime).toLocaleDateString()}</p>
          <p>Số câu đúng: ${result.totalCorrectAnswers}/${quiz.questions.length}</p>
          <p>Số điểm đạt được: ${result.score}/10</p>
          <button class="detail-btn" data-id="${result.resultID}">Xem chi tiết bài trắc nghiệm</button>
        `;
        scoreListContainer.appendChild(quizDiv);
      });

      scoreListContainer.querySelectorAll(".detail-btn").forEach(btn => {
        btn.addEventListener("click", function () {
          const resultID = this.dataset.id;
          window.location.href = `quiz-answer-score.html?resultID=${resultID}`;
        });
      });
    });
  }
});

// === Các hàm phụ ===

function getQuizs() {
  return getQuizPublics()
    .then(res => res.result)
    .catch(err => {
      console.error("Lỗi khi lấy quiz:", err);
    });
}

function getJoinQuiz(data, quiz) {
  joinQuiz(data)
    .then(res => {
      const resultRes = res.result;
      localStorage.setItem("quiz", JSON.stringify(quiz));
      localStorage.setItem("resultRes", JSON.stringify(resultRes));

      const now = Date.now();
      const durationMs = Number(quiz.duration) * 60 * 1000;
      localStorage.setItem("quizEndTime", now + durationMs);

      window.location.href = "quiz.html?id=" + quiz.quizID;
      localStorage.removeItem("indexPage");
    })
    .catch(err => {
      console.error("Lỗi khi vào quiz:", err);
    });
}

function fetchStudentQuizResults(studentID) {
  return StatisticsResultStudents(studentID)
    .then(res => res.result || [])
    .catch(err => {
      console.error("Lỗi khi lấy kết quả:", err);
      return [];
    });
}

function showLoading() {
  const overlay = document.getElementById("loading-overlay");
  if (overlay) overlay.style.display = "flex";
}

// Hàm gọi API tạo hoặc lấy cuộc trò chuyện
async function createCourseChat(name1, name2) {
  try {
    const res = await createChats(name1, name2); // gọi GET API

    if (res.result && res.result.chatID) {
      const chatID = res.result.chatID;
      window.location.href = `chat_course-form.html?chatID=${chatID}`;
    } else {
      alert("Không thể tạo hoặc tìm thấy cuộc trò chuyện.");
    }
  } catch (err) {
    console.error("Lỗi khi tạo chat:", err);
    alert("Đã xảy ra lỗi khi tạo cuộc trò chuyện.");
  }
}
