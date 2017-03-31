package com.example.sashok.lesssoner;

import android.os.Parcelable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sashok on 25.3.17.
 */

public class Word  implements Serializable{
    public static  String TABLE_NAME_PL_WORD="pl";
    public static  String TABLE_COLUMN_NAME_PL_WORD="words_pl";
    public static  String TABLE_COLUMN_NAME_RUS_WORD="words_rus";
    public static  String TABLE_COLUMN_NAME_ID="_id";
    public static  String TABLE_COLUMN_PL_ID="_pl_id";
    public static  String TABLE_NAME_RUS_WORD="rus";
    public static  String TABLE_COLUMN_DEGREE="degree";
    public static  String TABLE_COLUMN_PL_FAVOURITE="favourite";
    public  String pl_word;
    public int degree,id;
    public List<String> translation;
    public Word(){
        translation=new ArrayList<>();
    }
    private void writeObject(
            ObjectOutputStream aOutputStream
    ) throws IOException {
        //perform the default serialization for all non-transient, non-static fields
        aOutputStream.defaultWriteObject();
    }

    public boolean favourite;
}
