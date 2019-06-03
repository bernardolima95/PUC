package aed3;
import java.io.*;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class Arquivo<T extends Entidade> {

    private RandomAccessFile arquivo;
    private String nomeArquivo;
    private String nomeIndice;
    private Constructor<T> construtor;
    private Indice indice;

    public Arquivo(Constructor<T> c, String nomeArq, String nomeIdx) throws IOException {
        this.nomeArquivo = nomeArq;
        this.nomeIndice = nomeIdx;
        this.construtor = c;
        arquivo = new RandomAccessFile(this.nomeArquivo, "rw");
        indice = new Indice(20, this.nomeIndice);
        if(arquivo.length()<4) {
            arquivo.writeInt(0);
            indice.apagar();
        }
    }

    public int incluir(T obj) throws IOException {

        int ultimoID;
        arquivo.seek(0);
        ultimoID = arquivo.readInt();
        arquivo.seek(0);
        arquivo.writeInt(ultimoID+1);
        obj.setId(ultimoID+1);

        arquivo.seek(arquivo.length());
        long endereco = arquivo.getFilePointer();
        byte[] b;
        b = obj.toByteArray();
        arquivo.writeByte(' ');
        arquivo.writeShort(b.length);
        arquivo.write(b);
        indice.inserir(obj.getId(), endereco);
        return obj.getId();
    }

    public int incluirAlt(T obj, int id) throws IOException {

        int ultimoID;
        arquivo.seek(0);
        ultimoID = arquivo.readInt();
        arquivo.seek(0);
        arquivo.writeInt(ultimoID);
        obj.setId(id);

        arquivo.seek(arquivo.length());
        long endereco = arquivo.getFilePointer();
        byte[] b;
        b = obj.toByteArray();
        arquivo.writeByte(' ');
        arquivo.writeShort(b.length);
        arquivo.write(b);
        indice.inserir(obj.getId(), endereco);
        return obj.getId();
    }

    public T ler(int id) throws Exception {
        T obj = construtor.newInstance();
        short tamanho;
        byte[] b;
        byte lapide;

        long endereco = indice.buscar(id);
        if(endereco != -1) {
            arquivo.seek(endereco);
            lapide = arquivo.readByte();
            tamanho = arquivo.readShort();
            b = new byte[tamanho];
            arquivo.read(b);
            obj.fromByteArray(b);
            return obj;
        }
        return null;
    }

    public boolean excluir(int id) throws Exception {
        T obj = construtor.newInstance();
        short tamanho;
        byte[] b;
        byte lapide;
        long endereco;

        endereco = indice.buscar(id);
        if(endereco != -1) {
            arquivo.seek(endereco);
            arquivo.writeByte('*');
            indice.excluir(id);
            return true;
        }
        return false;
    }

    public ArrayList<T> listar() throws Exception {
        ArrayList<T> lista = new ArrayList<>();
        T obj;
        short tamanho;
        byte[] b;
        byte lapide;

        arquivo.seek(4);
        while(arquivo.getFilePointer() < arquivo.length()) {
            lapide = arquivo.readByte();
            tamanho = arquivo.readShort();
            b = new byte[tamanho];
            arquivo.read(b);
            obj = construtor.newInstance();
            obj.fromByteArray(b);
            if(lapide==' ')
                lista.add(obj);
        }
        return lista;
    }

    public boolean checaExclusao(int idCategoria) throws Exception {
        Produto obj = new Produto();
        short tamanho;
        byte[] b;
        byte lapide;
        boolean check = false;

        arquivo.seek(4);
        while (arquivo.getFilePointer() < arquivo.length()) {
            lapide = arquivo.readByte();
            tamanho = arquivo.readShort();
            b = new byte[tamanho];
            arquivo.read(b);
            obj.fromByteArray(b);
            if (lapide == ' ' && obj.idCategoria == idCategoria) {
                check = true;
            }
        }
        return check;
    }

    public boolean checaExclusaoCliente(int idC) throws Exception {
        Compra obj = new Compra();
        short tamanho;
        byte[] b;
        byte lapide;
        boolean check = false;

        arquivo.seek(4);
        while (arquivo.getFilePointer() < arquivo.length()) {
            lapide = arquivo.readByte();
            tamanho = arquivo.readShort();
            b = new byte[tamanho];
            arquivo.read(b);
            obj.fromByteArray(b);
            if (lapide == ' ' && obj.idC == idC) {
                check = true;
            }
        }
        return check;
    }

    public boolean checaExclusaoCompra(int idComp) throws Exception {
        ProdutoComprado obj = new ProdutoComprado();
        short tamanho;
        byte[] b;
        byte lapide;
        boolean check = false;

        arquivo.seek(4);
        while (arquivo.getFilePointer() < arquivo.length()) {
            lapide = arquivo.readByte();
            tamanho = arquivo.readShort();
            b = new byte[tamanho];
            arquivo.read(b);
            obj.fromByteArray(b);
            if (lapide == ' ' && obj.idComp == idComp) {
                check = true;
            }
        }
        return check;
    }

    public boolean checaExclusaoProduto(int idProd) throws Exception {
        ProdutoComprado obj = new ProdutoComprado();
        short tamanho;
        byte[] b;
        byte lapide;
        boolean check = false;

        arquivo.seek(4);
        while (arquivo.getFilePointer() < arquivo.length()) {
            lapide = arquivo.readByte();
            tamanho = arquivo.readShort();
            b = new byte[tamanho];
            arquivo.read(b);
            obj.fromByteArray(b);
            if (lapide == ' ' && obj.idProd == idProd) {
                check = true;
            }
        }
        return check;
    }

    public void listagemProdutos(int idCategoria) throws Exception {
        Produto obj = new Produto();
        short tamanho;
        byte[] b;
        byte lapide;

        arquivo.seek(4);
        while (arquivo.getFilePointer() < arquivo.length()) {
            lapide = arquivo.readByte();
            tamanho = arquivo.readShort();
            b = new byte[tamanho];
            arquivo.read(b);
            obj.fromByteArray(b);
            if (lapide == ' ' && obj.idCategoria == idCategoria) {
                System.out.println(obj.getNome());
            }
        }
    }

}

