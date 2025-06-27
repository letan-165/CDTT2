import { createQuiz } from '../api/QuizService.js';
import { topics } from '../styles/js/search-topic-fun.js';

function showLoading() {
  const overlay = document.getElementById("loading-overlay");
  if (overlay) overlay.style.display = "flex";
}

function hideLoading() {
  const overlay = document.getElementById("loading-overlay");
  if (overlay) overlay.style.display = "none";
}

document.addEventListener("DOMContentLoaded", () => {

  showLoading();

  const btn = document.getElementById("save-quiz");
  if (btn) {
    btn.addEventListener("click", handlecreateQuiz);

  }

  
     hideLoading();
});

function handlecreateQuiz() {
  const title = document.getElementById('quiz-title').value.trim();
  const description = document.getElementById('quiz-description').value.trim();
  const startTime = document.getElementById('quiz-start').value.trim();
  const endTime = document.getElementById('quiz-end').value.trim();
  const duration = document.getElementById('quiz-duration').value.trim();

    console.log("Dữ liệu người dùng nhập:");
  console.log("Title:", title);
  console.log("Description:", description);
  console.log("Start Time:", new Date(startTime).toISOString());
  console.log("End Time:", new Date(endTime).toISOString());
  console.log("Duration:", duration);

  if (!title || !description || !startTime || !endTime || !duration) {
    alert("Vui lòng nhập đầy đủ thông tin bài quiz!");
    return;
  }

  const teacherID = localStorage.getItem("userID");

    console.log("teacherID từ localStorage:", teacherID);


  if (!teacherID) {
    alert("Không tìm thấy userID! Vui lòng đăng nhập lại.");
    return;
  }

  const data = {
    teacherID,
    title,
    topics,
    description,
    startTime: new Date(startTime).toISOString(),
    endTime: new Date(endTime).toISOString(),
    duration
  };

  createQuiz(data)
    .then(res => {
      alert("Tạo bài quiz thành công!");
      console.log(res);
      window.location.href = "teacher-page.html";
    })
    .catch(err => {
      alert("Đã xảy ra lỗi khi tạo quiz!");
      console.error(err);
    });
}

window.handleCreateQuiz = handlecreateQuiz;