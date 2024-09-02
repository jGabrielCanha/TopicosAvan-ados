package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pagamentos")
data class Pagamento(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val alunoId: Int,
    val valor: Double,
    val data: String
)
