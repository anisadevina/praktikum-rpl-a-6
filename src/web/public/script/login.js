const loginForm = document.getElementById("loginForm");
const usernameInput = document.getElementById("username");
const passwordInput = document.getElementById("password");
const usernameError = document.getElementById("usernameError");
const passwordError = document.getElementById("passwordError");
const formError = document.getElementById("formError");
const togglePasswordBtn = document.querySelector(".toggle-password");

// ── 1. Toggle show/hide password ───────────────────────────────────────────
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

// ── 2. Validasi Front-End Sederhana ────────────────────────────────────────
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

// ── 3. Submit ke Backend via API (Fetch) ───────────────────────────────────
loginForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    formError.textContent = "";

    // Cek validasi kosong
    const validUsername = validateRequired(
        usernameInput,
        usernameError,
        "Username",
    );
    const validPassword = validateRequired(
        passwordInput,
        passwordError,
        "Password",
    );

    if (!validUsername || !validPassword) {
        return; // Hentikan jika ada yang kosong
    }

    // Siapkan tombol loading
    const submitBtn = loginForm.querySelector('button[type="submit"]');
    let originalBtnText = "Masuk";
    if (submitBtn) {
        originalBtnText = submitBtn.innerHTML;
        submitBtn.disabled = true;
    }

    const formData = new FormData(loginForm);

    try {
        const response = await fetch("/login", {
            method: "POST",
            headers: {
                "X-CSRF-TOKEN": document.querySelector('input[name="_token"]')
                    .value,
                Accept: "application/json",
            },
            body: formData,
        });

        const data = await response.json();

        // Tangani Error Validasi dari LoginRequest (Status 422)
        if (response.status === 422) {
            for (const field in data.errors) {
                const errorElement = document.getElementById(`${field}Error`);
                if (errorElement) {
                    errorElement.textContent = data.errors[field][0];
                }
            }
            return;
        }

        // Tangani Gagal Login / Kredensial Salah (Status 401)
        if (response.status === 401) {
            formError.textContent = data.message;
            return;
        }

        // Tangani error server lainnya (Status 500 dll)
        if (!response.ok) {
            throw new Error(data.message || "Terjadi kesalahan pada server");
        }

        // Jika Sukses (Status 200)
        localStorage.setItem("api_token", data.access_token);
        sessionStorage.setItem("loggedUser", JSON.stringify(data.user));
        window.location.href = "/beranda";
    } catch (error) {
        console.error("Error:", error);
        formError.textContent = "Gagal terhubung ke server. Silakan coba lagi.";
    } finally {
        // Kembalikan tombol ke semula
        if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.innerHTML = originalBtnText;
        }
    }
});

// ── 4. Hilangkan Error Saat Mengetik ───────────────────────────────────────
[usernameInput, passwordInput].forEach((input) => {
    input.addEventListener("input", () => {
        const errEl = document.getElementById(input.id + "Error");
        if (errEl) errEl.textContent = "";
        input.classList.remove("is-error");
        formError.textContent = ""; // Hapus error general juga
    });
});
