package com.mycompany.perpustakaan;

/*
 * Enum ini menyatakan status sebuah transaksi peminjaman:
 *   - AKTIF        -> buku masih dibawa anggota
 *   - DIKEMBALIKAN -> buku sudah dikembalikan
 *
 * @author Abiyyu Nauval Sugiarto (2455301002)
 */
public enum StatusPeminjaman {
    /* Buku masih dibawa anggota. */
    AKTIF,
    /* Buku sudah dikembalikan. */
    DIKEMBALIKAN
}
