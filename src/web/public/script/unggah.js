document.addEventListener("DOMContentLoaded", function () {
    // ─── 1. VIEW CONTROLLERS (Navigasi Antar Tampilan) ─────────
    const unggahView = document.getElementById("unggah-view");
    const formUnggahView = document.getElementById("form-unggah-view");
    const btnGoToForm = document.getElementById("btn-go-to-form");
    const btnBatal = document.getElementById("btn-batal-unggah");
    const formUnggah = document.getElementById("form-unggah");

    // Buka form unggah
    btnGoToForm.addEventListener("click", function () {
        unggahView.classList.add("hidden");
        formUnggahView.classList.remove("hidden");
        window.scrollTo({ top: 0, behavior: "smooth" });
    });

    // Batal → kembali ke daftar tabel
    btnBatal.addEventListener("click", function () {
        formUnggahView.classList.add("hidden");
        unggahView.classList.remove("hidden");
        resetForm();
        window.scrollTo({ top: 0, behavior: "smooth" });
    });

    // ─── 2. AMBIL DATA AWAL DARI API (Fetch Data) ─────────────
    async function fetchUnggahData() {
        try {
            const response = await fetch("/unggah/data");
            const json = await response.json();

            if (json.status !== "success") return;
            const data = json.data;

            // A. Isi Username Topbar (opsional jika topbar tidak di-render di blade)
            const topbarUsername = document.querySelector(".topbar-username");
            if (topbarUsername && data.user)
                topbarUsername.textContent = data.user.username;

            // B. Isi Dropdown Mata Kuliah
            const selectMatkul = document.querySelector(
                'select[name="id_matkul"]',
            );
            if (selectMatkul) {
                // Bersihkan dulu selain opsi default
                selectMatkul.innerHTML =
                    '<option value="" disabled selected>Pilih Mata Kuliah</option>';
                data.mataKuliah.forEach((mk) => {
                    selectMatkul.innerHTML += `<option value="${mk.id_matkul}">${escapeHTML(mk.nama_matkul)}</option>`;
                });
            }

            // C. Isi Dropdown Dosen
            const selectDosen = document.querySelector(
                'select[name="id_dosen"]',
            );
            if (selectDosen) {
                selectDosen.innerHTML =
                    '<option value="" disabled selected>Pilih Dosen</option>';
                data.dosen.forEach((dsn) => {
                    selectDosen.innerHTML += `<option value="${dsn.id_dosen}">${escapeHTML(dsn.nama_dosen)}</option>`;
                });
            }

            // D. Isi Tabel Riwayat Unggahan
            renderTable(data.dokumen);
        } catch (error) {
            console.error("Gagal memuat data unggah:", error);
        }
    }

    // Fungsi bantu untuk me-render tabel
    function renderTable(dokumenList) {
        const tbody = document.getElementById("unggah-table-body");
        if (!tbody) return;

        if (dokumenList.length === 0) {
            tbody.innerHTML = `<tr><td colspan="5" class="td-empty">Belum ada file yang diunggah.</td></tr>`;
            return;
        }

        tbody.innerHTML = dokumenList
            .map(
                (d) => `
            <tr>
                <td class="td-filename">${escapeHTML(d.judul || d.judul_file)}</td>
                <td>${escapeHTML(d.nama_matkul)}</td>
                <td>${escapeHTML(d.tahun_dokumen || d.tahun)}</td>
                <td>
                    <span class="status-badge status-${(d.status || "menunggu").toLowerCase()}">
                        ${d.status ? d.status.charAt(0).toUpperCase() + d.status.slice(1) : "Menunggu"}
                    </span>
                </td>
                <td class="td-catatan">${escapeHTML(d.catatan_admin || "–")}</td>
            </tr>
        `,
            )
            .join("");
    }

    // Panggil saat halaman pertama kali dimuat
    fetchUnggahData();

    // ─── 3. DROPZONE (Logika Drag & Drop File) ────────────────
    const dropzoneArea = document.getElementById("dropzone-area");
    const fileInput = document.getElementById("file-input");
    const dropzoneContent = document.getElementById("dropzone-content");
    const dropzoneSelected = document.getElementById("dropzone-selected");
    const fileNameDisplay = document.getElementById("file-name-display");
    const btnHapusFile = document.getElementById("btn-hapus-file");

    // Klik dropzone → buka file picker
    dropzoneArea.addEventListener("click", function (e) {
        if (e.target === btnHapusFile || btnHapusFile.contains(e.target))
            return;
        fileInput.click();
    });

    // File dipilih via picker biasa
    fileInput.addEventListener("change", function () {
        if (this.files && this.files[0]) {
            showSelectedFile(this.files[0]);
        }
    });

    // Event saat file diseret (drag over)
    dropzoneArea.addEventListener("dragover", function (e) {
        e.preventDefault();
        this.classList.add("dragover");
    });

    dropzoneArea.addEventListener("dragleave", function () {
        this.classList.remove("dragover");
    });

    // Event saat file dilepas (drop)
    dropzoneArea.addEventListener("drop", function (e) {
        e.preventDefault();
        this.classList.remove("dragover");
        const file = e.dataTransfer.files[0];

        if (file && file.type === "application/pdf") {
            fileInput.files = e.dataTransfer.files;
            showSelectedFile(file);
        } else {
            alert("Maaf, hanya file PDF yang diperbolehkan.");
        }
    });

    // Batal pilih file (hapus file)
    btnHapusFile.addEventListener("click", function (e) {
        e.stopPropagation();
        fileInput.value = "";
        dropzoneSelected.classList.add("hidden");
        dropzoneContent.classList.remove("hidden");
    });

    function showSelectedFile(file) {
        fileNameDisplay.textContent = file.name;
        dropzoneContent.classList.add("hidden");
        dropzoneSelected.classList.remove("hidden");
    }

    // ─── 4. SUBMIT FORM (Proses Upload File ke API) ───────────
    formUnggah.addEventListener("submit", async function (e) {
        e.preventDefault(); // Cegah halaman reload

        const file = fileInput.files[0];
        const submitBtn = document.querySelector(".btn-unggah-submit");

        // Validasi Front-End Sederhana
        if (!file) {
            alert("Pilih file PDF terlebih dahulu!");
            return;
        }
        if (file.size > 20 * 1024 * 1024) {
            // 20 MB
            alert("Ukuran file terlalu besar! Maksimal 20MB.");
            return;
        }

        // Bungkus seluruh isian form, termasuk file, ke dalam tipe FormData
        const formData = new FormData(formUnggah);

        // Ambil token CSRF agar diizinkan masuk oleh keamanan Laravel
        const csrfToken = document.querySelector('input[name="_token"]').value;

        try {
            // Ubah teks tombol jadi proses loading
            const originalBtnText = submitBtn.textContent;
            submitBtn.textContent = "Mengunggah...";
            submitBtn.disabled = true;

            // Tembak API POST
            const response = await fetch("/unggah", {
                method: "POST",
                headers: {
                    "X-CSRF-TOKEN": csrfToken,
                    Accept: "application/json", // Agar error 422 (Validasi) dibalas dengan format JSON
                },
                body: formData,
            });

            const result = await response.json();

            if (response.ok && result.status === "success") {
                alert("Berhasil: " + result.message);

                // 1. Refresh isi tabel (agar data barunya otomatis masuk)
                fetchUnggahData();

                // 2. Sembunyikan form dan kembali ke tabel
                formUnggahView.classList.add("hidden");
                unggahView.classList.remove("hidden");

                // 3. Bersihkan form
                resetForm();
                window.scrollTo({ top: 0, behavior: "smooth" });
            } else {
                // Menangkap pesan error dari validasi Laravel (422)
                let errorMsg = result.message || "Periksa kembali isianmu.";
                if (result.errors) {
                    // Gabungkan semua pesan error validasi (jika ada)
                    const errorsArray = Object.values(result.errors).flat();
                    errorMsg = errorsArray.join("\n");
                }
                alert("Gagal mengunggah:\n" + errorMsg);
            }
        } catch (error) {
            console.error("Upload error:", error);
            alert(
                "Terjadi kesalahan jaringan saat mengunggah. Coba lagi nanti.",
            );
        } finally {
            // Kembalikan tombol seperti semula setelah selesai (sukses/gagal)
            submitBtn.textContent = "Unggah";
            submitBtn.disabled = false;
        }
    });

    // ─── 5. UTILITY (Fungsi Bantuan) ──────────────────────────

    // Membersihkan semua isian di form
    function resetForm() {
        formUnggah.reset();
        fileInput.value = "";
        dropzoneSelected.classList.add("hidden");
        dropzoneContent.classList.remove("hidden");
    }

    // Melindungi web dari serangan XSS (mencegah tag <script> dimasukkan user)
    function escapeHTML(str) {
        if (!str) return "";
        return String(str).replace(/[&<>'"]/g, function (tag) {
            return (
                {
                    "&": "&amp;",
                    "<": "&lt;",
                    ">": "&gt;",
                    "'": "&#39;",
                    '"': "&quot;",
                }[tag] || tag
            );
        });
    }

    const btnKeluar = document.getElementById("btn-keluar");
    if (btnKeluar) {
        btnKeluar.addEventListener("click", (e) => {
            e.preventDefault();
            // Bersihkan jejak di memori browser
            sessionStorage.removeItem("loggedUser");

            // Cari form tersembunyi lalu submit secara otomatis
            const logoutForm = document.getElementById("logout-form");
            if (logoutForm) {
                logoutForm.submit();
            } else {
                window.location.href = "/login";
            }
        });
    }
});
