document.addEventListener("DOMContentLoaded", function () {
    const editor = document.getElementById("editor");
    const hiddenCatatan = document.getElementById("hidden-catatan");
    const counter = document.getElementById("char-counter");
    const formatBtns = document.querySelectorAll(".format-btn");
    const formatSelect = document.getElementById("format-select");

    if (editor) {
        // Fungsi format teks (Bold, Italic, Underline, Undo)
        formatBtns.forEach(btn => {
            btn.addEventListener("click", function (e) {
                e.preventDefault();
                const command = this.getAttribute("data-command");
                document.execCommand(command, false, null);
                editor.focus();
                checkActiveFormats();
            });
        });

        // Fungsi dropdown Normal/Heading
        if (formatSelect) {
            formatSelect.addEventListener("change", function () {
                document.execCommand("formatBlock", false, this.value);
                editor.focus();
            });
        }

        // Cek tombol mana yang sedang aktif saat kursor berpindah
        editor.addEventListener("keyup", checkActiveFormats);
        editor.addEventListener("mouseup", checkActiveFormats);

        function checkActiveFormats() {
            formatBtns.forEach(btn => {
                const command = btn.getAttribute("data-command");
                if (command !== "undo" && document.queryCommandState(command)) {
                    btn.classList.add("active");
                } else {
                    btn.classList.remove("active");
                }
            });
        }

        // Penghitung Karakter & Sinkronisasi ke Input Hidden
        editor.addEventListener("input", function () {
            let textLength = this.innerText.replace(/\n/g, "").length; 
            
            if (textLength > 1000) {
                // Potong teks jika lebih dari 1000 karakter
                alert("Maksimal 1000 karakter!");
                this.blur(); 
                textLength = 1000;
            }
            
            counter.textContent = `${textLength}/1000`;
            
            // Masukkan HTML editor ke input tersembunyi
            hiddenCatatan.value = this.innerHTML;
        });

        // Pastikan isi disinkronisasi saat form akan dikirim ke backend
        const form = editor.closest("form");
        if (form) {
            form.addEventListener("submit", function () {
                hiddenCatatan.value = editor.innerHTML;
            });
        }
    }
});