package aed3;

public class Ranking {

    protected String nome;
    protected int vendas;

    public Ranking(String nome, int vendas) {
        this.nome = nome;
        this.vendas = vendas;
    }

    public Ranking() {
        this.nome = "";
        this.vendas = 0;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getVendas() {
        return vendas;
    }

    public void setVendas(int vendas) {
        this.vendas = vendas;
    }

    public String toString(){
        return "Nome: "+this.nome+
                "| Vendas: " +this.vendas;
    }

    public void aumentaVenda(int qtd) {
        this.vendas = this.vendas + qtd;
    }
}
