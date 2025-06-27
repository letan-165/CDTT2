import fetchAPI from './router.js';

const CHAT_SERVICE = "/chat_service";

export const getChats = (name) => {
  return fetchAPI(`${CHAT_SERVICE}/chat/public/${name}`, "GET");
};


export const createChats = (name1, name2) => {
  return fetchAPI(`${CHAT_SERVICE}/chat/public/${name1}/${name2}`, "POST");
};