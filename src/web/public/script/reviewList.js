document.addEventListener("DOMContentLoaded", function () {
    // Navigasi Sidebar
    const PAGE_PATHS = {
        beranda: "/beranda",
        matkul:  "/matkul",
        forum:   "/forum",
        unggah:  "/unggah",
        review:  "/review-dokumen",
        arsip:   "/arsip",
    };

    document.querySelectorAll(".nav-item[data-page]").forEach((item) => {
        item.addEventListener("click", () => {
            const target = PAGE_PATHS[item.dataset.page];
            if (target) window.location.href = target;
        });
    });

    // Navigasi ke halaman detail saat baris diklik
    const tableRows = document.querySelectorAll(".clickable-row");
    tableRows.forEach(row => {
        row.addEventListener("click", function () {
            const url = this.getAttribute("data-href");
            if (url) window.location.href = url;
        });
    });

    // Keluar / Logout
    const btnKeluar = document.getElementById("btn-keluar");
    if (btnKeluar) {
        btnKeluar.addEventListener("click", (e) => {
            e.preventDefault();
            sessionStorage.removeItem("loggedUser");
            const form = document.getElementById("logout-form");
            form ? form.submit() : (window.location.href = "/login");
        });
    }
});