package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class EstudanteAdapter extends RecyclerView.Adapter<EstudanteAdapter.StudentViewHolder> {

    private final Context context;
    private final List<Estudante> estudanteList;
    private final List<Estudante> selectedStudents = new ArrayList<>();
    private final String userType;
    private final EstudanteDatabaseHelp dbHelper;

    public EstudanteAdapter(Context context, List<Estudante> estudanteList, String userType) {
        this.context = context;
        this.estudanteList = estudanteList;
        this.userType = userType;
        this.dbHelper = new EstudanteDatabaseHelp(context);
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);
        return new StudentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Estudante estudante = estudanteList.get(position);

        holder.textViewName.setText(estudante.getName());
        holder.textViewPhone.setText(estudante.getPhone());
        holder.textViewAge.setText("Idade: " + estudante.getAge());

        if (userType.equals("Administrador")) {
            holder.textViewCpf.setVisibility(View.VISIBLE);
            holder.textViewCpf.setText(estudante.getCpf());
        } else {
            holder.textViewCpf.setVisibility(View.GONE);
        }

        // Configurar o número de faturas abertas
        List<Pagamento> pagamentos = dbHelper.getPagamentosByAlunoId(estudante.getId());
        int openInvoicesCount = pagamentos.size();
        holder.textViewInvoices.setText("Faturas Abertas: " + openInvoicesCount);

        // Botão "+"
        holder.fabGenerateInvoice.setOnClickListener(v -> {
            if (userType.equals("Administrador")) {
                Intent intent = new Intent(context, AdicionarPagamentoActivity.class);
                intent.putExtra("STUDENT_ID", estudante.getId());
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Visitantes não podem adicionar faturas.", Toast.LENGTH_SHORT).show();
            }
        });

        // Botão "Ver Detalhes"
        holder.buttonViewStudent.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetalhesAtividadeEstudante.class);
            intent.putExtra("STUDENT_ID", estudante.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return estudanteList.size();
    }

    public List<Estudante> getSelectedStudents() {
        return selectedStudents;
    }

    public void updateData(List<Estudante> newList) {
        estudanteList.clear();
        estudanteList.addAll(newList);
        notifyDataSetChanged();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewCpf, textViewPhone, textViewAge, textViewInvoices;
        Button buttonViewStudent;
        FloatingActionButton fabGenerateInvoice;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewCpf = itemView.findViewById(R.id.text_view_cpf);
            textViewPhone = itemView.findViewById(R.id.text_view_phone);
            textViewAge = itemView.findViewById(R.id.text_view_age);
            textViewInvoices = itemView.findViewById(R.id.text_view_invoices);
            buttonViewStudent = itemView.findViewById(R.id.button_view_student);
            fabGenerateInvoice = itemView.findViewById(R.id.fab_generate_invoice);
        }
    }
}
