import {connect, createChat, sendChat, disconnect} from "./chat-widget2.js";
document.addEventListener("DOMContentLoaded", () => {
  const container = document.getElementById("my-chatbox-container");
  const queryParams = new URLSearchParams(window.location.search);
  const quizId = queryParams.get("id");
  container.innerHTML = `
    <div id="chat-widget">
      <!-- Nút tròn để mở chat -->
      <div id="chat-toggle">💬</div>

      <!-- Hộp chat -->
      <div id="chat-box" style="display:none; flex-direction: column;">
        <div id="chat-header">
          <span id="chat-title">Trợ lý AI</span> 
          <span id="chat-close">✖</span> <!-- Nút đóng -->
        </div>

        <!-- Vùng hiển thị tin nhắn -->
        <div id="chat-messages"></div>
        <!-- Vùng nhập tin nhắn -->
        <div id="chat-input-area">
          <input type="text" id="chat-input" placeholder="Nhập tin nhắn..." />
          <button id="chat-send">Gửi</button>
        </div>
      </div>
    </div>
  `;

  
  const toggle = container.querySelector("#chat-toggle");   
  const box = container.querySelector("#chat-box");         
  const close = container.querySelector("#chat-close");     
  const sendBtn = container.querySelector("#chat-send");    
  const input = container.querySelector("#chat-input");     
  const messages = container.querySelector("#chat-messages"); 
  const title = container.querySelector("#chat-title");

  toggle.addEventListener("click", () => {
    box.style.display = "flex";    
    toggle.style.display = "none"; 
    
    const type = quizId != null ? "QUESTION" : "SUPPORT";
    title.textContent = type === "QUESTION" ? "Quiz thêm câu hỏi" : "Quiz Tư vấn";
    connect(type)
  });

  close.addEventListener("click", () => {
    box.style.display = "none";     
    toggle.style.display = "flex";  

    disconnect()
  });

  
  sendBtn.addEventListener("click", () => {
    const userMessage = input.value.trim();
    if (userMessage) {
      sendChat(quizId,userMessage)
      input.value = ""
    }
  });

  
  function addMessage(sender, text) {
    const msg = document.createElement("div");
    msg.textContent = `${sender}: ${text}`;
    msg.style.marginBottom = "8px";
    messages.appendChild(msg);

    // Cuộn xuống dưới cùng khi có tin mới
    messages.scrollTop = messages.scrollHeight;
  }
});
