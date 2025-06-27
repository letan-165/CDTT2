let stompClient = null;
//Chú ý những cái "stompClient"
const CONNECT = "http://localhost:8081/chat_service/chatbot-ws?token="
const SUBSCRIVE = "/user/queue/message"
const GETCHAT = "/app/chatbot-ws.create"
const SENDCHAT = "/app/chatbot-ws.send"


export const connect = (type) =>  {
  const token = localStorage.getItem("token");
  //Kết nối
  const socket = new SockJS(
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

      createChat(type)
     
    },
    function (error) {
      console.error("Connection error:", error);
    }
  );
};
export const createChat = (type) => {
  if (stompClient && stompClient.connected) {
    const msg = {type};   

    stompClient.send(GETCHAT, {}, JSON.stringify(msg));
    console.log("Đã gửi:", msg);
  } else {
    console.warn("Chưa kết nối!");
  }
};

export const sendChat = (quizID, content) => {
  if (stompClient && stompClient.connected) {
    addMessage("user", content);
    const msg = {quizID,content}; 
    //Gửi tin nhắn
    stompClient.send(SENDCHAT, {}, JSON.stringify(msg));
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

  const messages = document.querySelector("#chat-messages");
  messages.innerHTML = "";

  const list = data.messages || [];

  list.forEach((msgItem) => {
    addMessage(msgItem.role, msgItem.content);
  });

  messages.scrollTop = messages.scrollHeight;
}


function addMessage(role, content) {
  const messages = document.querySelector("#chat-messages");

  const msg = document.createElement("div");
  const sender = role === "user" ? "Bạn" : "AI";
  msg.textContent = `${sender}: ${content}`;
  msg.style.marginBottom = "8px";
  messages.appendChild(msg);

  // Tự động cuộn xuống dòng cuối
  messages.scrollTop = messages.scrollHeight;
}
