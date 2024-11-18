package com.example.topicos02

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class CadastroAlunoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_aluno)

        val etNome = findViewById<EditText>(R.id.etNome)
        val etIdade = findViewById<EditText>(R.id.etIdade)
        val btnEnviarDados = findViewById<Button>(R.id.btnEnviarDados)

        btnEnviarDados.setOnClickListener {
            val nome = etNome.text.toString()
            val idade = etIdade.text.toString()

            val intent = Intent(this, DetalhesAlunoActivity::class.java).apply {
                putExtra("nome", nome)
                putExtra("idade", idade)
            }
            startActivity(intent)
        }
    }
}
