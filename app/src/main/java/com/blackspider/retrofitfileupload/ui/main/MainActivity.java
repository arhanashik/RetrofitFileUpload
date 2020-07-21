package com.blackspider.retrofitfileupload.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.blackspider.retrofitfileupload.R;
import com.blackspider.retrofitfileupload.data.local.FileEntity;
import com.blackspider.retrofitfileupload.data.remote.response.RemoteFileEntity;
import com.blackspider.retrofitfileupload.ui.main.adapter.FilesAdapter;
import com.blackspider.util.helper.Util;
import com.blackspider.util.lib.remote.RemoteUtil;
import com.blackspider.util.lib.remote.callback.FileListCallback;
import com.blackspider.util.lib.remote.callback.FileUploadCallback;
import com.blackspider.util.lib.remote.callback.MultipleFileUploadCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;

public class MainActivity extends AppCompatActivity{

    private static final int STORAGE_READ_PERMISSION_CODE = 11;
    private static final int CHOOSE_FILE_CODE = 22;

    private FilesAdapter adapter;
    private List<Uri> filesUri = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if(hasStorageReadPermission()) chooseFile();
            else requestStorageReadPermission();
        });

        //initialize recycler view
        RecyclerView recyclerView = findViewById(R.id.rv_files);
        adapter = new FilesAdapter();
        recyclerView.setAdapter(adapter);

        getFiles();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            if(filesUri.size() > 0){
                if(filesUri.size() > 1) {
                    showMessage("Uploading all " + filesUri.size() + " files");
                    uploadMultipleFiles(filesUri);
                } else {
                    showMessage("Uploading...");
                    uploadFile(filesUri.get(0), "Single Upload");
                }
            }else {
                showMessage("Select files to upload");
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Get the selected files
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CHOOSE_FILE_CODE){
            if(resultCode == RESULT_OK && data != null && data.getData() != null) {
                Log.e("onActivityResult", data.getData().toString());
                filesUri.add(data.getData());

                showMessage(filesUri.size() + " files selected!");
            }else {
                showMessage("No file selected!");
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Check if user is granted storage access permission. If yes, read the files
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if(requestCode == STORAGE_READ_PERMISSION_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                chooseFile();
            }else {
                showMessage("Storage permission is required!");
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Check storage permission is granted or not
     * */
    private boolean hasStorageReadPermission(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Request storage permission
     * */
    private void requestStorageReadPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                STORAGE_READ_PERMISSION_CODE);
    }

    /**
     * Choose file from the device storage
     * */
    private void chooseFile(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, CHOOSE_FILE_CODE);
    }

    /**
     * Upload single file to the server
     * */
    private void uploadFile(@NonNull final Uri fileUri, String desc) {

        String realPath = Util.getRealPathFromURI(this , fileUri);
        if(TextUtils.isEmpty(realPath)){
            showMessage("File not found!");
            return;
        }

        File file = new File(realPath);
        String mediaType = getContentResolver().getType(fileUri);
        if(mediaType == null) {
            showMessage("Invalid file format!");
            return;
        }

        RemoteUtil.on().uploadFile(file,
                (MediaType.parse(mediaType)),
                desc,
                new FileUploadCallback() {
                    @Override
                    public void onResponse(String error, String url) {
                        if(error == null){
                            showMessage("File Uploaded Successfully...");
                            filesUri.remove(fileUri);
                            getFiles();
                        }else {
                            showMessage(error);
                        }
                    }
                });
    }

    /**
     * Upload multiple files to the server
     * */
    private void uploadMultipleFiles(List<Uri> fileUri){
        List<FileEntity> fileEntities = new ArrayList<>();

        for(Uri uri : fileUri){
            String realPath = Util.getRealPathFromURI(this , uri);
            if(!TextUtils.isEmpty(realPath)){
                File file = new File(realPath);
                String mediaType = getContentResolver().getType(uri);
                if(mediaType != null) {
                    fileEntities.add(new FileEntity(file, mediaType));
                }
            }
        }

        if(fileEntities.size() > 0){
            RemoteUtil.on().uploadMultipleFiles(fileEntities, new MultipleFileUploadCallback() {
                @Override
                public void onResponse(String error, List<String> urls) {
                    if(error == null){
                        showMessage("Total " + urls.size() + " file uploaded");
                        filesUri.clear();
                        getFiles();
                    }else {
                        showMessage(error);
                    }
                }
            });
        }else {
            showMessage("No valid file found!");
        }
    }

    /**
     * Get files from the server
     * */
    private void getFiles() {
        RemoteUtil.on().getFileList(new FileListCallback() {
            @Override
            public void onResponse(String error, List<RemoteFileEntity> files) {
                if(error == null){
                    showMessage("Total: " + files.size() + " files found!");
                    adapter.setData(files);
                }else {
                    showMessage(error);
                }
            }
        });
    }

    private void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
