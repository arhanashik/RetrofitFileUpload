package com.blackspider.util.lib.remote.callback;
/*
 *  ****************************************************************************
 *  * Created by : Arhan Ashik on 11/27/2018 at 1:52 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * Last edited by : Arhan Ashik on 11/27/2018.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

public interface FileUploadCallback {
    void onResponse(String error, String urls);
}
