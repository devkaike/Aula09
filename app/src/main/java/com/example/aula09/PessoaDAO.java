package com.example.aula09;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PessoaDAO extends SQLiteOpenHelper {

    private static final int versao = 1;

    private static final String nomeBD = "bd_pessoa";

    private static final String tbl_Pessoa = "tblPessoa";

    private static final String c_cod = "codigo";

    private static final String c_nome = "nome";

    private static final String c_email = "email";

    private static final String c_tel = "telefone";


    public PessoaDAO(@Nullable Context context) {
        super(context, nomeBD, null, versao);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + tbl_Pessoa + "(" +
                c_cod + " INTEGER PRIMARY KEY," +
                c_nome + " TEXT," +
                c_tel + " TEXT," +
                c_email + " TEXT )";
        db.execSQL(query);
    }

    public void adicionarPessoa(Pessoa p){
        SQLiteDatabase bd  = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(c_nome, p.getNome());
        valores.put(c_tel, p.getTel());
        valores.put(c_email, p.getEmail());
        bd.insert(tbl_Pessoa, null, valores);
        bd.close();
    }

    public void apagarPessoa(Pessoa p){
        SQLiteDatabase bd = this.getWritableDatabase();
        bd.delete(tbl_Pessoa, c_cod+" = ?", new String[]{String.valueOf(p.getCod())});
        bd.close();
    }

    public Pessoa obterPessoa(int cod){
        SQLiteDatabase bd = this.getWritableDatabase();
        Cursor cursor = bd.query(tbl_Pessoa,
                new String[]{c_cod, c_nome, c_tel, c_email},
                c_cod + " = ?",
                new String[]{String.valueOf(cod)},
                null, null, null);
        if(cursor != null)
            cursor.moveToFirst();
        else
            return null;

        Pessoa pessoa = new Pessoa(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3));
        return pessoa;
    }

    public List<Pessoa> obterPessoas() {
        SQLiteDatabase bd = this.getWritableDatabase();
        List<Pessoa> pessoas = new ArrayList<Pessoa>();
        String query = "SELECT * FROM " + tbl_Pessoa;
        Cursor cursor = bd.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) { // Verifica se o cursor não é nulo e se contém dados
            do {
                Pessoa pessoa = new Pessoa(
                        cursor.getInt(cursor.getColumnIndex(c_cod)), // Obtém o código usando o índice da coluna
                        cursor.getString(cursor.getColumnIndex(c_nome)), // Obtém o nome
                        cursor.getString(cursor.getColumnIndex(c_tel)),  // Obtém o telefone
                        cursor.getString(cursor.getColumnIndex(c_email))); // Obtém o email
                pessoas.add(pessoa);
            } while (cursor.moveToNext());
        }
        // Certifique-se de fechar o cursor após o uso
        if (cursor != null) {
            cursor.close();
        }
        return pessoas;
    }


    public void atualizaPessoa(Pessoa pessoa){
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(c_nome, pessoa.getNome());
        valores.put(c_tel, pessoa.getTel());
        valores.put(c_email, pessoa.getEmail());
        bd.update(tbl_Pessoa, valores, c_cod+ " = ?",
                new String[]{String.valueOf(pessoa.getCod())});
        bd.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
