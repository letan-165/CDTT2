const toggleBtns = document.querySelectorAll('.toggle-password');

toggleBtns.forEach(btn => {
  btn.addEventListener('click', () => {
    const targetId = btn.getAttribute('data-target');
    const input = document.getElementById(targetId);
    const img = btn.querySelector('img');

    if (input.type === 'password') {
      input.type = 'text';
      img.src = '../styles/img/eye-slash.png'; 
    } else {
      input.type = 'password';
      img.src = '../styles/img/eye.png'; 
    }
  });
});
