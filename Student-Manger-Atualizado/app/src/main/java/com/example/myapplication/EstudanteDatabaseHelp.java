package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class EstudanteDatabaseHelp extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "studentss.db"; // Nome do banco de dados
    private static final int DATABASE_VERSION = 4; // Versão do banco de dados

    // Nome da tabela e colunas para alunos
    private static final String TABLE_STUDENTS = "students";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_CPF = "cpf";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_ACTIVE = "active";
    private static final String COLUMN_COURSE_TYPE = "course_type";

    // Nome da tabela e colunas para pagamentos
    private static final String TABLE_PAGAMENTOS = "pagamentos";
    private static final String COLUMN_PAGAMENTO_ID = "id";
    private static final String COLUMN_PAGAMENTO_ALUNO_ID = "aluno_id";
    private static final String COLUMN_PAGAMENTO_VALOR = "valor";
    private static final String COLUMN_PAGAMENTO_DATA = "data";
    private static final String COLUMN_PAGAMENTO_DESCRICAO = "descricao";

    public EstudanteDatabaseHelp(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createStudentsTable(db);
        createPagamentosTable(db);
    }

    private void createStudentsTable(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE " + TABLE_STUDENTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_CPF + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_AGE + " INTEGER, " +
                COLUMN_ACTIVE + " INTEGER DEFAULT 1, " +
                COLUMN_COURSE_TYPE + " TEXT)";
        db.execSQL(createTableSQL);
    }

    private void createPagamentosTable(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE " + TABLE_PAGAMENTOS + " (" +
                COLUMN_PAGAMENTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PAGAMENTO_ALUNO_ID + " INTEGER, " +
                COLUMN_PAGAMENTO_VALOR + " REAL, " +
                COLUMN_PAGAMENTO_DATA + " TEXT, " +
                COLUMN_PAGAMENTO_DESCRICAO + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_PAGAMENTO_ALUNO_ID + ") REFERENCES " + TABLE_STUDENTS + "(" + COLUMN_ID + "))";
        db.execSQL(createTableSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            upgradeToVersion4(db);
        }
    }

    private void upgradeToVersion4(SQLiteDatabase db) {
        try {
            db.execSQL("ALTER TABLE " + TABLE_PAGAMENTOS + " ADD COLUMN " + COLUMN_PAGAMENTO_DESCRICAO + " TEXT");
        } catch (Exception e) {
            Log.e("DB_UPGRADE", "Erro ao atualizar a tabela pagamentos: " + e.getMessage());
        }
    }

    // Adiciona um novo estudante ao banco de dados
    public void addStudent(Estudante estudante) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = getStudentContentValues(estudante);
            db.beginTransaction();
            db.insertOrThrow(TABLE_STUDENTS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("DB_ERROR", "Erro ao adicionar estudante: " + e.getMessage());
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private ContentValues getStudentContentValues(Estudante estudante) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, estudante.getName());
        values.put(COLUMN_CPF, estudante.getCpf());
        values.put(COLUMN_PHONE, estudante.getPhone());
        values.put(COLUMN_AGE, estudante.getAge());
        values.put(COLUMN_ACTIVE, estudante.isActive() ? 1 : 0);
        values.put(COLUMN_COURSE_TYPE, estudante.getCourseType());
        return values;
    }

    // Adiciona um novo pagamento ao banco de dados
    public void addPagamento(Pagamento pagamento) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = getPagamentoContentValues(pagamento);
            db.beginTransaction();
            db.insertOrThrow(TABLE_PAGAMENTOS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("DB_ERROR", "Erro ao adicionar pagamento: " + e.getMessage());
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private ContentValues getPagamentoContentValues(Pagamento pagamento) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PAGAMENTO_ALUNO_ID, pagamento.getAlunoId());
        values.put(COLUMN_PAGAMENTO_VALOR, pagamento.getValor());
        values.put(COLUMN_PAGAMENTO_DATA, pagamento.getData());
        values.put(COLUMN_PAGAMENTO_DESCRICAO, pagamento.getDescricao());
        return values;
    }

    // Deleta um estudante e seus pagamentos relacionados
    public void deleteStudent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(TABLE_PAGAMENTOS, COLUMN_PAGAMENTO_ALUNO_ID + " = ?", new String[]{String.valueOf(id)});
            db.delete(TABLE_STUDENTS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("DB_ERROR", "Erro ao deletar estudante: " + e.getMessage());
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Deleta um pagamento específico
    public void deletePagamento(int pagamentoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(TABLE_PAGAMENTOS, COLUMN_PAGAMENTO_ID + " = ?", new String[]{String.valueOf(pagamentoId)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("DB_ERROR", "Erro ao deletar pagamento: " + e.getMessage());
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Atualiza as informações de um estudante
    public void updateStudent(Estudante estudante) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = getStudentContentValues(estudante);
            db.beginTransaction();
            db.update(TABLE_STUDENTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(estudante.getId())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("DB_ERROR", "Erro ao atualizar estudante: " + e.getMessage());
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Atualiza as informações de um pagamento
    public void updatePagamento(Pagamento pagamento) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = getPagamentoContentValues(pagamento);
            db.beginTransaction();
            db.update(TABLE_PAGAMENTOS, values, COLUMN_PAGAMENTO_ID + " = ?", new String[]{String.valueOf(pagamento.getId())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("DB_ERROR", "Erro ao atualizar pagamento: " + e.getMessage());
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Obtém um estudante pelo ID
    public Estudante getStudentById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Estudante estudante = null;
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_STUDENTS, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                estudante = getStudentFromCursor(cursor);
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Erro ao obter estudante: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return estudante;
    }

    private Estudante getStudentFromCursor(Cursor cursor) {
        return new Estudante(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CPF)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACTIVE)) == 1,
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_TYPE))
        );
    }

    // Obtém todos os estudantes
    public List<Estudante> getAllStudents() {
        List<Estudante> estudanteList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_STUDENTS, null);
            if (cursor.moveToFirst()) {
                do {
                    estudanteList.add(getStudentFromCursor(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Erro ao obter todos os estudantes: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return estudanteList;
    }

    // Obtém todos os pagamentos de um aluno pelo ID do aluno
    public List<Pagamento> getPagamentosByAlunoId(int alunoId) {
        List<Pagamento> pagamentosList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_PAGAMENTOS, null, COLUMN_PAGAMENTO_ALUNO_ID + " = ?", new String[]{String.valueOf(alunoId)}, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    pagamentosList.add(getPagamentoFromCursor(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Erro ao obter pagamentos: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return pagamentosList;
    }

    private Pagamento getPagamentoFromCursor(Cursor cursor) {
        return new Pagamento(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PAGAMENTO_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PAGAMENTO_ALUNO_ID)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PAGAMENTO_VALOR)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAGAMENTO_DATA)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAGAMENTO_DESCRICAO))
        );
    }
    public List<Estudante> searchStudentsByName(String name) {
        List<Estudante> estudanteList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta SQL para buscar pelo nome
        Cursor cursor = db.rawQuery("SELECT * FROM students WHERE name LIKE ?", new String[]{"%" + name + "%"});

        if (cursor.moveToFirst()) {
            do {
                Estudante estudante = new Estudante();
                estudante.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                estudante.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                estudante.setAge(cursor.getInt(cursor.getColumnIndexOrThrow("age")));
                estudanteList.add(estudante);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return estudanteList;
    }


}
