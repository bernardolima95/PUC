package aed3;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.io.*;

//Bernardo Cerqueira de Lima
//Patrícia Baracho Porto

public class CRUD {

   static int catLock = 0; //Se nenhuma categoria ainda foi adicionada, trava o acesso a insercao de novos produtos.
   static int report = 0; //Contador que determina os loops do relatorio

   public static void main(String[] args) {
      int opcao;
      Arquivo<Produto> arqProduto;
      Arquivo<Categoria> arqCategoria;
      Arquivo<Cliente> arqCliente;
      Arquivo<Compra> arqCompra;
      Arquivo<ProdutoComprado> arqProdutoComprado;
      IndiceComposto CompraProduto;
      IndiceComposto ProdutoCompra;
      File f, g, h, i, j;

      try {
         f = new File("produtos.db");
         g = new File("categorias.db");
         h = new File("clientes.db");
         i = new File("compras.db");
         j = new File("produtoscomprados.db");
         f.delete();
         g.delete();
         h.delete();
         i.delete();
         j.delete();

         arqProduto = new Arquivo<>(Produto.class.getConstructor(), "produtos.db", "Produtos.idx");
         arqCategoria = new Arquivo<>(Categoria.class.getConstructor(), "categorias.db", "Categorias.idx");
         arqCliente = new Arquivo<>(Cliente.class.getConstructor(), "clientes.db", "Clientes.idx");
         arqCompra = new Arquivo<>(Compra.class.getConstructor(), "compras.db", "Compras.idx");
         arqProdutoComprado = new Arquivo<>(ProdutoComprado.class.getConstructor(), "produtoscomprados.db", "ProdutosComprados.idx");
         CompraProduto = new IndiceComposto(5, "CompraProduto.idx");
         ProdutoCompra = new IndiceComposto(5, "ProdutoCompra.idx");
         inicializarDB(arqProduto, arqCategoria, arqCliente);

         do {
            System.out.println("\n---------MENU PRINCIPAL | PRODUTOS---------\n" +
                    "Digite a operação desejada: \n1- Inclusão\n2- Alteração" +
                    "\n3- Exclusão" + "\n4- Consulta de Produtos" +
                    "\n5- Menu de Categorias \n6- Menu de Clientes\n7- Menu de Compras\n8- Relatórios\n9- Sair");
            opcao = lerInt();
            menu(opcao, arqProduto, arqCategoria, arqCliente, arqCompra, arqProdutoComprado, CompraProduto, ProdutoCompra);
         } while (opcao != 9);

      } catch (Exception e){
         e.printStackTrace();
      }
   }

   //Metodo de teste que inicializa alguns produtos para facilitar o teste
   private static void inicializarDB(Arquivo<Produto> arqProduto, Arquivo<Categoria> arqCategoria, Arquivo<Cliente> arqCliente) throws Exception{
      int idC;
      idC = arqCategoria.incluir(new Categoria("Doces"));
      arqProduto.incluir( new Produto("Snickers", idC, arqCategoria.ler(idC).getNome(),"Chocolate, caramelo, amendoim", (short)4, (float) 1.99));
      arqProduto.incluir( new Produto("Mars", idC, arqCategoria.ler(idC).getNome(),"Chocolate", (short)3, (float) 2.99));
      arqProduto.incluir( new Produto("M&Ms", idC, arqCategoria.ler(idC).getNome(),"Chocolatinhos", (short)5, (float) 3.99));
      arqCliente.incluir( new Cliente("Bernardo", "bernardolima95@gmail.com"));
   }

   public static int lerInt(){
      Scanner s1 = new Scanner(System.in);
      return Integer.parseInt(s1.nextLine());
   }

   public static float lerFloat(){
      Scanner s1 = new Scanner(System.in);
      return Float.parseFloat(s1.nextLine());
   }

   public static String lerString(){
      Scanner s1 = new Scanner(System.in);
      return s1.nextLine();
   }

