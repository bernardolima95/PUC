package aed3;

import java.io.*;

public class ProdutoComprado implements Entidade {

        protected int id;
        protected int idComp;
        protected int idProd;
        protected int qtd;
        protected float precoUnit;


        public ProdutoComprado(int idComp, int idProd, int qtd, float precoUnit) {
            this.id = -1;
            this.idComp = idComp;
            this.idProd = idProd;
            this.qtd = qtd;
            this.precoUnit = precoUnit;
        }

    public int getQtd() {
        return qtd;
    }

    public int getIdComp() {
        return idComp;
    }

    public int getIdProd() {
        return idProd;
    }

    public ProdutoComprado() {
            this.id = -1;
            this.idComp = -1;
            this.idProd = -1;
            this.qtd = -1;
            this.precoUnit = -1;
        }

        public int getId(){
            return this.id;
        }

        public void setId(int id){
            this.id = id;
        }

        public String toString() {
            return "Id ProdComp: " + this.id +
                    "\nId Compra: " + this.idComp +
                    "\nId Prod: " + this.idProd +
                    "\nQuantidade: " + this.qtd +
                    "\nPreco Unidade: " + this.precoUnit;
        }

        public byte[] toByteArray() throws IOException {
            ByteArrayOutputStream dados = new ByteArrayOutputStream();
            DataOutputStream saida = new DataOutputStream(dados);
            saida.writeInt(this.id);
            saida.writeInt(this.idComp);
            saida.writeInt(this.idProd);
            saida.writeInt(this.qtd);
            saida.writeFloat(this.precoUnit);

            return dados.toByteArray();
        }

        public void fromByteArray(byte[] b) throws IOException {
            ByteArrayInputStream dados = new ByteArrayInputStream(b);
            DataInputStream entrada = new DataInputStream(dados);

            this.id = entrada.readInt();
            this.idComp = entrada.readInt();
            this.idProd = entrada.readInt();
            this.qtd = entrada.readInt();
            this.precoUnit = entrada.readFloat();
        }
}

