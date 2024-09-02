package com.example.myapplication

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class DetalhesAlunoActivity : AppCompatActivity() {

    private val alunoViewModel: AlunoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_aluno)

        val nomeTextView: TextView = findViewById(R.id.nome_textview)
        val cpfTextView: TextView = findViewById(R.id.cpf_textview)
        val telefoneTextView: TextView = findViewById(R.id.telefone_textview)
        val cursoTextView: TextView = findViewById(R.id.curso_textview)
        val ativoTextView: TextView = findViewById(R.id.ativo_textview)
        val imageView: ImageView = findViewById(R.id.foto_imageview)

        val alunoId = intent.getIntExtra("alunoId", -1)
        val aluno = alunoViewModel.alunos.value?.find { it.id == alunoId }

        aluno?.let {
            nomeTextView.text = it.nome
            cpfTextView.text = it.cpf
            telefoneTextView.text = it.telefone
            cursoTextView.text = it.tipoCurso
            ativoTextView.text = if (it.ativo) "Ativo" else "Inativo"

            it.fotoBytes?.let { foto ->
                val bitmap = BitmapFactory.decodeByteArray(foto, 0, foto.size)
                imageView.setImageBitmap(bitmap)
            }
        }
    }
}
