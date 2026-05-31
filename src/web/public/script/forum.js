// ─── Konfigurasi Path Halaman ────────────────────────────────────────────────
const PAGE_PATHS = {
    beranda: "/beranda",
    matkul:  "/matkul",
    forum:   "/forum",
    unggah:  "/unggah",
    arsip:   "/arsip",
};

const ACTIVE_PAGE = "forum";

// ─── Ambil CSRF Token dari meta tag ──────────────────────────────────────────
function getCsrf() {
    return document.querySelector('meta[name="csrf-token"]').getAttribute("content");
}

// ─── Sidebar Aktif ────────────────────────────────────────────────────────────
function setupSidebarActive() {
    const navItems = document.querySelectorAll(".nav-item[data-page]");
    navItems.forEach((item) => {
        item.classList.remove("active");
        if (item.dataset.page === ACTIVE_PAGE) item.classList.add("active");
        item.addEventListener("click", () => {
            const target = PAGE_PATHS[item.dataset.page];
            if (target && item.dataset.page !== ACTIVE_PAGE) {
                window.location.href = target;
            }
        });
    });
}

// ─── Helper: Render satu kartu topik ─────────────────────────────────────────
function generateTopicCard(t) {
    const balasanHTML = t.balasan.map((b) => `
        <div class="reply-branch">
            <div class="reply-card" data-id-topik="${t.id_topik}">
                <div class="reply-meta">
                    <div class="card-avatar mini">
                        <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="#1E1E1E" stroke-width="2.5">
                            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/>
                        </svg>
                    </div>
                    <strong>${b.is_anonim ? "Anonim" : b.username}</strong>
                </div>
                <p class="reply-body-text">${b.pesan_balasan}</p>
            </div>
        </div>
    `).join("");

    return `
        <div class="topic-card" id="topik-${t.id_topik}">
            <div class="topic-meta">
                <div class="card-avatar">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#1E1E1E" stroke-width="2.5">
                        <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/>
                    </svg>
                </div>
                <span class="card-username">${t.is_anonim ? "Anonim" : t.username}</span>
                <span class="badge badge-category">${t.tag === "tanya jawab" ? "Tanya Jawab Soal" : "General"}</span>
            </div>
            <p class="topic-text">${t.pesan_topik}</p>
            <span class="time-stamp">${t.waktu_topik_human}</span>

            <div class="topic-actions">
                <button class="action-link btn-trigger-reply">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <polyline points="9 17 4 12 9 7"/><path d="M20 18v-2a4 4 0 0 0-4-4H4"/>
                    </svg>
                    Balas
                </button>
                <button class="action-link">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
                    </svg>
                    ${t.jumlah_balasan}
                </button>
            </div>

            <hr class="card-divider">

            <div class="dropdown-toggle">
                <span class="toggle-text">Lihat Semua Jawaban</span>
                <svg class="toggle-caret" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3">
                    <polyline points="6 9 12 15 18 9"/>
                </svg>
            </div>

            <div class="replies-thread-container hidden">
                <div class="replies-list">
                    ${balasanHTML || '<p style="color:#aaa; font-style:italic; padding:8px 0; font-size:13px;">Belum ada balasan.</p>'}
                </div>
            </div>

            <div class="reply-form-input main-topic-input hidden" data-id-topik="${t.id_topik}">
                <div class="card-avatar mini">
                    <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="#1E1E1E" stroke-width="2.5">
                        <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/>
                    </svg>
                </div>
                <input type="text" class="input-balasan" placeholder="Tulis balasan Anda...">
                <label class="label-anonim-balasan">
                    <input type="checkbox" class="checkbox-anonim-balasan"> Anonim
                </label>
                <button type="button" class="btn-send-message-submit btn-kirim-balasan">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                        <line x1="22" y1="2" x2="11" y2="13"/><polygon points="22 2 15 22 11 13 2 9 22 2"/>
                    </svg>
                </button>
            </div>
        </div>
    `;
}