   public static void menu(int opcao, Arquivo<Produto> arqProduto, Arquivo<Categoria> arqCategoria, Arquivo<Cliente> arqCliente, Arquivo<Compra> arqCompra, Arquivo<ProdutoComprado> arqProdutoComprado, IndiceComposto CP, IndiceComposto PC) throws Exception{
      switch(opcao){
         case 1:
            System.out.println("-------------------------\nInsercao\n-------------------------\n");
            inserir(arqProduto, 0, arqCategoria);//O int id=0 como parametro define que é um incluir comum
            break;
         case 2:
            System.out.println("-------------------------\nAlteração\n-------------------------\n");
            alteracao(arqProduto, arqCategoria);
            break;
         case 3:
            System.out.println("-------------------------\nExclusão\n-------------------------\n");
            exclusao(arqProduto, arqProdutoComprado);
            break;
         case 4:
            System.out.println("-------------------------\nConsulta de Produtos\n-------------------------\n");
            buscar(arqProduto);
            break;
         case 5:
            System.out.println("-------------------------\nMenu de Categorias\n-------------------------\n");
            menuCategoria(arqProduto, arqCategoria, arqCliente, arqCompra);
            break;
         case 6:
            System.out.println("-------------------------\nMenu de Clientes\n-------------------------\n");
            menuCliente(arqProduto, arqCategoria, arqCliente, arqCompra);
            break;
         case 7:
            System.out.println("-------------------------\nMenu de Compras\n-------------------------\n");
            menuCompra(arqProduto, arqCategoria, arqCliente, arqCompra, arqProdutoComprado, CP, PC);
            break;
         case 8:
            System.out.println("-------------------------\nRelatorios\n-------------------------\n");
            relatorio(arqProduto, arqCategoria, arqCliente, arqCompra, arqProdutoComprado);
            break;
      }
   }

   public static void menuCategoria(Arquivo<Produto> arqProduto, Arquivo<Categoria> arqCategoria, Arquivo<Cliente> arqCliente,  Arquivo<Compra> arqCompra) throws Exception{
      int opcao;
      do {
         System.out.println("--------------------------------\nDigite a operação desejada para categorias: \n1- Inclusão\n2- Alteração" +
                 "\n3- Exclusão" + "\n4- Consulta de Categorias\n5- Listagem de Produtos\n6- Voltar para o menu principal");
         opcao = lerInt();
         switch(opcao){
            case 1:
               System.out.println("-------------------------\nInsercao\n-------------------------\n");
               inserirCat(arqCategoria, 0);//O int id=0 como parametro define que é um incluir comum
               break;
            case 2:
               System.out.println("-------------------------\nAlteração\n-------------------------\n");
               alteracaoCat(arqProduto, arqCategoria);
               break;
            case 3:
               System.out.println("-------------------------\nExclusão\n-------------------------\n");
               exclusaoCat(arqProduto, arqCategoria);
               break;
            case 4:
               System.out.println("-------------------------\nConsulta de Categorias\n-------------------------\n");
               buscarCat(arqCategoria);
               break;
            case 5:
               System.out.println("-------------------------\nListagem de Produtos\n---------------------------\n");
               listaProd(arqProduto);
               break;
         }
      } while (opcao != 6);
   }

   public static void menuCliente(Arquivo<Produto> arqProduto, Arquivo<Categoria> arqCategoria, Arquivo<Cliente> arqCliente, Arquivo<Compra> arqCompra) throws Exception {
      int opcao;
      do {
         System.out.println("--------------------------------\nDigite a operação desejada para clientes: \n1- Inclusão\n2- Alteração" +
                 "\n3- Exclusão" + "\n4- Consulta de Clientes\n5- Listagem de Clientes\n6- Voltar para o menu principal");
         opcao = lerInt();
         switch (opcao) {
            case 1:
               System.out.println("-------------------------\nInsercao\n-------------------------\n");
               inserirCli(arqCliente, 0);//O int id=0 como parametro define que é um incluir comum
               break;
            case 2:
               System.out.println("-------------------------\nAlteração\n-------------------------\n");
               alteracaoCli(arqCliente, arqCompra);
               break;
            case 3:
               System.out.println("-------------------------\nExclusão\n-------------------------\n");
               exclusaoCli(arqCliente, arqCompra);
               break;
            case 4:
               System.out.println("-------------------------\nConsulta de Clientes\n-------------------------\n");
               buscarCli(arqCliente);
               break;
//            case 5:
//               System.out.println("-------------------------\nListagem de Clientes\n---------------------------\n");
//               buscaCliente(arqCliente);
//               break;
         }
      } while (opcao != 6);
   }

