# Tugas Kecil 1 - IF2211 Strategi Algoritma

## Daftar Isi

* [Tugas Kecil 1 - IF2211 Strategi Algoritma](#tugas-kecil-1---if2211-strategi-algoritma)
    1. [Daftar Isi](#daftar-isi)
    2. [IQ Puzzler Pro Solver](#iq-puzzler-pro-solver)
    3. [Tentang IQ Puzzler Pro](#tentang-iq-puzzler-pro)
    4. [Cara Kerja](#cara-kerja)
    5. [Cara *Compile* dan Menjalankan](#cara-compile-dan-menjalankan)
    6. [*Library* yang Digunakan](#library-yang-digunakan)

## IQ Puzzler Pro Solver 
Dibuat oleh Jonathan Levi (13523132)

Program ini dibuat dengan tujuan untuk mendapatkan solusi dari permainan IQ Puzzler Pro.

## Tentang IQ Puzzler Pro
IQ Puzzler Pro adalah permainan papan yang mana pemain harus dapat mengisi seluruh papan dengan *piece* yang tersedia.

Permainan dimulai dengan papan kosong. Pemain dapat meletakkan *piece* sedemikian sehingga tidak ada blok yang bertumpang tindih atau *overlapping*. Setiap *piece* dapat dirotasikan maupun dicerminkan.

Puzzle dinyatakan selesai jika dan hanya jika papan terisi penuh dan seluruh *piece* berhasil diletakkan tanpa tumpang tindih.

## Cara Kerja
1. Program membaca file teks yang diberikan oleh pengguna (terletak di folder [test](test)), yaitu jumlah baris, jumlah kolom, banyaknya piece, mode atau config, dan bentuk piece.
    * Jika config yang dipakai adalah custom, maka program membaca bentuk papan (board) juga.
2. Piece yang dibaca disimpan sebagai kumpulan koordinat.
3. Semua piece dirotasikan dan dicerminkan, tiap hasil orientasi tersebut disimpan sebagai kumpulan koordinat dan akan digunakan nanti.
4. Program memasang piece pertama pada papan yang masih kosong.
5. Program lanjut mencari posisi untuk mencoba memasang piece selanjutnya pada papan tanpa tumpang tindih (overlapping) dengan piece lainnya.
    * Jika piece tidak dapat dipasang, maka dipakai orientasi (rotasi atau pencerminan) lain dari piece tersebut, program mencari lokasi sampai piece tersebut dapat dipasang pada papan.  
        * Jika semua orientasi terpakai dan piece tersebut tetap tidak dapat dipasang di mana pun tanpa tumpang tindih, cabut piece sebelumnya dan coba pasang dengan orientasi lain dari piece tersebut (backtracking).
    * Jika piece dapat dipasang, ulangi tahap 5 hingga piece terakhir dapat dipasang dan tidak tersisa satu pun slot atau cell pada papan.
6.	Program berhenti atau menyimpan hasil dalam teks dan/atau gambar, sesuai keinginan pengguna.


## *Requirement*
* Java (dicoba pada 21.0.4 LTS Microsoft Build)

## Cara *Compile* dan Menjalankan
1. Ubah *directory* pada terminal ke folder "src".
    ```
    cd src
    ```
2. *Compile* file Java yang ada.
    ```
    javac Main.java SaveImg.java
    ```
3. Ubah *directory* pada terminal ke folder "bin".
    ```
    cd bin
    ```
4. Jalankan program.
    ```
    java Main
    ```

## *Library* yang Digunakan
* java.io
* java.nio
* java.util
* java.awt
* javax.imageio