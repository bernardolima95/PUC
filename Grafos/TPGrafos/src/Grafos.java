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
    private JButton button1;
    private JButton caixeiroViajante;
    public static ArrayList<Aeroporto> aeroportos = new ArrayList<>();
    public static ArrayList<Rota> rotas = new ArrayList<>();
    public static MatrizInc matrizInc = new MatrizInc();
    public static MatrizInc matrizDis = new MatrizInc();
    private static double w = 1920;
    private static double h = 967;
    public static int nAeroportos;


    public static void main(String[] args) throws IOException{
        JFrame frame = new JFrame("Grafos");
        frame.setContentPane(new Grafos().Mapa);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        imprimeLista(aeroportos);
        System.out.println(distanciaAeroporto(aeroportos.get(0), aeroportos.get(1)));
        imprimeMatriz(matrizInc.getMatriz());
        Dijkstra d = new Dijkstra(nAeroportos);
        d.calcula(matrizInc, 0, 6);
    }

    public Grafos() throws IOException{
        menorTarifa.addActionListener(new ActionListener() {
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
            }});

        menorDistancia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                textArea1.setText("CLICADO");
                String nome1 = nomeAero1.getText();
                String nome2 = nomeAero2.getText();
                ArrayList<String> trajeto = new ArrayList<String>();
                ArrayList<Rota> rotasTrajeto = new ArrayList<Rota>();
                trajeto = encontraDijsktra(nome1, nome2, 2);

                textArea1.setText(trajeto.toString());
                rotasTrajeto = formaRotas(trajeto);

                try{
                    BufferedImage atualizada = ImageIO.read(new File("src/original.jpg"));
                    for (int i = 0; i < rotasTrajeto.size(); i++){
                        desenharTrajeto(atualizada, rotasTrajeto.get(i));
                    }
                    Image dimg = atualizada.getScaledInstance(1200, 800, Image.SCALE_SMOOTH);
                    ImageIcon iconNew = new ImageIcon(dimg);
                    ImageIO.write(atualizada, "jpg", new File("src/atualizada.jpg"));
                    jl.setIcon(iconNew);
                }
                catch (IOException f){
                    f.printStackTrace();
                }
        }
        });

        caixeiroViajante.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                textArea1.setText("CLICADO");
                String nome1 = nomeAero1.getText();
                String nome2 = nomeAero2.getText();
                ArrayList<String> trajeto = new ArrayList<String>();
                ArrayList<Rota> rotasTrajeto = new ArrayList<Rota>();
                CicloHamiltoniano ch = new CicloHamiltoniano();
                ch.findHamiltonianCycle(matrizInc);
                CaixeiroViajante cv = new CaixeiroViajante();
                trajeto = cv.tsp(matrizInc);

                textArea1.setText(trajeto.toString());
                rotasTrajeto = formaRotas(trajeto);

                try{
                    BufferedImage atualizada = ImageIO.read(new File("src/original.jpg"));
                    for (int i = 0; i < rotasTrajeto.size(); i++){
                        desenharTrajeto(atualizada, rotasTrajeto.get(i));
                    }
                    Image dimg = atualizada.getScaledInstance(1200, 800, Image.SCALE_SMOOTH);
                    ImageIcon iconNew = new ImageIcon(dimg);
                    ImageIO.write(atualizada, "jpg", new File("src/atualizada.jpg"));
                    jl.setIcon(iconNew);
                }
                catch (IOException f){
                    f.printStackTrace();
                }
            }
        });

        BufferedImage img = ImageIO.read(new File("src/equiretangular.jpg"));
        aeroportos = leArquivo("src/aeroportos.txt");
        leRotas("src/rotas.txt");
        double width = 1920;
        double height = 967;

        for (int i = 0; i < aeroportos.size(); i++){
            desenharPontos(img, aeroportos.get(i));
        }
        for (int k = 0; k < rotas.size(); k++){
            desenharRotas(img, rotas.get(k));
        }

        Image dimg = img.getScaledInstance(1200, 800, Image.SCALE_SMOOTH);
        ImageIO.write(img, "jpg", new File("src/original.jpg"));
        ImageIcon icon = new ImageIcon(dimg);
        jl.setIcon(icon);

    }


    public static BufferedImage desenharPontos(BufferedImage imagem, Aeroporto aeroporto) throws IOException{
        double x = aeroporto.getX();
        double y = aeroporto.getY();

        Graphics2D g2d = imagem.createGraphics();
        g2d.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.yellow);
        g2d.setFont(new Font("Arial Black", Font.PLAIN, 8));

        Ellipse2D.Double shape = new Ellipse2D.Double(x, y, 30, 30);
        g2d.drawString(aeroporto.getNome(), (float)x+6, (float)y+16);
        g2d.draw(shape);
        return imagem;
    }


