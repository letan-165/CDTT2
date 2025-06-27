import { getQuizsByTeacher } from '../../api/QuizService.js';

document.addEventListener("DOMContentLoaded", async function () {
  const container = document.getElementById("course-list");
  const loadingEl = document.getElementById("course-loading");
  const teacherId = localStorage.getItem("userID");

  try {
    const response = await getQuizsByTeacher(teacherId);
    const quizList = response.result || [];

    if (loadingEl) loadingEl.style.display = "none";

    if (quizList.length === 0) {
      container.innerHTML = "<p>Không có khóa học nào.</p>";
      return;
    }

    quizList.forEach((quiz, index) => {
      console.log(`Quiz #${index + 1}`);
      console.log("quizID:", quiz.quizID);
      console.log("teacherID:", quiz.teacherID);
       console.log("teacherName:", quiz.teacherName);
      console.log("title:", quiz.title);
    });

    quizList.forEach((quiz) => {
      const course = {
        img: quiz.img || "../styles/img/java.png",
        title: quiz.title || "Không có tiêu đề",
        teacher: quiz.teacherName || "Không rõ",
        startTime: quiz.startTime || "Không rõ",
        endTime: quiz.endTime || "Không rõ",
        duration: quiz.duration ? `${quiz.duration} phút` : "Chưa rõ"
      };
      renderCourseItem(course, quiz.quizID);
    });

    updateQuizCount();

  } catch (err) {
    console.error("Lỗi khi gọi API:", err);
    if (loadingEl) loadingEl.style.display = "none";
    container.innerHTML = "<p>Lỗi khi tải khóa học.</p>";
  }

  function renderCourseItem(course, quizId = null) {
    const buttonLink = quizId !== null
      ? `add-question-repair.html?id=${quizId}`
      : "#";

    const courseHTML = `
      <div class="course-item">
        <img src="${course.img}" alt="${course.title}" />
        <div class="details">
          <strong>${course.title}</strong>
          <p>GV: ${course.teacher}</p>
        </div>
        <div class="meta">
          <span>
            <img src="../styles/img/clock.png" alt="clock" class="icon" />
            ${course.endTime}
          </span>
          <span>
            ${course.duration}
          </span>
          <button onclick="window.location.href='${buttonLink}'">Xem</button>
        </div>
      </div>
    `;
    container.insertAdjacentHTML("beforeend", courseHTML);
  }

  function updateQuizCount() {
    const count = container.querySelectorAll('.course-item').length;
    const countElement = document.getElementById("quiz-count");
    if (countElement) {
      countElement.textContent = count;
    }
    localStorage.setItem("quizCreatedCount", count);
  }
});

// Hiển thị tên giáo viên
document.addEventListener("DOMContentLoaded", () => {
  const username = localStorage.getItem("username");
  if (username) {
    const welcomeEl = document.getElementById("welcome-name");
    if (welcomeEl) {
      welcomeEl.textContent = `Xin chào ${username}`;
    }
  }
});