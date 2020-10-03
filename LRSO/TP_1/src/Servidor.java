import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {
    static final int PORT = 5000;

    public static void main(final String[] args) throws IOException, SocketException {
        // Inicializa o servidor
        final ServerSocket servidor = new ServerSocket(PORT);

        ArrayList<Candidato> candidatos = new ArrayList<Candidato>();

        // Inicializa um banco de candidatos elegíveis
        candidatos.add(new Candidato("Alecssandro Kalel", "Dr. Demian Fernandes", "PST", "12"));
        candidatos.add(new Candidato("Aureana Calorina", "Gustavo Josue Pacheco Jr.", "PLUA", "19"));
        candidatos.add(new Candidato("John Victor X", "Martinho Valencia", "Sociedade", "33"));
        candidatos.add(new Candidato("Goleiro Victor", "Goleiro Fabio", "CAM", "13"));
        candidatos.add(new Candidato("Brengler Runo", "Tabata Guerra Verdugo", "RTPB", "23"));


        // Recebe clientes enquanto o servidor estiver aberto.
        boolean fim = false;

        while (!fim) {
            Socket cliente = null;

            try {
                // Se um cliente se conecta, aceita a conexão.
                cliente = servidor.accept();

                System.out.println("Eleitor conectado: " + cliente);

                // Pega a entrada e saída do cliente.
                final DataInputStream clientInput = new DataInputStream(cliente.getInputStream());
                final DataOutputStream clientOutput = new DataOutputStream(cliente.getOutputStream());

                System.out.println("Alocando nova thread para o eleitor...");

                // Cria uma thread para o cliente.
                final Thread thread = new Eleicao(cliente, clientInput, clientOutput, candidatos);
                thread.start();

            } catch (final Exception ex) {
                fim = true;
                cliente.close();
                servidor.close();
                ex.printStackTrace();
            }
        }
    }
}

class Eleicao extends Thread{
    Candidato candidato;
    Candidato votado = null;

    final ArrayList<Candidato> candidatos;
    final DataInputStream input;
    final DataOutputStream output;
    final Socket cliente;

    //Eleicao possui um "banco" de candidatos
    public Eleicao(Socket cliente, DataInputStream input, DataOutputStream output,
            ArrayList<Candidato> candidatos) {
        this.cliente = cliente;
        this.input = input;
        this.output = output;
        this.candidatos = candidatos;
        this.candidato = null;
    }

    //Recebe a lista de candidatos e calcula suas parcelas de voto.
    public ArrayList<Candidato> calculaVotos(ArrayList<Candidato> candidatos){
        int totalVotos = 0;

        for (int i = 0; i < candidatos.size(); i++) {
            totalVotos = totalVotos + candidatos.get(i).getVotos();
        }

        final int contagem = totalVotos;

        this.candidatos.forEach((candidato -> {
            candidato.setParcela((double)candidato.getVotos()/contagem);
        }));

        return candidatos;
    }

    public String imprimePosicoes(ArrayList<Candidato> candidatos){
        String posicoes = "";
        for (int i = 0; i < candidatos.size(); i++) {
            posicoes = posicoes + ("\nNome: " + candidatos.get(i).getNome() + " Parcela de votos: " + candidatos.get(i).getParcela());
        }

        return posicoes;
    }

    @Override
    public void run(){
        String received;

        // Lista os candidatos e pede por identificacao: eleitor (1) ou agente eleitoral, que deve digitar o codigo de finalização ("finaliza").
        try {

            output.writeUTF(Arrays.toString(candidatos.toArray()) 
                    + "\n-----------------------\n\nBem vindo as eleicoes para prefeito 2020. Se veio aqui para votar, digite (1)." 
                    + "\nSe for um agente eleitoral e todos os votos estiverem concluidos, digite a senha secreta.");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        boolean running = true;
        while (running) {
            try {

                // Recebe respostas do cliente até ele fechar a conexão.
                received = input.readUTF();

                switch (received) {
                    
                    //Eleitor
                    case "1":
                    
                        output.writeUTF("Insira o numero do candidato e aperte CONFIRMA (ENTER):");
                        final String numero = input.readUTF();
            
                        //Itera funcionalmente em uma lista de candidatos e encontra o correto.
                        this.candidatos.forEach((candidato -> {
                            if (numero.equals(candidato.numero)) {
                                this.candidato = candidato;
                                votado = candidato;
                            }
                        }));
            
                        if (this.candidato == null) {
                            output.writeUTF("Candidato inexistente.\n");
                            break;                        
                        }

                        candidato.computaVoto();
                        output.writeUTF("Voto computado com sucesso.");
                        
                        break;

                    //Agente Eleitoral    
                    case "finaliza":
                        ArrayList<Candidato> resultadoFinal = new ArrayList<>(calculaVotos(candidatos));
                        resultadoFinal.sort(new ContadorUrnas());
                        output.writeUTF("O prefeito eleito de Belo Horizonte eh: " + resultadoFinal.get(0).getNome() + " com " + resultadoFinal.get(0).getParcela()+ "% dos votos!"
                                        + "\n Posicoes: " + imprimePosicoes(resultadoFinal)
                                        + "\n------------ \n Digite TERMINO para finalizar a eleicao");
                        break;
                        

                    default:
                        output.writeUTF("Opcao invalida.");
                        running = false;
                        this.input.close();
                        this.output.close();
                        this.cliente.close();
                        break;
                }

                output.writeUTF("Digite FIM para finalizar.\n");
                running = false;

            } catch (IOException ex) {
                running = false;               
            }

        try {
            // Fechando recursos
            this.input.close();
            this.output.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
}

class Candidato {
    public String nome;
    public String vice;
    public String partido;
    public String numero;
    public int nVotos;
    public double parcela;

    public Candidato(String nome, String vice, String partido, String numero) {
        this.nome = nome;
        this.vice = vice;
        this.partido = partido;
        this.numero = numero;
        this.nVotos = 0;
        this.parcela = 0;
    }

    public int getVotos(){
        return this.nVotos;
    }

    public String getNome(){
        return this.nome;
    }

    public double getParcela(){
        return this.parcela;
    }

    public void computaVoto(){
        this.nVotos = this.nVotos + 1;
    }

    public void setParcela(double parcela){
        this.parcela = parcela * 100;
    }

    @Override
    public String toString() {
         return ("\nCandidato:"+this.nome +
                " No: "+ this.numero +
                " Vice: "+ this.vice +
                " Partido : " + this.partido);
    }

}

class ContadorUrnas implements Comparator<Candidato> 
{
    @Override
    public int compare(Candidato o1, Candidato o2) {
        return o2.getVotos() - o1.getVotos();
    }
}