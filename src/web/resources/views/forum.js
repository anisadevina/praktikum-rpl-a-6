document.addEventListener("DOMContentLoaded", function() {
    
    // CONTROLLERS VIEW
    const forumView = document.getElementById("forum-view");
    const createTopicView = document.getElementById("create-topic-view");
    
    const btnGoToCreate = document.getElementById("btn-go-to-create");
    const btnCancelCreate = document.getElementById("btn-cancel-create");
    const btnSubmitTopic = document.getElementById("btn-submit-topic");
    
    const forumFeedContainer = document.getElementById("forum-feed-container");
    const topicMessageInput = document.getElementById("topic-message");
    const charCounter = document.getElementById("char-counter");

    let selectedCategory = "Tanya Jawab Soal";

    btnGoToCreate.addEventListener("click", function() {
        forumView.classList.add("hidden");
        createTopicView.classList.remove("hidden");
    });

    btnCancelCreate.addEventListener("click", function() {
        createTopicView.classList.add("hidden");
        forumView.classList.remove("hidden");
        resetForm();
    });

    const categoryButtons = document.querySelectorAll(".category-btn");
    categoryButtons.forEach(btn => {
        btn.addEventListener("click", function() {
            categoryButtons.forEach(b => b.classList.remove("active"));
            this.classList.add("active");
            selectedCategory = this.getAttribute("data-cat");
        });
    });

    topicMessageInput.addEventListener("input", function() {
        charCounter.textContent = `${this.value.length}/2000`;
    });

    // UNGHAH TOPIK BARU (STRUKTUR DUKUNGAN PERCABANGAN PENUH)
    btnSubmitTopic.addEventListener("click", function() {
        const messageText = topicMessageInput.value.trim();
        if (messageText === "") {
            alert("Pesan topik tidak boleh kosong!");
            return;
        }

        const identityOption = document.querySelector('input[name="display-as"]:checked').value;
        const finalUsername = (identityOption === "Anonim") ? "Anonim" : "namapengguna";

        const newCardHTML = `
            <div class="topic-card">
                <div class="topic-meta">
                    <div class="card-avatar">
                        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#1E1E1E" stroke-width="2.5">
                            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/>
                        </svg>
                    </div>
                    <span class="card-username">${finalUsername}</span>
                    <span class="badge badge-category">${selectedCategory}</span>
                </div>
                <p class="topic-text">${escapeHTML(messageText)}</p>
                <span class="time-stamp">Baru saja</span>
                
                <div class="topic-actions">
                    <button class="action-link btn-trigger-reply">
                        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="9 17 4 12 9 7"/><path d="M20 18v-2a4 4 0 0 0-4-4H4"/></svg>
                        Balas
                    </button>
                    <button class="action-link">
                        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>
                        0
                    </button>
                </div>

                <hr class="card-divider">

                <div class="dropdown-toggle">
                    <span class="toggle-text">Lihat Semua Jawaban</span>
                    <svg class="toggle-caret" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3"><polyline points="6 9 12 15 18 9"/></svg>
                </div>

                <div class="replies-thread-container hidden">
                    <div class="replies-list"></div>

                    <div class="reply-form-input main-topic-input">
                        <div class="card-avatar mini"><svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="#1E1E1E" stroke-width="2.5"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg></div>
                        <input type="text" placeholder="Tulis balasan Anda...">
                        <button class="btn-send-message-submit btn-submit-main-reply">
                            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="22" y1="2" x2="11" y2="13"/><polygon points="22 2 15 22 11 13 2 9 22 2"/></svg>
                        </button>
                    </div>
                </div>
            </div>
        `;

        forumFeedContainer.insertAdjacentHTML("afterbegin", newCardHTML);
        resetForm();
        createTopicView.classList.add("hidden");
        forumView.classList.remove("hidden");
    });

    function resetForm() {
        topicMessageInput.value = "";
        charCounter.textContent = "0/2000";
        categoryButtons.forEach(b => b.classList.remove("active"));
        categoryButtons[0].classList.add("active");
        selectedCategory = "Tanya Jawab Soal";
    }

    function escapeHTML(str) {
        return str.replace(/[&<>'"]/g, tag => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', "'": '&#39;', '"': '&quot;' }[tag] || tag));
    }

    // ==========================================
    // INTERAKSI DELEGATION (MAIN ENGINE THREAD)
    // ==========================================
    
    document.body.addEventListener("click", function(e) {
        
        // 1. Buka Tutup Semua Jawaban
        const toggleBtn = e.target.closest(".dropdown-toggle");
        if (toggleBtn) {
            const card = toggleBtn.closest(".topic-card");
            const threadContainer = card.querySelector(".replies-thread-container");
            const isCollapsed = threadContainer.classList.toggle("hidden");
            toggleBtn.classList.toggle("open");
            toggleBtn.querySelector(".toggle-text").textContent = isCollapsed ? "Lihat Semua Jawaban" : "Sembunyikan Jawaban";
        }

        // 2. Klik Tombol "Balas" Utama di Kartu Postingan
        const replyAction = e.target.closest(".btn-trigger-reply");
        if (replyAction) {
            const card = replyAction.closest(".topic-card");
            const threadContainer = card.querySelector(".replies-thread-container");
            const toggleBtn = card.querySelector(".dropdown-toggle");
            
            threadContainer.classList.remove("hidden");
            if (toggleBtn) {
                toggleBtn.classList.add("open");
                toggleBtn.querySelector(".toggle-text").textContent = "Sembunyikan Jawaban";
            }
            threadContainer.querySelector(".main-topic-input input").focus();
        }

        // 3. Submit Kirim Balasan Level Utama (Melalui Input Bar Paling Bawah)
        const submitMainReplyBtn = e.target.closest(".btn-submit-main-reply");
        if (submitMainReplyBtn) {
            executeMainTopicReply(submitMainReplyBtn);
        }

        // 4. JAWABAN KELUHAN 2: Klik Tombol "Balas" di Dalam Komentar Orang Lain (Memicu Spawning Form Inline)
        const commentReplyTrigger = e.target.closest(".btn-comment-reply-trigger");
        if (commentReplyTrigger) {
            const currentBranch = commentReplyTrigger.closest(".reply-branch");
            let subRepliesList = currentBranch.querySelector(".sub-replies-list");
            
            // Pengaman jika kontainer sub-reply belum dibuat di HTML
            if (!subRepliesList) {
                currentBranch.insertAdjacentHTML("beforeend", `<div class="sub-replies-list"></div>`);
                subRepliesList = currentBranch.querySelector(".sub-replies-list");
            }

            // Periksa apakah form ketik balas sudah ada di situ agar tidak duplikat
            let existingInputForm = subRepliesList.querySelector(".dynamic-nested-input-form");
            if (!existingInputForm) {
                const formHTML = `
                    <div class="reply-form-input dynamic-nested-input-form">
                        <div class="card-avatar mini"><svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="#1E1E1E" stroke-width="2.5"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg></div>
                        <input type="text" placeholder="Balas komentar ini...">
                        <button class="btn-send-message-submit btn-submit-nested-reply">
                            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="22" y1="2" x2="11" y2="13"/><polygon points="22 2 15 22 11 13 2 9 22 2"/></svg>
                        </button>
                    </div>
                `;
                subRepliesList.insertAdjacentHTML("beforeend", formHTML);
                existingInputForm = subRepliesList.querySelector(".dynamic-nested-input-form");
            }
            existingInputForm.querySelector("input").focus();
        }

        // 5. Submit Kirim Balasan Bersarang / Menjorok (Nested Reply)
        const submitNestedReplyBtn = e.target.closest(".btn-submit-nested-reply");
        if (submitNestedReplyBtn) {
            executeNestedReply(submitNestedReplyBtn);
        }
    });

    // 6. Dukungan Tombol Enter di Keyboard saat Mengetik Komentar
    document.body.addEventListener("keydown", function(e) {
        if (e.key === "Enter") {
            if (e.target.closest(".main-topic-input input")) {
                const btn = e.target.closest(".main-topic-input").querySelector(".btn-submit-main-reply");
                executeMainTopicReply(btn);
            } else if (e.target.closest(".dynamic-nested-input-form input")) {
                const btn = e.target.closest(".dynamic-nested-input-form").querySelector(".btn-submit-nested-reply");
                executeNestedReply(btn);
            }
        }
    });

    // FUNGSI PROSES CETAK BALASAN UTAMA (LEVEL 1)
    function executeMainTopicReply(buttonElement) {
        const formInput = buttonElement.closest(".reply-form-input");
        const inputField = formInput.querySelector("input");
        const replyText = inputField.value.trim();

        if (replyText === "") return;

        const repliesListContainer = formInput.closest(".replies-thread-container").querySelector(".replies-list");

        const replyBranchNodeHTML = `
            <div class="reply-branch">
                <div class="reply-card">
                    <div class="reply-meta">
                        <div class="card-avatar mini"><svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="#1E1E1E" stroke-width="2.5"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg></div>
                        <strong>namapengguna</strong>
                    </div>
                    <p class="reply-body-text">${escapeHTML(replyText)}</p>
                    <button class="reply-action-btn btn-comment-reply-trigger">
                        <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="9 17 4 12 9 7"/><path d="M20 18v-2a4 4 0 0 0-4-4H4"/></svg> Balas
                    </button>
                </div>
                <div class="sub-replies-list"></div>
            </div>
        `;

        repliesListContainer.insertAdjacentHTML("beforeend", replyBranchNodeHTML);
        inputField.value = "";
    }

    // FUNGSI PROSES CETAK BALASAN BERSARANG (LEVEL MENJOROK / NESTED)
    function executeNestedReply(buttonElement) {
        const formInput = buttonElement.closest(".dynamic-nested-input-form");
        const inputField = formInput.querySelector("input");
        const replyText = inputField.value.trim();

        if (replyText === "") return;

        const subRepliesList = formInput.closest(".sub-replies-list");

        const childBranchNodeHTML = `
            <div class="reply-branch">
                <div class="reply-card">
                    <div class="reply-meta">
                        <div class="card-avatar mini"><svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="#1E1E1E" stroke-width="2.5"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg></div>
                        <strong>namapengguna</strong>
                    </div>
                    <p class="reply-body-text">${escapeHTML(replyText)}</p>
                    <button class="reply-action-btn btn-comment-reply-trigger">
                        <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="9 17 4 12 9 7"/><path d="M20 18v-2a4 4 0 0 0-4-4H4"/></svg> Balas
                    </button>
                </div>
                <div class="sub-replies-list"></div>
            </div>
        `;

        // Masukkan balasan menjorok baru tepat sebelum formulir input diletakkan
        formInput.insertAdjacentHTML("beforebegin", childBranchNodeHTML);
        
        // Hapus formulir input ketik dinamis setelah komentar sukses terkirim
        formInput.remove();
    }

});