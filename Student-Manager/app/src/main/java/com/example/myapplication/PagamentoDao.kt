package com.example.myapplication

import androidx.room.*

@Dao
interface PagamentoDao {
    @Insert
    suspend fun inserir(pagamento: Pagamento)

    @Update
    suspend fun atualizar(pagamento: Pagamento)

    @Delete
    suspend fun deletar(pagamento: Pagamento)

    @Query("SELECT * FROM pagamentos WHERE alunoId = :alunoId")
    suspend fun buscarPagamentosPorAluno(alunoId: Int): List<Pagamento>
}
