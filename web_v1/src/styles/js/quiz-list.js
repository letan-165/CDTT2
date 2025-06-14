// File: quiz-list.js
document.addEventListener("DOMContentLoaded", function () {
  const quizListData = [
    { id: 1, title: "Buổi 1. 08/05/2025: C++ cơ bản" },
    { id: 2, title: "Buổi 2. 10/05/2025: C++ nâng cao" },
    { id: 3, title: "Buổi 3. 12/05/2025: Con trỏ & Class" },
    { id: null, title: "Buổi 4. 14/05/2025: OOP nâng cao" },
    { id: null, title: "Buổi 5. 16/05/2025: Dự án nhỏ" },
    { id: null, title: "Buổi 6. 18/05/2025: Review tổng hợp" },
    { id: null, title: "Buổi 7. 20/05/2025: Ôn tập" },
    { id: null, title: "Buổi 8. 22/05/2025: Thi thử" },
    { id: null, title: "Buổi 9. 24/05/2025: Dự án thực tế" },
    { id: null, title: "Buổi 10. 26/05/2025: Bảo vệ đồ án" }
  ];

  const ul = document.querySelector(".quiz-list-scroll ul");

  if (ul) {
    quizListData.forEach(quiz => {
      const li = document.createElement("li");
      if (quiz.id) {
        li.innerHTML = `<a href="?id=${quiz.id}">${quiz.title}</a>`;
      } else {
        li.textContent = quiz.title;
      }
      ul.appendChild(li);
    });
  }
});
