package com.fwing.compot.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志工具类
 *
 */

public class LogHelper {
    private static final String TAG = "Fwing";
    public synchronized static void v(String msg) {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        String toStringBuffer = "[" + traceElement.getFileName() + " | " +
                traceElement.getLineNumber() + " | " + traceElement.getMethodName() + "]" + msg;
        Log.v(TAG,toStringBuffer);
    }

    public synchronized static void d(String msg) {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        String toStringBuffer = "[" + traceElement.getFileName() + " | " +
                traceElement.getLineNumber() + " | " + traceElement.getMethodName() + "] " + msg;
        Log.d(TAG,toStringBuffer);
    }

    public synchronized static void i(String msg) {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        String toStringBuffer = "[" + traceElement.getFileName() + " | " +
                traceElement.getLineNumber() + " | " + traceElement.getMethodName() + "] " + msg;
        Log.i(TAG,toStringBuffer);
    }

    public synchronized static void w(String msg) {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        String toStringBuffer = "[" + traceElement.getFileName() + " | " +
                traceElement.getLineNumber() + " | " + traceElement.getMethodName() + "] " + msg;
        Log.w(TAG,toStringBuffer);
    }

    public synchronized static void e(String msg) {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        String toStringBuffer = "[" + traceElement.getFileName() + " | " +
                traceElement.getLineNumber() + " | " + traceElement.getMethodName() + "] " + msg;
        Log.e(TAG,toStringBuffer);
    }


    public synchronized static void v(String...msgs) {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        StringBuilder toStringBuffer = new StringBuilder("[").append(traceElement.getFileName()).append(" | ")
                .append(traceElement.getLineNumber()).append(" | ").append(traceElement.getMethodName()).append("] ");
        if (msgs != null) {
            toStringBuffer.append("Log.v");
        }
        assert msgs != null;
        for (String msg : msgs) {
            toStringBuffer.append(String.format("===%s", msg));
        }
        Log.v(TAG,toStringBuffer.toString());
    }

    public synchronized static void d(String...msgs) {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        StringBuilder toStringBuffer = new StringBuilder("[").append(traceElement.getFileName()).append(" | ")
                .append(traceElement.getLineNumber()).append(" | ").append(traceElement.getMethodName()).append("] ");
        if (msgs != null) {
            toStringBuffer.append("Log.d");
        }
        assert msgs != null;
        for (String msg : msgs) {
            toStringBuffer.append(String.format("===%s", msg));
        }
        Log.d(TAG,toStringBuffer.toString());
    }

    public synchronized static void i(String...msgs) {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        StringBuilder toStringBuffer = new StringBuilder("[").append(traceElement.getFileName()).append(" | ")
                .append(traceElement.getLineNumber()).append(" | ").append(traceElement.getMethodName()).append("] ");
        if (msgs != null) {
            toStringBuffer.append("Log.i");
        }
        assert msgs != null;
        for (String msg : msgs) {
            toStringBuffer.append(String.format("===%s", msg));
        }
        Log.d(TAG,toStringBuffer.toString());
    }

    public synchronized static void w(String...msgs) {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        StringBuilder toStringBuffer = new StringBuilder("[").append(traceElement.getFileName()).append(" | ")
                .append(traceElement.getLineNumber()).append(" | ").append(traceElement.getMethodName()).append("] ");
        if (msgs != null) {
            toStringBuffer.append("Log.w");
        }
        assert msgs != null;
        for (String msg : msgs) {
            toStringBuffer.append(String.format("===%s", msg));
        }
        Log.w(TAG,toStringBuffer.toString());
    }

    public synchronized static void e(String...msgs) {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        StringBuilder toStringBuffer = new StringBuilder("[").append(traceElement.getFileName()).append(" | ")
                .append(traceElement.getLineNumber()).append(" | ").append(traceElement.getMethodName()).append("] ");
        if (msgs != null) {
            toStringBuffer.append("Log.e");
        }
        assert msgs != null;
        for (String msg : msgs) {
            toStringBuffer.append(String.format("===%s", msg));
        }
        Log.e(TAG,toStringBuffer.toString());
    }

    // 当前文件名
    public static String file() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        return traceElement.getFileName();
    }

    // 当前方法名
    public static String func() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        return traceElement.getMethodName();
    }

    // 当前行号
    public static int line() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        return traceElement.getLineNumber();
    }

    // 当前时间
    @SuppressLint("SimpleDateFormat")
    public static String time() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(now);
    }
}
