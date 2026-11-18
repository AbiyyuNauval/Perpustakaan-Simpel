package com.mycompany.perpustakaan;

import java.time.LocalDate;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/*
 * [TAHAP 3 - PERBAIKAN]
 * Kelas ini melengkapi test baseline untuk LayananPerpustakaan: semua input
 * tidak valid, semua aturan penolakan peminjaman, buku kategori BARU,
 * pengembalian lengkap, dan laporan. Sebagian test melibatkan beberapa
 * kelas sekaligus (integrasi kecil).
 *
 * @author Abiyyu Nauval Sugiarto (2455301002)
 */
public class LayananLanjutanTest {

    private static final LocalDate HARI_INI = LocalDate.of(2026, 11, 2);
    private static final LocalDate BERLAKU = LocalDate.of(2027, 12, 31);
    private static final String ISBN_REGULER = "9780306406157";
    private static final String ISBN_BARU = "9786020000022";
    private static final String ISBN_SATU = "9786020000046";   // hanya 1 eksemplar

    private LayananPerpustakaan layanan;

    /*
     * Method setUp.
     * Menyiapkan 5 anggota dan 3 buku sebelum setiap test.
     */
    @Before
    public void setUp() {
        layanan = new LayananPerpustakaan();
        layanan.daftarkanAnggota(new Anggota("M01", "Budi", JenisAnggota.MAHASISWA, BERLAKU));
        layanan.daftarkanAnggota(new Anggota("M02", "Andi", JenisAnggota.MAHASISWA, BERLAKU));
        layanan.daftarkanAnggota(new Anggota("D01", "Bu Indah", JenisAnggota.DOSEN, BERLAKU));
        layanan.daftarkanAnggota(new Anggota("U01", "Sari", JenisAnggota.UMUM, BERLAKU));
        layanan.daftarkanAnggota(new Anggota("X01", "Lama", JenisAnggota.UMUM, LocalDate.of(2026, 1, 31)));
        layanan.tambahBuku(new Buku(ISBN_REGULER, "Rekayasa Perangkat Lunak", KategoriBuku.REGULER, 5));
        layanan.tambahBuku(new Buku(ISBN_BARU, "Pengujian Perangkat Lunak Modern", KategoriBuku.BARU, 3));
        layanan.tambahBuku(new Buku(ISBN_SATU, "Basis Data", KategoriBuku.REGULER, 1));
    }

    /*
     * Method testInputTidakValid.
     * TC-13: Semua input tidak valid ditolak IllegalArgumentException:
     * pendaftaran null/ganda, pinjam dengan data salah, kembalikan dengan data salah.
     */
    @Test
    public void testInputTidakValid() {
        assertThrows(IllegalArgumentException.class, () -> layanan.daftarkanAnggota(null));
        assertThrows(IllegalArgumentException.class,
                () -> layanan.daftarkanAnggota(new Anggota("M01", "Dobel", JenisAnggota.UMUM, BERLAKU)));
        assertThrows(IllegalArgumentException.class, () -> layanan.tambahBuku(null));
        assertThrows(IllegalArgumentException.class,
                () -> layanan.tambahBuku(new Buku(ISBN_REGULER, "Dobel", KategoriBuku.REGULER, 1)));

        assertThrows(IllegalArgumentException.class, () -> layanan.pinjam("M01", ISBN_REGULER, null));
        assertThrows(IllegalArgumentException.class, () -> layanan.pinjam("TIDAK-ADA", ISBN_REGULER, HARI_INI));
        assertThrows(IllegalArgumentException.class, () -> layanan.pinjam("M01", "9786020000053", HARI_INI));

        final Peminjaman p = layanan.pinjam("M01", ISBN_REGULER, HARI_INI);
        assertThrows(IllegalArgumentException.class, () -> layanan.kembalikan(p.getId(), null));
        assertThrows(IllegalArgumentException.class, () -> layanan.kembalikan("PJ-999", HARI_INI));
        assertThrows(IllegalArgumentException.class, () -> layanan.kembalikan(p.getId(), HARI_INI.minusDays(1)));
    }

