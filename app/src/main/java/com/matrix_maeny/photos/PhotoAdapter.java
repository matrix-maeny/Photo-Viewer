package com.matrix_maeny.photos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.viewHolder> {

    private Context context;
    private ArrayList<String> list;

    public PhotoAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.photo_view_model, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        File file = new File(list.get(position));

        if (file.exists()) {
            Picasso.get().load(file).placeholder(R.drawable.place_holder).into(holder.photoIv);

            holder.photoIv.setOnClickListener(v -> {

                Intent intent = new Intent(context.getApplicationContext(),ImageActivity.class);
                intent.putExtra("image",list.get(position));

                context.startActivity(intent);

            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        ImageView photoIv;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            photoIv = itemView.findViewById(R.id.photoIv);

        }
    }
}
