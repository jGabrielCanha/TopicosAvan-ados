package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import java.io.ByteArrayOutputStream

class CadastrarAlunoActivity : AppCompatActivity() {

    private lateinit var alunoViewModel: AlunoViewModel
    private lateinit var nomeEditText: EditText
    private lateinit var cpfEditText: EditText
    private lateinit var telefoneEditText: EditText
    private lateinit var ativoCheckBox: CheckBox
    private lateinit var cursoRadioGroup: RadioGroup
    private lateinit var imageView: ImageView  // ImageView para mostrar a foto

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar_aluno)

        // Inicializar os DAOs a partir do AppDatabase
        val alunoDao = (application as MyApplication).database.alunoDao()
        val pagamentoDao = (application as MyApplication).database.pagamentoDao()

        // Criar o ViewModel usando o ViewModelFactory
        val factory = AlunoViewModelFactory(alunoDao, pagamentoDao)
        alunoViewModel = ViewModelProvider(this, factory).get(AlunoViewModel::class.java)

        nomeEditText = findViewById(R.id.edittext_nome)
        cpfEditText = findViewById(R.id.edittext_cpf)
        telefoneEditText = findViewById(R.id.edittext_telefone)
        ativoCheckBox = findViewById(R.id.checkbox_ativo)
        cursoRadioGroup = findViewById(R.id.radiogroup_curso)
        imageView = findViewById(R.id.imageView)

        findViewById<Button>(R.id.button_salvar).setOnClickListener {
            salvarAluno()
        }

        findViewById<Button>(R.id.btnTakePhoto).setOnClickListener {
            tirarFoto()
        }

        findViewById<Button>(R.id.button_listar_alunos).setOnClickListener {
            val intent = Intent(this, ListarAlunosActivity::class.java)
            startActivity(intent)
        }
    }

    private fun tirarFoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(packageManager)?.also {
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
    }

    private fun salvarAluno() {
        val nome = nomeEditText.text.toString()
        val cpf = cpfEditText.text.toString()
        val telefone = telefoneEditText.text.toString()
        val ativo = ativoCheckBox.isChecked
        val tipoCurso = when (cursoRadioGroup.checkedRadioButtonId) {
            R.id.radiobutton_graduacao -> "Graduação"
            R.id.radiobutton_posgraduacao -> "Pós-graduação"
            else -> ""
        }

        val fotoBytes: ByteArray? = imageView.drawable?.let {
            val bitmap = (it as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.toByteArray()
        }

        val aluno = Aluno(
            nome = nome,
            cpf = cpf,
            telefone = telefone,
            ativo = ativo,
            tipoCurso = tipoCurso,
            fotoBytes = fotoBytes  // Salva a foto no objeto Aluno
        )

        alunoViewModel.addAluno(aluno)
        finish()
    }
}
