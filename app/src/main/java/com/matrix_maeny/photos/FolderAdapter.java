package com.matrix_maeny.photos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.viewHolder> {

    private Context context;
    private ArrayList<String> namesList;

    public FolderAdapter(Context context, ArrayList<String> namesList) {
        this.context = context;
        this.namesList = namesList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.folder_view_model, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        holder.folderName.setText(namesList.get(position));
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context.getApplicationContext(),SubFolderActivity.class);
            intent.putExtra("position",holder.getAdapterPosition());
            context.startActivity(intent);
            // go to specific folder activity
        });
    }

    @Override
    public int getItemCount() {
        return namesList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView folderName;
        CardView cardView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            folderName = itemView.findViewById(R.id.folderNameTv);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }
}
