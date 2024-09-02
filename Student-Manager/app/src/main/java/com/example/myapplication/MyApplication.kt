package com.example.myapplication

import android.app.Application
import androidx.room.Room

class MyApplication : Application() {

    // Inicializa o banco de dados
    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java, "app_database"
        ).build()
    }
}
