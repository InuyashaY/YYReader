package yzl.swu.yyreader.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.Toast;

import yzl.swu.yyreader.App;

public class Utils {


    /**
     * 获取屏幕高度(px)
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int dpToPx(Context context,int dp){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,metrics);
    }

    public static int getImageid(Context context,String imageName){
        int id=0;
        ApplicationInfo appInfo = context.getApplicationInfo();
        id = context.getResources().getIdentifier(imageName, "drawable", appInfo.packageName);
        return id;
    }

    public static void showToast(String msg){
        Toast.makeText(App.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

}
