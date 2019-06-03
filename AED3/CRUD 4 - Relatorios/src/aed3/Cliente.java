package aed3;

import java.io.*;

public class Cliente implements aed3.Entidade {

    protected int id;
    protected String nome;
    protected String email;

    public Cliente(String nome, String email){
        this.id = -1;
        this.nome = nome;
        this.email = email;
    }

    public Cliente(){
        this.id = -1;
        this.nome = " ";
        this.email = " ";
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getNome(){
        return this.nome;
    }

    public String toString() {
        return "Id Cliente: " + this.id +
                "\nNome: " + this.nome +
                "\nEmail: " + this.email;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream(dados);
        saida.writeInt(this.id);
        saida.writeUTF(this.nome);
        saida.writeUTF(this.email);

        return dados.toByteArray();
    }

    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream dados = new ByteArrayInputStream(b);
        DataInputStream entrada = new DataInputStream(dados);

        this.id = entrada.readInt();
        this.nome = entrada.readUTF();
        this.email = entrada.readUTF();
    }

}
