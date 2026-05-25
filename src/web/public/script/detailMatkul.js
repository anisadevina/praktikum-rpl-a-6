/**
 * STUDY SCOPE — Detail Mata Kuliah Page Script
 */

// ─── Konfigurasi Path ─────────────────────────────────────────────────────────
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

// ─── Username dari session ────────────────────────────────────────────────────
function renderUsername() {
    const el = document.getElementById("topbar-username");
    if (!el) return;
    const stored = sessionStorage.getItem("loggedUser");
    if (stored) {
        try {
            const user = JSON.parse(stored);
            if (user.username) el.textContent = user.username;
        } catch (_) {}
    }
}

// ─── Search ───────────────────────────────────────────────────────────────────
function setupSearch() {
    const input = document.getElementById("search-input");
    if (!input) return;
    input.addEventListener("keydown", (e) => {
        if (e.key === "Enter") {
            const query = input.value.trim();
            if (!query) return;
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

        // Atur parameter URL berdasarkan pilihan
        if (selectedTahun) {
            urlParams.set("tahun", selectedTahun);
        } else {
            urlParams.delete("tahun");
        }

        // Reload halaman untuk memicu filter di web.php
        window.location.search = urlParams.toString();
    });
}

// ─── Bookmark Toggle ──────────────────────────────────────────────────────────
function setupBookmarks() {
    document.querySelectorAll(".arsip-bookmark").forEach((btn) => {
        btn.addEventListener("click", (e) => {
            e.stopPropagation();
            btn.classList.toggle("bookmarked");
            const id = btn.dataset.id;
            // TODO: kirim ke backend untuk simpan/hapus bookmark
            // fetch(`/arsip/bookmark/${id}`, { method: 'POST', ... })
            console.log(`Bookmark toggled for arsip id=${id}`);
        });
    });
}

// ─── Klik Item Arsip ──────────────────────────────────────────────────────────
function setupArsipItems() {
    document.querySelectorAll(".arsip-item").forEach((item) => {
        item.addEventListener("click", (e) => {
            // Jangan trigger kalau klik bookmark
            if (e.target.closest(".arsip-bookmark")) return;
            const id = item.dataset.id;
            // TODO: buka/download arsip saat URL tersedia
            // window.location.href = `/arsip/view/${id}`;
            console.log(`Open arsip id=${id}`);
        });
    });
}

// ─── Tombol Keluar ────────────────────────────────────────────────────────────
function setupLogout() {
    const btnKeluar = document.getElementById("btn-keluar");
    if (!btnKeluar) return;
    btnKeluar.addEventListener("click", (e) => {
        e.preventDefault();
        sessionStorage.removeItem("loggedUser");
        const logoutForm = document.getElementById("logout-form");
        if (logoutForm) {
            logoutForm.submit();
        } else {
            window.location.href = "/login";
        }
    });
}

// ─── Init ─────────────────────────────────────────────────────────────────────
document.addEventListener("DOMContentLoaded", () => {
    setupSidebarActive();
    renderUsername();
    setupSearch();
    setupFilter();
    setupBookmarks();
    setupArsipItems();
    setupLogout();
});
