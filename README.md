# Unit Testing & Code Coverage dengan JUnit + JaCoCo

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
