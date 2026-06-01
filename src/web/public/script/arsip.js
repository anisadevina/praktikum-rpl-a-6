// Konfigurasi Path 
const PAGE_PATHS = {
    beranda: "/beranda",
    matkul: "/matkul",
    forum: "/forum",
    unggah: "/unggah",
    arsip: "/arsip",
};

const ACTIVE_PAGE = "arsip";

// Sidebar Aktif
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

// Username dari session 
function renderUsername() {
    const el = document.getElementById("topbar-username");
    if (!el) return;
    const stored = sessionStorage.getItem("loggedUser");
    if (stored) {
        try {
            const user = JSON.parse(stored);
            if (user.username) el.textContent = user.username;
        } catch (_) {}
    }
}

// Search
function setupSearch() {
    const input = document.getElementById("search-input");
    if (!input) return;

    input.addEventListener("keydown", (e) => {
        if (e.key === "Enter") {
            const query = input.value.trim();
            const urlParams = new URLSearchParams(window.location.search);
            if (query) {
                urlParams.set("q", query);
            } else {
                urlParams.delete("q");
            }
            window.location.href = `${PAGE_PATHS.arsip}?${urlParams.toString()}`;
        }
    });

    // Saat input dikosongkan, langsung refresh
    input.addEventListener("input", () => {
        if (input.value === "") {
            const urlParams = new URLSearchParams(window.location.search);
            urlParams.delete("q");
            window.location.href = `${PAGE_PATHS.arsip}?${urlParams.toString()}`;
        }
    });
}

// Filter Tahun
function setupFilter() {
    const select = document.getElementById("filter-tahun");
    if (!select) return;

    select.addEventListener("change", function () {
        const selectedTahun = this.value;
        const urlParams = new URLSearchParams(window.location.search);

        if (selectedTahun) {
            urlParams.set("tahun", selectedTahun);
        } else {
            urlParams.delete("tahun");
        }

        window.location.search = urlParams.toString();
    });
}

// Klik Item Arsip > Buka file langsung di tab baru
function setupArsipItems() {
    document.querySelectorAll(".arsip-item").forEach((item) => {
        item.addEventListener("click", (e) => {
            if (e.target.closest(".arsip-bookmark")) return;

            const fileUrl = item.dataset.fileUrl;
            if (fileUrl) {
                window.open(fileUrl, "_blank");
            }
        });
    });
}

// Bookmark Toggle
function setupBookmarks() {
    document.querySelectorAll(".arsip-bookmark").forEach((btn) => {
        btn.addEventListener("click", async (e) => {
            e.stopPropagation();

            const id         = btn.dataset.id;
            const isBookmark = btn.classList.contains("bookmarked");

            // Optimistic UI — langsung toggle dulu sebelum response
            btn.classList.toggle("bookmarked");

            try {
                const response = await fetch(`/arsip/bookmark/${id}`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        "X-CSRF-TOKEN": document.querySelector('meta[name="csrf-token"]')?.content ?? "",
                    },
                    body: JSON.stringify({ bookmarked: !isBookmark }),
                });

                if (!response.ok) throw new Error("Gagal menyimpan bookmark");

                const data = await response.json();

                // Kalau di-unbookmark, hapus item dari list
                if (!data.bookmarked) {
                    const item = btn.closest(".arsip-item");
                    if (item) {
                        item.style.transition = "opacity 0.3s";
                        item.style.opacity    = "0";
                        setTimeout(() => item.remove(), 300);
                    }
                }
            } catch (err) {
                // Rollback toggle kalau gagal
                btn.classList.toggle("bookmarked");
                console.error("Bookmark error:", err);
            }
        });
    });
}

// Tombol Keluar
function setupLogout() {
    const btnKeluar = document.getElementById("btn-keluar");
    if (!btnKeluar) return;
    btnKeluar.addEventListener("click", async (e) => {
        e.preventDefault();
        try {
            const csrfInput = document.querySelector('#logout-form input[name="_token"]');
            if (!csrfInput) throw new Error("CSRF token tidak ditemukan");

            const response = await fetch("/logout", {
                method: "POST",
                headers: {
                    "X-CSRF-TOKEN": csrfInput.value,
                    "Accept": "application/json",
                },
            });

            if (response.ok) {
                sessionStorage.removeItem("loggedUser");
                window.location.href = "/";
            } else {
                console.error("Logout ditolak oleh server.");
            }
        } catch (error) {
            console.error("Error saat logout:", error);
        }
    });
}

// Init
document.addEventListener("DOMContentLoaded", () => {
    setupSidebarActive();
    setupSearch();
    setupFilter();
    setupArsipItems();
    setupBookmarks();
    setupLogout();
});