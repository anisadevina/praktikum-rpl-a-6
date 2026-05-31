const registerForm = document.getElementById("registerForm");
const usernameInput = document.getElementById("username");
const nimInput = document.getElementById("nim");
const emailInput = document.getElementById("email_user");
const passwordInput = document.getElementById("password");
const usernameError = document.getElementById("usernameError");
const nimError = document.getElementById("nimError");
const emailError = document.getElementById("email_userError");
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
    if (!input.value.trim().endsWith("@student.uns.ac.id")) {
        errorEl.textContent =
            "Email harus menggunakan SSO UNS (@student.uns.ac.id).";
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
    if (!/^[a-zA-Z][a-zA-Z0-9]{7}$/.test(input.value.trim())) {
        errorEl.textContent =
            "NIM harus 8 karakter, diawali huruf (contoh: L0124003).";
        input.classList.add("is-error");
        return false;
    }
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
        input.classList.add("is-error");
        return false;
    }
    errorEl.textContent = "";
    input.classList.remove("is-error");
    return true;
}

// ── Submit ─────────────────────────────────────────────────────────────────
registerForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    formError.textContent = "";

    const validUsername = validateRequired(
        usernameInput,
        usernameError,
        "Username",
    );
    const validNIM = validateNIM(nimInput, nimError);
    const validEmail = validateEmail(emailInput, emailError);
    const validPassword = validatePassword(passwordInput, passwordError);

    if (!validUsername || !validNIM || !validEmail || !validPassword) {
        return;
    }

    const submitBtn = registerForm.querySelector('button[type="submit"]');
    let originalBtnText = "Daftar";
    if (submitBtn) {
        originalBtnText = submitBtn.innerHTML;
        submitBtn.disabled = true;
        submitBtn.innerHTML = "Memproses...";
    }

    const formData = new FormData(registerForm);

    try {
        const response = await fetch("/register", {
            method: "POST",
            headers: {
                "X-CSRF-TOKEN": document.querySelector('input[name="_token"]')
                    .value,
                Accept: "application/json",
            },
            body: formData,
        });

        const data = await response.json();

        if (response.status === 422) {
            for (const field in data.errors) {
                const errorElement = document.getElementById(`${field}Error`);
                if (errorElement) {
                    errorElement.textContent = data.errors[field][0];
                }
            }
            return;
        }

        if (!response.ok) {
            throw new Error(data.message || "Terjadi kesalahan pada server");
        }

        sessionStorage.setItem("registerSuccess", "true");
        window.location.href = "/";
    } catch (error) {
        console.error("Error:", error);
        formError.textContent = "Gagal terhubung ke server. Silakan coba lagi";
    } finally {
        if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.innerHTML = originalBtnText;
        }
    }
});

// ── Reset error on input ───────────────────────────────────────────────────
[usernameInput, nimInput, emailInput, passwordInput].forEach((input) => {
    input.addEventListener("input", () => {
        const errEl = document.getElementById(input.id + "Error");
        if (errEl) errEl.textContent = "";
        input.classList.remove("is-error");
    });
});
