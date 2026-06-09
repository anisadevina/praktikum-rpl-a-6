const MATKUL_KATEGORI = [
    {
        id: "matematika",
        bg: "#EEF2FF",
        stroke: "#4F46E5",
        test: /kalkulus|aljabar|matematika|statistika|probabilitas|numerik|diskrit|riset operasi|teori game|pemodelan statistika/,
        icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round">
            <path d="M4 19l4-4 3 3 4-5 5 6"/><path d="M4 5h16"/><path d="M4 9h8"/>
        </svg>`,
    },
    {
        id: "pemrograman",
        bg: "#F3E8FF",
        stroke: "#7C3AED",
        test: /pemrograman|algoritma|konsep program|dasar program|aplikasi bergerak|mobile|web|flutter|kotlin/,
        icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="16 18 22 12 16 6"/><polyline points="8 6 2 12 8 18"/>
        </svg>`,
    },
    {
        id: "basis-data",
        bg: "#FDF8C8",
        stroke: "#EA580C",
        test: /basis data|database|sql|dbms|data enterprise|perancangan.*basis|sistem manajemen basis|big data|infrastruktur.*data|data analitik/,
        icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round">
            <ellipse cx="12" cy="5" rx="9" ry="3"/>
            <path d="M21 12c0 1.66-4 3-9 3s-9-1.34-9-3"/>
            <path d="M3 5v14c0 1.66 4 3 9 3s9-1.34 9-3V5"/>
        </svg>`,
    },
    {
        id: "ai-ml",
        bg: "#FDF2F8",
        stroke: "#DB2777",
        test: /kecerdasan|machine learning|data mining|natural language|nlp|neural|komputasional|expert system|basis pengetahuan|penalaran|timeseries|citra biomedik|pemodelan dan simulasi/,
        icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="12" cy="12" r="3"/>
            <circle cx="4" cy="6" r="2"/><circle cx="20" cy="6" r="2"/>
            <circle cx="4" cy="18" r="2"/><circle cx="20" cy="18" r="2"/>
            <line x1="6" y1="6" x2="9" y2="10"/><line x1="18" y1="6" x2="15" y2="10"/>
            <line x1="6" y1="18" x2="9" y2="14"/><line x1="18" y1="18" x2="15" y2="14"/>
        </svg>`,
    },
    {
        id: "jaringan",
        bg: "#F0FDFA",
        stroke: "#0D9488",
        test: /jaringan|network|sistem operasi|terdistribusi|wireless|mobile computing|komputasi grid|cloud|iot|internet of things|komputasi terdistribusi/,
        icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round">
            <rect x="2" y="3" width="6" height="4" rx="1"/>
            <rect x="16" y="3" width="6" height="4" rx="1"/>
            <rect x="9" y="17" width="6" height="4" rx="1"/>
            <line x1="5" y1="7" x2="5" y2="13"/>
            <line x1="19" y1="7" x2="19" y2="13"/>
            <line x1="5" y1="13" x2="19" y2="13"/>
            <line x1="12" y1="13" x2="12" y2="17"/>
        </svg>`,
    },
    {
        id: "keamanan",
        bg: "#FFF1F2",
        stroke: "#E11D48",
        test: /keamanan|kriptografi|cyber|security|pengamanan/,
        icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round">
            <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/>
        </svg>`,
    },
    {
        id: "rekayasa",
        bg: "#FEFCE8",
        stroke: "#CA8A04",
        test: /rekayasa|perangkat lunak|sdlc|proyek|jaminan mutu|manajemen sistem|manajemen jaringan|manajemen data|business intelligence/,
        icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="12" cy="12" r="3"/>
            <path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1-2.83 2.83l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-4 0v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83-2.83l.06-.06A1.65 1.65 0 0 0 4.68 15a1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1 0-4h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 2.83-2.83l.06.06A1.65 1.65 0 0 0 9 4.68a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 4 0v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 2.83l-.06.06A1.65 1.65 0 0 0 19.4 9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 0 4h-.09a1.65 1.65 0 0 0-1.51 1z"/>
        </svg>`,
    },
    {
        id: "desain",
        bg: "#EEF2FF",
        stroke: "#4338CA",
        test: /interaksi manusia|multimedia|citra digital|sinyal digital|pengolahan citra|pengolahan sinyal|desain aplikasi/,
        icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round">
            <rect x="3" y="3" width="18" height="14" rx="2"/>
            <line x1="8" y1="21" x2="16" y2="21"/>
            <line x1="12" y1="17" x2="12" y2="21"/>
        </svg>`,
    },
    {
        id: "teori",
        bg: "#F8FAFC",
        stroke: "#475569",
        test: /teori bahasa|automata|turing|semantik web|teori komputer/,
        icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round">
            <path d="M12 2L2 7l10 5 10-5-10-5z"/>
            <path d="M2 17l10 5 10-5"/>
            <path d="M2 12l10 5 10-5"/>
        </svg>`,
    },
    {
        id: "sistem-digital",
        bg: "#ECFEFF",
        stroke: "#0891B2",
        test: /sistem digital|organisasi sistem|arsitektur komputer|organisasi.*komputer/,
        icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round">
            <rect x="4" y="4" width="16" height="16" rx="2"/>
            <rect x="9" y="9" width="6" height="6"/>
            <line x1="9" y1="1" x2="9" y2="4"/><line x1="15" y1="1" x2="15" y2="4"/>
            <line x1="9" y1="20" x2="9" y2="23"/><line x1="15" y1="20" x2="15" y2="23"/>
            <line x1="20" y1="9" x2="23" y2="9"/><line x1="20" y1="14" x2="23" y2="14"/>
            <line x1="1" y1="9" x2="4" y2="9"/><line x1="1" y1="14" x2="4" y2="14"/>
        </svg>`,
    },
];

// Default — Umum / Sosial / Pengembangan Diri
const MATKUL_DEFAULT = {
    bg: "#D7FDCC",
    stroke: "#386641",
    icon: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round">
        <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/>
        <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"/>
    </svg>`,
};

function getKategoriMatkul(nama) {
    const n = nama.toLowerCase();
    return MATKUL_KATEGORI.find(k => k.test.test(n)) || MATKUL_DEFAULT;
}

function getIconMatkul(nama) {
    return getKategoriMatkul(nama).icon;
}

function getBgMatkul(nama) {
    return getKategoriMatkul(nama).bg;
}

function getStrokeMatkul(nama) {
    return getKategoriMatkul(nama).stroke;
}