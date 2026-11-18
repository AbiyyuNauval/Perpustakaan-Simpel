package com.mycompany.perpustakaan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
 * Kelas ini adalah kelas utama (service) yang mengatur seluruh aturan bisnis
 * perpustakaan dengan memanggil kelas-kelas lain:
 *   - Anggota, Buku, Peminjaman  : data dan state
 *   - KalkulatorDenda           : perhitungan denda
 *
 * Fitur:
 *   1. Pinjam buku      (6 aturan validasi)
 *   2. Kembalikan buku  (hitung denda, tambah stok)
 *   3. Bayar denda dan laporan (peminjaman terlambat, total denda)
 *
 * @author Abiyyu Nauval Sugiarto (2455301002)
 */
public class LayananPerpustakaan {

    private final Map<String, Anggota> daftarAnggota = new HashMap<String, Anggota>();
    private final Map<String, Buku> daftarBuku = new HashMap<String, Buku>();
    private final Map<String, Peminjaman> daftarPeminjaman = new LinkedHashMap<String, Peminjaman>();
    private final KalkulatorDenda kalkulator = new KalkulatorDenda();
    private int nomorUrut = 0;

    // =================================================================
    // PENDAFTARAN DATA
    // =================================================================

    /*
     * Method daftarkanAnggota.
     * Mendaftarkan anggota baru ke perpustakaan.
     * Gagal jika anggota null atau ID-nya sudah terdaftar.
     */
    public void daftarkanAnggota(Anggota anggota) {
        if (anggota == null) {
            throw new IllegalArgumentException("Anggota tidak boleh null");
        }
        if (daftarAnggota.containsKey(anggota.getId())) {
            throw new IllegalArgumentException("ID anggota sudah terdaftar");
        }
        daftarAnggota.put(anggota.getId(), anggota);
    }

    /*
     * Method tambahBuku.
     * Menambahkan buku baru ke koleksi perpustakaan.
     * Gagal jika buku null atau ISBN-nya sudah terdaftar.
     */
    public void tambahBuku(Buku buku) {
        if (buku == null) {
            throw new IllegalArgumentException("Buku tidak boleh null");
        }
        if (daftarBuku.containsKey(buku.getIsbn())) {
            throw new IllegalArgumentException("ISBN sudah terdaftar");
        }
        daftarBuku.put(buku.getIsbn(), buku);
    }

    // =================================================================
    // TRANSAKSI
    // =================================================================

    /*
     * Method pinjam.
     * Meminjamkan satu buku kepada anggota dan membuat transaksi baru.
     * Jatuh tempo = tanggal pinjam + lama pinjam (lihat hitungLamaPinjam).
     * Aturan (dicek berurutan):
     *   1. Buku kategori REFERENSI tidak boleh dipinjam.
     *   2. Keanggotaan harus masih berlaku.
     *   3. Anggota tidak sedang diblokir karena denda.
     *   4. Jumlah pinjaman aktif belum mencapai batas jenis anggota.
     *   5. Anggota tidak sedang meminjam judul yang sama.
     *   6. Stok buku harus tersedia.
     */
    public Peminjaman pinjam(String idAnggota, String isbn, LocalDate tanggal) {
        wajibTanggal(tanggal);
        Anggota anggota = cariAnggota(idAnggota);
        Buku buku = cariBuku(isbn);

        if (!buku.getKategori().isBisaDipinjam()) {
            throw new IllegalStateException("Buku referensi hanya dapat dibaca di tempat");
        }
        if (!anggota.isMasihBerlaku(tanggal)) {
            throw new IllegalStateException("Keanggotaan sudah kedaluwarsa");
        }
        if (anggota.isDiblokir()) {
            throw new IllegalStateException("Anggota diblokir karena denda tertunggak");
        }
        if (hitungPinjamanAktif(anggota.getId()) >= anggota.getJenis().getBatasBuku()) {
            throw new IllegalStateException("Batas jumlah peminjaman tercapai");
        }
        if (sedangMeminjam(anggota.getId(), buku.getIsbn())) {
            throw new IllegalStateException("Anggota sedang meminjam buku yang sama");
        }
        if (!buku.isTersedia()) {
            throw new IllegalStateException("Stok buku habis");
        }

        buku.kurangiStok();
        LocalDate jatuhTempo = tanggal.plusDays(hitungLamaPinjam(anggota, buku));
        Peminjaman peminjaman = new Peminjaman(buatIdBaru(), anggota, buku, tanggal, jatuhTempo);
        daftarPeminjaman.put(peminjaman.getId(), peminjaman);
        return peminjaman;
    }

