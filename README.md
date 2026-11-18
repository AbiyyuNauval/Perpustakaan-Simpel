# Unit Testing & Code Coverage dengan JUnit + JaCoCo

Repository tugas **Eksplorasi Tools Pengujian (Pertemuan 12–13)**, mata kuliah Kualitas dan Pengujian Perangkat Lunak.
Objek uji: **Sistem Peminjaman Buku Perpustakaan** (`PerpustakaanDemo`). Sistem ini diuji dengan **17 test case JUnit 4**, dan kecukupan pengujiannya diukur dengan **JaCoCo**.

| Tools | Versi | Fungsi |
|---|---|---|
| JUnit | 4.13.2 | Menjalankan unit test dan membandingkan expected dengan actual (assertion) |
| JaCoCo | 0.8.8 | Mengukur code coverage (instruction, branch, line, method) dan membuat laporan HTML |
| Apache Maven | 3.6.3 / 3.9.x | Build tool: mengunduh library dan menjalankan test + JaCoCo |
| NetBeans IDE | 8.2 | IDE yang dipakai kelompok |
| JDK | 8 | Kode ditulis untuk Java 8 |

## Struktur repository

```
KPPL-JUnit-JaCoCo-PerpustakaanDemo/
├── pom.xml                 # konfigurasi Maven: dependency JUnit + plugin JaCoCo
├── nbactions.xml           # menu "Run Maven -> jacoco" di NetBeans (clean test)
├── src/main/java/com/mycompany/perpustakaan/    # kode yang diuji (8 kelas)
│   ├── LayananPerpustakaan.java   # kelas utama: pinjam, kembalikan, bayar denda, laporan
│   ├── KalkulatorDenda.java       # perhitungan denda bertingkat
│   ├── Anggota.java, Buku.java, Peminjaman.java
│   └── JenisAnggota.java, KategoriBuku.java, StatusPeminjaman.java   # enum aturan
└── src/test/java/com/mycompany/perpustakaan/    # 6 file test, 17 test case
```

## Aturan bisnis yang diuji

| Jenis anggota | Maks. buku | Lama pinjam | Denda/hari |
|---|---|---|---|
| Mahasiswa | 3 | 7 hari | Rp500 |
| Dosen | 5 | 14 hari | Rp0 |
| Umum | 2 | 5 hari | Rp1.000 |

| Kategori buku | Boleh dipinjam | Batas lama pinjam | Pengali denda |
|---|---|---|---|
| Reguler | Ya | ikut anggota | 1x |
| Baru | Ya | 3 hari | 2x |
| Referensi | Tidak | – | – |

- Mulai hari ke-8 keterlambatan, tarif denda menjadi 2x. Denda maksimum Rp50.000 per peminjaman.
- Anggota diblokir jika denda tertunggaknya ≥ Rp20.000.
- `pinjam()` mengecek 6 aturan secara berurutan: buku referensi, masa berlaku, blokir, batas jumlah, judul sama, stok.

## Cara menjalankan

**NetBeans 8.2:** buka proyek (File → Open Project), lalu klik kanan proyek → *Run Maven* → **jacoco**.

**Command line:**

```
mvn clean test
```

Hasil:
- Output JUnit tampil di jendela Output / terminal (`Tests run`, `Failures`, `Errors`).
- Laporan coverage: buka `target/site/jacoco/index.html` di browser.

Build pertama membutuhkan internet untuk mengunduh JUnit dan plugin JaCoCo dari Maven Central.

## Konfigurasi JaCoCo (pom.xml)

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.8</version>
    <executions>
        <execution>
            <id>prepare-agent</id>          <!-- pasang agen perekam sebelum test -->
            <goals><goal>prepare-agent</goal></goals>
        </execution>
        <execution>
            <id>report</id>                 <!-- buat laporan HTML setelah fase test -->
            <phase>test</phase>
            <goals><goal>report</goal></goals>
        </execution>
    </executions>
</plugin>
```

## Test case per tahap

Pengujian dilakukan dalam tiga tahap. Test di tahap 3 dan 4 ditambahkan berdasarkan bagian kode yang belum teruji menurut laporan JaCoCo dari tahap sebelumnya.

| Tahap | File test | Test case | Jumlah |
|---|---|---|---|
| 2 – Baseline | `LayananPerpustakaanTest` | TC-01 – TC-05 | 5 |
| 3 – Perbaikan | `KalkulatorDendaTest`, `BukuTest`, `AnggotaTest`, `LayananLanjutanTest` | TC-06 – TC-16 | 11 |
| 4 – Analisis | `LaporanTerlambatTest` | TC-17 | 1 |

Repository ini berisi kondisi akhir (tahap 4, semua file test). Untuk mengulang tahap tertentu, hapus sementara file test tahap berikutnya dari `src/test/...`, lalu jalankan `mvn clean test`:
- **Tahap 2:** sisakan `LayananPerpustakaanTest.java` saja.
- **Tahap 3:** hapus `LaporanTerlambatTest.java`.

Rincian skenario, data uji, dan expected result tiap test case ada di dokumen *Skenario Pengujian (Test Case)*.

## Hasil

Hasil eksekusi kelompok pada 18 November 2026:

| Tahap | Tests run | Failures | Instruction | Branch | Cabang belum teruji |
|---|---|---|---|---|---|
| 2 – Baseline | 5 | 0 | 69% | 48% | 63 dari 122 |
| 3 – Perbaikan | 16 | 0 | 100% | 98% | 2 (Peminjaman:49, LayananPerpustakaan:220) |
| 4 – Analisis | 17 | 0 | 100% | 99% | 1 (LayananPerpustakaan:220) |

Cabang di `LayananPerpustakaan` baris 220 (`batasKategori < lama`) tidak mungkin bernilai false dengan aturan saat ini: batas buku Baru 3 hari selalu lebih kecil dari lama pinjam terpendek (5 hari). Bukti lengkap ada di dokumen *Evidence dan Report Hasil Pengujian*.

## Kelompok

- Abiyyu Nauval Sugiarto (2455301002)
- Nama Anggota 2 (NIM)
- Nama Anggota 3 (NIM)
