package com.example.rylan.weather.util;

import android.util.Log;

/**
 * Created by Rylan on 2016/10/20.
 */
public class LogUtil {
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;

    // 只需要修改 LEVEL 常量的值，就可以自由地控制日志的打印行为了。
    // 比如让LEVEL 等于 VERBOSE 就可以把所有的日志都打印出来，
    // 让 LEVEL 等于 WARN 就可以只打印警告以上级别的日志，
    // 让 LEVEL 等于 NOTHING 就可以把所有日志都屏蔽掉。

    public static int LEVEL = WARN;// 其他 int 都是 final 的，除了 LEVEL

    public static void log(String tag, String msg, final int level) {

        if (level > LEVEL) {
            switch (level) {
                case VERBOSE:

                    Log.v(tag, msg);
                    break;
                case DEBUG:

                    Log.d(tag, msg);
                    break;
                case INFO:
                    Log.i(tag, msg);
                    break;
                case WARN:
                    Log.w(tag, msg);
                    break;
                case ERROR:
                    Log.e(tag, msg);
                    break;
                case NOTHING:
                default:
                    break;
            }
        }
    }

//    public static void log(String tag, String msg) {
//        log(tag, msg, DEBUG);
//    }
}
