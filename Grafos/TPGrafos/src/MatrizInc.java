public class MatrizInc {
    private int lin;
    private int col;
    private Rota[][] matriz;

    public void setMatriz(int l, int c) {
        this.lin = l;
        this.col = c;
        this.matriz = new Rota[lin][col];
    }

    public MatrizInc(){
        lin = 0;
        col = 0;
    }

    public void setAresta(int v1, int v2, Rota rota) {
        try {
            matriz[v1][v2] = new Rota(rota.getAeroporto1(), rota.getAeroporto2(), rota.getPreco());
        } catch (ArrayIndexOutOfBoundsException index) {
            System.out.println("Vertices inexistentes.");
        }
    }

    public Rota[][] getMatriz() {
        return matriz;
    }

    public Rota getAresta(int v1, int v2) {
        try {
            return matriz[v1][v2];
        } catch (ArrayIndexOutOfBoundsException index) {
            System.out.println("Aresta inexistente.");
        }
        return null;
    }
}