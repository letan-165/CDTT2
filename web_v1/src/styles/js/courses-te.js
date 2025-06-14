document.addEventListener("DOMContentLoaded", function () {
  const defaultCourses = {
    1: {
      img: "../styles/img/java.png",
      title: "Khóa học Java",
      teacher: "Ngô Thịnh",
      time: "6h 30m",
      rating: "4.9"
    }
  };

  const container = document.getElementById("course-list");

  // Render mặc định
  for (let id in defaultCourses) {
    renderCourseItem(defaultCourses[id]);
  }

  // Render các quiz đã lưu
  const quizList = JSON.parse(localStorage.getItem("quizList")) || [];
  quizList.forEach((quiz) => {
    renderCourseItem(quiz, quiz.quizId); // Truyền quizId vào
  });

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
            ${course.time}
          </span>
          <span>
            <img src="../styles/img/pointed-star.png" alt="star" class="icon" />
            ${course.rating}
          </span>
          <button onclick="window.location.href='${buttonLink}'">Xem</button>
        </div>
      </div>
    `;
    container.insertAdjacentHTML("beforeend", courseHTML);
  }
});
