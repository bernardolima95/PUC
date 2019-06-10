import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Grafos {

    private JPanel Mapa, panel;
    private JPanel digite;
    private JButton menorTarifa;
    private JTextField nomeAero1, nomeAero2;
    private JLabel jl;
    private JTextArea textArea1;
    private JLabel dest;
    private JLabel orig;
    private JButton menorDistancia;
    private JButton caixeiroViajante;
    public static ArrayList<Aeroporto> aeroportos = new ArrayList<>();
    public static ArrayList<Rota> rotas = new ArrayList<>();
    public static MatrizInc matrizDis = new MatrizInc();
    public static MatrizInc matrizInc = new MatrizInc();
    private static double w = 1920;
    private static double h = 967;
    public static int nAeroportos;


    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Grafos");
        frame.setContentPane(new Grafos().Mapa);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        imprimeLista(aeroportos);
        imprimeMatriz(matrizInc.getMatriz());
        imprimeMatriz(matrizInc.getMatrizIncidencia());
        imprimeMatriz(matrizDis.getMatriz());
    }

    public Grafos() throws IOException {
        menorTarifa.addActionListener(new ActionListener() { //Calcula o trajeto de menor tarifa de a para b
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome1 = nomeAero1.getText();
                String nome2 = nomeAero2.getText();
                ArrayList<String> trajeto = new ArrayList<String>();
                ArrayList<Rota> rotasTrajeto = new ArrayList<Rota>();
                trajeto = encontraDijsktra(nome1, nome2, 1);

                textArea1.setText(trajeto.toString());
                rotasTrajeto = formaRotas(trajeto);

                try {
                    BufferedImage atualizada = ImageIO.read(new File("src/original.jpg"));
                    for (int i = 0; i < rotasTrajeto.size(); i++) {
                        desenharTrajeto(atualizada, rotasTrajeto.get(i));
                    }
                    Image dimg = atualizada.getScaledInstance(1200, 800, Image.SCALE_SMOOTH);
                    ImageIcon iconNew = new ImageIcon(dimg);
                    ImageIO.write(atualizada, "jpg", new File("src/atualizada.jpg"));
                    jl.setIcon(iconNew);
                } catch (IOException f) {
                    f.printStackTrace();
                }
            }
        });

        menorDistancia.addActionListener(new ActionListener() { //Calcula o trajeto de menor distancia de a para b
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome1 = nomeAero1.getText();
                String nome2 = nomeAero2.getText();
                ArrayList<String> trajeto = new ArrayList<String>();
                ArrayList<Rota> rotasTrajeto = new ArrayList<Rota>();
                trajeto = encontraDijsktra(nome1, nome2, 2);

                textArea1.setText(trajeto.toString());
                rotasTrajeto = formaRotas(trajeto);

                try {
                    BufferedImage atualizada = ImageIO.read(new File("src/original.jpg"));
                    for (int i = 0; i < rotasTrajeto.size(); i++) {
                        desenharTrajeto(atualizada, rotasTrajeto.get(i));
                    }
                    Image dimg = atualizada.getScaledInstance(1200, 800, Image.SCALE_SMOOTH);
                    ImageIcon iconNew = new ImageIcon(dimg);
                    ImageIO.write(atualizada, "jpg", new File("src/atualizada.jpg"));
                    jl.setIcon(iconNew);
                } catch (IOException f) {
                    f.printStackTrace();
                }
            }
        });

        caixeiroViajante.addActionListener(new ActionListener() { //Calcula o menor subconjunto que passa por todos os vértices
            @Override
            public void actionPerformed(ActionEvent e) {

                ArrayList<String> trajeto = new ArrayList<String>();
                ArrayList<Rota> rotasTrajeto = new ArrayList<Rota>();

                if (encontraHamiltoniano()) { //Verifica se o cálculo é possível, procurando pela existência de um ciclo hamiltoniano
                    CicloHamiltoniano ch = new CicloHamiltoniano();
                    trajeto = ch.trazCH(MatrizInc.getMatrizIncidencia());
                    textArea1.setText(trajeto.toString());
                    matrizInc.restauraMatrizIncidencia();
                } else textArea1.setText("Não há ciclo hamiltoniano no grafo.");

                rotasTrajeto = formaRotas(trajeto);

                try {
                    BufferedImage atualizada = ImageIO.read(new File("src/original.jpg"));
                    for (int i = 0; i < rotasTrajeto.size(); i++) {
                        desenharTrajeto(atualizada, rotasTrajeto.get(i));
                    }
                    Image dimg = atualizada.getScaledInstance(1200, 800, Image.SCALE_SMOOTH);
                    ImageIcon iconNew = new ImageIcon(dimg);
                    ImageIO.write(atualizada, "jpg", new File("src/atualizada.jpg"));
                    jl.setIcon(iconNew);
                } catch (IOException f) {
                    f.printStackTrace();
                }
            }
        });

        textArea1.setText("Digite o nome dos aeroportos nas caixas à esquerda.");
        BufferedImage img = ImageIO.read(new File("src/equiretangular.jpg"));
        aeroportos = leArquivo("src/aeroportos.txt");
        leRotas("src/rotas.txt");
        long startTime = System.currentTimeMillis();
        defineAltitude(); // Define a altitude entre as rotas
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Tempo Altitude:" + totalTime);

        for (int i = 0; i < aeroportos.size(); i++) { //Desenha todos os vértices
            desenharPontos(img, aeroportos.get(i));
        }
        for (int k = 0; k < rotas.size(); k++) { //Desenha todas as arestas
            desenharRotas(img, rotas.get(k));
        }

        Image dimg = img.getScaledInstance(1200, 800, Image.SCALE_SMOOTH);
        ImageIO.write(img, "jpg", new File("src/original.jpg")); //Salva a imagem para uso futuro
        ImageIcon icon = new ImageIcon(dimg);
        jl.setIcon(icon);

    }

    //Método que desenha um vértice a partir de um objeto Aeroporto
    public static BufferedImage desenharPontos(BufferedImage imagem, Aeroporto aeroporto) throws IOException {
        double x = aeroporto.getX();
        double y = aeroporto.getY();

        Graphics2D g2d = imagem.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial Black", Font.PLAIN, 8));

        Ellipse2D.Double shape = new Ellipse2D.Double(x, y, 30, 30);
        g2d.drawString(aeroporto.getNome(), (float) x + 6, (float) y + 16);
        g2d.draw(shape);
        return imagem;
    }

    /*Método que desenha uma linha representando uma aresta entre dois vértices,
    *também inicializa as matrizes de incidência para distância e preço.
    * */
    public static BufferedImage desenharRotas(BufferedImage imagem, Rota rota) throws IOException {
        double x1, x2, y1, y2;
        x1 = x2 = y1 = y2 = 0;

        double[] rotacao = new double[3];

        Aeroporto a = new Aeroporto();
        Aeroporto b = new Aeroporto();
        Rota copia = new Rota(rota.getAeroporto1(), rota.getAeroporto2(), 0);


        int m, n, preco;
        m = n = 0;

        Graphics2D g2d = imagem.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial Black", Font.PLAIN, 11));

        for (int i = 0; i < aeroportos.size(); i++) {
            if (rota.getAeroporto1().equals(aeroportos.get(i).getNome())) {
                x1 = aeroportos.get(i).getX();
                y1 = aeroportos.get(i).getY();
                a = aeroportos.get(i);
                m = i;
            }
            if (rota.getAeroporto2().equals(aeroportos.get(i).getNome())) {
                x2 = aeroportos.get(i).getX();
                y2 = aeroportos.get(i).getY();
                b = aeroportos.get(i);
                n = i;
            }
        }


        copia.setPreco((int) distanciaAeroporto(a, b));
        matrizDis.setAresta(m, n, copia);
        matrizDis.setAresta(n, m, copia);

        //Cria as arestas da matriz de incidencia com pesos sendo os precos.
        matrizInc.setAresta(m, n, rota);
        matrizInc.setAresta(n, m, rota);

        if ((((1920 - x1) + (x2)) < (x1 - x2) && x1 > x2)) { //Para voos da Asia/Oceania para a America
            g2d.draw(new Line2D.Double(x1, y1, 1920, y2)); //Desenha do aeroporto até a beira do mapa
            rotacao = calculaRotacao(x1, y1, 1920, y2);
            g2d.rotate(rotacao[0], rotacao[1], rotacao[2]);
            g2d.drawString(rota.getAlt() + " pés", (float) rotacao[1], (float) rotacao[2] - 10);
            g2d.rotate(-rotacao[0], rotacao[1], rotacao[2]);
            g2d.draw(new Line2D.Double(0, y1, x2, y2));
        } else if (((((x1) + (1920 - x2)) < (x2 - x1)) && x2 > x1)) { //Para voos da America para a Asia/Oceania
            g2d.draw(new Line2D.Double(x1, y1, 0, y2)); //Desenha do aeroporto até a beira do mapa
            rotacao = calculaRotacao(x1, y1, 0, y2);
            g2d.rotate(rotacao[0], rotacao[1], rotacao[2]);
            g2d.drawString(rota.getAlt() + " pés", (float) rotacao[1], (float) rotacao[2] - 10);
            g2d.rotate(-rotacao[0], rotacao[1], rotacao[2]);
            g2d.draw(new Line2D.Double(1920, y2, x2, y2));
        } else {
            g2d.draw(new Line2D.Double(x1, y1, x2, y2));
            rotacao = calculaRotacao(x1, y1, x2, y2);
            g2d.rotate(rotacao[0], rotacao[1], rotacao[2]);
            g2d.drawString(rota.getAlt() + " pés", (float) rotacao[1], (float) rotacao[2] - 10);
            g2d.rotate(-rotacao[0], rotacao[1], rotacao[2]);
        }
        return imagem;
    }

    //Calcula a rotação ao redor do ângulo de uma reta, para posicionar o texto paralelo à ela
    public static double[] calculaRotacao(double x1, double y1, double x2, double y2) {
        double[] rotacao = new double[3];
        double xCentro = x1 + ((x2 - x1) / 2);
        double yCentro = y1 + ((y2 - y1) / 2);
        double deg = Math.toDegrees(Math.atan2(yCentro - y2, xCentro - x2) + Math.PI);
        if ((deg > 90) && (deg < 270)) {
            deg += 180;
        }
        double angulo = Math.toRadians(deg);

        rotacao[0] = angulo;
        rotacao[1] = xCentro;
        rotacao[2] = yCentro;

        return rotacao;
    }

    //Método que redesenha as rotas de azul quando recebe um trajeto calculado por algoritmo.
    public static BufferedImage desenharTrajeto(BufferedImage imagem, Rota rota) throws IOException {
        double x1, x2, y1, y2;
        x1 = x2 = y1 = y2 = 0;

        Graphics2D g2d = imagem.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLUE);
        g2d.setFont(new Font("Arial Black", Font.PLAIN, 8));

        for (int i = 0; i < aeroportos.size(); i++) {
            if (rota.getAeroporto1().equals(aeroportos.get(i).getNome())) {
                x1 = aeroportos.get(i).getX();
                y1 = aeroportos.get(i).getY();
            }
            if (rota.getAeroporto2().equals(aeroportos.get(i).getNome())) {
                x2 = aeroportos.get(i).getX();
                y2 = aeroportos.get(i).getY();
            }
        }


        if ((((1920 - x1) + (x2)) < (x1 - x2) && x1 > x2)) { //Para voos da Asia/Oceania para a America
            g2d.draw(new Line2D.Double(x1, y1, 1920, y2)); //Desenha do aeroporto até a beira do mapa
            g2d.draw(new Line2D.Double(0, y1, x2, y2));
        } else if (((((x1) + (1920 - x2)) < (x2 - x1)) && x2 > x1)) { //Para voos da America para a Asia/Oceania
            g2d.draw(new Line2D.Double(x1, y1, 0, y2)); //Desenha do aeroporto até a beira do mapa
            g2d.draw(new Line2D.Double(1920, y2, x2, y2));
        } else {
            g2d.draw(new Line2D.Double(x1, y1, x2, y2));
        }
        return imagem;
    }

    //Lê o arquivo aeroporto.txt e cria os objetos Aeroporto.
    public static ArrayList<Aeroporto> leArquivo(String filename) throws IOException {
        double lat, longi, x, y;
        int num;

        String nome, linha;
        ArrayList<Aeroporto> aeroportos = new ArrayList<Aeroporto>();


        BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        linha = bf.readLine();
        num = Integer.parseInt(linha);
        linha = bf.readLine();
        nAeroportos = num;
        matrizInc.setMatriz(num, num); //Cria a matriz de incidencia ao saber o numero de aeroportos.
        matrizDis.setMatriz(num, num);

        do {
            String[] separada = linha.split("\\s+");
            System.out.println(Arrays.toString(separada));
            nome = separada[0];
            lat = Double.parseDouble(separada[1]);
            longi = Double.parseDouble(separada[2]);
            x = achaCoordX(longi);
            y = achaCoordY(lat);
            Aeroporto atual = new Aeroporto(nome, lat, longi, x, y);
            aeroportos.add(atual);
        } while ((linha = bf.readLine()) != null);

        return aeroportos;
    }

    //Encontra as coordenadas x de pixel a partir da longitude
    public static double achaCoordX(double lon) {
        return ((lon + 180) * (w / 360));
    }

    //Encontra as coordenadas y de pixel a partir da latitude
    public static double achaCoordY(double lat) {
        return (((lat * -1) + 90) * (h / 180));
    }

    //Lê o arquivo de rotas e cria os objetos Rota
    public static void leRotas(String filename) throws IOException {
        int num;
        String nome1, nome2, linha;
        int preco;

        ArrayList<ArrayList<Aeroporto>> matrizAdj = new ArrayList<ArrayList<Aeroporto>>();
        ArrayList<Aeroporto> conectados = new ArrayList<Aeroporto>();

        BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        linha = bf.readLine();

        num = Integer.parseInt(linha);

        linha = bf.readLine();

        do {
            String[] separada = linha.split("\\s+");
            System.out.println(Arrays.toString(separada));
            nome1 = separada[0];
            nome2 = separada[1];
            preco = Integer.parseInt(separada[2]);

            Rota atual = new Rota(nome1, nome2, preco);

            rotas.add(atual);
        } while ((linha = bf.readLine()) != null);
    }

    //Calcula a distancia entre aeroportos usando a formula de distancia de ponto a ponto
    public static double distanciaAeroporto(Aeroporto a, Aeroporto b) {
        double x1 = a.getLat();
        double x2 = b.getLat();
        double y1 = a.getLong();
        double y2 = b.getLong();

        double ac = Math.abs(y2 - y1);
        double cb = Math.abs(x2 - x1);
        return Math.hypot(ac, cb); //Projeta o triangulo e calcula a hipotenusa.
    }

    public static ArrayList<String> encontraDijsktra(String nome1, String nome2, int cod) {
        int aero1 = descobreCodigo(nome1);
        int aero2 = descobreCodigo(nome2);
        ArrayList<String> trajeto = new ArrayList<String>();

        if (aero1 != -1 && aero2 != -1) {
            Dijkstra f = new Dijkstra(nAeroportos);
            if (cod == 1) trajeto = f.calcula(matrizInc, aero1, aero2);
            if (cod == 2) trajeto = f.calcula(matrizDis, aero1, aero2);
        }

        return trajeto;
    }

    //Função que busca pelo ciclo hamiltonia em uma matriz de incidencia.
    public boolean encontraHamiltoniano() {
        boolean ciclo = false;

        CicloHamiltoniano ch = new CicloHamiltoniano();
        ciclo = ch.encontraCH(matrizInc.getMatrizIncidencia());
        matrizInc.restauraMatrizIncidencia();

        return ciclo;
    }

    public static void imprimeMatriz(Rota[][] matriz) {
        System.out.print("[");
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz.length; j++) {
                if (matriz[i][j] != null) System.out.print(matriz[i][j].getPreco() + ", ");
                else System.out.print("n/a, ");
            }
            System.out.print("]");
            System.out.println();
        }
    }

    public static void imprimeMatriz(int[][] matriz) {
        System.out.print("[");
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz.length; j++) {
                System.out.print(matriz[i][j] + ", ");
            }
            System.out.print("]");
            System.out.println();
        }
    }

    public static void imprimeLista(ArrayList<Aeroporto> aeroportos) {
        for (int i = 0; i < aeroportos.size(); i++) {
            System.out.println(aeroportos.get(i).toString());
        }
    }

    public static int descobreCodigo(String nome) {
        int aero = -1;
        for (int i = 0; i < aeroportos.size(); i++) {
            if (aeroportos.get(i).getNome().equals(nome)) aero = i;
        }

        return aero;
    }

    public static String descobreNome(int cod) {
        String aero = "";
        for (int i = 0; i < aeroportos.size(); i++) {
            if (i == cod) aero = aeroportos.get(i).getNome();
        }

        return aero;
    }

    public static ArrayList<Rota> formaRotas(ArrayList<String> trajeto) {
        ArrayList<Rota> rotasTrajeto = new ArrayList<Rota>();
        Rota r = new Rota();
        for (int i = 0; i < trajeto.size() - 1; i++) {
            r = new Rota();
            r.setAeroporto1(trajeto.get(i));
            r.setAeroporto2(trajeto.get(i + 1));
            rotasTrajeto.add(r);
        }
        return rotasTrajeto;
    }

    //Define a altitude entre rotas através de várias iterações.
    public static void defineAltitude() {
        double x1, y1, x2, y2;
        x1 = y1 = x2 = y2 = 0;
        double xx1, yy1, xx2, yy2;
        xx1 = yy1 = xx2 = yy2 = 0;


        for (int j = 0; j < rotas.size(); j++) { //Pega uma rota de todas as rotas
            for (int i = 0; i < aeroportos.size(); i++) {
                if (rotas.get(j).getAeroporto1().equals(aeroportos.get(i).getNome())) {
                    x1 = aeroportos.get(i).getX();
                    y1 = aeroportos.get(i).getY();
                }
                if (rotas.get(j).getAeroporto2().equals(aeroportos.get(i).getNome())) {
                    x2 = aeroportos.get(i).getX();
                    y2 = aeroportos.get(i).getY();
                }
            }

            Line2D line1 = new Line2D.Float((float) x1, (float) y1, (float) x2, (float) y2);

            for (int k = 0; k < rotas.size(); k++) {
                for (int i = 0; i < aeroportos.size(); i++) {
                    if (rotas.get(k).getAeroporto1().equals(aeroportos.get(i).getNome())) {
                        xx1 = aeroportos.get(i).getX();
                        yy1 = aeroportos.get(i).getY();
                    }
                    if (rotas.get(k).getAeroporto2().equals(aeroportos.get(i).getNome())) {
                        xx2 = aeroportos.get(i).getX();
                        yy2 = aeroportos.get(i).getY();
                    }
                }

                Line2D line2 = new Line2D.Float((float) x1, (float) y1, (float) x2, (float) y2);
                if (line2.intersectsLine(line1) && x1 != xx1 && y1 != yy1 && x2 != xx1 && y2 != yy1 && x1 != xx2 && y1 != yy2) {
                    if (rotas.get(j).getAlt() == rotas.get(k).getAlt()) {
                        rotas.get(j).setAlt(rotas.get(j).getAlt() + 1000);
                        defineAltitude();
//                        System.out.println("Intersecao encontrada: " + rotas.get(j).toString() + " E " + rotas.get(k).toString());
                    }
                }
            }

        }
    }

}
