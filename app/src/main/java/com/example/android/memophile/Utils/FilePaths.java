package com.example.android.memophile.Utils;

import android.os.Environment;

/**
 * Created by aditya314 on 3/29/2018.
 */

public class FilePaths {

    //"storage/emulated/0"
    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

    public String PICTURES = ROOT_DIR + "/Pictures";
    public String CAMERA = ROOT_DIR + "/DCIM/camera";

}
