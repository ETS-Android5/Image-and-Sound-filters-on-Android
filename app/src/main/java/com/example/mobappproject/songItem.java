package com.example.mobappproject;

import android.widget.ImageView;
import android.widget.TextView;

public class songItem {

    private int imageRes;
    private String mName;

    public songItem(int imgRes, String songName){
        imageRes = imgRes;
        mName = songName;
    }

    public int getImgRes(){
        return imageRes;
    }

    public String getmName(){
        return mName;
    }
}
