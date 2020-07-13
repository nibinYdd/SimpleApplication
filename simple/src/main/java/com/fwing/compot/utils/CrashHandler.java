package com.fwing.compot.utils;

import android.content.Intent;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.NonNull;


import com.fwing.compot.app.BaseApplication;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;


/**
 * 崩溃处理类
 *
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashTag";

    private volatile static CrashHandler mInstance;

    public static CrashHandler get() {
        if (mInstance == null) {
            synchronized (CrashHandler.class) {
                if (mInstance == null) {
                    mInstance = new CrashHandler();
                }
            }
        }
        return mInstance;
    }

    private CrashHandler() {
    }

    /** 系统默认的UncaughtException处理类 */
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    /** 是否拦截 */
    private boolean isInterceptor = true;
    /** 启动页的Class */
    private Class<?> mClass = null;

    /** 初始化代码 */
     public void init(){
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 设置是否对异常进行拦截
     * @param interceptor 是否拦截
     */
    public CrashHandler setInterceptor(boolean interceptor) {
        isInterceptor = interceptor;
        return this;
    }

    /** 设置启动页的class，app崩溃后会重启该类 */
    public CrashHandler setLauncherClass(@NonNull Class<?> c) {
        this.mClass = c;
        return this;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable t) {
        if (isInterceptor){// 用户处理
            write2File(t);
            try {
                Thread.sleep(2500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            t.printStackTrace();
            exceptionExit();
            return;
        }

        if (mDefaultHandler != null){
            mDefaultHandler.uncaughtException(thread, t);
            return;
        }
        exceptionExit();
    }

    /** 异常退出 */
    private void exceptionExit() {
        if (mClass != null && BaseApplication.getInstance() != null){
            // 闪退后重新打开启动页而不是当前页
            Intent intent = new Intent(BaseApplication.getInstance(), mClass);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            BaseApplication.getInstance().getApplicationContext().startActivity(intent);
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);  // 非0表示异常退出
    }

    private void write2File(Throwable t){
        final String time = new SimpleDateFormat("yyyy_MM_dd-HH_mm_ss").format(new Date());
        final StringBuilder sb = new StringBuilder();
        final String head = "************* Log Head ****************" +
                "\nTime Of Crash      : " + time +
                "\nDevice Manufacturer: " + Build.MANUFACTURER +
                "\nDevice Model       : " + Build.MODEL +
                "\nAndroid Version    : " + Build.VERSION.RELEASE +
                "\nAndroid SDK        : " + Build.VERSION.SDK_INT +
                "\nApp VersionName    : " + CommonUtils.getAppVersionName() +
                "\nApp VersionCode    : " + CommonUtils.getAppVersionCode() +
                "\n************* Log Head ****************\n\n";
        sb.append(head).append(getFullStackTrace(t));
        final String crashInfo = sb.toString();
        LogHelper.e(crashInfo);
        String packageName = BaseApplication.getInstance().getPackageName();
        String[] n = packageName.split("\\.");
        if(n.length>0){
            final String crashFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ds/"+n[n.length-1]+"/crash/";
            final String fileName =time + ".txt";
            CommonUtils.writeFileFromString(crashFilePath,fileName, crashInfo, true);
        }
    }

    private static final String LINE_SEP = System.getProperty("line.separator");

    private static String getFullStackTrace(Throwable throwable) {
        final List<Throwable> throwableList = new ArrayList<>();
        while (throwable != null && !throwableList.contains(throwable)) {
            throwableList.add(throwable);
            throwable = throwable.getCause();
        }
        final int size = throwableList.size();
        final List<String> frames = new ArrayList<>();
        List<String> nextTrace = getStackFrameList(throwableList.get(size - 1));
        for (int i = size; --i >= 0; ) {
            final List<String> trace = nextTrace;
            if (i != 0) {
                nextTrace = getStackFrameList(throwableList.get(i - 1));
                removeCommonFrames(trace, nextTrace);
            }
            if (i == size - 1) {
                frames.add(throwableList.get(i).toString());
            } else {
                frames.add(" Caused by: " + throwableList.get(i).toString());
            }
            frames.addAll(trace);
        }
        StringBuilder sb = new StringBuilder();
        for (final String element : frames) {
            sb.append(element).append(LINE_SEP);
        }
        return sb.toString();
    }

    private static List<String> getStackFrameList(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        final String stackTrace = sw.toString();
        final StringTokenizer frames = new StringTokenizer(stackTrace, LINE_SEP);
        final List<String> list = new ArrayList<>();
        boolean traceStarted = false;
        while (frames.hasMoreTokens()) {
            final String token = frames.nextToken();
            // Determine if the line starts with <whitespace>at
            final int at = token.indexOf("at");
            if (at != -1 && token.substring(0, at).trim().isEmpty()) {
                traceStarted = true;
                list.add(token);
            } else if (traceStarted) {
                break;
            }
        }
        return list;
    }

    private static void removeCommonFrames(final List<String> causeFrames, final List<String> wrapperFrames) {
        int causeFrameIndex = causeFrames.size() - 1;
        int wrapperFrameIndex = wrapperFrames.size() - 1;
        while (causeFrameIndex >= 0 && wrapperFrameIndex >= 0) {
            // Remove the frame from the cause trace if it is the same
            // as in the wrapper trace
            final String causeFrame = causeFrames.get(causeFrameIndex);
            final String wrapperFrame = wrapperFrames.get(wrapperFrameIndex);
            if (causeFrame.equals(wrapperFrame)) {
                causeFrames.remove(causeFrameIndex);
            }
            causeFrameIndex--;
            wrapperFrameIndex--;
        }
    }
}
