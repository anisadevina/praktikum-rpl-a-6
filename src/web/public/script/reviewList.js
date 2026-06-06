document.addEventListener("DOMContentLoaded", function () {
    // ─── 1. NAVIGASI SIDEBAR ──────────────────────────────────
    const PAGE_PATHS = {
        beranda: "/beranda",
        matkul: "/matkul",
        forum: "/forum",
        unggah: "/unggah",
        review: "/review-dokumen",
        arsip: "/arsip",
    };

    document.querySelectorAll(".nav-item[data-page]").forEach((item) => {
        item.addEventListener("click", () => {
            const target = PAGE_PATHS[item.dataset.page];
            if (target) window.location.href = target;
        });
    });

    // ─── 2. FUNGSI AMBIL DATA API (FETCH) ─────────────────────
    const tbody = document.getElementById("review-table-body");

    // Bungkus proses fetch ke dalam fungsi async yang rapi
    async function fetchReviewList() {
        if (!tbody) return;

        try {
            const res = await fetch("/api/review-dokumen");
            const json = await res.json();

            if (json.status === "success" && json.data.length > 0) {
                // Render tabel jika ada data
                tbody.innerHTML = json.data
                    .map((d) => {
                        // Format tanggal
                        const dateObj = new Date(d.waktu_unggah);
                        const tgl = isNaN(dateObj)
                            ? d.waktu_unggah
                            : dateObj.toLocaleDateString("id-ID");

                        // Huruf kapital untuk status
                        const statusLabel = d.status
                            ? d.status.charAt(0).toUpperCase() +
                              d.status.slice(1)
                            : "Menunggu";

                        // Perhatikan: window.location.href langsung dipasang di sini!
                        return `
                        <tr class="clickable-row" style="cursor:pointer;" onclick="window.location.href='/review-dokumen/${d.id_dokumen}'">
                            <td>${escapeHTML(d.judul)}</td>
                            <td>${escapeHTML(d.nama_matkul)}</td>
                            <td>${tgl}</td>
                            <td><span class="status-badge status-${(d.status || "menunggu").toLowerCase()}">${statusLabel}</span></td>
                        </tr>
                    `;
                    })
                    .join("");
            } else {
                tbody.innerHTML =
                    '<tr><td colspan="4" style="text-align:center;">Tidak ada dokumen yang perlu direview.</td></tr>';
            }
        } catch (err) {
            console.error("Fetch error:", err);
            tbody.innerHTML =
                '<tr><td colspan="4" style="text-align:center;">Gagal memuat data dari server.</td></tr>';
        }
    }

    // Panggil fungsi saat halaman selesai dimuat
    fetchReviewList();

    // ─── 3. UTILITY & LOGOUT ──────────────────────────────────

    // Fungsi untuk mencegah XSS (serangan script)
    function escapeHTML(str) {
        if (!str) return "";
        return String(str).replace(
            /[&<>'"]/g,
            (tag) =>
                ({
                    "&": "&amp;",
                    "<": "&lt;",
                    ">": "&gt;",
                    "'": "&#39;",
                    '"': "&quot;",
                })[tag] || tag,
        );
    }

    // Keluar / Logout
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

    const topbarSearchInput = document.querySelector(".search-bar input");
    if (topbarSearchInput) {
        topbarSearchInput.addEventListener("keydown", function (e) {
            // Jika user menekan tombol Enter
            if (e.key === "Enter") {
                e.preventDefault(); // Mencegah form tersubmit otomatis
                const query = this.value.trim();

                if (query !== "") {
                    // Pindah ke halaman matkul sambil membawa teks yang dicari
                    window.location.href = `/matkul?q=${encodeURIComponent(query)}`;
                } else {
                    // Jika kosong, sekadar pindah ke halaman matkul
                    window.location.href = `/matkul`;
                }
            }
        });
    }

    const menuPopup = document.getElementById("menu-popup-setting");
    const toggleBtn = document.getElementById("toggle-popup-btn");
    const slider = document.getElementById("toggle-slider");
    const circle = document.getElementById("toggle-circle");

    async function initPopupToggle() {
        const menuPopup = document.getElementById("menu-popup-setting");
        const toggleBtn = document.getElementById("toggle-popup-btn");

        if (!menuPopup || !toggleBtn) return;

        // Tampilkan menu setting
        menuPopup.style.display = "flex";
        menuPopup.classList.remove("hidden");

        // Ambil status saat ini dari API (Cache)
        try {
            const res = await fetch("/api/pengaturan/popup");
            const json = await res.json();

            // Cukup centang checkbox-nya, CSS akan otomatis menggesernya jadi warna Hijau!
            if (json.status === "success" && json.data === "on") {
                toggleBtn.checked = true;
            }
        } catch (e) {
            console.error("Gagal load status popup");
        }

        // Aksi ketika admin mengklik tombol On/Off
        toggleBtn.addEventListener("change", async function () {
            const isOn = this.checked;

            try {
                // 1. Cek CSRF Token
                const csrfMeta = document.querySelector(
                    'meta[name="csrf-token"]',
                );
                if (!csrfMeta) {
                    alert(
                        "GAGAL: Tag <meta name='csrf-token'> tidak ditemukan di file Blade!",
                    );
                    toggleBtn.checked = !isOn; // Kembalikan posisi jika gagal
                    return;
                }

                // 2. Tembak API
                const res = await fetch("/api/pengaturan/popup", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        Accept: "application/json",
                        "X-CSRF-TOKEN": csrfMeta.content,
                    },
                    body: JSON.stringify({ status: isOn ? "on" : "off" }),
                });

                // 3. Cek respon server
                const json = await res.json();

                if (!res.ok || json.status === "error") {
                    alert(
                        "Ditolak Server: " +
                            (json.message || "Terjadi error di backend."),
                    );
                    toggleBtn.checked = !isOn; // Kembalikan posisi jika gagal
                }
            } catch (e) {
                console.error(e);
                alert("Koneksi gagal, tidak bisa mengubah pengaturan!");
                toggleBtn.checked = !isOn; // Kembalikan posisi jika gagal
            }
        });
    }

    initPopupToggle();
});
