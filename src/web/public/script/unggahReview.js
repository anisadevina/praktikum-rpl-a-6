document.addEventListener("DOMContentLoaded", function () {

    // ─── QUILL RICH TEXT EDITOR ───────────────────────────────
    const quill = new Quill('#quill-editor', {
        theme: 'snow',
        placeholder: 'Tulis catatan atau komentar di sini....',
        modules: {
            toolbar: [
                [{ 'header': [1, 2, 3, false] }],
                ['bold', 'italic', 'underline', 'strike'],
                [{ 'list': 'ordered' }, { 'list': 'bullet' }],
                ['link'],
                ['clean']
            ]
        }
    });

    // Hitung karakter
    const charCountEl = document.getElementById('quill-char-count');
    const maxChars    = 1000;

    quill.on('text-change', function () {
        const text = quill.getText().trim();
        const len  = text.length;

        if (charCountEl) charCountEl.textContent = len;

        // Batasi karakter
        if (len > maxChars) {
            quill.deleteText(maxChars, len - maxChars);
        }
    });

    // ─── KIRIM REVIEW ─────────────────────────────────────────
    const btnKirim = document.getElementById('btn-kirim-review');

    if (btnKirim) {
        btnKirim.addEventListener('click', function () {
            const keputusan = document.querySelector('input[name="keputusan"]:checked')?.value;
            const catatan   = quill.root.innerHTML;
            const action    = this.getAttribute('data-action');

            if (!keputusan) {
                alert('Pilih keputusan terlebih dahulu.');
                return;
            }

            // Buat form dinamis dan submit ke route BE
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = action;

            const csrf = document.createElement('input');
            csrf.type  = 'hidden';
            csrf.name  = '_token';
            csrf.value = document.querySelector('meta[name="csrf-token"]')?.content
                         || '{{ csrf_token() }}';

            const inputKeputusan = document.createElement('input');
            inputKeputusan.type  = 'hidden';
            inputKeputusan.name  = 'keputusan';
            inputKeputusan.value = keputusan;

            const inputCatatan = document.createElement('input');
            inputCatatan.type  = 'hidden';
            inputCatatan.name  = 'catatan';
            inputCatatan.value = catatan;

            form.appendChild(csrf);
            form.appendChild(inputKeputusan);
            form.appendChild(inputCatatan);
            document.body.appendChild(form);
            form.submit();
        });
    }

    // ─── PDF PREVIEW ──────────────────────────────────────────
    const pdfPreviewArea = document.getElementById('pdf-preview-area');
    const pdfIframe      = document.querySelector('.pdf-iframe');

    if (pdfPreviewArea && pdfIframe) {
        pdfPreviewArea.addEventListener('click', function () {
            // Buka PDF di tab baru
            window.open(pdfIframe.src, '_blank');
        });
    }

});