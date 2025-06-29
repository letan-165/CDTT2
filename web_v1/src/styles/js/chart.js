import { StatisticsResultTimes } from '../../api/QuizService.js';

document.addEventListener("DOMContentLoaded", async () => {
  const studentID = localStorage.getItem("userID");

  // 👉 Log StudentID ra console để kiểm tra
  console.log("📌 StudentID lấy từ localStorage:", studentID);

  if (!studentID) {
    console.warn("Không tìm thấy studentID trong localStorage.");
    return;
  }

  try {
    const response = await StatisticsResultTimes(studentID);

    // 👉 Log toàn bộ phản hồi từ API
    console.log("📊 Dữ liệu trả về từ StatisticsResultTimes:", response);

    const weeklyData = response?.result || [];

    if (!Array.isArray(weeklyData) || weeklyData.length !== 7) {
      console.warn("Dữ liệu thống kê không hợp lệ.");
      return;
    }

    const labels = ["T2", "T3", "T4", "T5", "T6", "T7", "CN"];

    const ctx = document.getElementById('studyTimeChart').getContext('2d');
    new Chart(ctx, {
      type: 'line',
      data: {
        labels: labels,
        datasets: [{
          label: 'Phút học mỗi ngày',
          data: weeklyData,
          fill: false,
          borderColor: 'black',
          backgroundColor: 'black',
          tension: 0.3,
          pointStyle: 'circle',
          pointRadius: 5,
          pointBackgroundColor: 'black'
        }]
      },
      options: {
        responsive: true,
        scales: {
          y: {
            beginAtZero: true,
            title: {
              display: true,
              text: 'Phút'
            }
          },
          x: {
            title: {
              display: true,
              text: 'Ngày trong tuần'
            }
          }
        },
        plugins: {
          legend: {
            display: false
          }
        }
      }
    });

  } catch (err) {
    console.error("❌ Lỗi khi lấy dữ liệu thống kê thời gian học:", err);
  }
});