   public static void menuCompra (Arquivo < Produto > arqProduto, Arquivo <Categoria> arqCategoria, Arquivo <Cliente> arqCliente, Arquivo <Compra> arqCompra, Arquivo<ProdutoComprado> arqProdutoComprado, IndiceComposto CP, IndiceComposto PC) throws Exception {
      int opcao;
      do{
         System.out.println("--------------------------------\nDigite a operação desejada para compras: \n1- Inclusão\n2- Alteração" +
                    "\n3- Exclusão" + "\n4- Consulta de Compras\n5- Exclusão de Dependencias \n6- Voltar para o menu principal");
         opcao = lerInt();
         switch (opcao) {
            case 1:
               System.out.println("-------------------------\nInsercao\n-------------------------\n");
               inserirCompra(arqCompra, arqCliente, arqProduto, arqProdutoComprado,0, CP, PC);//O int id=0 como parametro define que é um incluir comum
               break;
            case 2:
               System.out.println("-------------------------\nAlteração\n-------------------------\n");
               alteracaoCompra(arqCompra, arqCliente, arqProduto, arqProdutoComprado, CP, PC);
               break;
            case 3:
               System.out.println("-------------------------\nExclusão\n-------------------------\n");
               exclusaoCompra(arqCompra, arqProdutoComprado);
               break;
            case 4:
               System.out.println("-------------------------\nConsulta de Compras\n-------------------------\n");
               buscarCompra(arqCompra);
               break;
            case 5:
               System.out.println("-------------------------\nConsulta de Compras\n-------------------------\n");
               exclusaoRelacao(arqProdutoComprado);
               break;
         }
      } while (opcao != 6);
   }

   //Insere um item no arquivo. O codigo no parametro determina se a inserção é uma original (novo ID) ou uma alteração (mantém ID),
   //com 0 sendo uma inserção original, e qualquer valor alem deste sendo uma alteração, com cod servindo como o ID.
   public static void inserir(Arquivo<Produto> arqProduto, int cod, Arquivo<Categoria> arqCategoria) throws Exception{
      int id;
      int idCategoria;
      String nomeCategoria = "";
      boolean busca;

      if (catLock == 0){
         System.out.println("Nenhuma categoria criada, voce sera redirecionado ao menu de categorias para criar uma.\n---------------");
         inserirCat(arqCategoria, 0);
         System.out.println("\n Categoria adicionada, voce sera retornado à inclusão de produto.\n------------------");
      }

      System.out.println("Digite o nome do produto a ser incluido, em ate 60 caracteres: ");
      String nome = lerString();
      if (nome.length() > 60 ) nome = nome.substring(0,60);

      System.out.println("Digite a descricao do produto a ser incluido, em ate 120 caracteres: ");
      String descricao = lerString();
      if (descricao.length() > 120 ) descricao = descricao.substring(0,120);

      do {
         System.out.println("Digite o ID da categoria do produto: ");
         idCategoria = lerInt();
         busca = buscarCat(arqCategoria, idCategoria);
         if (busca == false) {
            System.out.println("Categoria inexistente, escolha uma outra categoria.\n---------");
         }
         else{
            System.out.println("Categoria:" +arqCategoria.ler(idCategoria).getNome());
            nomeCategoria = arqCategoria.ler(idCategoria).getNome();
         }
      } while (busca == false);

      System.out.println("Digite a quantidade do produto em estoque: ");
      short qtd = (short)lerInt();

      System.out.println("Digite o preço do produto: ");
      float preco = lerFloat();

      if (cod == 0) {
         id = arqProduto.incluir( new Produto (nome, idCategoria, nomeCategoria, descricao, qtd, preco));
         System.out.println("Produto incluído com sucesso, ID: "+id);
      }
      else {
         arqProduto.incluirAlt( new Produto (nome, idCategoria, nomeCategoria, descricao, qtd, preco), cod);//Metodo incluir para caso de alteração, com "cod" = id. Mantem o ID.
         System.out.println("Produto alterado com sucesso, ID: "+cod);
      }
   }

