package com.example.findmein.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findmein.ProductDetails;
import com.example.findmein.R;
import com.example.findmein.model.RecentlyViewed;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecentlyViewedAdapter extends RecyclerView.Adapter<RecentlyViewedAdapter.RecentlyViewedViewHolder> {

    Context context;
    List<RecentlyViewed> recentlyViewedList;

    public RecentlyViewedAdapter(Context context, List<RecentlyViewed> recentlyViewedList) {
        this.context = context;
        this.recentlyViewedList = recentlyViewedList;
    }

    @NonNull
    @Override
    public RecentlyViewedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recently_viewed_items, parent, false);
        return new RecentlyViewedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentlyViewedViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.titulo.setText(recentlyViewedList.get(position).getTitulo());
        //holder.description.setText(recentlyViewedList.get(position).getDescription());
        holder.recompensa.setText(recentlyViewedList.get(position).getRecompensa());
        holder.categoria.setText(recentlyViewedList.get(position).getCategoria());
        //holder.fecha.setText(recentlyViewedList.get(position).getFecha());
        Picasso.get().load(recentlyViewedList.get(position).getBigimageurl()).into(holder.bg);

        holder.itemView.setOnClickListener(view -> {
            Intent i=new Intent(context, ProductDetails.class);
            i.putExtra("titulo", recentlyViewedList.get(position).getTitulo());
            i.putExtra("descripcion", recentlyViewedList.get(position).getDescription());
            i.putExtra("recompensa",recentlyViewedList.get(position).getRecompensa());
            i.putExtra("categoria",recentlyViewedList.get(position).getCategoria());
            i.putExtra("fecha",recentlyViewedList.get(position).getFecha());
            i.putExtra("latitud",recentlyViewedList.get(position).getLatitud());
            i.putExtra("longitud", recentlyViewedList.get(position).getLongitud());
            i.putExtra("image", recentlyViewedList.get(position).getBigimageurl());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return recentlyViewedList.size();
    }

    public  static class RecentlyViewedViewHolder extends RecyclerView.ViewHolder{

        TextView titulo, description, recompensa, categoria,fecha;
        ImageView bg;

        public RecentlyViewedViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.titulo);
            recompensa = itemView.findViewById(R.id.recompensa);
            categoria = itemView.findViewById(R.id.categoria);
            bg = itemView.findViewById(R.id.iv_reciente);
        }
    }

}
