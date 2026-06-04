document.addEventListener("DOMContentLoaded", async function () {

    // Navigasi Sidebar 
    const PAGE_PATHS = {
        beranda: "/beranda",
        matkul:  "/matkul",
        forum:   "/forum",
        unggah:  "/unggah",
        arsip:   "/arsip",
        review:  "/unggah/admin",
    };

    document.querySelectorAll(".nav-item[data-page]").forEach((item) => {
        item.addEventListener("click", () => {
            const target = PAGE_PATHS[item.dataset.page];
            if (target) window.location.href = target;
        });
    });

    //  Init Choices.js
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

    // Mata Kuliah — isi dari API
    const choicesMatkul = new Choices("#select-matkul", {
        ...choicesConfig,
        searchPlaceholderValue: "Cari mata kuliah...",
    });

    // Tahun — sudah ada option dari blade
    new Choices("#select-tahun", {
        ...choicesConfig,
        searchPlaceholderValue: "Cari tahun...",
    });

    // Dosen — isi dari API
    const choicesDosen = new Choices("#select-dosen", {
        ...choicesConfig,
        searchPlaceholderValue: "Cari nama dosen...",
    });

    // Kategori — sudah ada option dari blade
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

            // cek role
            if (json.data.user && json.data.user.role === "admin") {
                const menuAdmin = document.getElementById("menu-review-admin");
                if (menuAdmin) {
                    menuAdmin.style.display = "flex";
                    menuAdmin.classList.remove("hidden");
                }
            }

            // Isi dropdown Mata Kuliah
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

            // Isi dropdown Dosen
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

    // Submit Form
    const formUnggah = document.getElementById("form-unggah");
    const btnSubmit = document.getElementById("btn-submit");

    formUnggah.addEventListener("submit", async function (e) {
        e.preventDefault();

        const file = fileInput.files[0];
        if (!file) {
            alert("Pilih file PDF terlebih dahulu!");
            return;
        }
        if (file.size > 20 * 1024 * 1024) {
            alert("Ukuran file maksimal 20MB.");
            return;
        }

        const formData = new FormData(formUnggah);
        const csrfToken =
            document.querySelector('meta[name="csrf-token"]')?.content ?? "";

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
                const errors = result.errors
                    ? Object.values(result.errors).flat().join("\n")
                    : result.message || "Periksa kembali isianmu.";
                alert("Gagal mengunggah:\n" + errors);
            }
        } catch (err) {
            console.error("Upload error:", err);
            alert("Terjadi kesalahan jaringan. Coba lagi nanti.");
        } finally {
            btnSubmit.textContent = "Unggah";
            btnSubmit.disabled = false;
        }
    });

    // Logout
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