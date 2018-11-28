package com.blackspider.util.lib.remote;
/*
 *  ****************************************************************************
 *  * Created by : Arhan Ashik on 11/27/2018 at 1:46 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * Last edited by : Arhan Ashik on 11/27/2018.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

import android.support.annotation.NonNull;
import android.util.Log;

import com.blackspider.retrofitfileupload.data.local.FileEntity;
import com.blackspider.retrofitfileupload.data.remote.response.FileUploadResponse;
import com.blackspider.retrofitfileupload.data.remote.response.RemoteFileListResponse;
import com.blackspider.util.lib.remote.callback.FileListCallback;
import com.blackspider.util.lib.remote.callback.FileUploadCallback;
import com.blackspider.util.lib.remote.callback.MultipleFileUploadCallback;
import com.blackspider.retrofitfileupload.data.remote.response.MultipleFileUploadResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteUtil {
    private final String BASE_URL = "http://192.168.2.209/FileUploadApi/";

    private static RemoteUtil remoteUtil;
    private ApiClient apiClient;
    private Retrofit retrofit;

    private RemoteUtil(){
        if(retrofit == null){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        if(apiClient == null){
            apiClient = retrofit.create(ApiClient.class);
        }
    }

    public static RemoteUtil on(){
        if(remoteUtil == null) remoteUtil = new RemoteUtil();

        return remoteUtil;
    }

    public void uploadFile(File file, MediaType mediaType, String desc,
                           final FileUploadCallback callback){

        RequestBody requestFile = RequestBody.create(mediaType, file);
        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), desc);

        Call<FileUploadResponse> call = apiClient.uploadFile(requestFile, descBody);

        call.enqueue(new Callback<FileUploadResponse>() {
            @Override
            public void onResponse(@NonNull Call<FileUploadResponse> call,
                                   @NonNull Response<FileUploadResponse> response) {

                if(callback != null){
                    FileUploadResponse fileUploadResponse = response.body();
                    if (fileUploadResponse != null && !fileUploadResponse.isError()) {
                        if(fileUploadResponse.isError()){
                            callback.onResponse(fileUploadResponse.getMessage(), null);
                        }else {
                            callback.onResponse(null, fileUploadResponse.getUrl());
                        }
                    } else {
                        if (response.errorBody() != null) {
                            callback.onResponse(response.errorBody().toString(), null);
                        }else {
                            callback.onResponse("Ops! Upload failed.", null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FileUploadResponse> call, @NonNull Throwable t) {
                if(callback != null) callback.onResponse(t.getMessage(), null);
            }
        });
    }

    public void uploadMultipleFiles(List<FileEntity> fileEntities,
                                    final MultipleFileUploadCallback callback){

        List<MultipartBody.Part> parts = new ArrayList<>();

        int i = 0;
        for (FileEntity fileEntity : fileEntities){
            RequestBody requestBody = RequestBody.create(MediaType.parse(fileEntity.getFileType()),
                    fileEntity.getFile());

            MultipartBody.Part part = MultipartBody.Part.createFormData("files["+i+"]",
                    fileEntity.getFile().getName(), requestBody);

            parts.add(part);

            i++;
        }

        Call<MultipleFileUploadResponse> call = apiClient.uploadMultipleFiles(parts);

        call.enqueue(new Callback<MultipleFileUploadResponse>() {
            @Override
            public void onResponse(@NonNull Call<MultipleFileUploadResponse> call,
                                   @NonNull Response<MultipleFileUploadResponse> response) {

                Log.e("uploadMultipleFiles()", response.toString());
                if(callback != null){
                    MultipleFileUploadResponse multipleFileUploadResponse = response.body();
                    if (multipleFileUploadResponse != null) {
                        if(multipleFileUploadResponse.isError()){
                            callback.onResponse(multipleFileUploadResponse.getMessage(), null);
                        }else {
                            callback.onResponse(null, multipleFileUploadResponse.getUrls());
                        }
                    } else {
                        if (response.errorBody() != null) {
                            callback.onResponse(response.errorBody().toString(), null);
                        }else {
                            callback.onResponse(response.toString(), null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MultipleFileUploadResponse> call, @NonNull Throwable t) {
                if(callback != null) callback.onResponse(t.getMessage(), null);
            }
        });
    }

    public void getFileList(final FileListCallback callback){
        Call<RemoteFileListResponse> call = apiClient.getFiles();

        call.enqueue(new Callback<RemoteFileListResponse>() {
            @Override
            public void onResponse(@NonNull Call<RemoteFileListResponse> call,
                                   @NonNull Response<RemoteFileListResponse> response) {

                if(callback != null){
                    RemoteFileListResponse remoteFileListResponse = response.body();

                    if(remoteFileListResponse != null){
                        if(remoteFileListResponse.isError()){
                            callback.onResponse(remoteFileListResponse.getMessage(), null);
                        }else {
                            callback.onResponse(null, remoteFileListResponse.getFiles());
                        }

                    }else {
                        if (response.errorBody() != null) {
                            callback.onResponse(response.errorBody().toString(), null);
                        }else {
                            callback.onResponse("Ops! Can't get files.", null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RemoteFileListResponse> call, @NonNull Throwable t) {
                if(callback != null) callback.onResponse(t.getMessage(), null);
            }
        });
    }
}
