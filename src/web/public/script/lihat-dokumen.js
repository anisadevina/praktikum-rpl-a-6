// Konfigurasi Path 
const PAGE_PATHS = {
    beranda: "/beranda",
    matkul: "/matkul",
    forum: "/forum",
    unggah: "/unggah",
    arsip: "/arsip",
};

// Baca parameter 'prev'
const PREV_URL = decodeURIComponent(
    new URLSearchParams(window.location.search).get("prev") || ""
) || "/arsip"; // fallback ke arsip kalau tidak ada

// Tentukan sidebar aktif berdasarkan dari mana user datang
const ACTIVE_PAGE = PREV_URL.includes("/matkul") ? "matkul" : "arsip";

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

// Username dari session
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

// Search
function setupSearch() {
    const input = document.getElementById("search-input");
    if (!input) return;
    input.addEventListener("keydown", (e) => {
        if (e.key === "Enter") {
            const query = input.value.trim();
            if (!query) return;
            window.location.href = `${PAGE_PATHS.arsip}?q=${encodeURIComponent(query)}`;
        }
    });
}

// Tombol Back 
function setupBack() {
    const btn = document.getElementById("btn-back");
    if (!btn) return;
    btn.addEventListener("click", () => {
        // Selalu pakai PREV_URL agar konsisten di semua flow
        window.location.href = PREV_URL;
    });
}

// Zoom Controls 
function setupZoom() {
    const iframe = document.getElementById("pdf-iframe");
    const zoomLabel = document.getElementById("zoom-level");
    if (!iframe || !zoomLabel) return;

    let zoom = 100;
    const STEP = 10;
    const MIN = 50;
    const MAX = 200;

    function applyZoom() {
        iframe.style.transform = `scale(${zoom / 100})`;
        iframe.style.transformOrigin = "top center";
        // Kompensasi height agar tidak terpotong
        iframe.style.height = `${(100 / zoom) * 100}%`;
        zoomLabel.textContent = `${zoom}%`;
    }

    document.getElementById("btn-zoom-in")?.addEventListener("click", () => {
        if (zoom < MAX) { zoom += STEP; applyZoom(); }
    });

    document.getElementById("btn-zoom-out")?.addEventListener("click", () => {
        if (zoom > MIN) { zoom -= STEP; applyZoom(); }
    });
}

// Sembunyikan placeholder jika iframe berhasil load 
function setupIframeLoad() {
    const iframe = document.getElementById("pdf-iframe");
    const placeholder = document.getElementById("viewer-placeholder");
    if (!iframe || !placeholder) return;

    if (iframe.src && iframe.src !== window.location.href) {
        iframe.addEventListener("load", () => {
            placeholder.style.display = "none";
        });
    }
}

// Tombol Keluar
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

// Init
document.addEventListener("DOMContentLoaded", () => {
    setupSidebarActive();
    renderUsername();
    setupSearch();
    setupBack();
    setupZoom();
    setupIframeLoad();
    setupLogout();
});