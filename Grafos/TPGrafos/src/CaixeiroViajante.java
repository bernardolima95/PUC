import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

public class CaixeiroViajante {

        private int numberOfNodes;
        private Stack<Integer> stack;
        private ArrayList<String> trajeto = new ArrayList<String>();

        public CaixeiroViajante() {
            stack = new Stack<Integer>();
        }

        public ArrayList<String> tsp(MatrizInc MI) {
            Rota[][] adjacencyMatrix = MI.getMatriz();
            numberOfNodes = adjacencyMatrix[1].length - 1;
            int[] visited = new int[numberOfNodes + 1];
            visited[1] = 1;
            stack.push(1);
            int element, dst = 0, i;
            int min = Integer.MAX_VALUE;
            boolean minFlag = false;
            System.out.print(1 + "\t");

            while (!stack.isEmpty()) {
                element = stack.peek();
                i = 1;
                min = Integer.MAX_VALUE;
                while (i <= numberOfNodes) {
                    if (adjacencyMatrix[element][i] != null && adjacencyMatrix[element][i].getPreco() > 1 && visited[i] == 0) {
                        if (min > adjacencyMatrix[element][i].getPreco()) {
                            min = adjacencyMatrix[element][i].getPreco();
                            dst = i;
                            minFlag = true;
                        }
                    }
                    i++;
                }
                if (minFlag) {
                    visited[dst] = 1;
                    stack.push(dst);
                    trajeto.add(Grafos.descobreNome(dst));
//                    System.out.print(dst + "\t");
                    minFlag = false;
                    continue;
                }
                stack.pop();
            }
            return trajeto;
        }
}
