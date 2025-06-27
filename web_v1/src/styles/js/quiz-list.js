import { getQuizPublics } from '../../api/QuizService.js';
// File: quiz-list.js
document.addEventListener("DOMContentLoaded",async function () {
  const quizListData = await getQuizs();

  const ul = document.querySelector(".quiz-list-scroll ul");

  if (ul) {
    quizListData.forEach(quiz => {
      const li = document.createElement("li");
      if (quiz.quizID) {
        li.innerHTML = `<a href="?id=${quiz.quizID}">${quiz.title}</a>`;
      } else {
        li.textContent = quiz.title;
      }
      ul.appendChild(li);
    });
  }
});

function getQuizs(){
  return getQuizPublics()
    .then(res => {
      return res.result
    })
    .catch(err => {
      console.error("Login error:", err);
    });
}