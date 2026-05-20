const PAGE_PATHS = {
    beranda: "/beranda",
    matkul: "/matkul",
    forum: "/forum",
    unggah: "/unggah",
    arsip: "/arsip",
};

const ACTIVE_PAGE = "matkul";

// Simpan semua card awal untuk reset
let originalCards = "";

function setupSidebarActive() {
    const navItems = document.querySelectorAll(".nav-item[data-page]");
    navItems.forEach((item) => {
        item.classList.remove("active");
        if (item.dataset.page === ACTIVE_PAGE) {
            item.classList.add("active");
        }
        item.addEventListener("click", () => {
            const target = PAGE_PATHS[item.dataset.page];
            if (target && item.dataset.page !== ACTIVE_PAGE) {
                window.location.href = target;
            }
        });
    });
}

function setupSearch() {
    const input = document.getElementById("search-input");
    if (!input) return;

    input.addEventListener("keydown", (e) => {
        if (e.key === "Enter") {
            const query = input.value.trim();
            if (!query) return;
            showSearchResults(query);
        }
    });

    input.addEventListener("input", () => {
        if (input.value.trim() === "") {
            resetToMatkul();
        }
    });
}

function showSearchResults(query) {
    const title = document.getElementById("page-title");
    if (title) title.textContent = `Hasil pencarian: "${query}"`;

    fetch(`/search?q=${encodeURIComponent(query)}`)
        .then(res => res.json())
        .then(data => renderSearchResults(data))
        .catch(err => console.error("Search error:", err));
}

function renderSearchResults(results) {
    const container = document.getElementById("matkul-grid");
    if (!container) return;

    if (results.length === 0) {
        container.innerHTML = `<p style="color:var(--color-text-muted); font-style:italic;">Tidak ada mata kuliah yang ditemukan.</p>`;
        return;
    }

    container.innerHTML = results.map(mk => `
        <div class="matkul-card" data-id="${mk.id_matkul}">
          <div class="card-thumbnail">
            <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <rect x="3" y="3" width="18" height="18" rx="2"/>
              <circle cx="8.5" cy="8.5" r="1.5"/>
              <polyline points="21 15 16 10 5 21"/>
            </svg>
          </div>
          <div class="card-body">
            <p class="card-name" title="${mk.nama_matkul}">${mk.nama_matkul}</p>
            <div class="card-rating">
              <span class="card-rating-star">★</span>
              <span class="card-rating-score">${mk.tingkat_kesulitan}/5</span>
            </div>
            <p class="card-arsip">${mk.arsip} arsip (materi, tugas, soal)</p>
            <a class="card-btn" href="/matkul/detail?id=${mk.id_matkul}">Lihat Selengkapnya</a>
          </div>
        </div>
    `).join("");
}

function resetToMatkul() {
    const container = document.getElementById("matkul-grid");
    if (container) container.innerHTML = "";
    const title = document.getElementById("page-title");
    if (title) title.textContent = "Semua Mata Kuliah";
    window.location.reload();
}

document.addEventListener("DOMContentLoaded", () => {
    setupSidebarActive();
    setupSearch();
});