package comemo.example.yls.qqdemo.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by yls on 2016/12/29.
 */

public class ThreadUtils {
    public static Executor mExecutor = Executors.newFixedThreadPool(6);
    public static Handler mHandler = new Handler(Looper.getMainLooper());

    public static void runOnBackgroundThread(Runnable runnable) {
        mExecutor.execute(runnable);
    }

    public static void rinOnMainThread(Runnable runnable) {
        mHandler.post(runnable);
    }
}
