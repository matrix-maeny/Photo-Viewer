package com.matrix_maeny.photos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.matrix_maeny.photos.databinding.ActivitySubFolderBinding;

import java.util.ArrayList;
import java.util.Objects;

public class SubFolderActivity extends AppCompatActivity {

    private ActivitySubFolderBinding binding;
    private PhotoAdapter adapter;
    private ArrayList<String> list;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubFolderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        position = getIntent().getIntExtra("position", -1);

        Objects.requireNonNull(getSupportActionBar()).setTitle(FolderData.foldersNameList.get(position));

        initialize();

    }

    private void initialize() {
        list = FolderData.foldersImageList.get(position);
        if (list.isEmpty()) {
            binding.emptyViewSFA.setVisibility(View.VISIBLE);
            return;
        } else {
            binding.emptyViewSFA.setVisibility(View.GONE);
        }

        adapter = new PhotoAdapter(SubFolderActivity.this, list);
        binding.recyclerViewSFA.setLayoutManager(new GridLayoutManager(SubFolderActivity.this, 2));
        binding.recyclerViewSFA.setAdapter(adapter);
    }
}