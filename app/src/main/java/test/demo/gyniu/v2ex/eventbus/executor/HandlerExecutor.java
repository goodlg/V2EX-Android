package test.demo.gyniu.v2ex.eventbus.executor;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

import test.demo.gyniu.v2ex.utils.ExecutorUtils;

/**
 * Created by uiprj on 17-6-16.
 */
public class HandlerExecutor implements Executor {
    @Override
    public void execute(@NonNull Runnable command) {
        ExecutorUtils.runInUiThread(command);
    }
}
