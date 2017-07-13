package com.yyd.ynjc.Activity;

import android.content.Context;
import android.widget.Toast;

public class GenericClass {

    public static void ShowToast(Context context, String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }
}
