package com.blackspider.retrofitfileupload.data.remote.response;
/*
 *  ****************************************************************************
 *  * Created by : Arhan Ashik on 11/27/2018 at 4:12 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * Last edited by : Arhan Ashik on 11/27/2018.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RemoteFileListResponse {
    private boolean error;
    private String message;
    @SerializedName("files")
    private List<RemoteFileEntity> files;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<RemoteFileEntity> getFiles() {
        return files;
    }

    public void setFiles(List<RemoteFileEntity> files) {
        this.files = files;
    }
}
