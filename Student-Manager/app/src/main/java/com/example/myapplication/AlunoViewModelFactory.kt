package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AlunoViewModelFactory(
    private val alunoDao: AlunoDao,
    private val pagamentoDao: PagamentoDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlunoViewModel::class.java)) {
            return AlunoViewModel(alunoDao, pagamentoDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
