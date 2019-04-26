package com.example.plugin1;

import android.os.Environment;

import java.util.HashSet;

/**
 * Created by Administrator on 2019/3/26.
 */

public class FileUtil {
    public static boolean hasSdcard(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static void delete(){}
}
