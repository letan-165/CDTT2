import { SearchName } from '../../api/quiz.js';

const searchInput   = document.getElementById('search-input');
const suggestionBox = document.getElementById('suggestion-box');


async function handlerSearchName() {
  const query = searchInput.value.trim();

  if (!query) {
    suggestionBox.style.display = 'none';
  }

  try {

    const res = await SearchName({ search: query });

    // 3. Xử lý kết quả hợp lệ
    if (res.code === 1000 && Array.isArray(res.result)) {
      const quizzes = res.result;           
      suggestionBox.innerHTML = '';

      if (quizzes.length === 0) {
        suggestionBox.style.display = 'none';
        return;
      }

      quizzes.forEach(q => {
        // ----- Tạo phần tử gợi ý -----
        const item = document.createElement('div');
        item.className = 'suggestion-item';
        item.style.display = "flex";
        item.style.justifyContent = "space-between";
        item.style.alignItems = "center";
        item.style.padding = "8px";

        // Title (dòng đầu)
        const leftBox = document.createElement("div");

        const titleEl = document.createElement('div');
        titleEl.textContent = q.title;
        titleEl.style.fontWeight = '600';

        // Sub info (dòng dưới): topics + thời gian
        const topicsEl = document.createElement('div');
        topicsEl.textContent = q.topics.join(", ");
        topicsEl.style.fontSize = "12px";
        topicsEl.style.color = "#666";

        leftBox.appendChild(titleEl);
        leftBox.appendChild(topicsEl);

        const rightBox = document.createElement("div");
        const startVN = new Date(q.startTime).toLocaleString('vi-VN', { timeZone: 'Asia/Ho_Chi_Minh' });
        rightBox.textContent = startVN;
        rightBox.style.fontSize = "12px";
        rightBox.style.color = "#888";
        rightBox.style.whiteSpace = "nowrap";

        item.appendChild(leftBox);
        item.appendChild(rightBox);

        // ----- Hành động khi click -----
        item.onclick = () => {

          window.location.href = `course-detail.html?id=${q.quizID}`;
        };

        suggestionBox.appendChild(item);
      });

      suggestionBox.style.display = 'block';
    } else {
      // code khác 1000 hoặc result không phải mảng
      suggestionBox.style.display = 'none';
    }
  } catch (err) {
    console.error('Lỗi tìm kiếm:', err);
    suggestionBox.style.display = 'none';
  }
}

// Gọi API mỗi khi gõ
searchInput.addEventListener('input', handlerSearchName);

// Ẩn box khi click ngoài
document.addEventListener('click', e => {
  if (!searchInput.contains(e.target) && !suggestionBox.contains(e.target)) {
    suggestionBox.style.display = 'none';
  }
});
