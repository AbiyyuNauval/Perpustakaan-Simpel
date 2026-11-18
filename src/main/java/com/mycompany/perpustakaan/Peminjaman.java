package com.mycompany.perpustakaan;

import java.time.LocalDate;

/*
 * Kelas ini menyimpan satu transaksi peminjaman: siapa meminjam buku apa,
 * kapan dipinjam, kapan jatuh tempo, berapa dendanya, dan statusnya.
 * Objek dibuat dan diubah hanya oleh LayananPerpustakaan.
 *
 * @author Abiyyu Nauval Sugiarto (2455301002)
 */
public class Peminjaman {

    private final String id;
    private final Anggota anggota;
    private final Buku buku;
    private final LocalDate tanggalPinjam;
    private final LocalDate jatuhTempo;
    private LocalDate tanggalKembali;
    private long denda = 0;
    private StatusPeminjaman status = StatusPeminjaman.AKTIF;

    /*
     * Konstruktor Peminjaman.
     * Membuat transaksi peminjaman baru dengan status AKTIF.
     * Hanya dipanggil oleh LayananPerpustakaan.
     */
    Peminjaman(String id, Anggota anggota, Buku buku, LocalDate tanggalPinjam, LocalDate jatuhTempo) {
        this.id = id;
        this.anggota = anggota;
        this.buku = buku;
        this.tanggalPinjam = tanggalPinjam;
        this.jatuhTempo = jatuhTempo;
    }

    /*
     * Method isAktif.
     * Mengembalikan true jika buku belum dikembalikan.
     */
    public boolean isAktif() {
        return status == StatusPeminjaman.AKTIF;
    }

    /*
     * Method isTerlambat.
     * Mengembalikan true jika masih aktif dan tanggal sudah melewati jatuh tempo.
     */
    public boolean isTerlambat(LocalDate tanggal) {
        return isAktif() && tanggal.isAfter(jatuhTempo);
    }

    /*
     * Method selesai.
     * Menandai peminjaman sudah dikembalikan.
     */
    void selesai(LocalDate tanggalKembali, long denda) {
        this.tanggalKembali = tanggalKembali;
        this.denda = denda;
        this.status = StatusPeminjaman.DIKEMBALIKAN;
    }

    /*
     * Method getId.
     * Mengembalikan nomor transaksi.
     */
    public String getId() { return id; }

    /*
     * Method getAnggota.
     * Mengembalikan anggota peminjam.
     */
    public Anggota getAnggota() { return anggota; }

    /*
     * Method getBuku.
     * Mengembalikan buku yang dipinjam.
     */
    public Buku getBuku() { return buku; }

    /*
     * Method getTanggalPinjam.
     * Mengembalikan tanggal peminjaman.
     */
    public LocalDate getTanggalPinjam() { return tanggalPinjam; }

    /*
     * Method getJatuhTempo.
     * Mengembalikan tanggal jatuh tempo.
     */
    public LocalDate getJatuhTempo() { return jatuhTempo; }

    /*
     * Method getTanggalKembali.
     * Mengembalikan tanggal pengembalian (null jika belum dikembalikan).
     */
    public LocalDate getTanggalKembali() { return tanggalKembali; }

    /*
     * Method getDenda.
     * Mengembalikan denda transaksi ini (Rupiah).
     */
    public long getDenda() { return denda; }

    /*
     * Method getStatus.
     * Mengembalikan status transaksi.
     */
    public StatusPeminjaman getStatus() { return status; }
}