   //Busca o arquivo, procurando um produto com o ID desejado.
   public static void buscar(Arquivo<Produto> arqProduto) throws Exception{
      System.out.println("Digite o ID do produto a ser procurado: ");
      int id = lerInt();
      if (arqProduto.ler(id) != null){
         System.out.println("\nProduto encontrado:" + arqProduto.ler(id) + "\n");
      }
      else System.out.println("\n Produto não encontrado. \n");
   }

   //Faz o mesmo passo que a busca, e se encontra um arquivo, o deleta.
   public static void exclusao(Arquivo<Produto> arqProduto, Arquivo<ProdutoComprado> arqProdutoComprado) throws Exception{
      System.out.println("Digite o ID do produto a ser excluido: ");
      int id = lerInt();
      if (arqProdutoComprado.checaExclusaoProduto(id) != false){ //Busca por todos os elementos do arquivo atras de um elemento com o mesmo id de categoria desejado.
         System.out.println("O produto nao pode ser excluido porque possui dependencias. Remova as relações no menu de compras para permitir a exclusão. Voltando ao menu.");
         return;
      }
      if (arqProduto.ler(id) != null){
         arqProduto.excluir(id);
         System.out.println("Produto excluido com sucesso.");
      }
      else System.out.println("\n Produto não encontrado. \n");
   }

   //Procura por um produto através seu código de ID, caso o encontre, realiza a remoção e logo após uma inserção codificada com o id do arquivo deletado,
   //efetivamente causando uma alteração das caracteristicas do produto, mantendo o ID original.
   public static void alteracao(Arquivo<Produto> arqProduto, Arquivo<Categoria> arqCategoria) throws Exception{
      System.out.println("Digite o ID do produto a ser alterado: ");
      int id = lerInt();
      if (arqProduto.ler(id) != null){
         arqProduto.excluir(id);
         inserir(arqProduto, id, arqCategoria);
         System.out.println("Produto alterado com sucesso.");
      }
      else System.out.println("\n Produto não encontrado. \n");
   }

   //Busca o arquivo, procurando um produto com o ID desejado.
   public static boolean buscarProdBool(Arquivo<Produto> arqProduto, int id) throws Exception{
      if (arqProduto.ler(id) != null){
//         System.out.println("\nProduto encontrado:" + arqProduto.ler(id) + "\n");
         return true;
      }
      else{
         System.out.println("\n Produto não encontrado. \n");
         return false;
      }
   }


   //----------------------------------------METODOS PARA CATEGORIAS


   public static void alteracaoCat(Arquivo<Produto> arqProduto, Arquivo<Categoria> arqCategoria) throws Exception{
      System.out.println("Digite o ID da categoria a ser alterado: ");
      int id = lerInt();
      if (arqProduto.checaExclusao(id) != false){ //Busca por todos os elementos do arquivo atras de um elemento com o mesmo id de categoria desejado.
         System.out.println("A categoria nao pode ser alterada pois possui itens registrados a ela. Voltando ao menu.");
         return;
      }
      if (arqCategoria.ler(id) != null){
         arqCategoria.excluir(id);
         inserirCat(arqCategoria, id);
         System.out.println("Categoria alterada com sucesso.");
      }
      else System.out.println("\n Categoria não encontrada. \n");
   }

