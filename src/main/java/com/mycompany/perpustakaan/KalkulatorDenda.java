package com.mycompany.perpustakaan;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/*
 * Kelas ini menghitung keterlambatan dan denda pengembalian buku.
 * Aturan denda:
 *   1. Tarif per hari = denda dasar jenis anggota x pengali kategori buku.
 *   2. Hari ke-1 s.d. ke-7 terlambat : tarif normal.
 *   3. Hari ke-8 dan seterusnya      : tarif 2x lipat.
 *   4. Denda tidak pernah melebihi Rp50.000 per transaksi.
 *
 * @author Abiyyu Nauval Sugiarto (2455301002)
 */
public class KalkulatorDenda {

    /* Jumlah hari terlambat yang masih memakai tarif normal. */
    public static final int BATAS_HARI_TARIF_NORMAL = 7;

    /* Batas atas denda per transaksi (Rupiah). */
    public static final long DENDA_MAKSIMUM = 50000;

    /*
     * Method hitungHariTerlambat.
     * Menghitung selisih hari antara tanggal jatuh tempo dan tanggal kembali.
     * Hasilnya 0 jika buku dikembalikan tepat waktu atau lebih awal.
     */
    public int hitungHariTerlambat(LocalDate jatuhTempo, LocalDate tanggalKembali) {
        if (jatuhTempo == null || tanggalKembali == null) {
            throw new IllegalArgumentException("Tanggal tidak boleh null");
        }
        long selisih = ChronoUnit.DAYS.between(jatuhTempo, tanggalKembali);
        return selisih > 0 ? (int) selisih : 0;
    }

    /*
     * Method hitungDenda.
     * Menghitung denda (Rupiah) berdasarkan jenis anggota, kategori buku,
     * dan jumlah hari terlambat, mengikuti 4 aturan denda di atas.
     * Contoh: mahasiswa, buku reguler, terlambat 8 hari
     *   -> 7 x 500 + 1 x 500 x 2 = 4.500.
     */
    public long hitungDenda(JenisAnggota jenis, KategoriBuku kategori, int hariTerlambat) {
        if (jenis == null || kategori == null) {
            throw new IllegalArgumentException("Jenis anggota dan kategori buku wajib diisi");
        }
        if (hariTerlambat < 0) {
            throw new IllegalArgumentException("Hari terlambat tidak boleh negatif");
        }
        if (hariTerlambat == 0 || jenis.getDendaPerHari() == 0) {
            return 0;
        }

        long tarif = jenis.getDendaPerHari() * kategori.getPengaliDenda();
        long denda;
        if (hariTerlambat <= BATAS_HARI_TARIF_NORMAL) {
            denda = hariTerlambat * tarif;
        } else {
            long dendaNormal = BATAS_HARI_TARIF_NORMAL * tarif;
            long dendaEkstra = (long) (hariTerlambat - BATAS_HARI_TARIF_NORMAL) * tarif * 2;
            denda = dendaNormal + dendaEkstra;
        }

        if (denda > DENDA_MAKSIMUM) {
            return DENDA_MAKSIMUM;
        }
        return denda;
    }
}