//    public static double mercatorY(double lat){
//        double mercator = Math.log(Math.tan(lat/2 + Math.PI/4));
//        return ((991.00/2) - (1439.00*mercator/(2*Math.PI)));
//    }

    public static BufferedImage desenharRotas(BufferedImage imagem, Rota rota) throws IOException{
        double x1, x2, y1, y2;
        x1 = x2 = y1 = y2 = 0;
        Aeroporto a = new Aeroporto();
        Aeroporto b = new Aeroporto();

        int m, n, preco;
        m = n = 0;

        Graphics2D g2d = imagem.createGraphics();
        g2d.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.ORANGE);
        g2d.setFont(new Font("Arial Black", Font.PLAIN, 8));

        for (int i = 0; i < aeroportos.size(); i++){
            if (rota.getAeroporto1().equals(aeroportos.get(i).getNome())){
                x1 = aeroportos.get(i).getX();
                y1 = aeroportos.get(i).getY();
                a = aeroportos.get(i);
                m = i;
            }
            if (rota.getAeroporto2().equals(aeroportos.get(i).getNome())){
                x2 = aeroportos.get(i).getX();
                y2 = aeroportos.get(i).getY();
                b = aeroportos.get(i);
                n = i;
            }
        }

        //Cria as arestas da matriz de incidencia com pesos sendo os precos.
        matrizInc.setAresta(m, n, rota);
        matrizInc.setAresta(n, m, rota);

        rota.setPreco((int)distanciaAeroporto(a,b)); //Cria as arestas para a matriz de distancias entre os aeroportos.
        matrizDis.setAresta(m, n, rota);
        matrizDis.setAresta(n, m, rota);

        if((((1920 - x1)+(x2)) < (x1-x2) && x1 > x2)){ //Para voos da Asia/Oceania para a America
            g2d.draw(new Line2D.Double(x1, y1, 1920, y2)); //Desenha do aeroporto até a beira do mapa
            g2d.draw(new Line2D.Double(0, y1, x2, y2));
        }
        else if (((((x1) + (1920 - x2)) < (x2-x1)) && x2 > x1)){ //Para voos da America para a Asia/Oceania
            g2d.draw(new Line2D.Double(x1, y1, 0, y2)); //Desenha do aeroporto até a beira do mapa
            g2d.draw(new Line2D.Double(1920, y2, x2, y2));
        }
        else {
            g2d.draw(new Line2D.Double(x1, y1, x2, y2));
        }
        return imagem;
    }

    public static BufferedImage desenharTrajeto (BufferedImage imagem, Rota rota) throws IOException{
        double x1, x2, y1, y2;
        x1 = x2 = y1 = y2 = 0;

        Graphics2D g2d = imagem.createGraphics();
        g2d.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLUE);
        g2d.setFont(new Font("Arial Black", Font.PLAIN, 8));

