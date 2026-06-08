// Konfigurasi Path Halaman
const PAGE_PATHS = {
    beranda: "/beranda",
    matkul: "/matkul",
    forum: "/forum",
    unggah: "/unggah",
    arsip: "/arsip",
};

const ACTIVE_PAGE = "beranda";

// Sidebar Aktif
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

// Search
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

// Fetch Data API Beranda
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
                      <div class="card-thumbnail" style="background:${getBgMatkul(mk.nama_matkul)}; --icon-stroke:${getStrokeMatkul(mk.nama_matkul)};">
                        ${getIconMatkul(mk.nama_matkul)}
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

// Init
document.addEventListener("DOMContentLoaded", () => {
    setupSidebarActive();
    setupSearch();

    // Tarik data API beranda saat kerangka HTML sudah siap
    fetchBerandaData();

    // Tombol Keluar (Menggunakan API)
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