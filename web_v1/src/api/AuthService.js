import fetchAPI from './router.js';

const ACCOUNT_SERVICE = "/account_service";

export const login = (data) => {
  return fetchAPI(`${ACCOUNT_SERVICE}/auth/public/login`, "POST", data);
};
