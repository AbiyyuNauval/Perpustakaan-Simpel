package com.mycompany.perpustakaan;

import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/*
 * [TAHAP 2 - BASELINE]
 * Kelas ini berisi test awal yang sengaja hanya menguji alur utama
 * (happy path) dan dua aturan paling umum. Dipakai untuk melihat laporan
 * JaCoCo "sebelum" perbaikan sehingga celah coverage dapat diidentifikasi.
 *
 * @author Abiyyu Nauval Sugiarto (2455301002)
 */
public class LayananPerpustakaanTest {

    private static final LocalDate HARI_INI = LocalDate.of(2026, 11, 2);
    private static final LocalDate BERLAKU = LocalDate.of(2027, 12, 31);
    private static final String ISBN_REGULER = "9780306406157";
    private static final String ISBN_REFERENSI = "9786020000039";

    private LayananPerpustakaan layanan;

    /*
     * Method setUp.
     * Menyiapkan 2 anggota dan 2 buku sebelum setiap test.
     */
    @Before
    public void setUp() {
        layanan = new LayananPerpustakaan();
        layanan.daftarkanAnggota(new Anggota("M01", "Budi", JenisAnggota.MAHASISWA, BERLAKU));
        layanan.daftarkanAnggota(new Anggota("U01", "Sari", JenisAnggota.UMUM, BERLAKU));
        layanan.tambahBuku(new Buku(ISBN_REGULER, "Rekayasa Perangkat Lunak", KategoriBuku.REGULER, 5));
        layanan.tambahBuku(new Buku(ISBN_REFERENSI, "Kamus Istilah Komputer", KategoriBuku.REFERENSI, 1));
    }

    /*
     * Method testPinjamBerhasil.
     * TC-01: Mahasiswa meminjam buku reguler -> status AKTIF, jatuh tempo +7 hari.
     */
    @Test
    public void testPinjamBerhasil() {
        Peminjaman p = layanan.pinjam("M01", ISBN_REGULER, HARI_INI);
        assertEquals(StatusPeminjaman.AKTIF, p.getStatus());
        assertEquals(HARI_INI.plusDays(7), p.getJatuhTempo());
        assertEquals(1, layanan.hitungPinjamanAktif("M01"));
    }

    /*
     * Method testKembalikanTepatWaktu.
     * TC-02: Dikembalikan tepat pada jatuh tempo -> denda 0.
     */
    @Test
    public void testKembalikanTepatWaktu() {
        Peminjaman p = layanan.pinjam("M01", ISBN_REGULER, HARI_INI);
        assertEquals(0, layanan.kembalikan(p.getId(), p.getJatuhTempo()));
    }

    /*
     * Method testKembalikanTerlambat.
     * TC-03: Mahasiswa terlambat 3 hari -> denda 3 x Rp500 = Rp1.500.
     */
    @Test
    public void testKembalikanTerlambat() {
        Peminjaman p = layanan.pinjam("M01", ISBN_REGULER, HARI_INI);
        assertEquals(1500, layanan.kembalikan(p.getId(), p.getJatuhTempo().plusDays(3)));
    }

    /*
     * Method testBukuReferensiDitolak.
     * TC-04: Buku referensi tidak boleh dipinjam -> IllegalStateException.
     */
    @Test
    public void testBukuReferensiDitolak() {
        assertThrows(IllegalStateException.class,
                () -> layanan.pinjam("M01", ISBN_REFERENSI, HARI_INI));
    }

    /*
     * Method testBatasPeminjamanUmum.
     * TC-05: Anggota umum (maks 2 buku) meminjam yang ke-3 -> IllegalStateException.
     */
    @Test
    public void testBatasPeminjamanUmum() {
        layanan.tambahBuku(new Buku("9786020000015", "Algoritma", KategoriBuku.REGULER, 1));
        layanan.tambahBuku(new Buku("9786020000046", "Basis Data", KategoriBuku.REGULER, 1));
        layanan.pinjam("U01", ISBN_REGULER, HARI_INI);
        layanan.pinjam("U01", "9786020000015", HARI_INI);
        assertThrows(IllegalStateException.class,
                () -> layanan.pinjam("U01", "9786020000046", HARI_INI));
    }
}
