import {connect, getChat, sendChat, recallChat, disconnect} from "./chat-course.js"; // đảm bảo đúng đuôi .js và đúng đường dẫn
import { getChats } from '../../api/ChatService.js';


document.addEventListener("DOMContentLoaded", async () => {
  const chatList = document.querySelector(".chat-list");
  const chatHeader = document.querySelector(".chat-header strong");
  const name = localStorage.getItem("username")
  const sendBtnEl = document.querySelector(".send-btn");


  let chats = await getChatsFun(name)
  let receiverRight
  chats.forEach(chat => {
    const receiverLeft = (chat.user!=name) ? chat.user : chat.user2
    const li = document.createElement("li");
    const message = (chat.messages && chat.messages.length > 0 && chat.messages[0].content) 
    ? chat.messages[0].content 
    : "";

    const time = (chat.messages && chat.messages.length > 0 && chat.messages[0].time) 
      ? chat.messages[0].time 
      : "";


    li.innerHTML = `
      <div class="chat-info">
        <div class="name">${receiverLeft}</div>
        <div class="last-msg">${message}</div>
      </div>
      <div class="chat-meta">
        <div class="date">${time}</div>
      </div>
    `;

    li.addEventListener("click", () => {
      chatHeader.textContent = receiverLeft;
      receiverRight = receiverLeft
      getChat(receiverLeft);
    });

    chatList.appendChild(li);

    
  });

  // Kết nối WebSocket sau khi DOM load xong
  connect();

  sendBtnEl.addEventListener("click", () => {
      const textInput = document.getElementById("input-text");
      console.log("Đã click gửi:", textInput.value);
      sendChat(receiverRight,textInput.value )
  });

  
})
function getChatsFun(data){
  return getChats(data)
    .then(res => {
      return res.result
    })
    .catch(err => {
      console.error("Login error:", err);
    });
}