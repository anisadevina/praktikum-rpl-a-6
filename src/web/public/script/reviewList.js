document.addEventListener("DOMContentLoaded", async function () {
    const tbody = document.getElementById("tabel-review");

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
