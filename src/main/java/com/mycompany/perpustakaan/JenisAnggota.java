package com.mycompany.perpustakaan;

/*
 * Enum ini menyimpan aturan peminjaman untuk setiap jenis anggota:
 *
 * | Jenis     | Maks buku | Lama pinjam | Denda/hari |
 * |-----------|-----------|-------------|------------|
 * | MAHASISWA | 3         | 7 hari      | Rp500      |
 * | DOSEN     | 5         | 14 hari     | Rp0        |
 * | UMUM      | 2         | 5 hari      | Rp1.000    |
 *
 * @author Abiyyu Nauval Sugiarto (2455301002)
 */
public enum JenisAnggota {

    MAHASISWA(3, 7, 500),
    DOSEN(5, 14, 0),
    UMUM(2, 5, 1000);

    private final int batasBuku;
    private final int lamaPinjamMaks;
    private final long dendaPerHari;

    /*
     * Konstruktor JenisAnggota.
     * Menyimpan aturan batas buku, lama pinjam, dan denda per hari
     * untuk setiap jenis anggota.
     */
    JenisAnggota(int batasBuku, int lamaPinjamMaks, long dendaPerHari) {
        this.batasBuku = batasBuku;
        this.lamaPinjamMaks = lamaPinjamMaks;
        this.dendaPerHari = dendaPerHari;
    }

    /*
     * Method getBatasBuku.
     * Mengembalikan jumlah buku maksimum yang dipinjam bersamaan.
     */
    public int getBatasBuku() { return batasBuku; }

    /*
     * Method getLamaPinjamMaks.
     * Mengembalikan lama pinjam standar dalam hari.
     */
    public int getLamaPinjamMaks() { return lamaPinjamMaks; }

    /*
     * Method getDendaPerHari.
     * Mengembalikan denda dasar per hari keterlambatan (Rupiah).
     */
    public long getDendaPerHari() { return dendaPerHari; }
}
