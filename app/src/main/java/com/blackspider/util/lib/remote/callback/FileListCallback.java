package com.blackspider.util.lib.remote.callback;
/*
 *  ****************************************************************************
 *  * Created by : Arhan Ashik on 11/27/2018 at 4:15 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * Last edited by : Arhan Ashik on 11/27/2018.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

import com.blackspider.retrofitfileupload.data.remote.response.RemoteFileEntity;

import java.util.List;

public interface FileListCallback {
    void onResponse(String error, List<RemoteFileEntity> files);
}