   //Faz o mesmo passo que a busca, e se encontra a categoria, a deleta.
   public static void exclusaoCat(Arquivo<Produto> arqProduto, Arquivo<Categoria> arqCategoria) throws Exception{
      System.out.println("Digite o ID da categoria a ser excluida: ");
      int id = lerInt();
      if (arqProduto.checaExclusao(id) != false){ //Busca por todos os elementos do arquivo atras de um elemento com o mesmo id de categoria desejado.
         System.out.println("A categoria nao pode ser excluida pois possui itens registrados a ela. Voltando ao menu.");
         return;
      }
      if (arqCategoria.ler(id) != null){
         arqCategoria.excluir(id);
         System.out.println("Categoria excluida com sucesso.");
      }
      else System.out.println("\n Categoria nao encontrada. \n");
   }

   public static boolean buscarCat(Arquivo<Categoria> arqCategoria) throws Exception{
      System.out.println("Digite o ID da categoria a ser buscada: ");
      int id = lerInt();
      if (arqCategoria.ler(id) != null){
         System.out.println("\n Categoria encontrada:" + arqCategoria.ler(id) + "\n");
         return true;
      }
      else {
         System.out.println("\n Categoria nao encontrada. \n");
         return false;
      }
   }

   //Busca por uma categoria no arquivo. Serve ao metodo de insercao de produtos, checando se a categoria
   //do novo produto existe de fato.
   public static boolean buscarCat(Arquivo<Categoria> arqCategoria, int id) throws Exception{
      if (arqCategoria.ler(id) != null){
//         System.out.println("\n Nome da categoria encontrada:" + arqCategoria.ler(id) + "\n");
         return true;
      }
      else {
//         System.out.println("\n Categoria nao encontrada. \n");
         return false;
      }
   }

   //Insere uma categoria no arquivo. O codigo no parametro determina se a inserção é uma original (novo ID) ou uma alteração (mantém ID),
   //com 0 sendo uma inserção original, e qualquer valor alem deste sendo uma alteração, com cod servindo como o ID.
   public static void inserirCat(Arquivo<Categoria> arqCategoria, int cod) throws Exception{
      int id;
      System.out.println("Digite o nome da categoria a ser incluida, em ate 60 caracteres: ");
      String nome = lerString();
      if (nome.length() > 60 ) nome = nome.substring(0,60);

      if (cod == 0) {
         id = arqCategoria.incluir( new Categoria (nome));
         System.out.println("Categoria incluida com sucesso, ID: "+id);
      }
      else {
         arqCategoria.incluirAlt( new Categoria (nome), cod);//Metodo incluir para caso de alteração, com "cod" = id. Mantem o ID.
         System.out.println("Categoria incluida com sucesso, ID: "+cod);
      }
      catLock++;
   }

   //Metodo que lista os produtos.
   public static void listaProd(Arquivo<Produto> arqProduto) throws Exception{
      int id;
      System.out.println("Digite o ID da categoria a ser verificada: ");
      id = lerInt();
      System.out.println("------\nLISTAGEM\n------------");
      arqProduto.listagemProdutos(id); //Busca por todos os elementos do arquivo atras de um elemento com o mesmo id de categoria desejado.
   }

   //----------------------------------------METODOS PARA CLIENTES
   public static void alteracaoCli(Arquivo<Cliente> arqCliente, Arquivo<Compra> arqCompra) throws Exception{
      System.out.println("Digite o ID da cliente a ser alterado: ");
      int id = lerInt();
      if (arqCompra.checaExclusaoCliente(id) != false){ //Busca por todos os elementos do arquivo atras de um elemento com o mesmo id de categoria desejado.
         System.out.println("O cliente nao pode ser alterado porque tem compras ligadas ao ID dele. Voltando ao menu.");
         return;
      }
      if (arqCliente.ler(id) != null){
         arqCliente.excluir(id);
         inserirCli(arqCliente, id);
         System.out.println("Cliente alterado com sucesso.");
      }
      else System.out.println("\n Cliente nao encontrado.. \n");
   }

