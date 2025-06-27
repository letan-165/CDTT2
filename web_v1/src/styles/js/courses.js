import { getQuizPublics, StatisticsResultStudents } from '../../api/QuizService.js';

document.addEventListener("DOMContentLoaded", async function () {
  const userID = localStorage.getItem("userID");
  if (!userID) return;

  try {
    const courseData = await getQuizs();          // Lấy tất cả bài quiz
    renderCourses(courseData);                    // Hiển thị bài quiz ra giao diện
    updateCompletedQuizCount(userID);             // Cập nhật số bài đã hoàn thành
    updateRunningCourseCount(courseData);         // Cập nhật số bài đang diễn ra
    updateTotalCourseCount(courseData);           // Cập nhật tổng số bài quiz
  } catch (err) {
    console.error("Lỗi khi khởi tạo:", err);
  }
});

// Gọi API lấy danh sách quiz công khai
function getQuizs() {
  return getQuizPublics()
    .then(res => res.result || [])
    .catch(err => {
      console.error("Lỗi khi lấy quiz công khai:", err);
      return [];
    });
}

// Hiển thị danh sách quiz trong course-list
function renderCourses(courseData) {
   const container = document.getElementById("course-list");
  if (!container) return;

  // Ẩn loading
  const loadingEl = document.getElementById("course-loading");
  if (loadingEl) {
    loadingEl.style.display = "none";
  }
  const img = "../styles/img/logo-icon.png";

  for (let course of courseData) {
    const courseHTML = `
      <div class="course-item">
        <img src="${img}" alt="${course.title}" />
        <div class="details">
          <strong>${course.title}</strong>
          <p>GV: ${course.teacherName}</p>
        </div>
        <div class="meta">
          <span>
            <img src="../styles/img/clock.png" alt="clock" class="icon" />
            ${course.startTime}
          </span>
          <button onclick="window.location.href='course-detail.html?id=${course.quizID}'">Xem</button>
        </div>
      </div>
    `;
    container.insertAdjacentHTML("beforeend", courseHTML);
  }
}

// Đếm số bài đã hoàn thành (có kết quả)
async function updateCompletedQuizCount(userID) {
  try {
    const resultList = await StatisticsResultStudents(userID);
    const completedCount = (resultList.result || []).length;

    const countElement = document.getElementById("completed-count");
    if (countElement) {
      countElement.textContent = completedCount;
    }
  } catch (err) {
    console.error("Lỗi khi lấy kết quả đã hoàn thành:", err);
  }
}

// Đếm số bài đang diễn ra (trong khoảng startTime - endTime)
function updateRunningCourseCount(courseData) {
  const now = new Date();
  const runningCount = courseData.filter(course => {
    const start = new Date(course.startTime);
    const end = new Date(course.endTime);
    return start <= now && now <= end;
  }).length;

  const countElement = document.getElementById("running-count");
  if (countElement) {
    countElement.textContent = runningCount;
  }
}

// Đếm tổng số bài quiz
function updateTotalCourseCount(courseData) {
  const totalCount = courseData.length;

  const countElement = document.getElementById("total-course-count");
  if (countElement) {
    countElement.textContent = totalCount;
  }
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

document.addEventListener("DOMContentLoaded", () => {
  const quizCount = localStorage.getItem("quizCreatedCount");
  if (quizCount !== null) {
    const countElement = document.getElementById("quiz-count");
    if (countElement) {
      countElement.textContent = quizCount;
    }
  }
});


document.addEventListener("DOMContentLoaded", () => {
  const name = localStorage.getItem("username") || "Chưa rõ";
  const id = localStorage.getItem("userID") || "Chưa rõ";
  const email = localStorage.getItem("email") || "Chưa rõ";
  const phone = localStorage.getItem("phone") || "Chưa rõ";

  const infoBox = document.getElementById("user-info");
  if (infoBox) {
    infoBox.innerHTML = `
      <p><strong>Tên:</strong> ${name}</p>
      <p><strong>ID:</strong> @${id}</p>
      <p><strong>Email:</strong> ${email}</p>
      <p><strong>SĐT:</strong> ${phone}</p>
    `;
  }

  const welcomeEl = document.getElementById("welcome-name");
  if (welcomeEl) {
    welcomeEl.textContent = `Xin chào ${name}`;
  }
});