// ─── Fetch Data Forum dari API ────────────────────────────────────────────────
async function fetchForumData() {
    try {
        const response = await fetch("/forum/data", {
            headers: { "Accept": "application/json" },
        });
        if (!response.ok) throw new Error("Gagal mengambil data forum");

        const json = await response.json();
        const data = json.data;

        // Render username topbar
        const topbarUsername = document.getElementById("topbar-username");
        if (topbarUsername && data.user) {
            topbarUsername.textContent = data.user.username;
        }

        // Render daftar topik
        const container = document.getElementById("forum-feed-container");
        if (!container) return;

        if (data.topik.length === 0) {
            container.innerHTML = '<p style="color:#888; text-align:center; padding:2rem;">Belum ada topik. Jadilah yang pertama membuat topik!</p>';
            return;
        }

        container.innerHTML = data.topik.map(generateTopicCard).join("");

        // Scroll ke topik tertentu jika ada hash di URL
        const hash = window.location.hash;
        if (hash) {
            const target = document.querySelector(hash);
            if (target) {
                const thread = target.querySelector(".replies-thread-container");
                const toggle = target.querySelector(".dropdown-toggle");
                if (thread) {
                    thread.classList.remove("hidden");
                    toggle.classList.add("open");
                    toggle.querySelector(".toggle-text").textContent = "Sembunyikan Jawaban";
                    target.scrollIntoView({ behavior: "smooth" });
                }
            }
        }

    } catch (error) {
        console.error("Error memuat forum:", error);
        const container = document.getElementById("forum-feed-container");
        if (container) container.innerHTML = '<p style="color:red;">Gagal memuat data. Silakan muat ulang halaman.</p>';
    }
}

// ─── Submit Topik Baru via Fetch ──────────────────────────────────────────────
async function submitTopik() {
    const pesan    = document.getElementById("topic-message").value.trim();
    const tag      = document.getElementById("input-tag").value;
    const isAnonim = document.querySelector('input[name="is_anonim"]:checked').value;
    const errorEl  = document.getElementById("error-pesan");
    const formErr  = document.getElementById("form-error");

    // Reset error
    errorEl.style.display = "none";
    errorEl.textContent   = "";
    formErr.textContent   = "";

    // Validasi client-side
    if (pesan.length < 5) {
        errorEl.textContent   = "Pesan minimal 5 karakter.";
        errorEl.style.display = "block";
        return;
    }

    const btn = document.getElementById("btn-submit-topik");
    btn.disabled   = true;
    btn.textContent = "Mengunggah...";

    try {
        const response = await fetch("/forum/topik", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "X-CSRF-TOKEN": getCsrf(),
                "Accept":       "application/json",
            },
            body: JSON.stringify({ pesan_topik: pesan, tag, is_anonim: isAnonim }),
        });

        const data = await response.json();

        if (response.status === 422) {
            for (const field in data.errors) {
                if (field === "pesan_topik") {
                    errorEl.textContent   = data.errors[field][0];
                    errorEl.style.display = "block";
                } else {
                    formErr.textContent = data.errors[field][0];
                }
            }
            return;
        }

        if (!response.ok) throw new Error(data.message || "Terjadi kesalahan");

        // Sukses: kembali ke daftar, reset form, refresh data
        showForumView();
        resetFormTopik();
        await fetchForumData();

    } catch (error) {
        console.error("Error kirim topik:", error);
        formErr.textContent = "Gagal mengunggah topik. Silakan coba lagi.";
    } finally {
        btn.disabled    = false;
        btn.textContent = "Unggah";
    }
}

// ─── Submit Balasan via Fetch (delegasi ke container) ────────────────────────
async function submitBalasan(idTopik, pesan, isAnonim, formEl) {
    const btn = formEl.querySelector(".btn-kirim-balasan");
    btn.disabled = true;

    try {
        const response = await fetch("/forum/balasan", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "X-CSRF-TOKEN": getCsrf(),
                "Accept":       "application/json",
            },
            body: JSON.stringify({ id_topik: idTopik, pesan_balasan: pesan, is_anonim: isAnonim }),
        });

        const data = await response.json();
        if (!response.ok) throw new Error(data.message || "Gagal mengirim balasan");

        // Kosongkan input lalu refresh data
        formEl.querySelector(".input-balasan").value = "";
        await fetchForumData();

        // Buka thread balasan topik yang baru dibalas
        const card   = document.getElementById(`topik-${idTopik}`);
        if (card) {
            const thread = card.querySelector(".replies-thread-container");
            const toggle = card.querySelector(".dropdown-toggle");
            if (thread && thread.classList.contains("hidden")) {
                thread.classList.remove("hidden");
                toggle.classList.add("open");
                toggle.querySelector(".toggle-text").textContent = "Sembunyikan Jawaban";
            }
            card.scrollIntoView({ behavior: "smooth", block: "nearest" });
        }

    } catch (error) {
        console.error("Error kirim balasan:", error);
        alert("Gagal mengirim balasan. Silakan coba lagi.");
    } finally {
        btn.disabled = false;
    }
}

// ─── Helper: Tampil/sembunyikan view ─────────────────────────────────────────
function showForumView() {
    document.getElementById("forum-view").classList.remove("hidden");
    document.getElementById("create-topic-view").classList.add("hidden");
}

