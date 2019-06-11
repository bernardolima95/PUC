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
    private Button scanBtn;
    private TextView formatTxt, contentTxt;
    private ArrayList<Livro> livros = new ArrayList<Livro>();
    MeuAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanBtn = (Button)findViewById(R.id.scan_button);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);

        Livro l = new Livro("Harry Potter");
        livros.add(l);

        // set up the RecyclerView
        recyclerView = findViewById(R.id.meuRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MeuAdapter(this, livros);
        recyclerView.setAdapter(adapter);

        scanBtn.setOnClickListener(this);
    }

    public void onClick(View v){
        if(v.getId()==R.id.scan_button){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            if (scanContent != null && scanFormat != null && scanFormat.equalsIgnoreCase("EAN_13")) {
//                formatTxt.setText("FORMAT: " + scanFormat);
//                contentTxt.setText("CONTENT: " + scanContent);
                String bookSearchString = "https://www.googleapis.com/books/v1/volumes?"+
                        "q=isbn:"+scanContent+"&key=AIzaSyAfGtTIkVQbywYexHitQSG-Jbri3jfGNeI";
                new GetBookInfo().execute(bookSearchString);


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

    private class GetBookInfo extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... bookURLs) {
            StringBuilder bookBuilder = new StringBuilder();
            for (String bookSearchURL : bookURLs) {
                try {
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
                        while ((lineIn=bookReader.readLine())!=null) {
                            bookBuilder.append(lineIn);
                        }
                        //we have a result
                    }
                }
                catch(Exception e){ e.printStackTrace(); }
            }
            return bookBuilder.toString();
        }

        protected void onPostExecute(String result) {
            try{
                JSONObject resultObject = new JSONObject(result);
                JSONArray bookArray = resultObject.getJSONArray("items");
                JSONObject bookObject = bookArray.getJSONObject(0);
                JSONObject volumeObject = bookObject.getJSONObject("volumeInfo");
                Livro L = new Livro(volumeObject.getString("title"));
                livros.add(L);
                adapter.notifyDataSetChanged();

//parse results
            }
            catch (Exception e) {
                e.printStackTrace();
            }
//parse search results
        }
    }

}
