package com.example.leitorcodigo;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.leitorcodigo.com.google.zxing.integration.android.IntentIntegrator;
import com.example.leitorcodigo.com.google.zxing.integration.android.IntentResult;
import android.content.Intent;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private Button scanBtn, remover;
    private EditText removerLivro;
    private ArrayList<Livro> livros = new ArrayList<Livro>();
    MeuAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanBtn = (Button)findViewById(R.id.scan_button);//Inicializa botao de scan
        remover = (Button)findViewById(R.id.remover); //Inicializa botao de remover
        removerLivro = (EditText) findViewById(R.id.removerLivro); //...

        Livro l = new Livro("Harry Potter", "JK Rowling"); //Adiciona um livro a lista de livros
        livros.add(l);

        // set up the RecyclerView
        recyclerView = findViewById(R.id.meuRecyclerView);//Inicializa o RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MeuAdapter(this, livros);
        recyclerView.setAdapter(adapter);

        scanBtn.setOnClickListener(this);
        remover.setOnClickListener(this);
    }


    public void onClick(View v){
        switch(v.getId()) {
            case R.id.scan_button: //Ao clicar o botao de scan
                IntentIntegrator scanIntegrator = new IntentIntegrator(this);
                scanIntegrator.initiateScan();
            case R.id.remover:
                String nome = removerLivro.getText().toString();
                for (int i = 0; i < livros.size(); i++) {
                    if (livros.get(i).getNome().equals(nome)) {
                        livros.remove(i);
                        adapter.notifyDataSetChanged();
                        i = livros.size();
                        Toast torrada = Toast.makeText(getApplicationContext(), nome + " removido!", Toast.LENGTH_SHORT);
                        torrada.show();
                    }
                }
                removerLivro.setText("");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {//Pega o codigo ISBN pelo intentIntegrator
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            //Verifica se o codigo eh EAN_13
            if (scanContent != null && scanFormat != null && scanFormat.equalsIgnoreCase("EAN_13")) {
                String bookSearchString = "https://www.googleapis.com/books/v1/volumes?"+
                        "q=isbn:"+scanContent+"&key=AIzaSyAfGtTIkVQbywYexHitQSG-Jbri3jfGNeI"; //Verifica pelo Google Books que existe um livro
                new buscaLivro().execute(bookSearchString);//Se sim, pegar dados


            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Não é um livro!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Nenhum dado recebido!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private class buscaLivro extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... bookURLs) {//Busca a URL na API do Google Books
            StringBuilder bookBuilder = new StringBuilder();
            for (String bookSearchURL : bookURLs) {
                try {//Cria a string com as informacoes do livro a partir do JSON
                    HttpClient bookClient = new DefaultHttpClient();
                    HttpGet bookGet = new HttpGet(bookSearchURL);
                    HttpResponse bookResponse = bookClient.execute(bookGet);
                    StatusLine bookSearchStatus = bookResponse.getStatusLine();
                    if (bookSearchStatus.getStatusCode()==200) {
                        HttpEntity bookEntity = bookResponse.getEntity();
                        InputStream bookContent = bookEntity.getContent();
                        InputStreamReader bookInput = new InputStreamReader(bookContent);
                        BufferedReader bookReader = new BufferedReader(bookInput);
                        String lineIn;
                        while ((lineIn=bookReader.readLine())!=null) {//Cria a string
                            bookBuilder.append(lineIn);
                        }
                    }
                }
                catch(Exception e){ e.printStackTrace(); }
            }
            return bookBuilder.toString();
        }

        protected void onPostExecute(String result) {//Pega o titulo do livro.
            try{
                JSONObject resultObject = new JSONObject(result);
                JSONArray vetorLivro = resultObject.getJSONArray("items");
                JSONObject bookObject = vetorLivro.getJSONObject(0);
                JSONObject volumeObject = bookObject.getJSONObject("volumeInfo");

                String titulo = volumeObject.getString("title");//Busca o titulo
                String autor;

                StringBuilder construirAutor = new StringBuilder(""); //Para construir autor, verificar se existe mais de um e contruir a String
                try {
                    JSONArray vetorAutor = volumeObject.getJSONArray("authors");
                    for (int i = 0; i < vetorAutor.length(); i++) {
                        if (i > 0) construirAutor.append(", ");
                        construirAutor.append(vetorAutor.getString(i));
                    }
                    autor = construirAutor.toString();
                }
                catch (JSONException jse){
                    autor = "";
                }

                if(titulo != null){
                    Livro L = new Livro(titulo, autor);//Cria o livro
                    livros.add(L);
                    adapter.notifyDataSetChanged();//Avisa ao RecyclerView que os dados mudaram
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Livro nao encontrado no Google Books", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
