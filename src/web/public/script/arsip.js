// ─── Konfigurasi Path Halaman ─────────────────────────────────────────────────
const PAGE_PATHS = {
    beranda: "/beranda",
    matkul: "/matkul",
    forum: "/forum",
    unggah: "/unggah",
    arsip: "/arsip",
};

const ACTIVE_PAGE = "arsip";

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

    // Isi kolom input dengan kata kunci yang sedang dicari (dari URL)
    const params = new URLSearchParams(window.location.search);
    const currentQuery = params.get("q");
    if (currentQuery) input.value = currentQuery;

    input.addEventListener("keydown", (e) => {
        if (e.key === "Enter") {
            const query = input.value.trim();
            const urlParams = new URLSearchParams(window.location.search);
            if (query) {
                urlParams.set("q", query);
            } else {
                urlParams.delete("q");
            }
            window.location.href = `${PAGE_PATHS.arsip}?${urlParams.toString()}`;
        }
    });

    // Saat input dikosongkan, langsung refresh
    input.addEventListener("input", () => {
        if (input.value === "") {
            const urlParams = new URLSearchParams(window.location.search);
            urlParams.delete("q");
            window.location.href = `${PAGE_PATHS.arsip}?${urlParams.toString()}`;
        }
    });
}

// ─── Filter Tahun ─────────────────────────────────────────────────────────────
function setupFilter() {
    const select = document.getElementById("filter-tahun");
    if (!select) return;

    // Sinkronkan nilai select dengan URL saat ini
    const params = new URLSearchParams(window.location.search);
    const currentTahun = params.get("tahun");
    if (currentTahun) select.value = currentTahun;

    select.addEventListener("change", function () {
        const selectedTahun = this.value;
        const urlParams = new URLSearchParams(window.location.search);

        if (selectedTahun) {
            urlParams.set("tahun", selectedTahun);
        } else {
            urlParams.delete("tahun");
        }

        window.location.search = urlParams.toString();
    });
}

// ─── Helper: Generate HTML satu item arsip ────────────────────────────────────
function generateArsipItem(arsip) {
    return `
        <div class="arsip-item" data-id="${arsip.id_dokumen}" data-file-url="${arsip.file_url}">
          <div class="arsip-icon">
            <svg viewBox="0 0 24 24">
              <path d="M13 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V9z"/>
              <polyline points="13 2 13 9 20 9"/>
            </svg>
          </div>
          <div class="arsip-info">
            <span class="arsip-name">${arsip.judul}</span>
            <div class="arsip-meta">
              <span class="arsip-badge">${arsip.tahun_dokumen}</span>
              <span class="arsip-date">Diunggah pada ${arsip.waktu_unggah_human}</span>
            </div>
          </div>
          <div class="arsip-bookmark bookmarked" data-id="${arsip.id_dokumen}">
            <svg viewBox="0 0 24 24">
              <path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z"/>
            </svg>
          </div>
        </div>
    `;
}

// ─── Fetch Utama: Ambil Data dari API ─────────────────────────────────────────
async function fetchArsipData() {
    try {
        const params = new URLSearchParams(window.location.search);
        const query = params.get("q") || "";
        const tahun = params.get("tahun") || "";

        const url = `/arsip/data?q=${encodeURIComponent(query)}&tahun=${encodeURIComponent(tahun)}`;
        const response = await fetch(url);
        if (!response.ok) throw new Error("Gagal mengambil data arsip");

        const json = await response.json();
        const data = json.data;

        // 1. Render username di topbar
        const topbarUsername = document.getElementById("topbar-username");
        if (topbarUsername && data.user) {
            topbarUsername.textContent = data.user.username;
        }

        // 2. Render daftar arsip
        const arsipList = document.getElementById("arsip-list");
        if (!arsipList) return;

        if (data.daftarArsip.length === 0) {
            arsipList.innerHTML = `
                <div class="arsip-empty">
                  <svg viewBox="0 0 24 24">
                    <path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z"/>
                  </svg>
                  <p>Belum ada arsip tersimpan.</p>
                </div>
            `;
        } else {
            arsipList.innerHTML = data.daftarArsip.map(generateArsipItem).join("");
        }

        // 3. Pasang event listener setelah elemen dirender
        setupArsipItems();
        setupBookmarks();

    } catch (error) {
        console.error("Error:", error);
        const arsipList = document.getElementById("arsip-list");
        if (arsipList) {
            arsipList.innerHTML = '<p style="color: red;">Gagal memuat data. Silakan muat ulang halaman.</p>';
        }
    }
}

// ─── Klik Item Arsip > Buka file langsung di tab baru ────────────────────────
function setupArsipItems() {
    document.querySelectorAll(".arsip-item").forEach((item) => {
        item.addEventListener("click", (e) => {
            if (e.target.closest(".arsip-bookmark")) return;

            const fileUrl = item.dataset.fileUrl;
            if (fileUrl) {
                window.open(fileUrl, "_blank");
            }
        });
    });
}

// ─── Bookmark Toggle ──────────────────────────────────────────────────────────
function setupBookmarks() {
    document.querySelectorAll(".arsip-bookmark").forEach((btn) => {
        btn.addEventListener("click", async (e) => {
            e.stopPropagation();

            const id         = btn.dataset.id;
            const isBookmark = btn.classList.contains("bookmarked");

            // Optimistic UI — langsung toggle dulu sebelum response
            btn.classList.toggle("bookmarked");

            try {
                const response = await fetch(`/arsip/bookmark/${id}`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        "X-CSRF-TOKEN": document.querySelector('meta[name="csrf-token"]')?.content ?? "",
                    },
                    body: JSON.stringify({ bookmarked: !isBookmark }),
                });

                if (!response.ok) throw new Error("Gagal menyimpan bookmark");

                const data = await response.json();

                // Kalau di-unbookmark, hapus item dari list
                if (!data.bookmarked) {
                    const item = btn.closest(".arsip-item");
                    if (item) {
                        item.style.transition = "opacity 0.3s";
                        item.style.opacity    = "0";
                        setTimeout(() => item.remove(), 300);
                    }
                }
            } catch (err) {
                // Rollback toggle kalau gagal
                btn.classList.toggle("bookmarked");
                console.error("Bookmark error:", err);
            }
        });
    });
}

// ─── Tombol Keluar ────────────────────────────────────────────────────────────
function setupLogout() {
    const btnKeluar = document.getElementById("btn-keluar");
    if (!btnKeluar) return;
    btnKeluar.addEventListener("click", async (e) => {
        e.preventDefault();
        try {
            const csrfInput = document.querySelector('#logout-form input[name="_token"]');
            if (!csrfInput) throw new Error("CSRF token tidak ditemukan");

            const response = await fetch("/logout", {
                method: "POST",
                headers: {
                    "X-CSRF-TOKEN": csrfInput.value,
                    "Accept": "application/json",
                },
            });

            if (response.ok) {
                sessionStorage.removeItem("loggedUser");
                window.location.href = "/";
            } else {
                console.error("Logout ditolak oleh server.");
            }
        } catch (error) {
            console.error("Error saat logout:", error);
        }
    });
}

// ─── Inisialisasi Saat Halaman Dimuat ─────────────────────────────────────────
document.addEventListener("DOMContentLoaded", () => {
    setupSidebarActive();
    setupSearch();
    setupFilter();
    setupLogout();

    // Tarik data API setelah DOM siap
    fetchArsipData();
});