   public static void exclusaoCli(Arquivo<Cliente> arqCliente, Arquivo<Compra> arqCompra) throws Exception{
      System.out.println("Digite o ID da categoria a ser excluida: ");
      int id = lerInt();
      if (arqCompra.checaExclusaoCliente(id) != false){ //Busca por todos os elementos do arquivo atras de um elemento com o mesmo id de categoria desejado.
         System.out.println("O cliente nao pode ser excluido porque tem compras ligadas ao ID dele. Voltando ao menu.");
         return;
      }
      if (arqCliente.ler(id) != null){
         arqCliente.excluir(id);
         System.out.println("Cliente excluido com sucesso.");
      }
      else System.out.println("\n Cliente nao encontrado. \n");
   }

   public static boolean buscarCli(Arquivo<Cliente> arqCliente) throws Exception{
      System.out.println("Digite o ID do cliente a ser buscado: ");
      int id = lerInt();
      if (arqCliente.ler(id) != null){
         System.out.println("\n Cliente encontrado:" + arqCliente.ler(id) + "\n");
         return true;
      }
      else {
         System.out.println("\n Cliente nao encontrado. \n");
         return false;
      }
   }

   //Insere um cliente no arquivo. O codigo no parametro determina se a inserção é uma original (novo ID) ou uma alteração (mantém ID),
   //com 0 sendo uma inserção original, e qualquer valor alem deste sendo uma alteração, com cod servindo como o ID.
   public static void inserirCli(Arquivo<Cliente> arqCliente, int cod) throws Exception{
      int id;
      System.out.println("Digite o nome do cliente a ser incluido, em ate 60 caracteres: ");
      String nome = lerString();
      if (nome.length() > 60 ) nome = nome.substring(0,60);

      System.out.println("Digite o email do Cliente a ser incluido, em ate 40 caracteres: ");
      String email = lerString();
      if (email.length() > 40 ) email = email.substring(0,40);

      if (cod == 0) {
         id = arqCliente.incluir( new Cliente (nome, email));
         System.out.println("Cliente incluido com sucesso, ID: "+id);
      }
      else {
         arqCliente.incluirAlt( new Cliente (nome, email), cod);//Metodo incluir para caso de alteração, com "cod" = id. Mantem o ID.
         System.out.println("Cliente incluido com sucesso, ID: "+cod);
      }
   }

   public static boolean buscarCliBool(Arquivo<Cliente> arqCliente, int id) throws Exception{
      if (arqCliente.ler(id) != null){
//         System.out.println("\n Cliente encontrado:" + arqCliente.ler(id) + "\n");
         return true;
      }
      else {
         System.out.println("\n Cliente nao encontrado. \n");
         return false;
      }
   }

