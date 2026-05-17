const registerForm = document.getElementById("registerForm");
const usernameInput = document.getElementById("username");
const nimInput = document.getElementById("nim");
const emailInput = document.getElementById("email");
const passwordInput = document.getElementById("password");
const usernameError = document.getElementById("usernameError");
const nimError = document.getElementById("nimError");
const emailError = document.getElementById("emailError");
const passwordError = document.getElementById("passwordError");
const formError = document.getElementById("formError");
const togglePasswordBtn = document.querySelector(".toggle-password");

// ── Toggle show/hide password ──────────────────────────────────────────────
togglePasswordBtn.addEventListener("click", () => {
    const isPassword = passwordInput.type === "password";
    passwordInput.type = isPassword ? "text" : "password";

    const icon = document.getElementById("eyeIcon");
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

// ── Validasi ───────────────────────────────────────────────────────────────
function validateRequired(input, errorEl, label) {
    if (!input.value.trim()) {
        errorEl.textContent = `${label} tidak boleh kosong.`;
        input.classList.add("is-error");
        return false;
    }
    errorEl.textContent = "";
    input.classList.remove("is-error");
    return true;
}

function validateEmail(input, errorEl) {
    if (!input.value.trim()) {
        errorEl.textContent = "Email tidak boleh kosong.";
        input.classList.add("is-error");
        return false;
    }
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(input.value.trim())) {
        errorEl.textContent = "Format email tidak valid.";
        input.classList.add("is-error");
        return false;
    }
    errorEl.textContent = "";
    input.classList.remove("is-error");
    return true;
}

function validateNIM(input, errorEl) {
    if (!input.value.trim()) {
        errorEl.textContent = "NIM tidak boleh kosong.";
        input.classList.add("is-error");
        return false;
    }
<<<<<<< Updated upstream
    // if (!/^\d{8,15}$/.test(input.value.trim())) {
    //     errorEl.textContent = "NIM harus berupa angka (8–15 digit).";
    //     input.classList.add("is-error");
    //     return false;
    // }
    errorEl.textContent = "";
    input.classList.remove("is-error");
    return true;
}

function validatePassword(input, errorEl) {
    if (!input.value.trim()) {
        errorEl.textContent = "Password tidak boleh kosong.";
        input.classList.add("is-error");
        return false;
    }
    if (input.value.length < 8) {
        errorEl.textContent = "Password minimal harus 8 karakter.";
=======
    if (!/^\d{8,15}$/.test(input.value.trim())) {
        errorEl.textContent = "NIM harus berupa angka (8–15 digit).";
>>>>>>> Stashed changes
        input.classList.add("is-error");
        return false;
    }
    errorEl.textContent = "";
    input.classList.remove("is-error");
    return true;
}

// ── Submit ─────────────────────────────────────────────────────────────────
registerForm.addEventListener("submit", (e) => {
    e.preventDefault();
    formError.textContent = "";

    const validUsername = validateRequired(
        usernameInput,
        usernameError,
        "Username",
    );
    const validNIM = validateNIM(nimInput, nimError);
    const validEmail = validateEmail(emailInput, emailError);
<<<<<<< Updated upstream
    const validPassword = validatePassword(passwordInput, passwordError);

    if (!validUsername || !validNIM || !validEmail || !validPassword) {
        e.preventDefault();
        return;
    }

    registerForm.submit();
=======
    const validPassword = validateRequired(
        passwordInput,
        passwordError,
        "Password",
    );

    if (!validUsername || !validNIM || !validEmail || !validPassword) return;

    // TODO: ganti dengan panggilan API backend
    console.log("Register payload:", {
        username: usernameInput.value.trim(),
        nim: nimInput.value.trim(),
        email: emailInput.value.trim(),
        password: passwordInput.value,
    });

    // Setelah daftar → kembali ke halaman login
    alert("Pendaftaran berhasil! Silakan masuk dengan akun baru kamu.");
    window.location.href = "login.html";
>>>>>>> Stashed changes
});

// ── Reset error on input ───────────────────────────────────────────────────
[usernameInput, nimInput, emailInput, passwordInput].forEach((input) => {
    input.addEventListener("input", () => {
        const errEl = document.getElementById(input.id + "Error");
        if (errEl) errEl.textContent = "";
        input.classList.remove("is-error");
    });
});
