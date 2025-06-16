import fetchAPI from './router.js';

const ACCOUNT_SERVICE = "/account_service";

export const signUp = (data) => {
  return fetchAPI(`${ACCOUNT_SERVICE}/user/public`, "POST", data);
};

export const forgotPassword = (data) => {
  return fetchAPI(`${ACCOUNT_SERVICE}/user/public/forgotPassword`, "PUT", data);
};