package aed3;

import java.io.*;

public class Categoria implements aed3.Entidade {

    protected int id;
    protected String nomeEnt;

    public Categoria(String nomeEnt){
        this.id = -1;
        this.nomeEnt = nomeEnt;
    }

    public Categoria(){
        this.id = -1;
        this.nomeEnt = " ";
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome(){
        return this.nomeEnt;
    }

    public int getIdC() {
        return this.id;
    }

    public String toString() {
        return "Id Categoria: " + this.id +
                "\nNome: " + this.nomeEnt;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream(dados);
        saida.writeInt(this.id);
        saida.writeUTF(this.nomeEnt);

        return dados.toByteArray();
    }

    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream dados = new ByteArrayInputStream(b);
        DataInputStream entrada = new DataInputStream(dados);

        this.id = entrada.readInt();
        this.nomeEnt = entrada.readUTF();
    }

}
