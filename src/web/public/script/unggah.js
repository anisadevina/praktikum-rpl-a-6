document.addEventListener("DOMContentLoaded", function () {

    // ─── VIEW CONTROLLERS ─────────────────────────────────────
    const unggahView     = document.getElementById("unggah-view");
    const formUnggahView = document.getElementById("form-unggah-view");
    const btnGoToForm    = document.getElementById("btn-go-to-form");
    const btnBatal       = document.getElementById("btn-batal-unggah");
    const formUnggah     = document.getElementById("form-unggah");

    // Buka form unggah
    btnGoToForm.addEventListener("click", function () {
        unggahView.classList.add("hidden");
        formUnggahView.classList.remove("hidden");
        window.scrollTo({ top: 0, behavior: "smooth" });
    });

    // Batal → kembali ke daftar
    btnBatal.addEventListener("click", function () {
        formUnggahView.classList.add("hidden");
        unggahView.classList.remove("hidden");
        resetForm();
        window.scrollTo({ top: 0, behavior: "smooth" });
    });

    // ─── DROPZONE ─────────────────────────────────────────────
    const dropzoneArea     = document.getElementById("dropzone-area");
    const fileInput        = document.getElementById("file-input");
    const dropzoneContent  = document.getElementById("dropzone-content");
    const dropzoneSelected = document.getElementById("dropzone-selected");
    const fileNameDisplay  = document.getElementById("file-name-display");
    const btnHapusFile     = document.getElementById("btn-hapus-file");

    // Klik dropzone → buka file picker
    dropzoneArea.addEventListener("click", function (e) {
        if (e.target === btnHapusFile || btnHapusFile.contains(e.target)) return;
        fileInput.click();
    });

    // File dipilih via picker
    fileInput.addEventListener("change", function () {
        if (this.files && this.files[0]) {
            showSelectedFile(this.files[0]);
        }
    });

    // Drag over
    dropzoneArea.addEventListener("dragover", function (e) {
        e.preventDefault();
        this.classList.add("dragover");
    });

    dropzoneArea.addEventListener("dragleave", function () {
        this.classList.remove("dragover");
    });

    // Drop file
    dropzoneArea.addEventListener("drop", function (e) {
        e.preventDefault();
        this.classList.remove("dragover");
        const file = e.dataTransfer.files[0];
        if (file && file.type === "application/pdf") {
            fileInput.files = e.dataTransfer.files;
            showSelectedFile(file);
        } else {
            alert("Hanya file PDF yang diperbolehkan.");
        }
    });

    // Hapus file yang dipilih
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

    // ─── SUBMIT FORM ──────────────────────────────────────────
    formUnggah.addEventListener("submit", function (e) {
        e.preventDefault();

        const judul    = formUnggah.querySelector('[name="judul_file"]').value.trim();
        const matkul   = formUnggah.querySelector('[name="id_matkul"]').value;
        const tahun    = formUnggah.querySelector('[name="tahun"]').value;
        const dosen    = formUnggah.querySelector('[name="id_dosen"]').value;
        const kategori = formUnggah.querySelector('[name="kategori"]').value;
        const file     = fileInput.files[0];

        if (!matkul || !tahun || !dosen || !kategori || !judul || !file) {
            alert("Lengkapi semua field sebelum mengunggah.");
            return;
        }

        if (file.size > 20 * 1024 * 1024) {
            alert("Ukuran file maksimal 20MB.");
            return;
        }

        // Tambahkan baris baru ke tabel
        const tbody      = document.getElementById("unggah-table-body");
        const emptyRow   = tbody.querySelector(".td-empty");
        if (emptyRow) emptyRow.closest("tr").remove();

        const matkulText = formUnggah.querySelector('[name="id_matkul"] option:checked').text;

        const newRow = document.createElement("tr");
        newRow.innerHTML = `
            <td class="td-filename">${escapeHTML(judul)}</td>
            <td>${escapeHTML(matkulText)}</td>
            <td>${escapeHTML(tahun)}</td>
            <td><span class="status-badge status-menunggu">Menunggu</span></td>
            <td class="td-catatan">–</td>
        `;
        tbody.insertBefore(newRow, tbody.firstChild);

        // Kembali ke daftar
        formUnggahView.classList.add("hidden");
        unggahView.classList.remove("hidden");
        resetForm();
        window.scrollTo({ top: 0, behavior: "smooth" });
    });

    // ─── RESET ────────────────────────────────────────────────
    function resetForm() {
        formUnggah.reset();
        fileInput.value = "";
        dropzoneSelected.classList.add("hidden");
        dropzoneContent.classList.remove("hidden");
    }

    // ─── ESCAPE HTML ──────────────────────────────────────────
    function escapeHTML(str) {
        return String(str).replace(/[&<>'"]/g, function (tag) {
            return { "&": "&amp;", "<": "&lt;", ">": "&gt;", "'": "&#39;", '"': "&quot;" }[tag] || tag;
        });
    }

});