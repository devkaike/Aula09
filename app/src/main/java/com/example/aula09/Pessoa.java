package com.example.aula09;

import android.os.Parcel;
import android.os.Parcelable;

public class Pessoa implements Parcelable{
    private int cod;

    private String nome, tel, email;

    public Pessoa(int cod, String nome, String tel, String email) {
        this.cod = cod;
        this.nome = nome;
        this.tel = tel;
        this.email = email;
    }

    public Pessoa(String nome, String tel, String email) {
        this.nome = nome;
        this.tel = tel;
        this.email = email;
    }

    public Pessoa() {

    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    protected Pessoa(Parcel in) {
        cod = in.readInt();
        nome = in.readString();
        tel = in.readString();
        email = in.readString();
    }

    public static final Creator<Pessoa> CREATOR = new Creator<Pessoa>() {
        @Override
        public Pessoa createFromParcel(Parcel in) {
            return new Pessoa(in);
        }

        @Override
        public Pessoa[] newArray(int size) {
            return new Pessoa[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cod);
        dest.writeString(nome);
        dest.writeString(tel);
        dest.writeString(email);
    }
}
