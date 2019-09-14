package com.example.leitorcodigo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MeuAdapter extends RecyclerView.Adapter<MeuAdapter.ViewHolder> {

    private List<Livro> mData;
    private LayoutInflater mInflater;

    // Passa dados pro construtor
    MeuAdapter(Context context, List<Livro> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // Infla o layout de coluna
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.coluna, parent, false);
        return new ViewHolder(view);
    }

    // Coloca os dados na coluna via o metodo toString
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Livro livro = mData.get(position);
        holder.myTextView.setText(livro.toString());
    }

    // Conta numero de colunas
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // Guarda e recicla a view conforme o scroll é feito
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.colunaRecycler);
        }
    }

}
