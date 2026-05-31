// ─── Konfigurasi Path Halaman ────────────────────────────────────────────────
const PAGE_PATHS = {
    beranda: "/beranda",
    matkul: "/matkul",
    forum: "/forum",
    unggah: "/unggah",
    arsip: "/arsip",
};

const ACTIVE_PAGE = "matkul";

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

// ─── Search Bar (Membaca URL dan Arahkan Enter) ───────────────────────────────
function setupSearch() {
    const input = document.getElementById("search-input");
    if (!input) return;

    // Isi kolom input dengan kata kunci yang sedang dicari (dari URL)
    const params = new URLSearchParams(window.location.search);
    const currentQuery = params.get("q");
    if (currentQuery) input.value = currentQuery;

    input.addEventListener("keydown", (e) => {
        if (e.key === "Enter") {
            const query = input.value.trim();
            // Arahkan ke URL dengan query, atau kembalikan ke /matkul normal jika kosong
            window.location.href = query
                ? `/matkul?q=${encodeURIComponent(query)}`
                : "/matkul";
        }
    });
}

// ─── Helper: Cetak HTML Card Mata Kuliah ──────────────────────────────────────
function generateMatkulCard(mk) {
    return `
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
            <div class="card-difficulty">Tingkat kesulitan</div>
            <div class="card-rating">
              <span class="card-rating-star">★</span>
              <span class="card-rating-score">${mk.tingkat_kesulitan}/5</span>
            </div>
            <p class="card-arsip">${mk.arsip} arsip (materi, tugas, soal)</p>
            <a class="card-btn" href="/matkul/detail?id=${mk.id_matkul}">Lihat Selengkapnya</a>
          </div>
        </div>
    `;
}

// ─── Helper: Buat Ulang Pagination (Bootstrap 4 Style) ────────────────────────
function renderPagination(paginationData, currentQuery) {
    const container = document.getElementById("pagination-container");
    if (!container) return;
    container.innerHTML = "";

    // Sembunyikan pagination jika cuma ada 1 halaman
    if (paginationData.last_page <= 1) return;

    let html = '<ul class="pagination">';

    // Tombol Sebelumnya (Prev)
    if (paginationData.prev_page_url) {
        html += `<li class="page-item"><a class="page-link" href="?page=${paginationData.current_page - 1}&q=${currentQuery}">‹</a></li>`;
    } else {
        html += `<li class="page-item disabled"><span class="page-link">‹</span></li>`;
    }

    // Angka Halaman
    for (let i = 1; i <= paginationData.last_page; i++) {
        if (i === paginationData.current_page) {
            html += `<li class="page-item active"><span class="page-link">${i}</span></li>`;
        } else {
            html += `<li class="page-item"><a class="page-link" href="?page=${i}&q=${currentQuery}">${i}</a></li>`;
        }
    }

    // Tombol Selanjutnya (Next)
    if (paginationData.next_page_url) {
        html += `<li class="page-item"><a class="page-link" href="?page=${paginationData.current_page + 1}&q=${currentQuery}">›</a></li>`;
    } else {
        html += `<li class="page-item disabled"><span class="page-link">›</span></li>`;
    }

    html += "</ul>";
    container.innerHTML = html;
}

// ─── Fetch Utama: Ambil Data dari API ─────────────────────────────────────────
async function fetchMatkulData() {
    try {
        // 1. Baca parameter dari URL (page & q)
        const params = new URLSearchParams(window.location.search);
        const query = params.get("q") || "";
        const page = params.get("page") || 1;

        // 2. Kirim ke server
        const response = await fetch(
            `/matkul/data?page=${page}&q=${encodeURIComponent(query)}`,
        );
        if (!response.ok) throw new Error("Gagal mengambil data mata kuliah");

        const json = await response.json();
        const data = json.data;

        // 3. Render Topbar Username
        const topbarUsername = document.getElementById("topbar-username");
        if (topbarUsername && data.user)
            topbarUsername.textContent = data.user.username;

        // 4. Render Judul Utama
        const pageTitle = document.getElementById("page-title");
        if (pageTitle) {
            if (query) {
                pageTitle.style.display = "block";
                pageTitle.textContent = `Hasil pencarian: "${query}"`;
            } else {
                pageTitle.style.display = "none";
            }
        }

        // 5. Render "Terakhir Dilihat" (Hanya muncul jika tidak sedang mencari keyword)
        const titleTerakhir = document.getElementById("title-terakhir");
        const gridTerakhir = document.getElementById("matkul-terakhir-grid");

        if (
            !query &&
            data.mataKuliahTerakhir &&
            data.mataKuliahTerakhir.length > 0
        ) {
            if (titleTerakhir) titleTerakhir.style.display = "block";
            if (gridTerakhir) {
                gridTerakhir.style.display = "grid";
                gridTerakhir.innerHTML = data.mataKuliahTerakhir
                    .map(generateMatkulCard)
                    .join("");
            }
        } else {
            // Sembunyikan jika sedang mencari atau data kosong
            if (titleTerakhir) titleTerakhir.style.display = "none";
            if (gridTerakhir) {
                gridTerakhir.style.display = "none";
                gridTerakhir.innerHTML = "";
            }
        }

        // 6. Render "Semua Matkul"
        const titleSemua = document.getElementById("title-semua");
        const gridSemua = document.getElementById("matkul-semua-grid");

        if (titleSemua) {
            titleSemua.style.display = !query ? "block" : "none"; // Sembunyikan tulisan "Mata Kuliah Lain" kalau sedang mencari
        }

        if (gridSemua) {
            if (data.semuaMatkul.data.length === 0) {
                gridSemua.innerHTML = `<p style="color:var(--color-text-muted); font-style:italic;">Tidak ada mata kuliah yang ditemukan.</p>`;
            } else {
                gridSemua.innerHTML = data.semuaMatkul.data
                    .map(generateMatkulCard)
                    .join("");
            }
        }

        // 7. Bangun Tombol Pagination
        renderPagination(data.semuaMatkul, query);
    } catch (error) {
        console.error("Error:", error);
        const gridSemua = document.getElementById("matkul-semua-grid");
        if (gridSemua)
            gridSemua.innerHTML =
                '<p style="color: red;">Gagal memuat data. Silakan muat ulang halaman.</p>';
    }
}

// ─── Fitur Logout Berbasis API ────────────────────────────────────────────────
function setupLogout() {
    const btnKeluar = document.getElementById("btn-keluar");
    if (btnKeluar) {
        btnKeluar.addEventListener("click", async (e) => {
            e.preventDefault();
            try {
                const csrfInput = document.querySelector(
                    '#logout-form input[name="_token"]',
                );
                if (!csrfInput) return;

                const response = await fetch("/logout", {
                    method: "POST",
                    headers: {
                        "X-CSRF-TOKEN": csrfInput.value,
                        Accept: "application/json",
                    },
                });

                if (response.ok) {
                    sessionStorage.removeItem("loggedUser");
                    window.location.href = "/";
                }
            } catch (error) {
                console.error("Error saat logout:", error);
            }
        });
    }
}

// ─── Inisialisasi Saat Halaman Dimuat ─────────────────────────────────────────
document.addEventListener("DOMContentLoaded", () => {
    setupSidebarActive();
    setupSearch();
    setupLogout();

    sessionStorage.setItem(
        "savedMatkulUrl",
        window.location.pathname + window.location.search,
    );

    // Tarik data API setelah DOM siap
    fetchMatkulData();
});
