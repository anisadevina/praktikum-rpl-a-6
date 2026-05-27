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

    const params = new URLSearchParams(window.location.search);
    const currentQuery = params.get("q");
    if (currentQuery) input.value = currentQuery;

    input.addEventListener("keydown", (e) => {
        if (e.key === "Enter") {
            const query = input.value.trim();
            if (!query) {
                window.location.href = "/matkul";
                return;
            }
            window.location.href = `/matkul?q=${encodeURIComponent(query)}`;
        }
    });

    input.addEventListener("input", () => {
        if (input.value.trim() === "") {
            window.location.href = "/matkul";
        }
    });
}

function showSearchResults(query) {
    const title = document.getElementById("page-title");
    if (title) title.textContent = `Hasil pencarian: "${query}"`;

    fetch(`/search?q=${encodeURIComponent(query)}`)
        .then((res) => res.json())
        .then((data) => renderSearchResults(data))
        .catch((err) => console.error("Search error:", err));
}

function renderSearchResults(results) {
    const container = document.getElementById("matkul-grid");
    if (!container) return;

    if (results.length === 0) {
        container.innerHTML = `<p style="color:var(--color-text-muted); font-style:italic;">Tidak ada mata kuliah yang ditemukan.</p>`;
        return;
    }

    container.innerHTML = results
        .map(
            (mk) => `
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
    `,
        )
        .join("");
}

function resetToMatkul() {
    const container = document.getElementById("matkul-grid");
    if (container) container.innerHTML = "";
    const title = document.getElementById("page-title");
    if (title) title.textContent = "Semua Mata Kuliah";
    window.location.reload();
}

// Fungsi baru yang lebih aman dari duplikasi
function checkLastVisited() {
    const data = sessionStorage.getItem("lastVisitedMatkul");
    if (!data) return;

    const mk = JSON.parse(data);

    // Cari container Terakhir Dilihat (grid yang pertama kali muncul)
    const container = document.querySelector(".cards-grid");
    if (!container) return;

    // Cegah duplikat
    const existingCard = container.querySelector(
        `.matkul-card[data-id="${mk.id}"]`,
    );
    if (existingCard) {
        existingCard.remove();
    }

    // Buat elemen card baru
    const newCard = document.createElement("div");
    newCard.className = "matkul-card";
    newCard.dataset.id = mk.id;
    newCard.innerHTML = `
        <div class="card-thumbnail">
            <svg viewBox="0 0 24 24"><rect x="3" y="3" width="18" height="18" rx="2"/><circle cx="8.5" cy="8.5" r="1.5"/><polyline points="21 15 16 10 5 21"/></svg>
        </div>
        <div class="card-body">
            <p class="card-name" title="${mk.nama}">${mk.nama}</p>
            <div class="card-difficulty">Tingkat kesulitan</div>
            <div class="card-rating">
                <span class="card-rating-star">★</span>
                <span class="card-rating-score">${mk.tingkat}/5</span>
            </div>
            <p class="card-arsip">${mk.arsip} arsip (materi, tugas, soal)</p>
            <a class="card-btn" href="/matkul/detail?id=${mk.id}">Lihat Selengkapnya</a>
        </div>
    `;

    // Taruh di urutan paling pertama (kiri atas)
    container.prepend(newCard);

    // Jaga agar maksimal hanya ada 4 card di baris "Terakhir Dilihat" agar UI tidak hancur
    if (container.id !== "matkul-grid" && container.children.length > 4) {
        container.lastElementChild.remove();
    }

    // Hapus sesi agar tidak jalan lagi kalau halaman di-refresh manual
    sessionStorage.removeItem("lastVisitedMatkul");
}

document.addEventListener("DOMContentLoaded", () => {
    setupSidebarActive();
    setupSearch();
});

// Gunakan 'pageshow' agar tetap jalan saat klik tombol kembali
window.addEventListener("pageshow", (event) => {
    checkLastVisited();
});
