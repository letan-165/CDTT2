// Load sidebar.html vào sidebar-container
fetch('sidebar.html')
  .then(response => response.text())
  .then(data => {
    document.getElementById('sidebar-container').innerHTML = data;

    // Sự kiện click menu
    const commentMenu = document.getElementById('comment-menu');
    if (commentMenu) {
      commentMenu.addEventListener('click', () => {
        fetch('content2.html')
          .then(response => response.text())
          .then(content => {
            document.getElementById('content-container').innerHTML = content;
          });
      });
    }

    const homeMenu = document.getElementById('home-menu');
    if (homeMenu) {
      homeMenu.addEventListener('click', () => {
        location.reload(); 
      });
    }
  });

// Load form-list-quizz.html vào course-list
fetch('form-list-quizz.html')
  .then(res => res.text())
  .then(html => {
    const listEl = document.getElementById('course-list');
    if (listEl) {
      listEl.innerHTML = html;
      if (typeof renderQuiz === 'function') {
        renderQuiz();
      }
    }
  });
