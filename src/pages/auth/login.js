const loginForm     = document.getElementById('loginForm');
const usernameInput = document.getElementById('username');
const passwordInput = document.getElementById('password');
const usernameError = document.getElementById('usernameError');
const passwordError = document.getElementById('passwordError');
const formError     = document.getElementById('formError');
const togglePasswordBtn = document.querySelector('.toggle-password');

// ── Toggle show/hide password ──────────────────────────────────────────────
togglePasswordBtn.addEventListener('click', () => {
  const isPassword = passwordInput.type === 'password';
  passwordInput.type = isPassword ? 'text' : 'password';

  const icon = document.getElementById('eyeIcon');
  if (isPassword) {
    icon.innerHTML = `
      <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94"/>
      <path d="M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19"/>
      <line x1="1" y1="1" x2="23" y2="23"/>
    `;
  } else {
    icon.innerHTML = `
      <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
      <circle cx="12" cy="12" r="3"/>
    `;
  }
});

// ── Validasi field ─────────────────────────────────────────────────────────
function validateField(input, errorEl, message) {
  if (!input.value.trim()) {
    errorEl.textContent = message;
    input.classList.add('is-error');
    return false;
  }
  errorEl.textContent = '';
  input.classList.remove('is-error');
  return true;
}

// ── Submit ─────────────────────────────────────────────────────────────────
loginForm.addEventListener('submit', (e) => {
  e.preventDefault();
  formError.textContent = '';

  const validUsername = validateField(usernameInput, usernameError, 'Username tidak boleh kosong.');
  const validPassword = validateField(passwordInput, passwordError, 'Password tidak boleh kosong.');

  if (!validUsername || !validPassword) return;

  // TODO: ganti dengan panggilan API backend
  const mockUsers = [
    { username: 'admin',      password: 'admin123' },
    { username: 'mahasiswa',  password: 'pass123'  },
  ];

  const found = mockUsers.find(
    u => u.username === usernameInput.value.trim() && u.password === passwordInput.value
  );

  if (found) {
    // Simpan user ke sessionStorage lalu masuk ke beranda
    sessionStorage.setItem('loggedUser', JSON.stringify({ username: found.username }));
    window.location.href = '../beranda/beranda.html';
  } else {
    formError.textContent = 'Username atau password salah.';
  }
});

// ── Reset error on input ───────────────────────────────────────────────────
usernameInput.addEventListener('input', () => {
  usernameError.textContent = '';
  usernameInput.classList.remove('is-error');
});

passwordInput.addEventListener('input', () => {
  passwordError.textContent = '';
  passwordInput.classList.remove('is-error');
});