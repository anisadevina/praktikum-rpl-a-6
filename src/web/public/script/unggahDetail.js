document.addEventListener("DOMContentLoaded", async function () {
    // Navigasi Sidebar
    const PAGE_PATHS = {
        beranda: "/beranda",
        matkul: "/matkul",
        forum: "/forum",
        unggah: "/unggah",
        arsip: "/arsip",
        review: "/review-dokumen",
    };

    document.querySelectorAll(".nav-item[data-page]").forEach((item) => {
        item.addEventListener("click", () => {
            const target = PAGE_PATHS[item.dataset.page];
            if (target) window.location.href = target;
        });
    });

    // Init Choices.js
    const choicesConfig = {
        searchEnabled: true,
        searchResultLimit: 999,
        renderChoiceLimit: 5,
        itemSelectText: "",
        shouldSort: false,
        searchPlaceholderValue: "Ketik untuk mencari...",
        noResultsText: "Tidak ditemukan",
        noChoicesText: "Tidak ada pilihan",
    };

    const choicesMatkul = new Choices("#select-matkul", {
        ...choicesConfig,
        searchPlaceholderValue: "Cari mata kuliah...",
    });
    const choicesTahun = new Choices("#select-tahun", {
        ...choicesConfig,
        searchPlaceholderValue: "Cari tahun...",
    });

    const tahunSekarang = new Date().getFullYear();
    const pilihanTahun = [];

    for (let y = tahunSekarang; y >= 2022; y--) {
        pilihanTahun.push({ value: String(y), label: String(y) });
    }

    choicesTahun.setChoices(pilihanTahun, "value", "label", true);

    const choicesDosen = new Choices("#select-dosen", {
        ...choicesConfig,
        searchPlaceholderValue: "Cari nama dosen...",
    });
    new Choices("#select-kategori", {
        ...choicesConfig,
        searchPlaceholderValue: "Cari jenis file...",
    });

    // Fetch dropdown data dari API
    async function fetchDropdownData() {
        try {
            const response = await fetch("/unggah/data");
            const json = await response.json();
            if (json.status !== "success") return;

            if (json.data.user && json.data.user.role === "admin") {
                const menuAdmin = document.getElementById("menu-review-admin");
                if (menuAdmin) {
                    menuAdmin.style.display = "flex";
                    menuAdmin.classList.remove("hidden");
                }
            }

            const topbarUsername = document.querySelector(".topbar-username");
            if (topbarUsername && json.data.user) {
                topbarUsername.textContent = json.data.user.username;
            }

            if (json.data.mataKuliah) {
                choicesMatkul.setChoices(
                    json.data.mataKuliah.map((mk) => ({
                        value: String(mk.id_matkul),
                        label: mk.nama_matkul,
                    })),
                    "value",
                    "label",
                    true,
                );
            }

            if (json.data.dosen) {
                choicesDosen.setChoices(
                    json.data.dosen.map((d) => ({
                        value: String(d.id_dosen),
                        label: d.nama_dosen,
                    })),
                    "value",
                    "label",
                    true,
                );
            }
        } catch (err) {
            console.error("Gagal memuat data dropdown:", err);
        }
    }
    fetchDropdownData();

    // Dropzone
    const dropzoneArea = document.getElementById("dropzone-area");
    const fileInput = document.getElementById("file-input");
    const dropzoneContent = document.getElementById("dropzone-content");
    const dropzoneSelected = document.getElementById("dropzone-selected");
    const fileNameDisplay = document.getElementById("file-name-display");
    const btnHapusFile = document.getElementById("btn-hapus-file");

    dropzoneArea.addEventListener("click", function (e) {
        if (btnHapusFile.contains(e.target)) return;
        fileInput.click();
    });

    fileInput.addEventListener("change", function () {
        if (this.files?.[0]) showFile(this.files[0]);
    });

    dropzoneArea.addEventListener("dragover", (e) => {
        e.preventDefault();
        dropzoneArea.classList.add("dragover");
    });

    dropzoneArea.addEventListener("dragleave", () => {
        dropzoneArea.classList.remove("dragover");
    });

    dropzoneArea.addEventListener("drop", (e) => {
        e.preventDefault();
        dropzoneArea.classList.remove("dragover");
        const file = e.dataTransfer.files[0];
        if (file?.type === "application/pdf") {
            fileInput.files = e.dataTransfer.files;
            showFile(file);
        } else {
            alert("Hanya file PDF yang diperbolehkan.");
        }
    });

    btnHapusFile.addEventListener("click", (e) => {
        e.stopPropagation();
        fileInput.value = "";
        dropzoneSelected.classList.add("hidden");
        dropzoneContent.classList.remove("hidden");
    });

    function showFile(file) {
        fileNameDisplay.textContent = file.name;
        dropzoneContent.classList.add("hidden");
        dropzoneSelected.classList.remove("hidden");
    }

    // Submit Form & Validasi
    const formUnggah = document.getElementById("form-unggah");
    const btnSubmit = document.getElementById("btn-submit");

    formUnggah.addEventListener("submit", async function (e) {
        e.preventDefault();

        // Sembunyikan semua pesan error terlebih dahulu
        document
            .querySelectorAll(".error-text")
            .forEach((el) => (el.style.display = "none"));

        let isValid = true;

        // Ambil data
        const matkul = document.getElementById("select-matkul").value;
        const tahun = document.getElementById("select-tahun").value;
        const dosen = document.getElementById("select-dosen").value;
        const kategori = document.getElementById("select-kategori").value;
        const judul = document.getElementById("input-judul").value.trim();
        const file = fileInput.files[0];

        // Fungsi pemuncul error
        const showError = (id, message) => {
            const errorEl = document.getElementById(id);
            if (errorEl) {
                errorEl.textContent = message;
                errorEl.style.display = "block";
            }
            isValid = false;
        };

        // 1. Validasi Input Biasa
        if (!matkul) showError("err-matkul", "Mata kuliah wajib dipilih.");

        if (!tahun) {
            showError("err-tahun", "Tahun wajib dipilih.");
        } else if (tahun < 2022 || tahun > 2026) {
            showError("err-tahun", "Tahun harus antara 2022 hingga 2026.");
        }

        if (!dosen) showError("err-dosen", "Dosen pengampu wajib dipilih.");
        if (!kategori)
            showError("err-kategori", "Kategori file wajib dipilih.");
        if (!judul) showError("err-judul", "Judul file tidak boleh kosong.");

        // 2. Validasi File (Wajib ada, Format PDF, Maksimal 20MB)
        if (!file) {
            showError("err-file", "File PDF wajib diunggah.");
        } else {
            if (file.type !== "application/pdf") {
                showError("err-file", "Format file harus PDF.");
            }
            const maxSize = 20 * 1024 * 1024; // 20MB dalam bytes
            if (file.size > maxSize) {
                showError("err-file", "Ukuran file lebih dari 20MB.");
            }
        }

        // 3. Jika semua lolos validasi, kirim ke server
        if (isValid) {
            const formData = new FormData(formUnggah);
            const csrfToken =
                document.querySelector('meta[name="csrf-token"]')?.content ??
                "";

            try {
                btnSubmit.textContent = "Mengunggah...";
                btnSubmit.disabled = true;

                const response = await fetch("/unggah", {
                    method: "POST",
                    headers: {
                        "X-CSRF-TOKEN": csrfToken,
                        Accept: "application/json",
                    },
                    body: formData,
                });

                const result = await response.json();

                if (response.ok && result.status === "success") {
                    alert("Berhasil: " + result.message);
                    window.location.href = "/unggah"; // kembali ke list
                } else {
                    // Jika lolos validasi frontend tapi ditolak backend Laravel
                    if (result.errors) {
                        const errorMap = {
                            id_matkul: "err-matkul",
                            tahun: "err-tahun",
                            id_dosen: "err-dosen",
                            kategori_file: "err-kategori",
                            judul: "err-judul",
                            file_pdf: "err-file",
                        };
                        for (const [field, messages] of Object.entries(
                            result.errors,
                        )) {
                            if (errorMap[field])
                                showError(errorMap[field], messages[0]);
                        }
                    } else {
                        alert(
                            "Gagal mengunggah:\n" +
                                (result.message || "Periksa kembali isianmu."),
                        );
                    }
                }
            } catch (err) {
                console.error("Upload error:", err);
                alert("Terjadi kesalahan jaringan. Coba lagi nanti.");
            } finally {
                btnSubmit.textContent = "Unggah";
                btnSubmit.disabled = false;
            }
        }
    });

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

    // Search Bar Topbar
    const topbarSearchInput = document.querySelector(".search-bar input");
    if (topbarSearchInput) {
        topbarSearchInput.addEventListener("keydown", function (e) {
            if (e.key === "Enter") {
                e.preventDefault();
                const query = this.value.trim();
                window.location.href =
                    query !== ""
                        ? `/matkul?q=${encodeURIComponent(query)}`
                        : `/matkul`;
            }
        });
    }
});
