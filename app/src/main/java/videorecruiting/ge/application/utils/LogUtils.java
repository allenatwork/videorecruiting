package videorecruiting.ge.application.utils;

import android.util.Log;

/**
 * Created by thangtranquyet on 3/16/16.
 */
public class LogUtils {
    public static void logI (String tag, String message){
        if(tag != null && message != null)
            Log.i(tag, message);
    }
    public static void logD (String tag, String message){
        if(tag != null && message != null)
            Log.d(tag, message);
    }
    public static void logE (String tag, String message){
        if(tag != null && message != null)
            Log.e(tag, message);
    }
    public static void logD (String message){
        if(message != null)
            Log.d("DEBUG", message);
    }
}
