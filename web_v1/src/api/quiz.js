import fetchAPI from './router.js';

const QUIZ_SERVICE = "/quiz_service";

export const SaveQuestion = (questionList) => {
  return fetchAPI(`${QUIZ_SERVICE}/quiz/public/question`, "PUT", questionList);
};

export const DeleteQuestion = (questionList) => {
  return fetchAPI(`${QUIZ_SERVICE}/quiz/public/question/delete`, "PUT", questionList);
};

export const SearchName = (suggestion) => {
  return fetchAPI(`${QUIZ_SERVICE}/quiz/public/title/search`, "POST", suggestion);
};


export const SearchTopic = (suggestion) => {
  return fetchAPI(`${QUIZ_SERVICE}/quiz/public/topic/search`, "POST", suggestion);
};