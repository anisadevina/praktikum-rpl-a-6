// ─── Konfigurasi Path Halaman ────────────────────────────────────────────────
const PAGE_PATHS = {
    beranda: "/beranda",
    matkul: "/matkul",
    forum: "/forum",
    unggah: "/unggah",
    arsip: "/arsip",
};

const ACTIVE_PAGE = "beranda";

// ─── Sidebar Aktif ────────────────────────────────────────────────────────────
function setupSidebarActive() {
    const navItems = document.querySelectorAll(".nav-item[data-page]");
    navItems.forEach((item) => {
        item.classList.remove("active");
        if (item.dataset.page === ACTIVE_PAGE) {
            item.classList.add("active");
        }

        item.addEventListener("click", () => {
            const target = PAGE_PATHS[item.dataset.page];
            if (target && item.dataset.page !== ACTIVE_PAGE) {
                window.location.href = target;
            }
        });
    });
}

// ─── Search ───────────────────────────────────────────────────────────────────
function setupSearch() {
    const input = document.getElementById("search-input");
    if (!input) return;

    input.addEventListener("keydown", (e) => {
        if (e.key === "Enter") {
            const query = input.value.trim();
            if (!query) return;
            window.location.href = `/matkul?q=${encodeURIComponent(query)}`;
        }
    });
}

// ─── Fetch Data API Beranda ───────────────────────────────────────────────────
async function fetchBerandaData() {
    try {
        const response = await fetch("/beranda/data");
        if (!response.ok) throw new Error("Gagal mengambil data beranda");

        const json = await response.json();
        const data = json.data;

        // 1. Render Username
        const topbarUsername = document.getElementById("topbar-username");
        const heroUsername = document.getElementById("hero-username");
        if (topbarUsername) topbarUsername.textContent = data.user.username;
        if (heroUsername) heroUsername.textContent = data.user.username;

        // 2. Render Matkul Terakhir
        const matkulGrid = document.getElementById("matkul-grid");
        if (matkulGrid) {
            if (data.mataKuliahTerakhir.length === 0) {
                matkulGrid.innerHTML =
                    '<p style="color: var(--color-text-muted); font-style: italic; padding-bottom: 20px;">Belum mengakses mata kuliah apapun.</p>';
            } else {
                matkulGrid.innerHTML = data.mataKuliahTerakhir
                    .map(
                        (mk) => `
                    <div class="matkul-card" data-id="${mk.id_matkul}">
                      <div class="card-thumbnail">
                        <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                          <rect x="3" y="3" width="18" height="18" rx="2"/>
                          <circle cx="8.5" cy="8.5" r="1.5"/>
                          <polyline points="21 15 16 10 5 21"/>
                        </svg>
                      </div>
                      <div class="card-body">
                        <p class="card-name" title="${mk.nama_matkul}">${mk.nama_matkul}</p>
                        <div class="card-rating">
                          <span class="card-rating-star">★</span>
                          <span class="card-rating-score">${mk.tingkat_kesulitan}/5</span>
                        </div>
                        <p class="card-arsip">${mk.arsip} arsip (materi, tugas, soal)</p>
                        <a class="card-btn" href="/matkul/detail?id=${mk.id_matkul}">Lihat Selengkapnya</a>
                      </div>
                    </div>
                `,
                    )
                    .join("");
            }
        }

        // 3. Render Forum Terbaru
        const forumGrid = document.getElementById("forum-list");
        if (forumGrid) {
            if (data.forumTerbaru.length === 0) {
                forumGrid.innerHTML =
                    '<p style="color: var(--color-text-muted); font-style: italic;">Belum ada topik forum terbaru.</p>';
            } else {
                forumGrid.innerHTML = data.forumTerbaru
                    .map(
                        (post) => `
                    <a class="forum-card" href="/forum#topik-${post.id_topik}">
                      <div class="forum-card-header">
                        <div class="forum-avatar">
                          <svg viewBox="0 0 24 24">
                            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                            <circle cx="12" cy="7" r="4"/>
                          </svg>
                        </div>
                        <span class="forum-username">${post.is_anonim ? "Anonim" : post.username}</span>
                        <span class="forum-badge">${post.tag}</span>
                      </div>
                      <p class="forum-body">${post.pesan_topik.length > 120 ? post.pesan_topik.substring(0, 120) + "..." : post.pesan_topik}</p>
                    </a>
                `,
                    )
                    .join("");
            }
        }
    } catch (error) {
        console.error("Error memuat beranda:", error);
        const matkulGrid = document.getElementById("matkul-grid");
        if (matkulGrid)
            matkulGrid.innerHTML =
                '<p style="color: red;">Gagal memuat data. Silakan muat ulang halaman.</p>';
    }
}

