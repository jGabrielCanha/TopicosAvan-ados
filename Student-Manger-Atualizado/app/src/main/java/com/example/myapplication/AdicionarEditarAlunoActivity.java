package com.example.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdicionarEditarAlunoActivity extends AppCompatActivity {

    private EditText editTextName, editTextCpf, editTextPhone, editTextAge;
    private CheckBox checkBoxActive;
    private RadioGroup radioGroupCourseType;
    private Button buttonSave;
    private EstudanteDatabaseHelp databaseHelper;
    private int studentId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_student);

        initializeViews();
        setupTextWatchers();
        handleIntent();
        setupSaveButton();
    }

    private void initializeViews() {
        editTextName = findViewById(R.id.edit_text_name);
        editTextCpf = findViewById(R.id.edit_text_cpf);
        editTextPhone = findViewById(R.id.edit_text_phone);
        editTextAge = findViewById(R.id.edit_text_age);
        checkBoxActive = findViewById(R.id.checkbox_active);
        radioGroupCourseType = findViewById(R.id.radio_group_course_type);
        buttonSave = findViewById(R.id.button_save);
        databaseHelper = new EstudanteDatabaseHelp(this);
    }

    private void setupTextWatchers() {
        editTextCpf.addTextChangedListener(createCpfTextWatcher());
        editTextPhone.addTextChangedListener(createPhoneTextWatcher());
    }

    private TextWatcher createCpfTextWatcher() {
        return new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isUpdating) return;
                isUpdating = true;

                String formatted = formatCpf(s.toString());
                editTextCpf.setText(formatted);
                editTextCpf.setSelection(formatted.length());

                isUpdating = false;
            }
        };
    }

    private TextWatcher createPhoneTextWatcher() {
        return new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isUpdating) return;
                isUpdating = true;

                String formatted = formatPhone(s.toString());
                editTextPhone.setText(formatted);
                editTextPhone.setSelection(formatted.length());

                isUpdating = false;
            }
        };
    }

    private String formatCpf(String cpf) {
        cpf = cpf.replaceAll("[^\\d]", "");
        if (cpf.length() > 11) cpf = cpf.substring(0, 11);
        return cpf.length() > 3 ?
                cpf.substring(0, 3) + "." +
                        (cpf.length() > 6 ? cpf.substring(3, 6) + "." +
                                (cpf.length() > 9 ? cpf.substring(6, 9) + "-" + cpf.substring(9) : cpf.substring(6)) :
                                cpf.substring(3)) : cpf;
    }

    private String formatPhone(String phone) {
        phone = phone.replaceAll("[^\\d]", "");
        if (phone.length() > 11) phone = phone.substring(0, 11);
        return phone.length() > 0 ?
                "(" + (phone.length() > 2 ? phone.substring(0, 2) + ") " +
                        (phone.length() > 7 ? phone.substring(2, 7) + "-" + phone.substring(7) : phone.substring(2)) : phone) : phone;
    }

    private void handleIntent() {
        if (getIntent().hasExtra("student_id")) {
            studentId = getIntent().getIntExtra("student_id", -1);
            loadStudentData(studentId);
        }
    }

    private void setupSaveButton() {
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput()) {
                    saveStudent();
                }
            }
        });
    }

    private void saveStudent() {
        String name = editTextName.getText().toString().trim();
        String cpf = editTextCpf.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        int age = Integer.parseInt(editTextAge.getText().toString().trim());
        boolean isActive = checkBoxActive.isChecked();
        String courseType = ((RadioButton) findViewById(radioGroupCourseType.getCheckedRadioButtonId())).getText().toString();

        if (studentId == -1) {
            databaseHelper.addStudent(new Estudante(0, name, cpf, phone, age, isActive, courseType));
            showToast("Estudante adicionado com sucesso!");
        } else {
            databaseHelper.updateStudent(new Estudante(studentId, name, cpf, phone, age, isActive, courseType));
            showToast("Estudante atualizado com sucesso!");
        }

        setResult(RESULT_OK);
        finish();
    }

    private void loadStudentData(int studentId) {
        Estudante estudante = databaseHelper.getStudentById(studentId);
        if (estudante != null) {
            editTextName.setText(estudante.getName());
            editTextCpf.setText(estudante.getCpf());
            editTextPhone.setText(estudante.getPhone());
            editTextAge.setText(String.valueOf(estudante.getAge()));
            checkBoxActive.setChecked(estudante.isActive());

            if (estudante.getCourseType().equals("Graduação")) {
                ((RadioButton) findViewById(R.id.radio_undergraduate)).setChecked(true);
            } else if (estudante.getCourseType().equals("Pós-graduação")) {
                ((RadioButton) findViewById(R.id.radio_postgraduate)).setChecked(true);
            }
        }
    }

    private boolean validateInput() {
        String name = editTextName.getText().toString().trim();
        String cpf = editTextCpf.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String ageText = editTextAge.getText().toString().trim();

        if (name.isEmpty() || !name.matches("[a-zA-Z\\s]+")) {
            showToast("Por favor, insira um nome válido (somente letras)");
            return false;
        }

        if (cpf.isEmpty() || !cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            showToast("Por favor, insira um CPF válido no formato 000.000.000-00");
            return false;
        }

        if (phone.isEmpty() || !phone.matches("\\(\\d{2}\\) \\d{5}-\\d{4}")) {
            showToast("Por favor, insira um telefone válido no formato (00) 00000-0000");
            return false;
        }

        if (ageText.isEmpty()) {
            showToast("Por favor, insira a idade");
            return false;
        }

        try {
            int age = Integer.parseInt(ageText);
            if (age <= 0 || age > 120) {
                showToast("Por favor, insira uma idade válida");
                return false;
            }
        } catch (NumberFormatException e) {
            showToast("Por favor, insira uma idade válida");
            return false;
        }

        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
