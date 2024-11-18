package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainAtiv extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_STUDENT = 1;
    private static final int REQUEST_CODE_EDIT_STUDENT = 2;

    private RecyclerView recyclerView;
    private EstudanteAdapter estudanteAdapter;
    private List<Estudante> estudanteList;
    private FloatingActionButton fabAdd, fabDelete, fabEdit;
    private EstudanteDatabaseHelp estudanteDatabaseHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Verificar o tipo de usuário passado pelo Intent
        String userType = getIntent().getStringExtra("USER_TYPE");

        // Inicializar as views
        EditText searchNameField = findViewById(R.id.search_name_field);
        Button searchButton = findViewById(R.id.search_button);
        recyclerView = findViewById(R.id.recycler_view);
        fabAdd = findViewById(R.id.fab);
        fabDelete = findViewById(R.id.fab_delete);
        fabEdit = findViewById(R.id.fab_refresh);

        estudanteDatabaseHelp = new EstudanteDatabaseHelp(this);

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        estudanteAdapter = new EstudanteAdapter(this, new ArrayList<>(), userType);
        recyclerView.setAdapter(estudanteAdapter);

        // Configurar ação do botão de busca
        searchButton.setOnClickListener(v -> {
            String searchName = searchNameField.getText().toString().trim();

            if (!searchName.isEmpty()) {
                // Buscar estudantes pelo nome
                List<Estudante> estudanteList = estudanteDatabaseHelp.searchStudentsByName(searchName);

                if (!estudanteList.isEmpty()) {
                    estudanteAdapter.updateData(estudanteList);
                } else {
                    Toast.makeText(this, "Nenhum estudante encontrado com esse nome.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Por favor, digite um nome para buscar.", Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar Floating Action Buttons com base no tipo de usuário
        if (userType.equals("Administrador")) {
            fabAdd.setOnClickListener(v -> openAddStudentActivity());
            fabDelete.setOnClickListener(v -> handleDeleteStudents());
            fabEdit.setOnClickListener(v -> refreshStudentList());
        } else if (userType.equals("Visitante")) {
            // Desabilitar funcionalidades administrativas
            disableAdminFeatures();

            // Configurar o botão de refresh para o visitante
            fabEdit.setVisibility(View.VISIBLE);
            fabEdit.setOnClickListener(v -> refreshStudentList());
        }
    }


    private void openAddStudentActivity() {
        Intent intent = new Intent(MainAtiv.this, AdicionarEditarAlunoActivity.class);
        startActivity(intent);
    }

    private void handleDeleteStudents() {
        List<Estudante> selectedEstudantes = estudanteAdapter.getSelectedStudents();
        if (selectedEstudantes.isEmpty()) {
            showToast("Nenhum estudante selecionado");
        } else {
            showDeleteConfirmationDialog(selectedEstudantes);
        }
    }


    private void refreshStudentList() {
        estudanteList = estudanteDatabaseHelp.getAllStudents();
        estudanteAdapter.updateData(estudanteList);
    }

    private void disableAdminFeatures() {
        // Ocultar botões de adicionar, excluir e editar
        fabAdd.setVisibility(View.GONE);
        fabDelete.setVisibility(View.GONE);
        fabEdit.setVisibility(View.GONE);
    }

    private void showDeleteConfirmationDialog(List<Estudante> selectedEstudantes) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmação")
                .setMessage("Deseja realmente deletar " + selectedEstudantes.size() + " estudantes?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    deleteSelectedStudents(selectedEstudantes);
                    refreshStudentList();
                })
                .setNegativeButton("Não", null)
                .show();
    }

    private void deleteSelectedStudents(List<Estudante> selectedEstudantes) {
        for (Estudante estudante : selectedEstudantes) {
            estudanteDatabaseHelp.deleteStudent(estudante.getId());
        }
        showToast("Estudantes deletados com sucesso");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            refreshStudentList();
        }
    }
}