// ─── Init ─────────────────────────────────────────────────────────────────────
document.addEventListener("DOMContentLoaded", () => {
    setupSidebarActive();
    setupSearch();

    // Tarik data API beranda saat kerangka HTML sudah siap
    fetchBerandaData();

    // ── Tombol Keluar (Menggunakan API) ──
    const btnKeluar = document.getElementById("btn-keluar");
    if (btnKeluar) {
        btnKeluar.addEventListener("click", async (e) => {
            e.preventDefault();

            try {
                // Ambil token CSRF dari form tersembunyi
                const csrfInput = document.querySelector(
                    '#logout-form input[name="_token"]',
                );
                if (!csrfInput) throw new Error("CSRF token tidak ditemukan");

                const csrfToken = csrfInput.value;

                // Tembak rute /logout menggunakan metode POST
                const response = await fetch("/logout", {
                    method: "POST",
                    headers: {
                        "X-CSRF-TOKEN": csrfToken,
                        Accept: "application/json",
                    },
                });

                if (response.ok) {
                    sessionStorage.removeItem("loggedUser");
                    window.location.href = "/"; // Arahkan kembali ke halaman login
                } else {
                    console.error("Proses logout ditolak oleh server.");
                }
            } catch (error) {
                console.error("Error saat logout:", error);
            }
        });
    }
});

// --- FITUR POP-UP KUISIONER SEMESTER ---
async function cekDanTampilkanPopup() {
    // Jika pop-up sudah pernah dilihat di sesi login ini, batalkan (biar tidak spam).
    if (sessionStorage.getItem("popupSemesterSudahDilihat") === "true") {
        return;
    }

    try {
        const res = await fetch("/api/pengaturan/popup");
        const json = await res.json();

        // Jika admin menyalakan (ON) tombolnya, munculkan pop-up!
        if (json.status === "success" && json.data === "on") {
            munculkanModalGForm();
            // Simpan ingatan agar tidak muncul lagi saat di-refresh
            sessionStorage.setItem("popupSemesterSudahDilihat", "true");
        }
    } catch (e) {
        console.error("Gagal mengecek status pop-up:", e);
    }
}

function munculkanModalGForm() {
    // Membuat elemen background gelap (Overlay)
    const overlay = document.createElement("div");
    overlay.style.cssText =
        "position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.6); z-index:9999; display:flex; align-items:center; justify-content:center; backdrop-filter: blur(3px);";

    // Membuat kotak dialog
    const modal = document.createElement("div");
    modal.style.cssText =
        "background:#fff; padding:30px; border-radius:12px; width:90%; max-width:450px; text-align:center; box-shadow:0 10px 25px rgba(0,0,0,0.2); animation: popIn 0.3s ease-out;";

    modal.innerHTML = `
            <style>
                @keyframes popIn { from { transform: scale(0.8); opacity: 0; } to { transform: scale(1); opacity: 1; } }
            </style>
            <div style="background:#E8F5E9; color:#2E7D32; width:60px; height:60px; border-radius:50%; display:flex; align-items:center; justify-content:center; margin:0 auto 15px;">
                <svg width="30" height="30" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/><polyline points="10 9 9 9 8 9"/></svg>
            </div>
            <h2 style="margin:0 0 10px; color:#1f2937; font-size: 22px;">Akhir Semester Tiba!</h2>
            <p style="color:#4b5563; line-height:1.6; margin-bottom:24px; font-size:15px;">
                Silakan review dan bagikan pengalaman mata kuliahmu untuk membantu teman-teman dan junior lainnya di semester depan.
            </p>
            <a href="https://github.com/anisadevina/praktikum-rpl-a-6" target="_blank" style="display:block; background:#5A8C23; color:#fff; padding:12px 20px; border-radius:8px; text-decoration:none; font-weight:bold; font-size:16px; margin-bottom:12px; transition: 0.2s;">
                Isi Google Form Sekarang
            </a>
            <button id="btn-tutup-popup" style="background:none; border:none; color:#6b7280; font-size:14px; cursor:pointer; text-decoration:underline;">
                Nanti Saja, Lanjut ke Study Scope
            </button>
        `;

    overlay.appendChild(modal);
    document.body.appendChild(overlay);

    // Aksi tutup modal
    document.getElementById("btn-tutup-popup").addEventListener("click", () => {
        overlay.remove();
    });
}

// Eksekusi fungsinya
cekDanTampilkanPopup();
