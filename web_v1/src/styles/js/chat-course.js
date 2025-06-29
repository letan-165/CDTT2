let stompClient = null;
//Chú ý những cái "stompClient"
const CONNECT = "http://localhost:8888/chat_service/chat-ws?token="
const SUBSCRIVE = "/user/queue/messages"
const GETCHAT = "/app/chat-ws.get"
const SENDCHAT = "/app/chat-ws.send"
const RECALLCHAT = "/app/chat-ws.recall"


export const connect = () =>  {
  const token = localStorage.getItem("token");
  //Kết nối
  const socket = new WebSocket(
    `${CONNECT}${token}`
  );
  stompClient = Stomp.over(socket);

  stompClient.connect(
    {},
    function (frame) {
      console.log("Connected: " + frame);
      stompClient.subscribe(SUBSCRIVE, function (message) {
        console.log("SUBSCRIVE",message.body);
        renderIncomingMessage(message.body);
      });

     
    },
    function (error) {
      console.error("Connection error:", error);
    }
  );
};
export const getChat = (receiver) => {
  if (stompClient && stompClient.connected) {
    const msg = {receiver}; 

    stompClient.send(GETCHAT, {}, JSON.stringify(msg));
    console.log("Đã gửi:", msg);
  } else {
    console.warn("Chưa kết nối!");
  }
};

export const sendChat = (receiver, content) => {
  if (stompClient && stompClient.connected) {
    const msg = {receiver,content};
    //Gửi tin nhắn
    stompClient.send(SENDCHAT, {}, JSON.stringify(msg));
    console.log("Đã gửi:", msg);
  } else {
    console.warn("Chưa kết nối!");
  }
};

export const recallChat = (chatID, index) => {
  if (stompClient && stompClient.connected) {
    const msg = {chatID, index};
    //Thu hồi tin nhắn
    stompClient.send(RECALLCHAT, {}, JSON.stringify(msg));
    console.log("Đã gửi:", msg);
  } else {
    console.warn("Chưa kết nối!");
  }
};

export const disconnect = () => {
  if (stompClient) {
    //Ngắt kết nối
    stompClient.disconnect(() => {
      console.log("Đã ngắt kết nối");
    });
  }
};


function renderIncomingMessage(data) {
  data = JSON.parse(data);
  console.log("data", data);

  const chatMessages = document.querySelector(".chat-messages");
  chatMessages.innerHTML = "";

  const currentUser = localStorage.getItem("username");

  if (Array.isArray(data.messages)) {
    data.messages.forEach((msg) => {
      const messageDiv = document.createElement("div");

      // Thêm class trái/phải tùy người gửi
      const isCurrentUser = msg.sender === currentUser;
      messageDiv.className = isCurrentUser ? "message user" : "message friend";

      // Nội dung tin nhắn
      messageDiv.innerHTML = `
        <div class="msg-content">
          <div class="msg-sender"><strong>${msg.sender}</strong></div>
          <div class="msg-text">${msg.content}</div>
          <div class="msg-time">${new Date(msg.time).toLocaleTimeString()}</div>
        </div>
      `;

      chatMessages.appendChild(messageDiv);
    });

    chatMessages.scrollTop = chatMessages.scrollHeight;
  } else {
    console.warn("Dữ liệu không chứa danh sách tin nhắn:", data);
  }
}