//        System.out.println("Rotas:" +rota.getAeroporto1());
//        System.out.println(rota.getAeroporto2());

        for (int i = 0; i < aeroportos.size(); i++){
            if (rota.getAeroporto1().equals(aeroportos.get(i).getNome())){
                x1 = aeroportos.get(i).getX();
                y1 = aeroportos.get(i).getY();
            }
            if (rota.getAeroporto2().equals(aeroportos.get(i).getNome())){
                x2 = aeroportos.get(i).getX();
                y2 = aeroportos.get(i).getY();
            }
        }


        if((((1920 - x1)+(x2)) < (x1-x2) && x1 > x2)){ //Para voos da Asia/Oceania para a America
            g2d.draw(new Line2D.Double(x1, y1, 1920, y2)); //Desenha do aeroporto até a beira do mapa
            g2d.draw(new Line2D.Double(0, y1, x2, y2));
        }
        else if (((((x1) + (1920 - x2)) < (x2-x1)) && x2 > x1)){ //Para voos da America para a Asia/Oceania
            g2d.draw(new Line2D.Double(x1, y1, 0, y2)); //Desenha do aeroporto até a beira do mapa
            g2d.draw(new Line2D.Double(1920, y2, x2, y2));
        }
        else {
            g2d.draw(new Line2D.Double(x1, y1, x2, y2));
        }
        return imagem;
    }


    public static ArrayList<Aeroporto> leArquivo(String filename) throws IOException{
       double lat, longi, x, y;
       int num;

       String nome, linha;
       ArrayList<Aeroporto> aeroportos = new ArrayList<Aeroporto>();


       BufferedReader bf = new BufferedReader (new InputStreamReader(new FileInputStream(filename)));
       linha = bf.readLine();
       num = Integer.parseInt(linha);
       linha = bf.readLine();
       nAeroportos = num;
       matrizInc.setMatriz(num, num); //Cria a matriz de incidencia ao saber o numero de aeroportos.
       matrizDis.setMatriz(num, num);

       do{
           String[] separada = linha.split("\\s+");
           System.out.println(Arrays.toString(separada));
           nome = separada[0];
           lat = Double.parseDouble(separada[1]);
           longi = Double.parseDouble(separada[2]);
           x = achaCoordX(longi);
           y = achaCoordY(lat);
           Aeroporto atual = new Aeroporto(nome, lat, longi, x , y);
           aeroportos.add(atual);
       } while ((linha = bf.readLine()) != null);

       return aeroportos;
    }

    public static double achaCoordX(double lon){
        return  ((lon + 180) * (w/ 360));
    }

    public static double achaCoordY(double lat){
        return  (((lat * -1) + 90) * (h/ 180));
    }


    public static void leRotas(String filename) throws IOException{
        int num;
        String nome1, nome2, linha;
        int preco;

        ArrayList<ArrayList<Aeroporto>> matrizAdj = new ArrayList<ArrayList<Aeroporto>>();
        ArrayList<Aeroporto> conectados = new ArrayList<Aeroporto>();

        BufferedReader bf = new BufferedReader (new InputStreamReader(new FileInputStream(filename)));
        linha = bf.readLine();

        num = Integer.parseInt(linha);

        linha = bf.readLine();

        do{
            String[] separada = linha.split("\\s+");
            System.out.println(Arrays.toString(separada));
            nome1 = separada[0];
            nome2 = separada[1];
            preco = Integer.parseInt(separada[2]);

            Rota atual = new Rota(nome1, nome2, preco);

            rotas.add(atual);
        } while ((linha = bf.readLine()) != null);
    }

    //Dado dois nomes de aeroporto, busca seus objetos na lista de aeroportos e os devolve em uma nova lista, que sera
    //adicionada a matriz de adjacencia.
    public static ArrayList<Aeroporto> encontraAeroporto(ArrayList<Aeroporto> aeroportos, String nome1, String nome2){
        ArrayList<Aeroporto> conectados = new ArrayList<Aeroporto>();

        for(int i = 0; i < aeroportos.size(); i++){
            if (aeroportos.get(i).getNome().equals(nome1) || aeroportos.get(i).getNome().equals(nome2)){
                conectados.add(aeroportos.get(i));
            }
        }
        return conectados;
    }

    public static void imprimeLista(ArrayList<Aeroporto> aeroportos){
        for (int i = 0; i < aeroportos.size(); i++){
            System.out.println(aeroportos.get(i).toString());
        }
    }

    //Calcula a distancia entre aeroportos usando a formula de distancia de ponto a ponto
    public static double distanciaAeroporto(Aeroporto a, Aeroporto b){
        double x1 = a.getLat();
        double x2 = b.getLat();
        double y1 = a.getLong();
        double y2 = b.getLong();

        double ac = Math.abs(y2 - y1);
        double cb = Math.abs(x2 - x1);
        return Math.hypot(ac, cb); //Projeta o triangulo e calcula a hipotenusa.
    }

    public static ArrayList<String> encontraDijsktra(String nome1, String nome2, int cod){
        int aero1 = descobreCodigo(nome1);
        int aero2 = descobreCodigo(nome2);
        ArrayList<String> trajeto = new ArrayList<String>();

        if (aero1 != -1 && aero2 != -1){
            Dijkstra f = new Dijkstra(nAeroportos);
            if (cod == 1) trajeto = f.calcula(matrizInc, aero1, aero2);
            if (cod == 2) trajeto = f.calcula(matrizDis, aero1, aero2);
        }

        return trajeto;
    }

    public static void imprimeMatriz(Rota[][] matriz){
        System.out.print("[");
        for (int i = 0; i < matriz.length; i++){
            for(int j = 0; j < matriz.length; j++){
                if(matriz[i][j] != null) System.out.print(matriz[i][j].getPreco()+", ");
                else System.out.print("n/a, ");
            }
            System.out.print("]");
            System.out.println();
        }
    }

    public static int descobreCodigo(String nome){
        int aero = -1;
        for (int i = 0; i < aeroportos.size(); i++){
            if (aeroportos.get(i).getNome().equals(nome))  aero = i;
        }

        return aero;
    }

    public static String descobreNome(int cod){
        String aero = "";
        for (int i = 0; i < aeroportos.size(); i++){
            if (i == cod)  aero = aeroportos.get(i).getNome();
        }

        return aero;
    }

    public static ArrayList<Rota> formaRotas(ArrayList<String> trajeto){
        ArrayList<Rota> rotasTrajeto = new ArrayList<Rota>();
        Rota r = new Rota();
        for (int i = 0; i < trajeto.size() - 1; i++){
            r = new Rota();
            r.setAeroporto1(trajeto.get(i));
            r.setAeroporto2(trajeto.get(i+1));
            rotasTrajeto.add(r);
        }
        return rotasTrajeto;
    }



}
