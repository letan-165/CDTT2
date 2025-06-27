import {connect, getNotification,disconnect} from "./main-new.js";
document.addEventListener("DOMContentLoaded", () => {
  connect();

  
  window.addEventListener("beforeunload", () => {
    disconnect();
  });
});