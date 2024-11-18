package com.example.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.InputFilter.LengthFilter;
import android.text.method.DigitsKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AdicionarPagamentoActivity extends AppCompatActivity {

    private EditText editTextValue, editTextDate, editTextDescription;
    private Button buttonSaveInvoice;
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);

        initializeViews();
        retrieveStudentId();
        setupInputFields();
        setupSaveButton();
    }

    private void initializeViews() {
        editTextValue = findViewById(R.id.edit_text_value);
        editTextDate = findViewById(R.id.edit_text_date);
        editTextDescription = findViewById(R.id.edit_text_description);
        buttonSaveInvoice = findViewById(R.id.button_save_invoice);
    }

    private void retrieveStudentId() {
        studentId = getIntent().getIntExtra("STUDENT_ID", -1);
    }

    private void setupInputFields() {
        configureNumberInput(editTextValue);
        configureDateInput(editTextDate);
    }

    private void setupSaveButton() {
        buttonSaveInvoice.setOnClickListener(v -> saveInvoice());
    }

    private void configureNumberInput(EditText editText) {
        editText.setKeyListener(DigitsKeyListener.getInstance("0123456789,"));
        editText.setFilters(new InputFilter[]{new LengthFilter(15)});

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().replaceAll("\\s+", "");
                if (!s.toString().equals(input)) {
                    editText.setText(input);
                    editText.setSelection(input.length());
                }
            }
        });
    }

    private void configureDateInput(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            private String currentText = "";
            private final String dateFormatPlaceholder = "DDMMYYYY";  // Formato padrão de data

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(currentText)) {
                    String cleanedDate = cleanDateInput(s.toString());
                    if (cleanedDate.length() < 8) {
                        cleanedDate = cleanedDate + dateFormatPlaceholder.substring(cleanedDate.length());
                    } else {
                        cleanedDate = adjustDate(cleanedDate);
                    }

                    cleanedDate = formatDate(cleanedDate);
                    currentText = cleanedDate;
                    editText.setText(currentText);
                    editText.setSelection(Math.min(currentText.length(), cleanedDate.length()));
                }
            }

            private String cleanDateInput(String input) {
                return input.replaceAll("[^\\d]", "");
            }

            private String adjustDate(String date) {
                int day = Integer.parseInt(date.substring(0, 2));
                int month = Integer.parseInt(date.substring(2, 4));
                int year = Integer.parseInt(date.substring(4, 8));

                month = Math.max(1, Math.min(month, 12));
                day = Math.min(day, 31);

                return String.format("%02d%02d%04d", day, month, year);
            }

            private String formatDate(String date) {
                return String.format("%s/%s/%s", date.substring(0, 2), date.substring(2, 4), date.substring(4, 8));
            }
        });
    }

    private void saveInvoice() {
        String valueString = editTextValue.getText().toString().replace(",", ".").trim();
        String dateString = editTextDate.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (isValueValid(valueString) && isDateValid(dateString)) {
            double value = Double.parseDouble(valueString);
            Pagamento payment = new Pagamento(studentId, value, dateString, description);
            EstudanteDatabaseHelp dbHelper = new EstudanteDatabaseHelp(this);
            dbHelper.addPagamento(payment);
            showToast("Fatura salva com sucesso!");
            finish();
        }
    }

    private boolean isValueValid(String valueString) {
        if (valueString.isEmpty()) {
            showToast("Por favor, insira um valor válido.");
            return false;
        }
        return true;
    }

    private boolean isDateValid(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(dateString);
            return true;
        } catch (ParseException e) {
            showToast("Por favor, insira uma data válida no formato dd/MM/yyyy.");
            return false;
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
