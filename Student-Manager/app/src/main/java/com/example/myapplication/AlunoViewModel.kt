package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class AlunoViewModel(private val alunoDao: AlunoDao, private val pagamentoDao: PagamentoDao) : ViewModel() {

    private val _alunos = MutableLiveData<List<Aluno>>()
    val alunos: LiveData<List<Aluno>> get() = _alunos

    fun loadAlunos() {
        viewModelScope.launch {
            _alunos.value = alunoDao.obterTodos()
        }
    }

    fun addAluno(aluno: Aluno) {
        viewModelScope.launch {
            alunoDao.inserir(aluno)
            loadAlunos()
        }
    }

    fun updateAluno(aluno: Aluno) {
        viewModelScope.launch {
            alunoDao.atualizar(aluno)
            loadAlunos()
        }
    }

    fun deleteAluno(aluno: Aluno) {
        viewModelScope.launch {
            alunoDao.deletar(aluno)
            loadAlunos()
        }
    }

    fun getPagamentosPorAluno(alunoId: Int): LiveData<List<Pagamento>> {
        val pagamentos = MutableLiveData<List<Pagamento>>()
        viewModelScope.launch {
            pagamentos.value = pagamentoDao.buscarPagamentosPorAluno(alunoId)
        }
        return pagamentos
    }

    fun addPagamento(pagamento: Pagamento) {
        viewModelScope.launch {
            pagamentoDao.inserir(pagamento)
        }
    }
}
