public class MatrizInc {
    private int lin;
    private int col;
    private Rota[][] matriz;
    private static int[][] matrizIncidencia;
    private static int[][] matrizAdj;

    public void setMatriz(int l, int c) {
        this.lin = l;
        this.col = c;
        this.matriz = new Rota[lin][col];
        this.matrizIncidencia = new int[lin][col];
        this.matrizAdj = new int[lin][col];
    }

    public MatrizInc(){
        lin = 0;
        col = 0;
    }

    public void setAresta(int v1, int v2, Rota rota) {
        try {
            matriz[v1][v2] = new Rota(rota.getAeroporto1(), rota.getAeroporto2(), rota.getPreco());
            matrizIncidencia[v1][v2] = 1;
            matrizAdj[v1][v2] = rota.getPreco();
        } catch (ArrayIndexOutOfBoundsException index) {
            System.out.println("Vertices inexistentes.");
        }
    }

    public  Rota[][] getMatriz() {
        return matriz;
    }

    public static int[][] getMatrizIncidencia(){
        return matrizIncidencia;
    }

    public static int[][] getMatrizAdj(){
        return matrizAdj;
    }

    public void restauraMatrizIncidencia(){
        for (int i = 0; i < lin; i ++){
            for (int j = 0; j < col; j++) {
                if (matriz[i][j] != null) matrizIncidencia[i][j] = 1;
            }
        }
    }
}