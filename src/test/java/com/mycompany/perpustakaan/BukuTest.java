package com.mycompany.perpustakaan;

import org.junit.Test;
import static org.junit.Assert.*;

/*
 * [TAHAP 3 - PERBAIKAN]
 * Kelas ini menguji validasi konstruktor Buku dan batas stok. Batas stok
 * tidak bisa dicapai lewat LayananPerpustakaan, sehingga diuji langsung.
 *
 * @author Abiyyu Nauval Sugiarto (2455301002)
 */
public class BukuTest {

    private static final String ISBN = "9780306406157";

    /*
     * Method testKonstruktorBuku.
     * TC-09: Data valid tersimpan; ISBN null/kosong, judul null/kosong,
     * kategori null, dan eksemplar 0 ditolak.
     */
    @Test
    public void testKonstruktorBuku() {
        Buku b = new Buku(ISBN, "Rekayasa Perangkat Lunak", KategoriBuku.REGULER, 2);
        assertEquals(ISBN, b.getIsbn());
        assertEquals("Rekayasa Perangkat Lunak", b.getJudul());
        assertEquals(KategoriBuku.REGULER, b.getKategori());
        assertEquals(2, b.getTotalEksemplar());
        assertEquals(2, b.getTersedia());

        assertThrows(IllegalArgumentException.class, () -> new Buku(null, "Judul", KategoriBuku.REGULER, 1));
        assertThrows(IllegalArgumentException.class, () -> new Buku("  ", "Judul", KategoriBuku.REGULER, 1));
        assertThrows(IllegalArgumentException.class, () -> new Buku(ISBN, null, KategoriBuku.REGULER, 1));
        assertThrows(IllegalArgumentException.class, () -> new Buku(ISBN, "   ", KategoriBuku.REGULER, 1));
        assertThrows(IllegalArgumentException.class, () -> new Buku(ISBN, "Judul", null, 1));
        assertThrows(IllegalArgumentException.class, () -> new Buku(ISBN, "Judul", KategoriBuku.REGULER, 0));
    }

    /*
     * Method testBatasStok.
     * TC-10: Tambah stok saat penuh dan kurangi stok saat habis ditolak.
     */
    @Test
    public void testBatasStok() {
        final Buku b = new Buku(ISBN, "Judul", KategoriBuku.REGULER, 1);
        assertThrows(IllegalStateException.class, () -> b.tambahStok());
        b.kurangiStok();
        assertFalse(b.isTersedia());
        assertThrows(IllegalStateException.class, () -> b.kurangiStok());
    }
}
