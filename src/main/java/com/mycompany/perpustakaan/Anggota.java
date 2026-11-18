package com.mycompany.perpustakaan;

import java.time.LocalDate;

/*
 * Kelas ini menyimpan data anggota perpustakaan beserta masa berlaku
 * keanggotaan dan denda tertunggak. Anggota otomatis DIBLOKIR (tidak boleh meminjam)
 * jika denda tertunggak mencapai Rp20.000 atau lebih.
 *
 * @author Abiyyu Nauval Sugiarto (2455301002)
 */
public class Anggota {

    /* Denda tertunggak minimum yang membuat anggota diblokir. */
    public static final long BATAS_BLOKIR_DENDA = 20000;

    private final String id;
    private final String nama;
    private final JenisAnggota jenis;
    private final LocalDate berlakuSampai;
    private long dendaTertunggak = 0;

    /*
     * Konstruktor Anggota.
     * Membuat anggota baru dan memastikan ID, nama, jenis,
     * dan masa berlaku diisi dengan benar.
     */
    public Anggota(String id, String nama, JenisAnggota jenis, LocalDate berlakuSampai) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID anggota wajib diisi");
        }
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama anggota wajib diisi");
        }
        if (jenis == null) {
            throw new IllegalArgumentException("Jenis anggota wajib diisi");
        }
        if (berlakuSampai == null) {
            throw new IllegalArgumentException("Masa berlaku wajib diisi");
        }
        this.id = id;
        this.nama = nama;
        this.jenis = jenis;
        this.berlakuSampai = berlakuSampai;
    }

    /*
     * Method isMasihBerlaku.
     * Mengembalikan true jika tanggal belum melewati masa berlaku.
     */
    public boolean isMasihBerlaku(LocalDate tanggal) {
        return !tanggal.isAfter(berlakuSampai);
    }

    /*
     * Method isDiblokir.
     * Mengembalikan true jika denda tertunggak mencapai batas blokir.
     */
    public boolean isDiblokir() {
        return dendaTertunggak >= BATAS_BLOKIR_DENDA;
    }

    /*
     * Method tambahDenda.
     * Menambahkan denda baru ke denda tertunggak
     * (dipanggil saat buku dikembalikan terlambat).
     * Denda tidak boleh negatif.
     */
    void tambahDenda(long jumlah) {
        if (jumlah < 0) {
            throw new IllegalArgumentException("Denda tidak boleh negatif");
        }
        dendaTertunggak += jumlah;
    }

    /*
     * Method bayarDenda.
     * Membayar sebagian atau seluruh denda tertunggak.
     */
    public void bayarDenda(long jumlah) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah pembayaran harus lebih dari 0");
        }
        if (jumlah > dendaTertunggak) {
            throw new IllegalArgumentException("Pembayaran melebihi denda tertunggak");
        }
        dendaTertunggak -= jumlah;
    }

    /*
     * Method getId.
     * Mengembalikan nomor anggota.
     */
    public String getId() { return id; }

    /*
     * Method getNama.
     * Mengembalikan nama anggota.
     */
    public String getNama() { return nama; }

    /*
     * Method getJenis.
     * Mengembalikan jenis anggota.
     */
    public JenisAnggota getJenis() { return jenis; }

    /*
     * Method getBerlakuSampai.
     * Mengembalikan tanggal terakhir keanggotaan berlaku.
     */
    public LocalDate getBerlakuSampai() { return berlakuSampai; }

    /*
     * Method getDendaTertunggak.
     * Mengembalikan denda yang belum dibayar (Rupiah).
     */
    public long getDendaTertunggak() { return dendaTertunggak; }
}
