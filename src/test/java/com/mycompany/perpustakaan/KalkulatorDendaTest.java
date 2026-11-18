package com.mycompany.perpustakaan;

import java.time.LocalDate;
import org.junit.Test;
import static org.junit.Assert.*;

/*
 * [TAHAP 3 - PERBAIKAN]
 * Kelas ini menguji KalkulatorDenda secara langsung: perhitungan hari
 * terlambat, aturan tarif (Equivalence Partitioning), serta nilai batas
 * hari ke-7/ke-8 dan denda maksimum (Boundary Value Analysis).
 *
 * @author Abiyyu Nauval Sugiarto (2455301002)
 */
public class KalkulatorDendaTest {

    private static final LocalDate JATUH_TEMPO = LocalDate.of(2026, 11, 10);
    private final KalkulatorDenda k = new KalkulatorDenda();

    /*
     * Method testHitungHariTerlambat.
     * TC-06: Lebih awal / tepat waktu = 0 hari, lewat 3 hari = 3 hari,
     * tanggal null ditolak.
     */
    @Test
    public void testHitungHariTerlambat() {
        assertEquals(0, k.hitungHariTerlambat(JATUH_TEMPO, JATUH_TEMPO.minusDays(2)));
        assertEquals(0, k.hitungHariTerlambat(JATUH_TEMPO, JATUH_TEMPO));
        assertEquals(3, k.hitungHariTerlambat(JATUH_TEMPO, JATUH_TEMPO.plusDays(3)));
        assertThrows(IllegalArgumentException.class, () -> k.hitungHariTerlambat(null, JATUH_TEMPO));
        assertThrows(IllegalArgumentException.class, () -> k.hitungHariTerlambat(JATUH_TEMPO, null));
    }

    /*
     * Method testAturanTarifDenda.
     * TC-07: Tanpa denda (0 hari, dosen); batas tarif normal 7 hari = 3.500
     * dan hari ke-8 tarif 2x = 4.500; buku BARU pengali x2 = 3.000.
     */
    @Test
    public void testAturanTarifDenda() {
        assertEquals(0, k.hitungDenda(JenisAnggota.UMUM, KategoriBuku.REGULER, 0));
        assertEquals(0, k.hitungDenda(JenisAnggota.DOSEN, KategoriBuku.BARU, 30));
        assertEquals(3500, k.hitungDenda(JenisAnggota.MAHASISWA, KategoriBuku.REGULER, 7));
        assertEquals(4500, k.hitungDenda(JenisAnggota.MAHASISWA, KategoriBuku.REGULER, 8));
        assertEquals(3000, k.hitungDenda(JenisAnggota.MAHASISWA, KategoriBuku.BARU, 3));
    }

    /*
     * Method testBatasMaksimumDanParameterTidakValid.
     * TC-08: Umum 28 hari = 49.000, 29 hari dibatasi 50.000;
     * jenis null, kategori null, hari negatif ditolak.
     */
    @Test
    public void testBatasMaksimumDanParameterTidakValid() {
        assertEquals(49000, k.hitungDenda(JenisAnggota.UMUM, KategoriBuku.REGULER, 28));
        assertEquals(KalkulatorDenda.DENDA_MAKSIMUM,
                k.hitungDenda(JenisAnggota.UMUM, KategoriBuku.REGULER, 29));
        assertThrows(IllegalArgumentException.class,
                () -> k.hitungDenda(null, KategoriBuku.REGULER, 1));
        assertThrows(IllegalArgumentException.class,
                () -> k.hitungDenda(JenisAnggota.UMUM, null, 1));
        assertThrows(IllegalArgumentException.class,
                () -> k.hitungDenda(JenisAnggota.UMUM, KategoriBuku.REGULER, -1));
    }
}
