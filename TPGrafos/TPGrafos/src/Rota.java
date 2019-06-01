public class Rota {

    private String aeroporto1;
    private String aeroporto2;
    private int preco;

    public String getAeroporto1() {
        return aeroporto1;
    }

    public String getAeroporto2() {
        return aeroporto2;
    }

    public void setAeroporto1(String aeroporto1) {
        this.aeroporto1 = aeroporto1;
    }

    public void setAeroporto2(String aeroporto2) {
        this.aeroporto2 = aeroporto2;
    }

    public int getPreco() {
        return preco;
    }

    public void setPreco(int preco) {
        this.preco = preco;
    }

    public Rota(String aeroporto1, String aeroporto2, int preco) {
        this.aeroporto1 = aeroporto1;
        this.aeroporto2 = aeroporto2;
        this.preco = preco;
    }

    public Rota(){
        this.aeroporto1 = "";
        this.aeroporto2 = "";
        this.preco = 0;
    }
}