function showCreateView() {
    document.getElementById("forum-view").classList.add("hidden");
    document.getElementById("create-topic-view").classList.remove("hidden");
}

function resetFormTopik() {
    document.getElementById("topic-message").value = "";
    document.getElementById("char-counter").textContent  = "0/2000";
    document.getElementById("input-tag").value           = "tanya jawab";
    document.getElementById("error-pesan").style.display = "none";
    document.getElementById("form-error").textContent    = "";
    document.querySelectorAll(".category-btn").forEach((b, i) => {
        b.classList.toggle("active", i === 0);
    });
    document.querySelector('input[name="is_anonim"][value="0"]').checked = true;
}

// ─── Logout ───────────────────────────────────────────────────────────────────
function setupLogout() {
    const btn = document.getElementById("btn-keluar");
    if (!btn) return;
    btn.addEventListener("click", async (e) => {
        e.preventDefault();
        try {
            const response = await fetch("/logout", {
                method: "POST",
                headers: { "X-CSRF-TOKEN": getCsrf(), "Accept": "application/json" },
            });
            if (response.ok) {
                sessionStorage.removeItem("loggedUser");
                window.location.href = "/";
            }
        } catch (err) {
            console.error("Error logout:", err);
        }
    });
}

// ─── Init ─────────────────────────────────────────────────────────────────────
document.addEventListener("DOMContentLoaded", () => {
    setupSidebarActive();
    setupLogout();
    fetchForumData();

    // Tombol buat topik
    document.getElementById("btn-go-to-create").addEventListener("click", showCreateView);

    // Tombol batal
    document.getElementById("btn-cancel-create").addEventListener("click", () => {
        showForumView();
        resetFormTopik();
    });

    // Tombol submit topik
    document.getElementById("btn-submit-topik").addEventListener("click", submitTopik);

    // Pilih kategori
    document.querySelectorAll(".category-btn").forEach((btn) => {
        btn.addEventListener("click", function () {
            document.querySelectorAll(".category-btn").forEach((b) => b.classList.remove("active"));
            this.classList.add("active");
            document.getElementById("input-tag").value = this.dataset.cat;
        });
    });

    // Hitung karakter textarea
    document.getElementById("topic-message").addEventListener("input", function () {
        document.getElementById("char-counter").textContent = `${this.value.length}/2000`;
    });

    // Delegasi klik pada feed (toggle, balas)
    document.getElementById("forum-feed-container").addEventListener("click", (e) => {

        // Toggle lihat/sembunyikan jawaban
        const toggleBtn = e.target.closest(".dropdown-toggle");
        if (toggleBtn) {
            const card   = toggleBtn.closest(".topic-card");
            const thread = card.querySelector(".replies-thread-container");
            const isHidden = thread.classList.toggle("hidden");
            toggleBtn.classList.toggle("open", !isHidden);
            toggleBtn.querySelector(".toggle-text").textContent = isHidden ? "Lihat Semua Jawaban" : "Sembunyikan Jawaban";
        }

        // Tombol balas
        const replyBtn = e.target.closest(".btn-trigger-reply");
        if (replyBtn) {
            const card      = replyBtn.closest(".topic-card");
            const inputForm = card.querySelector(".main-topic-input");
            inputForm.classList.remove("hidden");
            inputForm.scrollIntoView({ behavior: "smooth", block: "nearest" });
            inputForm.querySelector(".input-balasan").focus();
        }

        // Tombol kirim balasan
        const kirimBtn = e.target.closest(".btn-kirim-balasan");
        if (kirimBtn) {
            const formEl   = kirimBtn.closest(".main-topic-input");
            const idTopik  = formEl.dataset.idTopik;
            const pesan    = formEl.querySelector(".input-balasan").value.trim();
            const isAnonim = formEl.querySelector(".checkbox-anonim-balasan").checked ? "1" : "0";
            if (!pesan) return;
            submitBalasan(idTopik, pesan, isAnonim, formEl);
        }
    });

    // Enter untuk kirim balasan
    document.getElementById("forum-feed-container").addEventListener("keydown", (e) => {
        if (e.key === "Enter" && e.target.classList.contains("input-balasan")) {
            const formEl   = e.target.closest(".main-topic-input");
            const idTopik  = formEl.dataset.idTopik;
            const pesan    = e.target.value.trim();
            const isAnonim = formEl.querySelector(".checkbox-anonim-balasan").checked ? "1" : "0";
            if (!pesan) return;
            submitBalasan(idTopik, pesan, isAnonim, formEl);
        }
    });
});