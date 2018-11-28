package com.blackspider.retrofitfileupload.data.local;
/*
 *  ****************************************************************************
 *  * Created by : Arhan Ashik on 11/27/2018 at 4:44 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * Last edited by : Arhan Ashik on 11/27/2018.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

import java.io.File;

public class FileEntity {
    private File file;
    private String fileType;

    public FileEntity(File file, String fileType) {
        this.file = file;
        this.fileType = fileType;
    }

    public File getFile() {
        return file;
    }

    public String getFileType() {
        return fileType;
    }
}
