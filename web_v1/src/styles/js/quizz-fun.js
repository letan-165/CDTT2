
  function loadQuizDetail(detailUrl, relatedListUrl) {
    fetch(detailUrl)
      .then(res => res.text())
      .then(html => {
        document.querySelector('.left-content').innerHTML = html;
      });

    fetch(relatedListUrl)
      .then(res => res.text())
      .then(html => {
        document.querySelector('.right-content').innerHTML = html;
      });
  }

