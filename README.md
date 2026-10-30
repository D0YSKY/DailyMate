# DailyMate — Modern Daily Planner & Focus App

DailyMate adalah aplikasi Daily Planner modern berbasis Android Native yang dibangun menggunakan **Kotlin**, **Jetpack Compose**, **Material Design 3**, dan **Room Database**.

## ✨ Fitur Utama
- **Home Dashboard**: Sapaan dinamis berdasarkan waktu, tanggal hari ini, indikator progress harian, pencarian tugas, dan filter status/kategori.
- **Add & Edit Task**: Form pembuatan tugas lengkap dengan judul, deskripsi, DatePicker, TimePicker, level prioritas (*High, Medium, Low*), dan kategori.
- **Calendar Schedule**: Tampilan kalender mingguan interaktif untuk melihat dan mengelola jadwal harian.
- **Checklist**: Halaman terpisah untuk memantau tugas yang pending vs selesai, dilengkapi fitur hapus tugas selesai massal (*Clear Completed*).
- **Focus Timer (Pomodoro)**: Timer teknik Pomodoro (Focus 25m, Short Break 5m, Long Break 15m) lengkap dengan pengikat tugas fokus dan statistik sesi.
- **Profile & Settings**: Pengaturan tema (*Dark Mode* / *Light Mode*), statistik produktivitas, serta opsi manajemen data lokal.

## 🛠️ Teknologi & Arsitektur
- **Language**: Kotlin 2.0
- **UI Toolkit**: Jetpack Compose + Material Design 3
- **Navigation**: Navigation Compose
- **Architecture**: MVVM (Model-View-ViewModel) + Repository Pattern
- **Local Persistence**: Room Database + Kotlin Coroutines & Flow
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 35 (Android 15)

## 🚀 Cara Menjalankan Project
1. Clone repositori ini:
   ```bash
   git clone https://github.com/1feinz/DailyMate.git
   ```
2. Buka project di **Android Studio** (disarankan versi Ladybug/Jellyfish atau lebih baru).
3. Biarkan **Gradle Sync** selesai mengunduh dependencies (menggunakan **JDK 17**).
4. Jalankan aplikasi pada Emulator Android atau Smartphone fisik via USB Debugging.
