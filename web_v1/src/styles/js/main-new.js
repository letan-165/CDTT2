let stompClient = null;
//Chú ý những cái "stompClient"
const CONNECT = "http://localhost:8083/notification_service/personal-ws?token="
const SUBSCRIVE = "/user/queue/notification"
const GETCHAT = "/app/personal-ws.list"


export const connect = () =>  {
  const token = localStorage.getItem("token");
  const socket = new SockJS(
    `${CONNECT}${token}`
  );
  stompClient = Stomp.over(socket);

  stompClient.connect(
    {},
    function (frame) {
      console.log("Connected: " + frame);
      stompClient.subscribe(SUBSCRIVE, function (message) {
        renderIncomingMessage(message.body);
      });
      
      getNotification()
     
    },
    function (error) {
      console.error("Connection error:", error);
    }
  );
};
export const getNotification = () => {
  if (stompClient && stompClient.connected) {
    stompClient.send(GETCHAT, {}, );
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
  try {
    const parsed = typeof data === "string" ? JSON.parse(data) : data;
    const notificationList = document.querySelector(".notification-list");

    if (!notificationList) return;

    // Xóa cũ (nếu bạn muốn làm mới danh sách)
    notificationList.innerHTML = "";

    if (Array.isArray(parsed.notifications)) {
      parsed.notifications.forEach((notification) => {
        const item = document.createElement("div");
        item.classList.add("notification-item");

        item.innerHTML = `
          <div class="text">
            <strong>${notification.subject}</strong>
            <p>${notification.content}</p>
          </div>
          <span class="time">${formatDisplayTime(notification.displayTime)}</span>
        `;

        notificationList.appendChild(item);
      });
    } else {
      console.warn("Không có danh sách thông báo trong dữ liệu nhận được");
    }

  } catch (err) {
    console.error("Lỗi khi render thông báo:", err);
  }
}

// Hàm định dạng thời gian ISO thành hh:mm hoặc theo ý bạn
function formatDisplayTime(isoString) {
  const date = new Date(isoString);
  return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}