    /*
     * Method testPinjamDitolak.
     * TC-14: Semua aturan penolakan peminjaman (IllegalStateException):
     * keanggotaan kedaluwarsa, diblokir karena denda, judul yang sama, stok habis.
     */
    @Test
    public void testPinjamDitolak() {
        assertThrows(IllegalStateException.class, () -> layanan.pinjam("X01", ISBN_REGULER, HARI_INI));

        Peminjaman p = layanan.pinjam("U01", ISBN_REGULER, HARI_INI);
        // Umum reguler terlambat 20 hari = 7.000 + 13 x 2.000 = 33.000 -> diblokir
        assertEquals(33000, layanan.kembalikan(p.getId(), p.getJatuhTempo().plusDays(20)));
        assertThrows(IllegalStateException.class, () -> layanan.pinjam("U01", ISBN_BARU, HARI_INI));

        layanan.pinjam("M01", ISBN_SATU, HARI_INI);
        assertThrows(IllegalStateException.class, () -> layanan.pinjam("M01", ISBN_SATU, HARI_INI));
        assertThrows(IllegalStateException.class, () -> layanan.pinjam("M02", ISBN_SATU, HARI_INI));
    }

    /*
     * Method testBukuBaruDanPengembalian.
     * TC-15: Buku BARU dibatasi 3 hari (dosen dan mahasiswa); mahasiswa terlambat
     * 2 hari = 2 x (500 x 2) = 2.000; status, tanggal, stok, dan denda anggota
     * diperbarui; buku yang sama bisa dipinjam lagi; dikembalikan dua kali ditolak.
     */
    @Test
    public void testBukuBaruDanPengembalian() {
        assertEquals(HARI_INI.plusDays(3), layanan.pinjam("D01", ISBN_BARU, HARI_INI).getJatuhTempo());
        final Peminjaman p = layanan.pinjam("M01", ISBN_BARU, HARI_INI);
        assertEquals("PJ-002", p.getId());
        assertEquals(HARI_INI, p.getTanggalPinjam());
        assertEquals(HARI_INI.plusDays(3), p.getJatuhTempo());

        LocalDate kembali = p.getJatuhTempo().plusDays(2);
        assertEquals(2000, layanan.kembalikan(p.getId(), kembali));
        assertEquals(StatusPeminjaman.DIKEMBALIKAN, p.getStatus());
        assertEquals(kembali, p.getTanggalKembali());
        assertEquals(2000, p.getDenda());
        assertEquals(2, p.getBuku().getTersedia());
        assertEquals(2000, p.getAnggota().getDendaTertunggak());

        layanan.pinjam("M01", ISBN_BARU, kembali);
        assertThrows(IllegalStateException.class, () -> layanan.kembalikan(p.getId(), kembali));
    }

    /*
     * Method testLaporan.
     * TC-16: Laporan terlambat per 12 Nov berisi 2 peminjaman; pinjaman aktif
     * dihitung per anggota; total denda Rp1.500 lalu Rp500 setelah dibayar.
     */
    @Test
    public void testLaporan() {
        Peminjaman a = layanan.pinjam("M01", ISBN_REGULER, HARI_INI);    // jatuh tempo +7
        Peminjaman b = layanan.pinjam("M02", ISBN_REGULER, HARI_INI);    // jatuh tempo +7
        layanan.pinjam("D01", ISBN_SATU, HARI_INI);                       // jatuh tempo +14

        List<Peminjaman> terlambat = layanan.daftarTerlambat(HARI_INI.plusDays(10));
        assertEquals(2, terlambat.size());
        assertThrows(IllegalArgumentException.class, () -> layanan.daftarTerlambat(null));

        layanan.kembalikan(a.getId(), a.getJatuhTempo().plusDays(1));    // denda 500
        layanan.kembalikan(b.getId(), b.getJatuhTempo().plusDays(2));    // denda 1.000
        assertEquals(0, layanan.hitungPinjamanAktif("M01"));
        assertEquals(1, layanan.hitungPinjamanAktif("D01"));
        assertEquals(1500, layanan.totalDendaTertunggak());

        layanan.bayarDenda("M02", 1000);
        assertEquals(500, layanan.totalDendaTertunggak());
    }
}
