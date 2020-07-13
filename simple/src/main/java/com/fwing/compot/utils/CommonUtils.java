package com.fwing.compot.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.fwing.compot.app.BaseApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * @author Fwing
 * @date 2019\12\14 0014 15:52
 */
public class CommonUtils {

    public static void showToast(Context context, String msg){
        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
    }

    public static boolean isNetworkConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) BaseApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        if(connectivityManager.getActiveNetworkInfo() == null){
            CommonUtils.showToast(BaseApplication.getInstance(),"请检查网络");
        }
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    public static void getAndroiodScreenProperty(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;// 屏幕宽度（像素）
        int height= dm.heightPixels; // 屏幕高度（像素）
        float density = dm.density;//屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;//屏幕密度dpi（120 / 160 / 240）
        //屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width/density);//屏幕宽度(dp)
        int screenHeight = (int)(height/density);//屏幕高度(dp)
        LogHelper.e(    screenWidth + "======" + screenHeight);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     * */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static boolean isEnglish(String charaString){
        return charaString.matches("^[a-zA-Z]*");
    }

    public static boolean isChinese(String str){
        String regEx = "[\\u4e00-\\u9fa5]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if(!m.find())
            return true;
        else
            return false;
    }


    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public static String getAppVersionName() {
        try {
            PackageManager pm = BaseApplication.getInstance().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(BaseApplication.getInstance().getPackageName(), 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getAppVersionCode() {
        try {
            PackageManager pm = BaseApplication.getInstance().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(BaseApplication.getInstance().getPackageName(), 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean writeFileFromString(String crashFilePath,
                                              String fileName,
                                              final String content,
                                              final boolean append) {
        File file = new File(crashFilePath);
        if (file == null || content == null) return false;
        if (!file.exists()) {
            file.mkdirs();
        }
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(new File(crashFilePath+fileName), append));
            bw.write(content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
