// courses.js
document.addEventListener("DOMContentLoaded", function () {
  const courseData = {
    1: {
      img: "../styles/img/java.png",
      title: "Khóa học Java",
      teacher: "Ngô Thịnh",
      time: "6h 30m",
      rating: "4.9"
    },
    2: {
      img: "../styles/img/python.png",
      title: "Khóa học Python",
      teacher: "Quốc Dũng",
      time: "5h 15m",
      rating: "4.8"
    },
    3: {
      img: "../styles/img/c-sharp.png",
      title: "Khóa học C#",
      teacher: "Hoàng Phú",
      time: "6h 30m",
      rating: "4.9"
    },
    4: {
      img: "../styles/img/js.png",
      title: "Khóa học JS",
      teacher: "Ngô Thịnh",
      time: "7h 00m",
      rating: "5.0"
    }
  };

  const container = document.getElementById("course-list");

  for (let id in courseData) {
    const course = courseData[id];
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
          <button onclick="window.location.href='course-detail.html?id=${id}'">Xem</button>
        </div>
      </div>
    `;
    container.insertAdjacentHTML("beforeend", courseHTML);
  }
});

course-detail