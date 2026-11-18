package com.mycompany.perpustakaan;

import java.time.LocalDate;
import org.junit.Test;
import static org.junit.Assert.*;

/*
 * [TAHAP 3 - PERBAIKAN]
 * Kelas ini menguji validasi konstruktor Anggota, batas masa berlaku,
 * batas blokir denda (Rp19.999 vs Rp20.000), dan pembayaran denda.
 *
 * @author Abiyyu Nauval Sugiarto (2455301002)
 */
public class AnggotaTest {

    private static final LocalDate BERLAKU = LocalDate.of(2026, 12, 31);

    /*
     * Method testKonstruktorAnggota.
     * TC-11: Data valid tersimpan dengan denda awal 0; ID/nama null atau
     * kosong, jenis null, dan masa berlaku null ditolak.
     */
    @Test
    public void testKonstruktorAnggota() {
        JenisAnggota m = JenisAnggota.MAHASISWA;
        Anggota a = new Anggota("M01", "Budi", m, BERLAKU);
        assertEquals("M01", a.getId());
        assertEquals("Budi", a.getNama());
        assertEquals(m, a.getJenis());
        assertEquals(BERLAKU, a.getBerlakuSampai());
        assertEquals(0, a.getDendaTertunggak());

        assertThrows(IllegalArgumentException.class, () -> new Anggota(null, "Budi", m, BERLAKU));
        assertThrows(IllegalArgumentException.class, () -> new Anggota(" ", "Budi", m, BERLAKU));
        assertThrows(IllegalArgumentException.class, () -> new Anggota("M01", null, m, BERLAKU));
        assertThrows(IllegalArgumentException.class, () -> new Anggota("M01", "", m, BERLAKU));
        assertThrows(IllegalArgumentException.class, () -> new Anggota("M01", "Budi", null, BERLAKU));
        assertThrows(IllegalArgumentException.class, () -> new Anggota("M01", "Budi", m, null));
    }

    /*
     * Method testMasaBerlakuBlokirDanDenda.
     * TC-12: Masa berlaku (31 Des berlaku, 1 Jan tidak); blokir (19.999 belum,
     * 20.000 diblokir); bayar sebagian berhasil; bayar 0, bayar berlebih,
     * dan denda negatif ditolak.
     */
    @Test
    public void testMasaBerlakuBlokirDanDenda() {
        final Anggota a = new Anggota("M01", "Budi", JenisAnggota.MAHASISWA, BERLAKU);
        assertTrue(a.isMasihBerlaku(BERLAKU));
        assertFalse(a.isMasihBerlaku(BERLAKU.plusDays(1)));

        a.tambahDenda(19999);
        assertFalse(a.isDiblokir());
        a.tambahDenda(1);
        assertTrue(a.isDiblokir());

        a.bayarDenda(5000);
        assertEquals(15000, a.getDendaTertunggak());
        assertThrows(IllegalArgumentException.class, () -> a.bayarDenda(0));
        assertThrows(IllegalArgumentException.class, () -> a.bayarDenda(15001));
        assertThrows(IllegalArgumentException.class, () -> a.tambahDenda(-1));
    }
}
