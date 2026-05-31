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

// ─── Search Bar ───────────────────────────────────────────────────────────────
function setupSearch() {
    const input = document.getElementById("search-input");
    if (!input) return;
    input.addEventListener("keydown", (e) => {
        if (e.key === "Enter") {
            const query = input.value.trim();
            if (!query) return;
            // Langsung arahkan ke halaman daftar matkul dengan query
            window.location.href = `${PAGE_PATHS.matkul}?q=${encodeURIComponent(query)}`;
        }
    });
}

// ─── Filter Tahun ─────────────────────────────────────────────────────────────
function setupFilter() {
    const select = document.getElementById("filter-tahun");
    if (!select) return;

    select.addEventListener("change", function () {
        const selectedTahun = this.value;
        const urlParams = new URLSearchParams(window.location.search);

        if (selectedTahun) {
            urlParams.set("tahun", selectedTahun);
        } else {
            urlParams.delete("tahun");
        }

        // Ubah URL di peramban secara diam-diam
        const newUrl = `${window.location.pathname}?${urlParams.toString()}`;
        window.history.replaceState({ path: newUrl }, "", newUrl);

        // Tampilkan efek loading pada kotak daftar arsip
        const arsipList = document.getElementById("arsip-list");
        if (arsipList) {
            arsipList.innerHTML = `<p style="color: var(--color-text-muted); font-style: italic; padding: 20px 0;">Menyaring arsip...</p>`;
        }

        // Panggil ulang fungsi fetch untuk menarik data terbaru
        fetchDetailMatkul();
    });
}

// ─── Tombol Kembali & Simpan Session ──────────────────────────────────────────
function setupBack() {
    const btn = document.getElementById("btn-back");
    if (!btn) return;

    btn.addEventListener("click", () => {
        const titleEl = document.getElementById("matkul-title");

        // Simpan data terakhir dilihat hanya jika loading sudah selesai
        if (titleEl && titleEl.textContent !== "Loading...") {
            const matkulData = {
                id: new URLSearchParams(window.location.search).get("id"),
                nama: titleEl.textContent,
                tingkat: document.getElementById("matkul-rating").textContent,
                arsip: document.getElementById("matkul-arsip-count")
                    .textContent,
            };
            sessionStorage.setItem(
                "lastVisitedMatkul",
                JSON.stringify(matkulData),
            );
        }
        const backUrl =
            sessionStorage.getItem("savedMatkulUrl") || PAGE_PATHS.matkul;
        window.location.href = backUrl;
    });
}

