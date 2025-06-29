import { SearchTopic } from '../../api/quiz.js';

const searchInput = document.getElementById('quiz-topic');
const suggestionBox = document.getElementById('suggestion-box');
export const topics = [];

const quizTopicValue = document.getElementById('quiz-topi-value');

// Cập nhật lại mảng topics từ input
const topicString = quizTopicValue.value; // "#Toán #Lý #Hoá"
const parsedTopics = topicString
  .split('#')           // tách bằng dấu #
  .map(t => t.trim())   // loại bỏ khoảng trắng
  .filter(t => t);      // loại bỏ chuỗi rỗng

topics.length = 0;        // Xóa toàn bộ phần tử cũ
topics.push(...parsedTopics); // Thêm phần tử mới



async function handlerSearchTopics() {
  const query = searchInput.value.trim().toLowerCase();

  if (!query) {
    suggestionBox.style.display = 'none';
    return;
  }

  try {
    const res = await SearchTopic({ search: query });

    if (res.code === 1000 && Array.isArray(res.result)) {
      suggestionBox.innerHTML = '';

      let hasMatch = false;
      const listTopics = res.result
      listTopics.unshift(query)
      listTopics.forEach(topic => {

        if (typeof topic === 'string') {
          hasMatch = true;

          const item = document.createElement('div');
          item.className = 'suggestion-item';
          item.textContent = topic;
          item.style.padding = '8px';
          item.style.cursor = 'pointer';

          item.onclick = () => {
            searchInput.value = "";
            quizTopicValue.value += ` #${topic}`;
            
            console.log(topics);
            suggestionBox.style.display = 'none';
          };

          suggestionBox.appendChild(item);
        }
      });

      suggestionBox.style.display = hasMatch ? 'block' : 'none';
    } else {
      suggestionBox.style.display = 'none';
    }
  } catch (err) {
    console.error('Lỗi tìm kiếm topic:', err);
    suggestionBox.style.display = 'none';
  }
}

searchInput.addEventListener('input', handlerSearchTopics);

document.addEventListener('click', e => {
  if (!searchInput.contains(e.target) && !suggestionBox.contains(e.target)) {
    suggestionBox.style.display = 'none';
  }
});
