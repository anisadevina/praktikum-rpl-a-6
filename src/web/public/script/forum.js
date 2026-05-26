document.addEventListener("DOMContentLoaded", function() {
    
    const forumView = document.getElementById("forum-view");
    const createTopicView = document.getElementById("create-topic-view");
    
    const btnGoToCreate = document.getElementById("btn-go-to-create");
    const btnCancelCreate = document.getElementById("btn-cancel-create");
    
    const topicMessageInput = document.getElementById("topic-message");
    const charCounter = document.getElementById("char-counter");
    const inputTag = document.getElementById("input-tag");

    if (!btnGoToCreate) return;

    // ── Tombol buat topik ──────────────────────────────────────────────
    btnGoToCreate.addEventListener("click", function() {
        forumView.classList.add("hidden");
        createTopicView.classList.remove("hidden");
    });

    // ── Tombol batal buat topik ─────────────────────────────────────────────
    btnCancelCreate.addEventListener("click", function() {
        createTopicView.classList.add("hidden");
        forumView.classList.remove("hidden");

        // Reset form
        document.getElementById('form-buat-topik').reset();
        charCounter.textContent = '0/2000';
        
        // Reset kategori ke default
        categoryButtons.forEach(b => b.classList.remove("active"));
        categoryButtons[0].classList.add("active");
        inputTag.value = 'tanya jawab';

        // Hapus pesan error jika ada
        document.querySelectorAll('.client-error').forEach(el => el.remove());
    });

    // ── Tombol pilih kategori ────────────────────────────────────────────── 
    const categoryButtons = document.querySelectorAll(".category-btn");
    categoryButtons.forEach(btn => {
        btn.addEventListener("click", function() {
            categoryButtons.forEach(b => b.classList.remove("active"));
            this.classList.add("active");
            inputTag.value = this.getAttribute("data-cat");
        });
    });

    // ── Hitung karakter textarea ──────────────────────────────────────────────
    if (topicMessageInput) {
        topicMessageInput.addEventListener("input", function() {
            charCounter.textContent = `${this.value.length}/2000`;
        });
    }

    // ── Validasi form buat topik sebelum submit ───────────────────────────── 
    const formBuatTopik = document.getElementById('form-buat-topik');
    if (formBuatTopik) {
        formBuatTopik.addEventListener('submit', function(e) {
            const pesan = document.getElementById('topic-message').value.trim();
            const tag = document.getElementById('input-tag').value;
            let valid = true;

            // Hapus pesan error sebelumnya
            const existingError = formBuatTopik.querySelector('.client-error');
            if (existingError) existingError.remove();

            if (pesan.length < 5) {
                e.preventDefault();
                valid = false;
                const error = document.createElement('span');
                error.className = 'client-error';
                error.style.cssText = 'color:red; font-size:13px; display:block; margin-top:4px;';
                error.textContent = 'Pesan minimal 5 karakter.';
                document.getElementById('char-counter').insertAdjacentElement('afterend', error);
            }

            if (!tag) {
                e.preventDefault();
                valid = false;
                const error = document.createElement('span');
                error.className = 'client-error';
                error.style.cssText = 'color:red; font-size:13px; display:block; margin-top:4px;';
                error.textContent = 'Pilih kategori terlebih dahulu.';
                document.getElementById('input-tag').insertAdjacentElement('afterend', error);
            }

            return valid;
        });
    }

    document.body.addEventListener("click", function(e) {
        
        // ── Tombol lihat semua jawaban ──────────────────────────────────────────────
        const toggleBtn = e.target.closest(".dropdown-toggle");
        if (toggleBtn) {
            e.stopPropagation();
            const card = toggleBtn.closest(".topic-card");
            const threadContainer = card.querySelector(".replies-thread-container");
            const isCollapsed = threadContainer.classList.toggle("hidden");
            toggleBtn.classList.toggle("open");
            toggleBtn.querySelector(".toggle-text").textContent = isCollapsed ? "Lihat Semua Jawaban" : "Sembunyikan Jawaban";
        }

        // ── Tombol balas ──────────────────────────────────────────────
       const replyAction = e.target.closest(".btn-trigger-reply");
        if (replyAction) {
            const card = replyAction.closest(".topic-card");
            const inputForm = card.querySelector(".main-topic-input");
            
            inputForm.classList.remove("hidden");
            inputForm.scrollIntoView({ behavior: "smooth", block: "nearest" });
            inputForm.querySelector("input[name='pesan_balasan']").focus();
        }

    });

    // ── Enter untuk submit balasan topik ──────────────────────────────────────────────
    document.body.addEventListener("keydown", function(e) {
        if (e.key === "Enter" && e.target.closest(".main-topic-input input")) {
            const form = e.target.closest("form");
            const btn = form.querySelector(".btn-send-message-submit");
            if (btn.disabled) return;
            btn.disabled = true;
            form.submit();
        }
    });

    document.body.addEventListener("submit", function(e) {
        const form = e.target.closest(".main-topic-input");
        if (form) {
            const btn = form.querySelector(".btn-send-message-submit");
            if (btn.disabled) return e.preventDefault();
            btn.disabled = true;
        }
    });

    const hash = window.location.hash;
    if (hash) {
        const targetCard = document.querySelector(hash);
        if (targetCard) {
            const thread = targetCard.querySelector(".replies-thread-container");
            const toggle = targetCard.querySelector(".dropdown-toggle");
            if (thread) {
                thread.classList.remove("hidden");
                toggle.classList.add("open");
                toggle.querySelector(".toggle-text").textContent = "Sembunyikan Jawaban";
                targetCard.scrollIntoView({ behavior: "smooth" });
            }
        }
    }

});