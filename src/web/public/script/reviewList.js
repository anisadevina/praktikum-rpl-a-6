document.addEventListener("DOMContentLoaded", function () {
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