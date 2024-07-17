package com.example.vmsv1;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BackgroundTaskExecutor {
    private static final ExecutorService executor = Executors.newFixedThreadPool(4);

    public static void runOnBackgroundThread(Runnable task) {
        executor.submit(task);
    }

    public static <T> Future<T> submit(Runnable task, T result) {
        return executor.submit(task, result);
    }

    public static void shutdown() {
        executor.shutdown();
    }
}
