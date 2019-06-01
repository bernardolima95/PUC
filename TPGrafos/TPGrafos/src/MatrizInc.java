public class MatrizInc {
    private int lin;
    private int col;
    private int[][] matriz;

    public void setMatriz(int l, int c) {
        this.lin = l;
        this.col = c;
        this.matriz = new int[lin + 1][col + 1];
    }

    public MatrizInc(){
        lin = 0;
        col = 0;
    }

    public void setAresta(int v1, int v2, int valor) {
        try {
            matriz[v1][v2] = valor;
        } catch (ArrayIndexOutOfBoundsException index) {
            System.out.println("Vertices inexistentes.");
        }
    }

    public int[][] getMatriz() {
        return matriz;
    }

    public int getAresta(int v1, int v2) {
        try {
            return matriz[v1][v2];
        } catch (ArrayIndexOutOfBoundsException index) {
            System.out.println("Aresta inexistente.");
        }
        return -1;
    }
}