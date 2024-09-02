package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class CadastrarPagamentoActivity : AppCompatActivity() {

    private val alunoViewModel: AlunoViewModel by viewModels()
    private lateinit var valorEditText: EditText
    private lateinit var dataEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar_pagamento)

        valorEditText = findViewById(R.id.edittext_valor)
        dataEditText = findViewById(R.id.edittext_data)

        findViewById<Button>(R.id.button_salvar_pagamento).setOnClickListener {
            salvarPagamento()
        }
    }

    private fun salvarPagamento() {
        val valor = valorEditText.text.toString().toDouble()
        val data = dataEditText.text.toString()
        val alunoId = intent.getIntExtra("alunoId", -1)

        val pagamento = Pagamento(
            alunoId = alunoId,
            valor = valor,
            data = data
        )

        alunoViewModel.addPagamento(pagamento)
        finish()
    }
}
