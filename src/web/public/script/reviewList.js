<<<<<<< HEAD
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
=======
document.addEventListener("DOMContentLoaded", async function () {
    const tbody = document.getElementById("tabel-review");
>>>>>>> b65fe0cae310072990658d3da0c593c3b9d85b52

    // Ambil data dari API
    try {
        const res = await fetch("/api/review-dokumen");
        const json = await res.json();

        if (json.status === "success" && json.data.length > 0) {
            tbody.innerHTML = json.data
                .map(
                    (d) => `
                <tr class="clickable-row" style="cursor:pointer;" onclick="window.location.href='/review-dokumen/${d.id_dokumen}'">
                    <td>${d.judul}</td>
                    <td>${d.nama_matkul}</td>
                    <td>${new Date(d.waktu_unggah).toLocaleDateString("id-ID")}</td>
                    <td><span class="status-badge status-${d.status}">${d.status}</span></td>
                </tr>
            `,
                )
                .join("");
        } else {
            tbody.innerHTML =
                '<tr><td colspan="4">Tidak ada dokumen.</td></tr>';
        }
    } catch (err) {
        tbody.innerHTML = '<tr><td colspan="4">Gagal memuat data.</td></tr>';
    }
});
