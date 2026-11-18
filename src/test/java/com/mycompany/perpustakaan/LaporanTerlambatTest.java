package com.mycompany.perpustakaan;

import java.time.LocalDate;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/*
 * [TAHAP 4 - HASIL ANALISIS LAPORAN]
 * Kelas ini ditambahkan setelah membaca laporan JaCoCo tahap 3:
 * kondisi "isAktif() && tanggal.isAfter(jatuhTempo)" pada Peminjaman.isTerlambat()
 * masih kuning karena isAktif() belum pernah bernilai false.
 * Artinya belum ada test yang memastikan buku yang SUDAH dikembalikan
 * tidak ikut masuk laporan keterlambatan.
 *
 * @author Abiyyu Nauval Sugiarto (2455301002)
 */
public class LaporanTerlambatTest {

    private static final LocalDate HARI_INI = LocalDate.of(2026, 11, 2);
    private static final LocalDate BERLAKU = LocalDate.of(2027, 12, 31);

    /*
     * Method testLaporanTidakMemuatBukuYangSudahDikembalikan.
     * TC-17: Peminjaman yang sudah dikembalikan tidak masuk laporan terlambat,
     * walaupun tanggal laporan sudah melewati jatuh temponya.
     */
    @Test
    public void testLaporanTidakMemuatBukuYangSudahDikembalikan() {
        LayananPerpustakaan layanan = new LayananPerpustakaan();
        layanan.daftarkanAnggota(new Anggota("M01", "Budi", JenisAnggota.MAHASISWA, BERLAKU));
        layanan.daftarkanAnggota(new Anggota("U01", "Sari", JenisAnggota.UMUM, BERLAKU));
        layanan.tambahBuku(new Buku("9780306406157", "Rekayasa Perangkat Lunak", KategoriBuku.REGULER, 2));

        Peminjaman sudahKembali = layanan.pinjam("M01", "9780306406157", HARI_INI);   // jatuh tempo +7
        Peminjaman masihDipinjam = layanan.pinjam("U01", "9780306406157", HARI_INI);  // jatuh tempo +5
        layanan.kembalikan(sudahKembali.getId(), HARI_INI.plusDays(3));

        List<Peminjaman> terlambat = layanan.daftarTerlambat(HARI_INI.plusDays(10));
        assertEquals(1, terlambat.size());
        assertSame(masihDipinjam, terlambat.get(0));
        assertFalse(sudahKembali.isTerlambat(HARI_INI.plusDays(10)));
    }
}
