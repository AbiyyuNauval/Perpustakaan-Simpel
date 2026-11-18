package com.mycompany.perpustakaan;

/*
 * Enum ini menyimpan aturan tambahan berdasarkan kategori buku:
 *   - REGULER   : boleh dipinjam, mengikuti lama pinjam anggota, denda x1.
 *   - BARU      : boleh dipinjam maksimal 3 hari, denda x2.
 *   - REFERENSI : hanya dibaca di tempat (tidak boleh dipinjam).
 *
 * @author Abiyyu Nauval Sugiarto (2455301002)
 */
public enum KategoriBuku {

    REGULER(true, 0, 1),
    BARU(true, 3, 2),
    REFERENSI(false, 0, 1);

    private final boolean bisaDipinjam;
    private final int batasLamaPinjamHari;
    private final int pengaliDenda;

    /*
     * Konstruktor KategoriBuku.
     * Menyimpan aturan boleh dipinjam, batas lama pinjam,
     * dan pengali denda untuk setiap kategori buku.
     */
    KategoriBuku(boolean bisaDipinjam, int batasLamaPinjamHari, int pengaliDenda) {
        this.bisaDipinjam = bisaDipinjam;
        this.batasLamaPinjamHari = batasLamaPinjamHari;
        this.pengaliDenda = pengaliDenda;
    }

    /*
     * Method isBisaDipinjam.
     * Mengembalikan true jika buku kategori ini boleh dipinjam.
     */
    public boolean isBisaDipinjam() { return bisaDipinjam; }

    /*
     * Method getBatasLamaPinjamHari.
     * Mengembalikan batas lama pinjam khusus kategori (0 = ikut anggota).
     */
    public int getBatasLamaPinjamHari() { return batasLamaPinjamHari; }

    /*
     * Method getPengaliDenda.
     * Mengembalikan pengali tarif denda.
     */
    public int getPengaliDenda() { return pengaliDenda; }
}
