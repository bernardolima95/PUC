import java.util.Arrays;

public class CicloHamiltoniano {

        private int     V, pathCount;
        private int[]   path;
        private Rota[][] graph;

        /** Function to find cycle **/
        public void findHamiltonianCycle(MatrizInc grafo) {
            Rota[][] g = grafo.getMatriz();
            V = g.length;
            path = new int[V];
            Arrays.fill(path, -1);
            graph = g;
            try
            {
                path[0] = 0;
                pathCount = 1;
                System.out.println("Me fode");
                solve(0);
                System.out.println("No solution");
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                display();
            }
        }

        /** function to find paths recursively **/
        public void solve(int vertex){
            /** solution **/
            if (graph[vertex][0].getPreco() >= 1 && pathCount == V) {
                System.out.println("Entrou");
            }
            /** all vertices selected but last vertex not linked to 0 **/
            if (pathCount == V) {
                System.out.println("Fezzz");
                return;
            }
            for (int v = 0; v < V; v++) {
                /** if connected **/
                System.out.println("Fez");
                if (graph[vertex][v].getPreco() >= 1) {
                    /** add to path **/
                    path[pathCount++] = v;
                    /** remove connection **/
                    graph[vertex][v].setPreco(0);
                    graph[v][vertex].setPreco(0);
                    /** if vertex not already selected solve recursively **/
                    if (!isPresent(v))
                        solve(v);
                    /** restore connection **/
                    graph[vertex][v].setPreco(1);
                    graph[v][vertex].setPreco(1);
                    /** remove path **/
                    path[--pathCount] = -1;
                    Grafos.imprimeMatriz(graph);
                }
            }

            Grafos.imprimeMatriz(graph);
        }

        /** function to check if path is already selected **/
        public boolean isPresent(int v) {
            for (int i = 0; i < pathCount - 1; i++) {
                if (path[i] == v)
                    return true;
            }
            return false;
        }

        /** display solution **/
        public void display()
        {
            System.out.print("\nPath : ");
            for (int i = 0; i <= V; i++)
                System.out.print(path[i % V] + " ");
            System.out.println();
        }
}
