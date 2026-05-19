/**
 * STUDY SCOPE — Beranda Page Script
 */

// ─── Konfigurasi Path Halaman ────────────────────────────────────────────────
const PAGE_PATHS = {
    beranda: "/beranda",
    matkul: "/matkul",
    forum: "/forum",
    unggah: "/unggah",
    arsip: "/arsip",
};

// ─── Dummy Data ───────────────────────────────────────────────────────────────

// const RECENT_MATKUL = [
//     {
//         id: 1,
//         nama: "Rekayasa Perangkat Lunak",
//         tingkat: "Menengah",
//         rating: 4.5,
//         arsip: 120,
//     },
//     { id: 2, nama: "Basis Data", tingkat: "Mudah", rating: 4.2, arsip: 85 },
//     {
//         id: 3,
//         nama: "Algoritma & Pemrograman",
//         tingkat: "Sulit",
//         rating: 4.7,
//         arsip: 200,
//     },
//     {
//         id: 4,
//         nama: "Jaringan Komputer",
//         tingkat: "Menengah",
//         rating: 4.0,
//         arsip: 60,
//     },
// ];

// const RECENT_FORUM = [
//     {
//         id: 1,
//         username: "username",
//         badge: "Tanya Jawab Soal",
//         body: "Ada yang punya soal UTS RPL tahun lalu? Aku lagi nyiapin ujian minggu depan dan butuh referensi untuk latihan soal essay-nya.",
//     },
//     {
//         id: 2,
//         username: "username",
//         badge: "Tanya Jawab Soal",
//         body: "Halo, ada yang bisa bantu jelasin perbedaan antara use case diagram dan sequence diagram? Aku masih bingung kapan harus pakai yang mana.",
//     },
//     {
//         id: 3,
//         username: "username",
//         badge: "General",
//         body: "Teman-teman, ada rekomendasi materi belajar Basis Data yang bagus? Terutama yang bahas normalisasi dan SQL tingkat lanjut.",
//     },
//     {
//         id: 4,
//         username: "username",
//         badge: "Tanya Jawab Soal",
//         body: "Untuk UAS Jaringan Komputer, kira-kira materi apa saja yang paling sering keluar? Tahun lalu katanya banyak soal tentang subnetting.",
//     },
// ];

// ─── Sidebar Aktif ────────────────────────────────────────────────────────────

const ACTIVE_PAGE = "beranda";

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

// function renderMatkulCards() {
//     const container = document.getElementById("matkul-grid");
//     if (!container) return;

//     container.innerHTML = RECENT_MATKUL.map(
//         (mk) => `
//         <div class="matkul-card" data-id="${mk.id}">
//           <div class="card-thumbnail">
//             <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
//               <rect x="3" y="3" width="18" height="18" rx="2"/>
//               <circle cx="8.5" cy="8.5" r="1.5"/>
//               <polyline points="21 15 16 10 5 21"/>
//             </svg>
//           </div>
//           <div class="card-body">
//             <p class="card-name" title="${mk.nama}">${mk.nama}</p>
//             <p class="card-difficulty">Tingkat kesulitan: ${mk.tingkat}</p>
//             <div class="card-rating">
//               <span class="card-rating-star">★</span>
//               <span class="card-rating-score">${mk.rating}/5</span>
//             </div>
//             <p class="card-arsip">${mk.arsip} arsip (materi, tugas, soal)</p>
//             <a class="card-btn" href="#" data-id="${mk.id}">Lihat Selengkapnya</a>
//           </div>
//         </div>
//     `,
//     ).join("");

//     container.querySelectorAll(".card-btn").forEach((btn) => {
//         btn.addEventListener("click", (e) => {
//             e.preventDefault();
//             const id = btn.dataset.id;
//             // TODO: aktifkan saat halaman matkul sudah tersedia
//             // window.location.href = `${PAGE_PATHS.matkul}?id=${id}`;
//             console.log(`Navigate to matkul id=${id}`);
//         });
//     });
// }

// function renderForumCards() {
//     const container = document.getElementById("forum-list");
//     if (!container) return;

//     container.innerHTML = RECENT_FORUM.map(
//         (post) => `
//         <div class="forum-card" data-id="${post.id}">
//           <div class="forum-card-header">
//             <div class="forum-avatar">
//               <svg viewBox="0 0 24 24">
//                 <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
//                 <circle cx="12" cy="7" r="4"/>
//               </svg>
//             </div>
//             <span class="forum-username">${post.username}</span>
//             <span class="forum-badge">${post.badge}</span>
//           </div>
//           <p class="forum-body">${post.body}</p>
//         </div>
//     `,
//     ).join("");

//     container.querySelectorAll(".forum-card").forEach((card) => {
//         card.addEventListener("click", () => {
//             const id = card.dataset.id;
//             // TODO: aktifkan saat halaman forum sudah tersedia
//             // window.location.href = `${PAGE_PATHS.forum}?id=${id}`;
//             console.log(`Navigate to forum post id=${id}`);
//         });
//     });
// }

// ─── Search ───────────────────────────────────────────────────────────────────

function setupSearch() {
    const input = document.getElementById("search-input");
    if (!input) return;

    input.addEventListener("keydown", (e) => {
        if (e.key === "Enter") {
            const query = input.value.trim();
            if (!query) return;
            // TODO: aktifkan saat halaman matkul sudah tersedia
            // window.location.href = `${PAGE_PATHS.matkul}?q=${encodeURIComponent(query)}`;
            console.log(`Search: ${query}`);
        }
    });
}

// ─── Init ─────────────────────────────────────────────────────────────────────

document.addEventListener("DOMContentLoaded", () => {
    setupSidebarActive();
    setupSearch();

    // ── Tombol Keluar ──
    const btnKeluar = document.getElementById("btn-keluar");
    if (btnKeluar) {
        btnKeluar.addEventListener("click", (e) => {
            e.preventDefault();

            // Bersihkan session browser
            sessionStorage.removeItem("loggedUser");

            // Jalankan logout di server Laravel
            document.getElementById("logout-form").submit();
        });
    }
});