   //-----------------------------------------METODOS PARA COMPRAS
   //Insere uma compra no arquivo. O codigo no parametro determina se a inserção é uma original (novo ID) ou uma alteração (mantém ID),
   //com 0 sendo uma inserção original, e qualquer valor alem deste sendo uma alteração, com cod servindo como o ID.
   public static void inserirCompra(Arquivo<Compra> arqCompra, Arquivo<Cliente> arqCliente, Arquivo<Produto> arqProduto, Arquivo<ProdutoComprado> arqProdutoComprado, int cod, IndiceComposto CP, IndiceComposto PC) throws Exception{
      int id, idProdComp, idCliente, idProduto, num, qtd;
      boolean busca;
      float precoCompra = 0;

      do{
         System.out.println("Digite o ID do cliente: ");
         idCliente = lerInt();
         busca = buscarCliBool(arqCliente, idCliente);
         if (busca == false) {
            System.out.println("Cliente inexistente, escolha um outro cliente.\n---------");
         }
         else{
            System.out.println("Cliente:" +arqCliente.ler(idCliente).getNome());
         }
      }while (busca == false);

      System.out.println("Digite quantos produtos diferentes foram comprados: ");
      num = lerInt();

      int[] idProds = new int[num]; // Guarda os IDs dos produtos para inserir nas arvores B+ compostas
      int[] qtdProds = new int[num];

      for (int i = 0; i < num; i++){
         System.out.println("\nProduto ["+(i+1)+"]");
         do{
            System.out.println("Digite o ID do produto: ");
            idProduto = lerInt();
            busca = buscarProdBool(arqProduto, idProduto);
            if (busca == false) {
               System.out.println("Produto inexistente, escolha um outro produto.\n---------");
            }
            else{
               System.out.println("Produto:" +arqProduto.ler(idProduto).getNome());
               System.out.println("Digite a quantidade deste produto comprada: ");
               qtd = lerInt();
               precoCompra = precoCompra + (qtd * arqProduto.ler(idProduto).getPreco());

               idProds[i] = arqProduto.ler(idProduto).getId();
               qtdProds[i] = qtd;
            }
         }while (busca == false);
      }

      System.out.println("Digite a data da compra, em formato String. Ex: 05/04/2019 é '05042019': ");
      String dataC = lerString();
      if (dataC.length() > 40 ) dataC = dataC.substring(0,40);

      if (cod == 0) {
         id = arqCompra.incluir( new Compra (idCliente, dataC, precoCompra));
         System.out.println("Compra incluida com sucesso, ID: "+id);
         for (int k = 0; k < num; k++){ //Insere as chaves nas arvores B+ compostas
            idProdComp = arqProdutoComprado.incluir(new ProdutoComprado(id, idProds[k], qtdProds[k], arqProduto.ler(idProds[k]).getPreco()));
            System.out.println("Relação incluida com sucesso, ID: "+idProdComp);
            System.out.println(arqProdutoComprado.ler(idProdComp).toString());
            report++;
            CP.inserir(id, idProds[k]);
            PC.inserir(idProds[k], id);
         }
      }
      else {
         arqCompra.incluirAlt( new Compra (idCliente, dataC, precoCompra), cod);//Metodo incluir para caso de alteração, com "cod" = id. Mantem o ID.
         System.out.println("Compra incluida com sucesso, ID: "+cod);
         for (int k = 0; k < num; k++){ //Insere as chaves nas arvores B+ compostas
            idProdComp = arqProdutoComprado.incluir(new ProdutoComprado(cod, idProds[k], qtdProds[k], arqProduto.ler(idProds[k]).getPreco()));
            System.out.println("Relação incluida com sucesso, ID: "+idProdComp);
            CP.inserir(cod, idProds[k]);
            PC.inserir(idProds[k], cod);
         }
      }
   }

   public static void alteracaoCompra(Arquivo<Compra> arqCompra, Arquivo<Cliente> arqCliente, Arquivo<Produto> arqProduto, Arquivo<ProdutoComprado> arqProdutoComprado, IndiceComposto CP, IndiceComposto PC) throws Exception{
      System.out.println("Digite o ID da compra a ser alterada: ");
      int id = lerInt();
      if (arqProdutoComprado.checaExclusaoCompra(id) != false){ //Busca por todos os elementos do arquivo atras de um elemento com o mesmo id de categoria desejado.
         System.out.println("A compra não pode ser alterada porque possui dependencias. Remova as relações no menu de compras para permitir a exclusão. Voltando ao menu.");
         return;
      }
      if (arqCompra.ler(id) != null){
         arqCompra.excluir(id);
         inserirCompra(arqCompra, arqCliente, arqProduto, arqProdutoComprado, id, CP, PC);
         System.out.println("Compra alterada com sucesso.");
      }
      else System.out.println("\n Compra nao encontrada \n");
   }

