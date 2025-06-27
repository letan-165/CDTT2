document.addEventListener("DOMContentLoaded", () => {
  const role = localStorage.getItem("role");
  const sidebarContainer = document.getElementById("sidebar-container");

  if (!sidebarContainer) return;

  let sidebarUrl = "";

  if (role === "TEACHER") {
    sidebarUrl = "sidebar-teacher.html";
  } else if (role === "STUDENT") {
    sidebarUrl = "sidebar-student.html";
  } else {
    console.warn("Không xác định được vai trò người dùng.");
    return;
  }

  fetch(sidebarUrl)
    .then(res => res.text())
    .then(html => {
      sidebarContainer.innerHTML = html;

      // Sau khi sidebar được chèn, highlight menu đang active
      highlightActiveMenuItem();

      function highlightActiveMenuItem() {
        const currentPath = window.location.pathname;
        const currentPage = currentPath.split("/").pop();

        // Lấy trang cha được đánh dấu nếu có
        const pageMarked = document.body.dataset.sidebarActive;

        const activePage = pageMarked || currentPage;

        const menuItems = document.querySelectorAll(".menu-item a");

        menuItems.forEach(link => {
          const href = link.getAttribute("href");

          if (href === activePage) {
            link.parentElement.classList.add("active");
          }
        });
      }

    })
    .catch(err => {
      console.error("Lỗi khi tải sidebar:", err);
    });
});
