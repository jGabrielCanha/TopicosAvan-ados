package com.example.topicos02

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetalhesAlunoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_aluno)

        val tvDetalhesNome = findViewById<TextView>(R.id.tvDetalhesNome)
        val tvDetalhesIdade = findViewById<TextView>(R.id.tvDetalhesIdade)

        val nome = intent.getStringExtra("nome")
        val idade = intent.getStringExtra("idade")

        tvDetalhesNome.text = "Nome: $nome"
        tvDetalhesIdade.text = "Idade: $idade"
    }
}
