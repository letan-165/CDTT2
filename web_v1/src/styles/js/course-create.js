document.addEventListener("DOMContentLoaded", () => {
  // Nếu chưa có phiên hoạt động thì reset localStorage
  if (!sessionStorage.getItem("quizSessionStarted")) {
    localStorage.removeItem("quizList"); // Xoá quiz cũ
    sessionStorage.setItem("quizSessionStarted", "true");
    console.log("Đã reset quizList khi bắt đầu phiên mới.");
  } else {
    console.log("Đã có phiên hoạt động, giữ nguyên quizList.");
  }

  // Xử lý khi người dùng nhấn nút lưu quiz
  document.getElementById("save-quiz").addEventListener("click", () => {
    const title = document.getElementById("quiz-title").value.trim();
    const duration = document.getElementById("quiz-duration").value.trim();
    const end = document.getElementById("quiz-end").value.trim();
    const start = document.getElementById("quiz-start").value.trim();
    const topic = document.getElementById("quiz-topic").value.trim();
    const description = document.getElementById("quiz-description").value.trim();

    // Kiểm tra input đầy đủ
    if (!title || !duration || !end || !start || !topic || !description) {
      alert("Vui lòng nhập đầy đủ thông tin trước khi lưu.");
      return;
    }

    // Tạo đối tượng quiz mới
    const newQuiz = {
      quizId: Date.now(), // ID duy nhất dựa trên timestamp
      title,
      duration,
      end,
      start,
      topic,
      description,
      img: "../styles/img/quiz.png", // Ảnh mặc định
      teacher: "Fushiree",           // Bạn có thể thay bằng tên người dùng
      time: duration,
      rating: "Chưa có"
    };

    // Lấy danh sách quiz hiện tại
    let quizList = JSON.parse(localStorage.getItem("quizList")) || [];
    quizList.push(newQuiz); // Thêm vào danh sách

    // Lưu lại vào localStorage
    localStorage.setItem("quizList", JSON.stringify(quizList));

    alert("Bài Quizz đã được lưu thành công!");
    window.location.href = "teacher-page.html"; // Quay về trang chính
  });
});
