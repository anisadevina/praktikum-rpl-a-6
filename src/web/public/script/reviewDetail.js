document.addEventListener("DOMContentLoaded", function () {
    // 1. LOGIKA EDITOR TEKS (FORMATTING & COUNTER)
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

    // ─── 2. AMBIL DATA DARI API (LOAD DETAIL) ─────────────────
    const urlParts = window.location.pathname.split("/");
    const idDokumen = urlParts[urlParts.length - 1];

    // Variabel untuk menyimpan URL PDF
    let filePdfUrl = "#";

    async function loadDetail() {
        try {
            const res = await fetch(`/api/review-dokumen/${idDokumen}`);
            const json = await res.json();

            if (json.status === "success") {
                const topbarUsername =
                    document.getElementById("topbar-username");
                if (topbarUsername && json.user) {
                    topbarUsername.textContent = json.user.username;
                }

                const valMatkul = document.getElementById("val-matkul");
                const valTahun = document.getElementById("val-tahun");
                const valDosen = document.getElementById("val-dosen");

                if (valMatkul)
                    valMatkul.textContent = json.data.nama_matkul || "-";
                if (valTahun)
                    valTahun.textContent =
                        json.data.tahun_dokumen || json.data.tahun || "-";
                if (valDosen) valDosen.textContent = json.data.dosen || "-";

                if (json.data.file_path) {
                    filePdfUrl = `/storage/${json.data.file_path}`;
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

    // ─── 3. PROSES PENGIRIMAN REVIEW (API SUBMIT) ─────────────
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

    // ─── 4. KELUAR / LOGOUT ───────────────────────────────────
    const btnKeluar = document.getElementById("btn-keluar");
    if (btnKeluar) {
        btnKeluar.addEventListener("click", (e) => {
            e.preventDefault();
            sessionStorage.removeItem("loggedUser");
            const formLogout = document.getElementById("logout-form");

            if (formLogout) {
                formLogout.submit();
            } else {
                window.location.href = "/login";
            }
        });
    }
});
