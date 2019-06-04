import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Criptografia {

    public static void main(String[] args) {

        try {

            String chave = "NOTAS"; //Não repita letras na chave.
            System.out.println("---------------CIFRA DAS COLUNAS--------------\nChave: "+chave);
            System.out.println("Digite uma opção: \n1-Criar um arquivo cifrado.\n2-Cifrar um arquivo\n3-Decifrar um arquivo.");
            int opcao = lerInt();

            switch(opcao){
                case 1:
                    System.out.println("-------------------------\nCRIAR CIFRADO\n-------------------------\n");
                    cifrarNovo(chave);
                    break;
                case 2:
                    System.out.println("-------------------------\nCIFRAR\n-------------------------\n");
                    cifrarArquivo(chave);
                    break;
                case 3:
                    System.out.println("-------------------------\nDECIFRAR\n-------------------------\n");
                    decifrarArquivo(chave);
                    break;
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void cifrarNovo(String chave) throws FileNotFoundException {
        System.out.println("Digite o nome do novo arquivo, com formato:");
        String nome = lerString();
        byte[] criptografada = escreveFrase(chave); //Escreve a frase num vetor de bytes e retorna ela cripgrafada
        escreveBytes(criptografada, nome); //Escreve os bytes no arquivo
        String cif = new String(criptografada);
        System.out.println("Arquivo criado.");
        System.out.println("Cifrada: " +cif);
    }

    public static void cifrarArquivo(String chave) throws java.io.IOException {
        System.out.println("Digite o nome do arquivo, com formato:");
        String nome = lerString();
        FileInputStream fis = new FileInputStream(nome);// Lê os bytes no arquivo
        byte[] bLida = fis.readAllBytes();
        byte[] criptografada = cifraColuna(bLida, chave); //Escreve a frase num vetor de bytes e retorna ela cripgrafada
        escreveBytes(criptografada, nome); //Escreve os bytes no arquivo
        String cif = new String(criptografada);
        System.out.println("Arquivo cifrado.");
        System.out.println("Cifrada: " +cif);
    }

    public static void decifrarArquivo(String chave) throws java.io.IOException{
        System.out.println("Digite o nome do arquivo, com formato:");
        String nome = lerString();
        FileInputStream fis = new FileInputStream(nome);// Lê os bytes no arquivo
        byte[] bLida = fis.readAllBytes();
        byte[] decifrada = decifraColuna(bLida, chave); //Escreve a frase num vetor de bytes e retorna ela cripgrafada
        escreveBytes(decifrada, nome); //Escreve os bytes no arquivo
        String dec = new String(decifrada);
        System.out.println("Arquivo decifrado.");
        System.out.println("Decifrada: " +dec);
    }


    public static byte[] escreveFrase(String chave){
        System.out.println("Digite aqui sua mensagem: ");
        String mensagem = lerString();
        byte[] bMensagem = mensagem.getBytes();
        byte[] cifrada = cifraColuna(bMensagem, chave);
        return cifrada;
    }

    public static int lerInt(){
        Scanner s1 = new Scanner(System.in);
        return Integer.parseInt(s1.nextLine());
    }


    private static void escreveBytes(byte[] bArquivo, String fileDest) {

        try (FileOutputStream fos = new FileOutputStream(fileDest)) {
            fos.write(bArquivo);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String lerString(){
        Scanner s1 = new Scanner(System.in);
        return s1.nextLine();
    }

    public static byte[] cifraColuna(byte[] bMensagem, String chave) {
        String mensagem = new String(bMensagem);

        int[] posicoesChave = posicaoChave(chave);

        int col = (int) Math.ceil((double) mensagem.length() / posicoesChave.length);

        char[][] matriz = new char[col][posicoesChave.length];
        int z = 0;
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < posicoesChave.length; j++) {
                if (mensagem.length() == z) {
                    matriz[i][j] = ' ';//Para preencher espaços nulos
                    z--;
                } else {
                    matriz[i][j] = mensagem.charAt(z);
                }

                z++;
            }
        }
        String cifrada = "";
        for (int i = 0; i < posicoesChave.length; i++) {
            for (int j = 0; j < posicoesChave.length; j++) {
                if (i == posicoesChave[j]) {
                    for (int a = 0; a < col; a++) {
                        cifrada = cifrada + matriz[a][j];
                    }
                }
            }
        }

        byte[] bCifrada = cifrada.getBytes();

        return bCifrada;
    }



    public static byte[] decifraColuna(byte[] bMensagem, String chave) {
        String mensagem = new String(bMensagem);

        int[] posicoesChave = posicaoChave(chave);

        int col = (int) Math.ceil((double) mensagem.length() / posicoesChave.length);

        String regex = "(?<=\\G.{" + col + "})";
        String[] get = mensagem.split(regex);

        char[][] matriz = new char[col][posicoesChave.length];

        for (int i = 0; i < posicoesChave.length; i++) {
            for (int j = 0; j < posicoesChave.length; j++) {
                if (posicoesChave[i] == j) {
                    for (int z = 0; z < col; z++) {
                        matriz[z][j] = get[posicoesChave[j]].charAt(z);
                    }
                }
            }
        }

        String decifrada = "";
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < posicoesChave.length; j++) {
                decifrada = decifrada + matriz[i][j];
            }
        }

        byte[] bDecifrada = decifrada.getBytes();

        return bDecifrada;
    }

    //Gera as posicoes da chave
    public static int[] posicaoChave(String chave) {
        String[] chaves = chave.split("");
        Arrays.sort(chaves);
        int[] num = new int[chave.length()];

        for (int i = 0; i < chaves.length; i++) {
            for (int j = 0; j < chave.length(); j++) {
                if (chaves[i].equals(chave.charAt(j) + "")) {
                    num[i] = j;
                }
            }
        }
        return num;
    }

}