    /*
     * Method kembalikan.
     * Menghitung denda keterlambatan, menambahkannya ke denda anggota,
     * mengembalikan stok, dan menutup transaksi.
     */
    public long kembalikan(String idPeminjaman, LocalDate tanggal) {
        wajibTanggal(tanggal);
        Peminjaman peminjaman = cariPeminjaman(idPeminjaman);

        if (!peminjaman.isAktif()) {
            throw new IllegalStateException("Buku sudah dikembalikan");
        }
        if (tanggal.isBefore(peminjaman.getTanggalPinjam())) {
            throw new IllegalArgumentException("Tanggal kembali tidak boleh sebelum tanggal pinjam");
        }

        int hariTerlambat = kalkulator.hitungHariTerlambat(peminjaman.getJatuhTempo(), tanggal);
        long denda = kalkulator.hitungDenda(
                peminjaman.getAnggota().getJenis(),
                peminjaman.getBuku().getKategori(),
                hariTerlambat);

        peminjaman.selesai(tanggal, denda);
        peminjaman.getAnggota().tambahDenda(denda);
        peminjaman.getBuku().tambahStok();
        return denda;
    }

    /*
     * Method bayarDenda.
     * Membayar denda tertunggak milik anggota tertentu,
     * sebagian atau seluruhnya.
     */
    public void bayarDenda(String idAnggota, long jumlah) {
        cariAnggota(idAnggota).bayarDenda(jumlah);
    }

    // =================================================================
    // LAPORAN
    // =================================================================

    /*
     * Method daftarTerlambat.
     * Mengembalikan semua peminjaman aktif yang sudah melewati jatuh tempo.
     */
    public List<Peminjaman> daftarTerlambat(LocalDate tanggal) {
        wajibTanggal(tanggal);
        List<Peminjaman> hasil = new ArrayList<Peminjaman>();
        for (Peminjaman p : daftarPeminjaman.values()) {
            if (p.isTerlambat(tanggal)) {
                hasil.add(p);
            }
        }
        return hasil;
    }

    /*
     * Method totalDendaTertunggak.
     * Mengembalikan total denda tertunggak seluruh anggota (Rupiah).
     */
    public long totalDendaTertunggak() {
        long total = 0;
        for (Anggota a : daftarAnggota.values()) {
            total += a.getDendaTertunggak();
        }
        return total;
    }

    /*
     * Method hitungPinjamanAktif.
     * Mengembalikan jumlah buku yang sedang dipinjam anggota tersebut.
     */
    public int hitungPinjamanAktif(String idAnggota) {
        int jumlah = 0;
        for (Peminjaman p : daftarPeminjaman.values()) {
            if (p.isAktif() && p.getAnggota().getId().equals(idAnggota)) {
                jumlah++;
            }
        }
        return jumlah;
    }

    // =================================================================
    // HELPER (private)
    // =================================================================

    /*
     * Method sedangMeminjam (helper).
     * Mengecek apakah anggota sedang meminjam ISBN tertentu.
     */
    private boolean sedangMeminjam(String idAnggota, String isbn) {
        for (Peminjaman p : daftarPeminjaman.values()) {
            if (p.isAktif()
                    && p.getAnggota().getId().equals(idAnggota)
                    && p.getBuku().getIsbn().equals(isbn)) {
                return true;
            }
        }
        return false;
    }

    /*
     * Method hitungLamaPinjam (helper).
     * Lama pinjam = aturan anggota, dibatasi aturan kategori jika lebih pendek.
     */
    private int hitungLamaPinjam(Anggota anggota, Buku buku) {
        int lama = anggota.getJenis().getLamaPinjamMaks();
        int batasKategori = buku.getKategori().getBatasLamaPinjamHari();
        if (batasKategori > 0 && batasKategori < lama) {
            lama = batasKategori;
        }
        return lama;
    }

    /*
     * Method cariAnggota (helper).
     * Mencari anggota berdasarkan ID; gagal jika tidak terdaftar.
     */
    private Anggota cariAnggota(String idAnggota) {
        Anggota anggota = daftarAnggota.get(idAnggota);
        if (anggota == null) {
            throw new IllegalArgumentException("Anggota tidak ditemukan: " + idAnggota);
        }
        return anggota;
    }

    /*
     * Method cariBuku (helper).
     * Mencari buku berdasarkan ISBN; gagal jika tidak terdaftar.
     */
    private Buku cariBuku(String isbn) {
        Buku buku = daftarBuku.get(isbn);
        if (buku == null) {
            throw new IllegalArgumentException("Buku tidak ditemukan: " + isbn);
        }
        return buku;
    }

    /*
     * Method cariPeminjaman (helper).
     * Mencari transaksi berdasarkan nomor; gagal jika tidak ada.
     */
    private Peminjaman cariPeminjaman(String idPeminjaman) {
        Peminjaman peminjaman = daftarPeminjaman.get(idPeminjaman);
        if (peminjaman == null) {
            throw new IllegalArgumentException("Peminjaman tidak ditemukan: " + idPeminjaman);
        }
        return peminjaman;
    }

    /*
     * Method wajibTanggal (helper).
     * Memastikan tanggal tidak null.
     */
    private void wajibTanggal(LocalDate tanggal) {
        if (tanggal == null) {
            throw new IllegalArgumentException("Tanggal wajib diisi");
        }
    }

    /*
     * Method buatIdBaru (helper).
     * Membuat nomor transaksi berurutan: PJ-001, PJ-002, dst.
     */
    private String buatIdBaru() {
        nomorUrut++;
        return String.format("PJ-%03d", nomorUrut);
    }
}
