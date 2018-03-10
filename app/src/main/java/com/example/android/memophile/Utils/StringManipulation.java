package com.example.android.memophile.Utils;

/**
 * Created by aditya314 on 3/10/2018.
 */

public class StringManipulation {

    public static String expandUsername(String username){
        return username.replace("_", " ");
    }

    public static String condenseUsername(String username){
        return username.replace(" " , "_");
    }
}
