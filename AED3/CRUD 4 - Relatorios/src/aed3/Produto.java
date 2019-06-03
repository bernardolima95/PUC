package aed3;
import java.io.*;

public class Produto implements aed3.Entidade {

    protected int id;
    protected int idCategoria;
    protected String nomeCategoria;
    protected String nome;
    protected String descricao;
    protected short qtd;
    protected float preco;

    public Produto(String nome, int idCategoria, String nomeCategoria, String descricao, short qtd, float preco) {
        this.id = -1;
        this.idCategoria = idCategoria;
        this.nome = nome;
        this.nomeCategoria = nomeCategoria;
        this.descricao = descricao;
        this.qtd = qtd;
        this.preco = preco;
    }

    public Produto() {
        this.id = -1;
        this.idCategoria = -1;
        this.nome = "";
        this.nomeCategoria = "";
        this.descricao = "";
        this.qtd = 0;
        this.preco = 0;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdC() {
        return this.idCategoria;
    }

    public float getPreco() {
        return preco;
    }

    public String getNome(){
        return this.nome;
    }

    public String toString() {
        return "Id: " + this.id +
                    "\nNome: " + this.nome +
                    "\nDescricao: " + this.descricao +
                    "\nQtd: " + this.qtd + " unidades." +
                    "\nPreço: R$" + this.preco +
                    "\nCategoria: " + this.nomeCategoria;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream(dados);

        saida.writeInt(this.id);
        saida.writeInt(this.idCategoria);
        saida.writeUTF(this.nomeCategoria);
        saida.writeUTF(this.nome);
        saida.writeUTF(this.descricao);
        saida.writeShort(this.qtd);
        saida.writeFloat(this.preco);

        return dados.toByteArray();
    }

    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream dados = new ByteArrayInputStream(b);
        DataInputStream entrada = new DataInputStream(dados);

        this.id = entrada.readInt();
        this.idCategoria = entrada.readInt();
        this.nomeCategoria = entrada.readUTF();
        this.nome = entrada.readUTF();
        this.descricao = entrada.readUTF();
        this.qtd = entrada.readShort();
        this.preco = entrada.readFloat();
    }

}
