package com.blackspider.util.lib.remote;
/*
 *  ****************************************************************************
 *  * Created by : Arhan Ashik on 11/27/2018 at 1:14 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * Last edited by : Arhan Ashik on 11/27/2018.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

import com.blackspider.retrofitfileupload.data.remote.response.FileUploadResponse;
import com.blackspider.retrofitfileupload.data.remote.response.RemoteFileListResponse;
import com.blackspider.retrofitfileupload.data.remote.response.MultipleFileUploadResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiClient {
    @Multipart
    @POST("Api.php?call=upload")
    Call<FileUploadResponse> uploadFile(@Part("file\"; filename=\"my_file.jpg\" ") RequestBody file,
                                        @Part("desc") RequestBody desc);

    @Multipart
    @POST("Api.php?call=multiple_upload")
    Call<MultipleFileUploadResponse> uploadMultipleFiles(@Part List<MultipartBody.Part> files);

    //get the file names from the server
    @GET("Api.php?call=getFiles")
    Call<RemoteFileListResponse> getFiles();
}
