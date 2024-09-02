package com.example.myapplication

import androidx.room.*

@Dao
interface AlunoDao {
    @Insert
    suspend fun inserir(aluno: Aluno)

    @Update
    suspend fun atualizar(aluno: Aluno)

    @Delete
    suspend fun deletar(aluno: Aluno)

    @Query("SELECT * FROM alunos")
    suspend fun obterTodos(): List<Aluno>
}
