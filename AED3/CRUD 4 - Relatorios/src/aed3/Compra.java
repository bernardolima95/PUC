package aed3;

import java.io.*;

public class Compra implements Entidade{
    protected int id;
    protected int idC;
    protected String dataCompra;
    protected float precoCompra;

    public Compra(int idC, String dataCompra, float precoCompra){
        this.id = -1;
        this.idC = idC;
        this.dataCompra = dataCompra;
        this.precoCompra = precoCompra;
    }

    public int getIdC() {
        return idC;
    }

    public Compra(){
        this.id = -1;
        this.idC = -1;
        this.dataCompra = " ";
        this.precoCompra = -1;
    }

    public int getId(){
        return this.id;
    }

    public String getDataCompra() {
        return dataCompra;
    }

    public void setId(int id){
        this.id = id;
    }

    public String toString() {
        return "Id Compra: " + this.id +
                "\nId Cliente: " + this.idC +
                "\nData Compra: " + this.dataCompra +
                "\nPreco Compra: " + this.precoCompra;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream(dados);
        saida.writeInt(this.id);
        saida.writeInt(this.idC);
        saida.writeUTF(this.dataCompra);
        saida.writeFloat(this.precoCompra);

        return dados.toByteArray();
    }

    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream dados = new ByteArrayInputStream(b);
        DataInputStream entrada = new DataInputStream(dados);

        this.id = entrada.readInt();
        this.idC = entrada.readInt();
        this.dataCompra = entrada.readUTF();
        this.precoCompra = entrada.readFloat();
    }
}
