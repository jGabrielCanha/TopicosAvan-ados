package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alunos")
data class Aluno(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val cpf: String,
    val telefone: String,
    val ativo: Boolean,
    val tipoCurso: String,
    val fotoBytes: ByteArray? = null  // Novo campo para armazenar a foto
)
