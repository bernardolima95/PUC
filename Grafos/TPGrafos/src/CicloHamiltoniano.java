import java.util.ArrayList;
import java.util.Arrays;

class CicloHamiltoniano {
        private int     V, contadorCaminho;
        private int[]   caminho;
        private int[][] grafo;
        public ArrayList<String> trajeto = new ArrayList<String>();
        
    public boolean encontraCH(int[][] g) {
        V = g.length;
        caminho = new int[V];
        Arrays.fill(caminho, -1);
        grafo = g;

        try {
            caminho[0] = 0;
            contadorCaminho = 1;
            calcula(0);
            System.out.println("Sem solução.");
            return false;
        }
        catch (Exception e) {
//                imprime();
            return true;
        }
    }

    public ArrayList<String> trazCH(int[][] g) {
        long startTime = System.currentTimeMillis();
        V = g.length;
        caminho = new int[V];
        Arrays.fill(caminho, -1);
        grafo = g;

        try {
            caminho[0] = 0;
            contadorCaminho = 1;
            calcula(0);
            System.out.println("Não há solução.");
            return trajeto;
        }
        catch (Exception e) {
            imprime();
            long endTime   = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            System.out.println("Tempo Hamiltoniano:" +totalTime);
            return trajeto;
        }
    }

        public void calcula(int vertice) throws Exception {

            if (grafo[vertice][0] == 1 && contadorCaminho == V) throw new Exception("Ciclo encontrado.");

            if (contadorCaminho == V) return; //Chega ao fim sem encontrar ciclo.

            for (int v = 0; v < V; v++) {
                if (grafo[vertice][v] == 1) {
                    caminho[contadorCaminho++] = v;

                    grafo[vertice][v] = 0;
                    grafo[v][vertice] = 0;

                    if (!visitado(v)) calcula(v);

                    grafo[vertice][v] = 1;
                    grafo[v][vertice] = 1;

                    caminho[--contadorCaminho] = -1;
                }
            }
        }


        public boolean visitado(int v) {
            for (int i = 0; i < contadorCaminho - 1; i++)
                if (caminho[i] == v)
                    return true;
            return false;
        }

        public ArrayList<String> imprime() {
            System.out.print("\nCaminho Hamiltoniano Encontrado: ");
            for (int i = 0; i <= V; i++) {
                System.out.print(Grafos.descobreNome(caminho[i % V]) + "->");
                trajeto.add(Grafos.descobreNome(caminho[i % V]));
            }
            System.out.println();
            return trajeto;
        }
}