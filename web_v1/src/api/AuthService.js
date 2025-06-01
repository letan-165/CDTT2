import fetchAPI from './router.js';

const AUTH_SERVICE="/auth_service";

export const login = (data) => {
  return fetchAPI(`${AUTH_SERVICE}/user/public`, "POST", data);
};
