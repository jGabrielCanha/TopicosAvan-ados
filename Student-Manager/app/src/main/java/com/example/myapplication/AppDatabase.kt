package com.example.myapplication

import androidx.room.Database
import androidx.room.RoomDatabase

//  entidade e a versão do banco de dados
@Database(entities = [Aluno::class, Pagamento::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun alunoDao(): AlunoDao
    abstract fun pagamentoDao(): PagamentoDao
}
