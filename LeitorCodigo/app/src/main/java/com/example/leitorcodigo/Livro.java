package com.example.leitorcodigo;

public class Livro {

    public String nome;
    public String autor;

    public String getNome() {
        return nome;
    }

    public Livro(String nome) {
        this.nome = nome;
    }

    public Livro(String nome, String autor){
        this.nome = nome;
        this.autor = autor;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        if (this.autor == null){
            return this.nome;
        }
        else return this.nome + " de " + this.autor;
    }
}
