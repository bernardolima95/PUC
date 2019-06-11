package com.example.leitorcodigo;

public class Livro {

    public String nome;

    public String getNome() {
        return nome;
    }

    public Livro(String nome) {
        this.nome = nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Livro{" +
                "nome='" + nome + '\'' +
                '}';
    }
}