// ─── Fetch Utama: Ambil Data Detail API ───────────────────────────────────────
async function fetchDetailMatkul() {
    try {
        const params = new URLSearchParams(window.location.search);
        const id = params.get("id");
        const tahun = params.get("tahun") || "";

        // Jika tidak ada ID, kembalikan ke halaman matkul
        if (!id) {
            window.location.href = "/matkul";
            return;
        }

        // Sinkronkan nilai dropdown filter dengan URL
        const filterSelect = document.getElementById("filter-tahun");
        if (filterSelect && tahun) {
            filterSelect.value = tahun;
        }

        // Tembak API
        const response = await fetch(
            `/matkul/detail/data?id=${id}&tahun=${tahun}`,
        );
        if (!response.ok) throw new Error("Gagal mengambil data detail matkul");

        const json = await response.json();

        // Jika ID matkul tidak valid/tidak ada di database
        if (json.status === "error") {
            window.location.href = "/matkul";
            return;
        }

        const data = json.data;

        // 1. Render Topbar Username
        const topbarUsername = document.getElementById("topbar-username");
        if (topbarUsername && data.user) {
            topbarUsername.textContent = data.user.username;
        }

        // 2. Render Info Matkul
        document.getElementById("breadcrumb-text").textContent =
            `Mata Kuliah - ${data.matkul.nama_matkul}`;
        document.getElementById("matkul-title").textContent =
            data.matkul.nama_matkul;
        document.getElementById("matkul-desc").textContent =
            data.matkul.deskripsi ||
            "Belum ada deskripsi untuk mata kuliah ini.";
        document.getElementById("fokus-matkul-name").textContent =
            data.matkul.nama_matkul;
        document.getElementById("matkul-rating").textContent =
            data.matkul.tingkat_kesulitan;
        document.getElementById("matkul-rating-sub").textContent =
            data.teksKesulitan;
        document.getElementById("matkul-arsip-count").textContent =
            data.jumlahArsip;

        // 3. Render Daftar Arsip
        const arsipList = document.getElementById("arsip-list");
        if (arsipList) {
            if (data.daftarArsip.length === 0) {
                arsipList.innerHTML = `<p style="color: var(--color-text-muted); font-style: italic; padding: 20px 0;">Belum ada arsip materi atau soal untuk mata kuliah ini.</p>`;
            } else {
                arsipList.innerHTML = data.daftarArsip
                    .map(
                        (arsip) => `
                    <div class="arsip-item" data-id="${arsip.id_dokumen || arsip.id}">
                        <div class="arsip-icon">
                            <svg viewBox="0 0 24 24">
                                <path d="M8 17l4 4 4-4"/>
                                <path d="M12 12v9"/>
                                <path d="M20.88 18.09A5 5 0 0 0 18 9h-1.26A8 8 0 1 0 3 16.29"/>
                            </svg>
                        </div>
                        <div class="arsip-info">
                            <span class="arsip-name">${arsip.nama_dokumen || arsip.nama}</span>
                            <div class="arsip-meta">
                                <span class="arsip-badge">${arsip.tahun_dokumen || arsip.tahun || ""}</span>
                                <span class="arsip-date">Diunggah pada ${arsip.waktu_unggah || arsip.created_at}</span>
                            </div>
                        </div>
                        <div class="arsip-bookmark" data-id="${arsip.id_dokumen || arsip.id}">
                            <svg viewBox="0 0 24 24">
                                <path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z"/>
                            </svg>
                        </div>
                    </div>
                `,
                    )
                    .join("");
            }

            // Pasang event listener ke item arsip yang baru saja dirender
            setupArsipItems();
            setupBookmarks();
        }
    } catch (error) {
        console.error("Error memuat detail matkul:", error);
        const arsipList = document.getElementById("arsip-list");
        if (arsipList)
            arsipList.innerHTML =
                '<p style="color: red;">Gagal memuat data. Silakan muat ulang halaman.</p>';
    }
}

// ─── Aksi Klik Item Arsip ─────────────────────────────────────────────────────
function setupArsipItems() {
    document.querySelectorAll(".arsip-item").forEach((item) => {
        item.addEventListener("click", (e) => {
            // Jangan trigger kalau user mengeklik ikon bookmark
            if (e.target.closest(".arsip-bookmark")) return;
            const id = item.dataset.id;
            console.log(`Buka arsip id=${id}`);
            // Nanti ubah ke: window.location.href = `/arsip/view/${id}`;
        });
    });
}

// ─── Bookmark Toggle ──────────────────────────────────────────────────────────
function setupBookmarks() {
    document.querySelectorAll(".arsip-bookmark").forEach((btn) => {
        btn.addEventListener("click", (e) => {
            e.stopPropagation(); // Mencegah arsip-item ikut terklik
            btn.classList.toggle("bookmarked");
            const id = btn.dataset.id;
            console.log(`Bookmark diubah untuk arsip id=${id}`);
            // Nanti tambahkan logika fetch API untuk menyimpan ke database
        });
    });
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

// ─── Init - DOMContentLoaded ──────────────────────────────────────────────────
document.addEventListener("DOMContentLoaded", () => {
    setupSidebarActive();
    setupSearch();
    setupFilter();
    setupBack();
    setupLogout();

    // Panggil fetch API segera setelah DOM siap
    fetchDetailMatkul();
});
