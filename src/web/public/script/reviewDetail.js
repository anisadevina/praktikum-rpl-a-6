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

    // ─── 2. LOGIKA EDITOR TEKS (FORMATTING & COUNTER) ─────────
    const editor = document.getElementById("editor");
    const hiddenCatatan = document.getElementById("hidden-catatan");
    const counter = document.getElementById("char-counter");
    const formatBtns = document.querySelectorAll(".format-btn");
    const formatSelect = document.getElementById("format-select");

    if (editor) {
        formatBtns.forEach((btn) => {
            btn.addEventListener("click", function (e) {
                e.preventDefault();
                const command = this.getAttribute("data-command");
                document.execCommand(command, false, null);
                editor.focus();
                checkActiveFormats();
            });
        });

        if (formatSelect) {
            formatSelect.addEventListener("change", function () {
                document.execCommand("formatBlock", false, this.value);
                editor.focus();
            });
        }

        editor.addEventListener("keyup", checkActiveFormats);
        editor.addEventListener("mouseup", checkActiveFormats);

        function checkActiveFormats() {
            formatBtns.forEach((btn) => {
                const command = btn.getAttribute("data-command");
                if (command !== "undo" && document.queryCommandState(command)) {
                    btn.classList.add("active");
                } else {
                    btn.classList.remove("active");
                }
            });
        }

        editor.addEventListener("input", function () {
            let textLength = this.innerText.replace(/\n/g, "").length;

            if (textLength > 1000) {
                alert("Maksimal 1000 karakter!");
                this.blur();
                textLength = 1000;
            }

            if (counter) counter.textContent = `${textLength}/1000`;
            if (hiddenCatatan) hiddenCatatan.value = this.innerHTML;
        });
    }

    // ─── 3. AMBIL DATA DARI API (LOAD DETAIL) ─────────────────
    const urlParts = window.location.pathname.split("/");
    const idDokumen = urlParts[urlParts.length - 1];

    let filePdfUrl = "#"; // Variabel untuk menyimpan URL PDF

    async function loadDetail() {
        try {
            const res = await fetch(`/api/review-dokumen/${idDokumen}`);
            const json = await res.json();

            if (json.status === "success") {
                // Update Topbar Username
                const topbarUsername =
                    document.getElementById("topbar-username");
                if (topbarUsername && json.user) {
                    topbarUsername.textContent = json.user.username;
                }

                // Update Teks Informasi
                const valMatkul = document.getElementById("val-matkul");
                const valTahun = document.getElementById("val-tahun");
                const valDosen = document.getElementById("val-dosen");

                if (valMatkul)
                    valMatkul.textContent = json.data.nama_matkul || "-";
                if (valTahun)
                    valTahun.textContent =
                        json.data.tahun_dokumen || json.data.tahun || "-";

                // Fallback ekstra aman: pakai json.data.dosen ATAU json.data.nama_dosen
                if (valDosen)
                    valDosen.textContent =
                        json.data.dosen || json.data.nama_dosen || "-";

                // Update URL PDF
                if (json.data.file_path) {
                    filePdfUrl = `/storage/${json.data.file_path}`;
                }

                if (json.data.status !== "menunggu") {
                    // 1. Sembunyikan tombol "Kirim"
                    const btnKirim = document.querySelector(".btn-kirim");
                    if (btnKirim) btnKirim.style.display = "none";

                    // 2. Set pilihan radio sesuai status dari DB dan matikan kliknya
                    const radios = document.querySelectorAll(
                        'input[name="status_dokumen"]',
                    );
                    radios.forEach((radio) => {
                        radio.disabled = true;
                        if (radio.value === json.data.status)
                            radio.checked = true;
                    });

                    // 3. Matikan teks editor
                    const editorContainer = document.getElementById("editor");
                    if (editorContainer) {
                        editorContainer.setAttribute(
                            "contenteditable",
                            "false",
                        );
                        editorContainer.style.backgroundColor = "#f5f5f5";
                        editorContainer.innerHTML =
                            json.data.catatan_admin ||
                            "<i>Tidak ada catatan.</i>";
                    }

                    // 4. Tambahkan pesan peringatan visual
                    const actionDiv = document.querySelector(".action-buttons");
                    if (actionDiv) {
                        const pesan = document.createElement("span");
                        // Warna teks: merah gelap jika ditolak, hijau jika disetujui
                        pesan.style.color =
                            json.data.status === "ditolak"
                                ? "#721c24"
                                : "#155724";
                        pesan.style.fontWeight = "bold";
                        pesan.style.marginRight = "auto";

                        const kataStatus =
                            json.data.status.charAt(0).toUpperCase() +
                            json.data.status.slice(1);
                        pesan.innerHTML = `Dokumen ini telah ${kataStatus} permanen.`;
                        actionDiv.prepend(pesan);
                    }
                }
            } else {
                console.error("Gagal dari server:", json.message);
                alert("Gagal memuat data: " + json.message);
            }
        } catch (e) {
            console.error("Gagal memuat detail dokumen:", e);
        }
    }

    if (idDokumen && !isNaN(idDokumen)) {
        loadDetail();
    }

    // Klik tombol preview PDF
    const btnPreviewPdf = document.getElementById("btn-preview-pdf");
    if (btnPreviewPdf) {
        btnPreviewPdf.addEventListener("click", function () {
            if (filePdfUrl !== "#") {
                window.open(filePdfUrl, "_blank");
            } else {
                alert("File PDF belum selesai dimuat atau tidak ditemukan.");
            }
        });
    }

    // ─── 4. PROSES PENGIRIMAN REVIEW (API SUBMIT) ─────────────
    const formReview = document.getElementById("form-review");

    if (formReview) {
        formReview.addEventListener("submit", async function (e) {
            e.preventDefault();

            const statusDipilih = document.querySelector(
                'input[name="status_dokumen"]:checked',
            );

            if (!statusDipilih) {
                alert("Pilih status Setujui atau Tolak terlebih dahulu!");
                return;
            }

            // Validasi: Catatan WAJIB jika ditolak
            const teksMurni = editor ? editor.innerText.trim() : "";
            if (statusDipilih.value === "ditolak" && teksMurni === "") {
                alert("Catatan WAJIB diisi jika kamu menolak dokumen ini!");
                if (editor) editor.focus();
                return;
            }

            // Pindahkan teks HTML editor ke input hidden
            if (hiddenCatatan && editor) {
                hiddenCatatan.value = editor.innerHTML;
            }

            const formData = new FormData(formReview);
            const csrfMeta = document.querySelector('meta[name="csrf-token"]');
            const csrfToken = csrfMeta ? csrfMeta.content : "";

            const btnSubmit = formReview.querySelector(".btn-kirim");
            if (btnSubmit) {
                btnSubmit.textContent = "Mengirim...";
                btnSubmit.disabled = true;
            }

            try {
                const res = await fetch(`/api/review-dokumen/${idDokumen}`, {
                    method: "POST",
                    headers: {
                        "X-CSRF-TOKEN": csrfToken,
                        Accept: "application/json",
                    },
                    body: formData,
                });

                const json = await res.json();

                if (res.ok && json.status === "success") {
                    alert("Berhasil: " + json.message);
                    window.location.href = "/review-dokumen";
                } else {
                    alert(
                        "Gagal: " +
                            (json.message ||
                                "Terjadi kesalahan pada validasi."),
                    );
                }
            } catch (err) {
                console.error("API Error:", err);
                alert("Kesalahan jaringan. Gagal mengirim review.");
            } finally {
                if (btnSubmit) {
                    btnSubmit.textContent = "Kirim";
                    btnSubmit.disabled = false;
                }
            }
        });
    }

    // ─── 5. KELUAR / LOGOUT ───────────────────────────────────
    const btnKeluar = document.getElementById("btn-keluar");
    if (btnKeluar) {
        btnKeluar.addEventListener("click", async (e) => {
            e.preventDefault();
            try {
                const csrfInput = document.querySelector(
                    '#logout-form input[name="_token"]',
                );

                if (!csrfInput) {
                    window.location.href = "/login";
                    return;
                }

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

    // ─── 6. PENCARIAN GLOBAL DI TOPBAR ────────────────────────
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
