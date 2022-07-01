package com.matrix_maeny.photos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.matrix_maeny.photos.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Currency;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private final int STORAGE_PERMISSION_CODE = 1;
    final Handler handler = new Handler();
    private ArrayList<String> list;
    private ArrayList<String> foldersNameList;
    private ArrayList<ArrayList<String>> foldersImageList;
    private FolderAdapter adapter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermission();
        } else {
            startGettingImages();
        }


    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Permission needed")
                    .setTitle("Storage permission needed to access images")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission denied..! Please enable manually", Toast.LENGTH_SHORT).show();
            } else {
                startGettingImages();
            }
        }
    }

    private void startGettingImages() {
//        new Thread() {
//            public void run() {
//                getImages();
////                getFolders();
//            }
//        }.start();

        new Thread() {
            public void run() {
//                getImages();
                getFolders();
            }
        }.start();
    }

    private void getFolders() {
        boolean sdPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        if (sdPresent) {
            handler.post(() -> progressDialog.show());

            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
            final String orderBy = MediaStore.Images.Media._ID;

            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    columns, null, null, orderBy);

//            int count = cursor.getCount();

            while (cursor.moveToNext()) {

                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

                String tempPath = cursor.getString(columnIndex);

                int lastIndex = tempPath.lastIndexOf('/');
                int firstIndex = tempPath.lastIndexOf('/', lastIndex - 1);

                String folderName = tempPath.substring(firstIndex + 1, lastIndex);

//                foldersNameList.add(folderName);

                if (!foldersNameList.contains(folderName)) {
                    foldersNameList.add(folderName);
                    foldersImageList.add(new ArrayList<>());
                }

                foldersImageList.get(foldersNameList.indexOf(folderName)).add(cursor.getString(columnIndex));
            }
            refreshData();


            cursor.close();
            handler.post(() -> progressDialog.dismiss());

            FolderData.foldersImageList = foldersImageList;
            FolderData.foldersNameList = foldersNameList;

        }

        handler.post(()->{
           if(foldersNameList.isEmpty()){
              binding.emptyView.setVisibility(View.VISIBLE);
           }else{
               binding.emptyView.setVisibility(View.GONE);

           }
        });

    }

//    private void getImages() {
//
//        boolean sdPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
//
//        if (sdPresent) {
//            handler.post(() -> progressDialog.show());
//
//            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
//            final String orderBy = MediaStore.Images.Media._ID;
//
//            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                    columns, null, null, orderBy);
//
//            int count = cursor.getCount();
//
//            while (cursor.moveToNext()) {
//
//                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
//                list.add(cursor.getString(columnIndex));
//                Log.i("imagePath", cursor.getString(columnIndex));
//            }
//            refreshData();
//
//
//            cursor.close();
//            handler.post(() -> progressDialog.dismiss());
//
//        }
//    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshData() {
        handler.post(() -> adapter.notifyDataSetChanged());
    }

    private void initialize() {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading...");

        binding.emptyView.setVisibility(View.GONE);
        foldersNameList = new ArrayList<>();
        foldersImageList = new ArrayList<>();
        list = new ArrayList<>();
        adapter = new FolderAdapter(MainActivity.this, foldersNameList);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // go to about activity
        startActivity(new Intent(MainActivity.this,AboutActivity.class));
        return super.onOptionsItemSelected(item);
    }
}