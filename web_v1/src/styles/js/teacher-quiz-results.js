import { StatisticsQuiz } from "../../api/QuizService.js";

// Hàm gọi API
function getStatisticsQuiz(data) {
  return StatisticsQuiz(data)
    .then(res => res.result)
    .catch(err => {
      console.error("Lỗi khi tải quiz:", err);
      return null;
    });
}

// Hàm hiển thị loading
function showLoading() {
  const overlay = document.getElementById("loading-overlay");
  if (overlay) overlay.style.display = "flex";
}

// Hàm ẩn loading
function hideLoading() {
  const overlay = document.getElementById("loading-overlay");
  if (overlay) overlay.style.display = "none";
}

document.addEventListener("DOMContentLoaded", async () => {
  const container = document.getElementById("quiz-result-container");
  const quizID = new URLSearchParams(window.location.search).get("quizId");

  if (!quizID) {
    container.innerHTML = "<p>Không tìm thấy quizID.</p>";
    return;
  }

  showLoading();

  try {
    const api = await getStatisticsQuiz(quizID);
    const results = api.results;
    console.log("API Response:", results);

    const quizBox = document.createElement("div");
    quizBox.className = "quiz-score-box";

    const title = document.createElement("h3");
    title.textContent = `Danh sách kết quả bài kiểm tra (ID: ${api.quizID})`;
    quizBox.appendChild(title);

    if (results.length === 0) {
      const emptyNote = document.createElement("p");
      emptyNote.textContent = " Chưa có học sinh nào làm bài này.";
      quizBox.appendChild(emptyNote);
    } else {
      // 👉 Bọc tất cả studentDiv trong 1 div để scroll
      const scrollWrapper = document.createElement("div");
      scrollWrapper.className = "quiz-result-scroll";

      results.forEach(result => {
        const studentDiv = document.createElement("div");
        studentDiv.style.borderTop = "1px dashed #ccc";
        studentDiv.style.padding = "8px 10px";
        studentDiv.style.backgroundColor = "white";
        studentDiv.style.borderRadius = "10px";
        studentDiv.style.margin = "10px 5px";

        studentDiv.innerHTML = `
          <p><strong> Student ID:</strong> ${result.studentID}</p>
          <p> Số câu đúng: ${result.totalCorrectAnswers}</p>
          <p> Điểm: ${result.score}/10</p>
        `;

        scrollWrapper.appendChild(studentDiv);
      });

      quizBox.appendChild(scrollWrapper); // ✅ Thêm scroll container vào quizBox
    }

    container.appendChild(quizBox);
  } catch (err) {
    console.error("Lỗi khi tải dữ liệu:", err);
    container.innerHTML = "<p>Đã xảy ra lỗi khi tải kết quả bài trắc nghiệm.</p>";
  } finally {
    hideLoading();
  }
});

// Hiển thị tên người dùng
document.addEventListener("DOMContentLoaded", () => {
  const username = localStorage.getItem("username");
  const welcomeEl = document.getElementById("welcome-name");
  if (username && welcomeEl) {
    welcomeEl.textContent = `Xin chào ${username}`;
  }
});

// Hiển thị số lượng quiz đã tạo
document.addEventListener("DOMContentLoaded", () => {
  const quizCount = localStorage.getItem("quizCreatedCount");
  const countElement = document.getElementById("quiz-count");
  if (quizCount !== null && countElement) {
    countElement.textContent = quizCount;
  }
});
