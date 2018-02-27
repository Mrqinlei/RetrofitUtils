package com.qinlei.retrofitutils;

import java.io.File;

/**
 * Created by qinlei
 * Created on 2018/2/27
 * Created description :
 */

public class FileUpload {
    private String key;
    private File file;

    public FileUpload(String key, File file) {
        this.key = key;
        this.file = file;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
