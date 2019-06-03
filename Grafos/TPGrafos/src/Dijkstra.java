import java.util.*; 
import java.lang.*;

public class Dijkstra {

    private static final int Fim = -1;
    private int V;
    private static ArrayList<String> trajeto = new ArrayList<String>();

    public Dijkstra(int num) {
        this.V = num;
    }

    int menorDistancia(Rota dist[], Boolean marcado[]) {
            int min = Integer.MAX_VALUE, min_index = -1;

            for (int v = 0; v < V; v++)
                if (marcado[v] == false && dist[v].getPreco() <= min) {
                    min = dist[v].getPreco();
                    min_index = v;
                }

            return min_index;
        }

    Rota[] inicializaDist(){
        Rota dist[] = new Rota[V];
        for (int i = 0; i < V; i++){
            dist[i] = new Rota();
            dist[i].setAeroporto2(Grafos.descobreNome(i));
        }

        return dist;
    }

    // Calcula o algoritmo de Dijkstra e devolve o trajeto.
    ArrayList<String> calcula(MatrizInc grafo, int inicial, int alvo) {
        trajeto.clear();
        Rota dist[] = inicializaDist();
        int[] caminho = new int[V];
        Boolean marcado[] = new Boolean[V]; //Vetor boolean que "marca" o vetor como visitado

        for (int i = 0; i < V; i++) {//Inicializa as distâncias do vetor de distancia como infinitas.
            dist[i].setPreco(Integer.MAX_VALUE);
            marcado[i] = false;
        }

        dist[inicial].setPreco(0);
        caminho[inicial] = Fim;
        dist[inicial].setAeroporto1(Grafos.descobreNome(inicial));//Adiciona o nome da origem ao trajeto.

        //Acha o caminho mais curto por todos os vértices
        for (int count = 0; count < V; count++) {

            int u = menorDistancia(dist, marcado); // Pega a menor distância entre o inicial e o marcado.
            marcado[u] = true;

            for (int v = 0; v < V; v++)//Atualiza o valor da distancia do vertice escolhido aos adjacentes.

                //Atualiza se dist[v] não está marcado E
                //há uma aresta entre u e v E
                //o caminho total da origem ate v atraves de u é menos que o valor de dist[v].
                if (!marcado[v] &&
                    (grafo.getMatriz()[u][v] != null && grafo.getMatriz()[u][v].getPreco() != 0) &&
                     dist[u].getPreco() != Integer.MAX_VALUE &&
                     dist[u].getPreco() + grafo.getMatriz()[u][v].getPreco() < dist[v].getPreco()){

                    dist[v].setPreco(dist[u].getPreco() + grafo.getMatriz()[u][v].getPreco());
                    System.out.println(dist[u].getAeroporto1() + " " + dist[v].getPreco());
                    caminho[v] = u;
                }
        }

        imprimeDijkstra(inicial, dist, caminho, alvo);
        System.out.println("\nTrajeto solicitado: "+trajeto);
        return trajeto;
    }

    private void imprimeDijkstra(int inicial, Rota dist[], int[] caminho, int alvo){
        System.out.print("-----------DIJKSTRA-----------\nVertice\t Distancia\tCaminho");

        for (int vertice = 0; vertice < V; vertice++) {
            if (vertice != inicial) {
                System.out.print("\n" + Grafos.descobreNome(inicial) + " -> ");
                System.out.print(Grafos.descobreNome(vertice) + " \t\t ");
                System.out.print("\t"+dist[vertice].getPreco()+"\t\t");
                if (vertice == alvo) {
                    salvaTrajeto(vertice, caminho);
                }
                imprimeCaminho(vertice, caminho);
            }
        }
    }
    
    private void imprimeCaminho(int verticeAtual, int[] caminho) {
        if (verticeAtual == Fim){
            return;
        }
        imprimeCaminho(caminho[verticeAtual], caminho);
        System.out.print(Grafos.descobreNome(verticeAtual) + ", ");
    }

    private void salvaTrajeto(int verticeAtual, int[] caminho) {
        if (verticeAtual == Fim){
            return;
        }
        salvaTrajeto(caminho[verticeAtual], caminho);
        trajeto.add(Grafos.descobreNome(verticeAtual));
    }
}

