package com.example.myapplication

import android.os.Bundle
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class ListarAlunosActivity : AppCompatActivity() {

    private lateinit var alunoViewModel: AlunoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_alunos)

        // Inicializar os DAOs
        val alunoDao = (application as MyApplication).database.alunoDao()
        val pagamentoDao = (application as MyApplication).database.pagamentoDao()

        // ViewModel usando o ViewModelFactory
        val factory = AlunoViewModelFactory(alunoDao, pagamentoDao)
        alunoViewModel = ViewModelProvider(this, factory).get(AlunoViewModel::class.java)

        val listView: ListView = findViewById(R.id.lista_alunos)

        alunoViewModel.alunos.observe(this) { alunos ->
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, alunos.map { it.nome })
            listView.adapter = adapter
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val alunoSelecionado = alunoViewModel.alunos.value?.get(position)
            val intent = Intent(this, DetalhesAlunoActivity::class.java)
            intent.putExtra("alunoId", alunoSelecionado?.id)
            startActivity(intent)
        }

        alunoViewModel.loadAlunos()
    }
}
