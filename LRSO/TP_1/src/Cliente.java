import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    static final int PORT = 5000;

    public static void main(String[] args) throws IOException {
        try {
            Scanner input = new Scanner(System.in);

            Socket cliente = new Socket("127.0.0.1", PORT);

            // Obtendo input e saida do cliente
            DataInputStream clientInput = new DataInputStream(cliente.getInputStream());
            DataOutputStream clientOutput = new DataOutputStream(cliente.getOutputStream());

            System.out.println(clientInput.readUTF());
            String linha = input.nextLine();
            clientOutput.writeUTF(linha);
            linha = clientInput.readUTF();
            System.out.println(linha);

            if (!linha.equals("Opcao invalida.") || !linha.equals("Candidato inexistente.")) {
                // Estabelece conexão entre o cliente e a eleicao
                while (true) {
                    linha = input.nextLine();
                    clientOutput.writeUTF(linha);

                    // Responde de acordo com a opcao selecionada pelo eleitor/agente.
                    if (linha.equals("FIM")){
                        System.out.println("Fechando votacao do eleitor: " + cliente);
                        cliente.close();
                        System.out.println("Sessao fechada.");
                        break;
                    } else if (linha.equals("1")) {
                        System.out.println(clientInput.readUTF());
                        linha = input.nextLine();
                        clientOutput.writeUTF(linha);
                        String respostaServidor = clientInput.readUTF();
                        System.out.println(respostaServidor);

                    } else if (linha.equals("TERMINO")) {
                        System.out.println("Eleicao finalizada pelo agente: " + cliente);
                        cliente.close();
                        System.out.println("Sessao fechada.");
                        break;
                    }

                    // Imprime a resposta do servidor
                    String resposta = clientInput.readUTF();
                    System.out.println(resposta);
                    System.out.println(clientInput.readUTF());
                }
            }

            // Fecha as streams
            input.close();
            clientInput.close();
            clientOutput.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
