document.addEventListener("DOMContentLoaded", function () {
    // Navigasi Sidebar
    const PAGE_PATHS = {
        beranda: "/beranda",
        matkul: "/matkul",
        forum: "/forum",
        unggah: "/unggah",
        arsip: "/arsip",
        review: "/unggah/admin",
    };

    document.querySelectorAll(".nav-item[data-page]").forEach((item) => {
        item.addEventListener("click", () => {
            const target = PAGE_PATHS[item.dataset.page];
            if (target) window.location.href = target;
        });
    });

    // Render Tabel dari API
    async function fetchDaftarUnggahan() {
        try {
            const response = await fetch("/unggah/data");
            const json = await response.json();
            if (json.status !== "success") return;

            // isi nama user
            const topbarUsername = document.querySelector(".topbar-username");
            if (topbarUsername && json.data.user) {
                topbarUsername.textContent = json.data.user.username;
            }

            // cek role
            if (json.data.user && json.data.user.role === "admin") {
                const menuAdmin = document.getElementById("menu-review-admin");
                if (menuAdmin) {
                    menuAdmin.style.display = "flex";
                    menuAdmin.classList.remove("hidden");
                }
            }

            renderTable(json.data.dokumen);
        } catch (err) {
            console.error("Gagal memuat daftar unggahan:", err);
            document.getElementById("unggah-table-body").innerHTML =
                `<tr><td colspan="5" class="td-empty">Gagal memuat data.</td></tr>`;
        }
    }

    function renderTable(list) {
        const tbody = document.getElementById("unggah-table-body");
        if (!tbody) return;

        if (!list || list.length === 0) {
            tbody.innerHTML = `<tr><td colspan="5" class="td-empty">Belum ada file yang diunggah.</td></tr>`;
            return;
        }

        const statusLabel = {
            menunggu: "Menunggu",
            disetujui: "Disetujui",
            ditolak: "Ditolak",
        };

        tbody.innerHTML = list
            .map((d) => {
                const status = (d.status || "menunggu").toLowerCase();
                const label = statusLabel[status] || status;
                const catatan = d.catatan_admin || "–";
                return `
                <tr>
                    <td class="td-filename">${escapeHTML(d.judul || d.judul_file || "–")}</td>
                    <td>${escapeHTML(d.nama_matkul || "–")}</td>
                    <td>${escapeHTML(String(d.tahun_dokumen || d.tahun || "–"))}</td>
                    <td><span class="status-badge status-${status}">${label}</span></td>
                    <td class="td-catatan">${escapeHTML(catatan)}</td>
                </tr>
            `;
            })
            .join("");
    }

    fetchDaftarUnggahan();

    // Logout
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

    // Utility
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
});