   public static boolean buscarCompra(Arquivo<Compra> arqCompra) throws Exception{
      System.out.println("Digite o ID da compra a ser buscada: ");
      int id = lerInt();
      if (arqCompra.ler(id) != null){
         System.out.println("\n Compra encontrada:" + arqCompra.ler(id) + "\n");
         System.out.println(arqCompra.ler(id).toString());
         return true;
      }
      else {
         System.out.println("\n Compra nao encontrada. \n");
         return false;
      }
   }

   public static void exclusaoCompra(Arquivo<Compra> arqCompra, Arquivo<ProdutoComprado> arqProdutoComprado) throws Exception{
      System.out.println("Digite o ID da compra a ser deletada: ");
      int id = lerInt();
      if (arqProdutoComprado.checaExclusaoCompra(id) != false){ //Busca por todos os elementos do arquivo atras de um elemento com o mesmo id de categoria desejado.
         System.out.println("A compra nao pode ser excluida porque possui dependencias. Remova as relações no menu de compras para permitir a exclusão. Voltando ao menu.");
         return;
      }
      if (arqCompra.ler(id) != null){
         arqCompra.excluir(id);
         System.out.println("Compra excluida com sucesso.");
      }
      else System.out.println("\n Compra nao encontrada. \n");
   }

   public static void exclusaoRelacao(Arquivo<ProdutoComprado> arqProdutoComprado) throws Exception{
      System.out.println("Digite o ID da relação a ser deletada: ");
      int id = lerInt();
      if (arqProdutoComprado.ler(id) != null){
         arqProdutoComprado.excluir(id);
         System.out.println("Relação excluida com sucesso.");
         report--;
      }
      else System.out.println("\n Relação nao encontrada. \n");
   }

   //---------------------------------------------------------RELATÓRIOS

   public static void relatorio(Arquivo<Produto> arqProduto, Arquivo<Categoria> arqCategoria, Arquivo<Cliente> arqCliente, Arquivo<Compra> arqCompra, Arquivo<ProdutoComprado> arqProdutoComprado) throws Exception{
      int idCompra, idCliente, idProduto, qtdVendas;
      String nomeCliente, nomeProduto, dataCompra;

      Ranking[] ranking = new Ranking[25];
      for (int f = 0; f < ranking.length; f++){
         ranking[f] = new Ranking();
      }

      for (int i = 1; i <= report; i++){
         idCompra = arqProdutoComprado.ler(i).getIdComp(); //Arranja o id da compra
         idProduto = arqProdutoComprado.ler(i).getIdProd(); //Arranja o id do produto comprado
         idCliente = arqCompra.ler(idCompra).getIdC(); //Arranja o id do cliente

         nomeCliente = arqCliente.ler(idCliente).getNome();
         nomeProduto = arqProduto.ler(idProduto).getNome();
         dataCompra = arqCompra.ler(idCompra).getDataCompra();
         qtdVendas = arqProdutoComprado.ler(i).getQtd();

         System.out.println("Relação ["+i+"], Compra ["+idCompra+"] .......... Cliente: "+nomeCliente+"    | Produto: "+nomeProduto+" | Data: "+dataCompra);

         for(int k = 0; k < ranking.length; k++){
            if(nomeProduto.equals(ranking[k].getNome())){
               ranking[k].aumentaVenda(qtdVendas);
               k = ranking.length;
            }
            else if (ranking[k].getVendas() == 0){
               ranking[k].setNome(nomeProduto);
               ranking[k].setVendas(qtdVendas);
               k = ranking.length;
            }
         }

      }

      selecao(ranking);
      System.out.println("\n -------------------\nOs 5 mais vendidos foram: ");
      for(int j = 24; j > 19; j--){
         System.out.println(ranking[j].toString());
      }

   }

   public static void selecao(Ranking[] array) {
      for (int i = 0; i < (array.length - 1); i++) {
         int menor = i;
         for (int j = (i + 1); j < array.length; j++){
            if (array[menor].getVendas() > array[j].getVendas()){
               menor = j;
            }
         }
         Ranking temp = array[menor];
         array[menor] = array[i];
         array[i] = temp;
      }
   }

}