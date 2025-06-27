import fetchAPI from './router.js';

const QUIZ_SERVICE = "/quiz_service";


export const getQuiz = (data) => {
  return fetchAPI(`${QUIZ_SERVICE}/quiz/public/${data}`, "GET");
};

export const getQuizPublics = () => {
  return fetchAPI(`${QUIZ_SERVICE}/quiz/public`, "GET");
};

export const getQuizsByTeacher = (data) => {
  return fetchAPI(`${QUIZ_SERVICE}/quiz/public/teacher/${data}`, "GET");
};

export const createQuiz = (data) => {
  return fetchAPI(`${QUIZ_SERVICE}/quiz/public`, "POST", data);
};


export const joinQuiz = (data) => {
  return fetchAPI(`${QUIZ_SERVICE}/result/public/join`, "POST", data);
};


export const submitQuiz = (data) => {
  return fetchAPI(`${QUIZ_SERVICE}/result/public/submit`, "PUT", data);
};

export const finishQuiz = (data) => {
  return fetchAPI(`${QUIZ_SERVICE}/result/public/${data}/finish`, "PUT");
};


export const StatisticsResultStudents = (data) => {
  return fetchAPI(`${QUIZ_SERVICE}/result/public/student/${data}/statistics`, "GET");
};

export const StatisticsQuiz = (data) => {
  return fetchAPI(`${QUIZ_SERVICE}/result/public/quiz/${data}/statistics`, "GET");
};