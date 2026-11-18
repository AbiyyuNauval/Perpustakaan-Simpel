package com.mycompany.perpustakaan;

/*
 * Kelas ini menyimpan data satu judul buku beserta jumlah eksemplar
 * (salinan fisik).
 * Stok tersedia berkurang saat dipinjam dan bertambah saat
 * dikembalikan. Stok tidak boleh negatif dan tidak boleh melebihi
 * jumlah eksemplar.
 *
 * @author Abiyyu Nauval Sugiarto (2455301002)
 */
public class Buku {

    private final String isbn;
    private final String judul;
    private final KategoriBuku kategori;
    private final int totalEksemplar;
    private int tersedia;

    /*
     * Konstruktor Buku.
     * Membuat buku baru dan memastikan ISBN, judul, kategori,
     * dan jumlah eksemplar valid. Stok awal = jumlah eksemplar.
     */
    public Buku(String isbn, String judul, KategoriBuku kategori, int jumlahEksemplar) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN buku wajib diisi");
        }
        if (judul == null || judul.trim().isEmpty()) {
            throw new IllegalArgumentException("Judul buku wajib diisi");
        }
        if (kategori == null) {
            throw new IllegalArgumentException("Kategori buku wajib diisi");
        }
        if (jumlahEksemplar < 1) {
            throw new IllegalArgumentException("Jumlah eksemplar minimal 1");
        }
        this.isbn = isbn;
        this.judul = judul;
        this.kategori = kategori;
        this.totalEksemplar = jumlahEksemplar;
        this.tersedia = jumlahEksemplar;
    }

    /*
     * Method isTersedia.
     * Mengembalikan true jika masih ada eksemplar yang dapat dipinjam.
     */
    public boolean isTersedia() {
        return tersedia > 0;
    }

    /*
     * Method kurangiStok.
     * Mengurangi stok tersedia sebanyak 1 saat buku dipinjam.
     * Gagal jika stok sudah habis.
     */
    void kurangiStok() {
        if (tersedia == 0) {
            throw new IllegalStateException("Stok buku habis");
        }
        tersedia--;
    }

    /*
     * Method tambahStok.
     * Menambah stok tersedia sebanyak 1 saat buku dikembalikan.
     * Gagal jika stok sudah sama dengan jumlah eksemplar.
     */
    void tambahStok() {
        if (tersedia >= totalEksemplar) {
            throw new IllegalStateException("Stok melebihi jumlah eksemplar");
        }
        tersedia++;
    }

    /*
     * Method getIsbn.
     * Mengembalikan nomor ISBN buku.
     */
    public String getIsbn() { return isbn; }

    /*
     * Method getJudul.
     * Mengembalikan judul buku.
     */
    public String getJudul() { return judul; }

    /*
     * Method getKategori.
     * Mengembalikan kategori buku.
     */
    public KategoriBuku getKategori() { return kategori; }

    /*
     * Method getTotalEksemplar.
     * Mengembalikan jumlah seluruh eksemplar.
     */
    public int getTotalEksemplar() { return totalEksemplar; }

    /*
     * Method getTersedia.
     * Mengembalikan jumlah eksemplar yang sedang tersedia.
     */
    public int getTersedia() { return tersedia; }
}
