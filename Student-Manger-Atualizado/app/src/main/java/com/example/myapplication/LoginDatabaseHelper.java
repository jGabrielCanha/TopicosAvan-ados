package com.example.myapplication;

import android.content.ContentValues;
import android.util.Log;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LoginDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LoginDB";
    private static final int DATABASE_VERSION = 2; // Incrementar a versão

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    public LoginDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                "userType TEXT)"; // Coluna userType adicionada
        db.execSQL(CREATE_USERS_TABLE);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Apagar a tabela antiga
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        // Criar a tabela nova
        onCreate(db);
    }


    public boolean addUser(String email, String password, String userType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("password", password);
        values.put("userType", userType);

        long result = db.insert("users", null, values);
        db.close();
        return result != -1;
    }



    // Method to validate login
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Limpar espaços e converter para minúsculas
        email = email.trim().toLowerCase();
        password = password.trim();

        // Log para depuração
        System.out.println("DEBUG: Email recebido = " + email);
        System.out.println("DEBUG: Senha recebida = " + password);

        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{email, password},
                null,
                null,
                null
        );

        // Depuração: Quantidade de registros encontrados
        System.out.println("DEBUG: Registros encontrados = " + cursor.getCount());

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }

    public void listUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);

        if (cursor.moveToFirst()) {
            do {
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
                System.out.println("DB_DEBUG: Email = " + email + ", Password = " + password);
            } while (cursor.moveToNext());
        } else {
            System.out.println("DB_DEBUG: Nenhum usuário encontrado.");
        }

        cursor.close();
        db.close();
    }
    public String getUserType(String email, String password) {
        email = email.trim().toLowerCase();
        password = password.trim();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT userType FROM users WHERE email = ? AND password = ?", new String[]{email, password});

            if (cursor.moveToFirst()) {
                String userType = cursor.getString(0);
                Log.d("DB_DEBUG", "Usuário encontrado: Tipo = " + userType);
                return userType;
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Erro ao buscar tipo de usuário", e);
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        Log.d("DB_DEBUG", "Nenhum usuário encontrado com essas credenciais.");
        return null;
    }



}