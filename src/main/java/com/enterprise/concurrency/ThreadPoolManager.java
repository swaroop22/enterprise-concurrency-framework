package com.enterprise.concurrency;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class ThreadPoolManager {

    private final ExecutorService fixedThreadPool;
    private final ExecutorService cachedThreadPool;
    private final ScheduledExecutorService scheduledPool;
    private final ForkJoinPool forkJoinPool;
    private final AtomicInteger taskCount;

    public ThreadPoolManager() {
        this.fixedThreadPool = Executors.newFixedThreadPool(8);
        this.cachedThreadPool = Executors.newCachedThreadPool();
        this.scheduledPool = Executors.newScheduledThreadPool(4);
        this.forkJoinPool = ForkJoinPool.commonPool();
        this.taskCount = new AtomicInteger(0);
        log.info("ThreadPoolManager initialized");
    }

    public void submitTask(Runnable task) {
        taskCount.incrementAndGet();
        fixedThreadPool.submit(() -> {
            try {
                task.run();
            } finally {
                taskCount.decrementAndGet();
            }
        });
    }

    public <T> Future<T> submitCallable(Callable<T> callable) {
        taskCount.incrementAndGet();
        return fixedThreadPool.submit(() -> {
            try {
                return callable.call();
            } finally {
                taskCount.decrementAndGet();
            }
        });
    }

    public void scheduleTask(Runnable task, long delay, TimeUnit unit) {
        scheduledPool.schedule(task, delay, unit);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
        return scheduledPool.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    public int getActiveTaskCount() {
        return taskCount.get();
    }

    public void shutdown() {
        log.info("Shutting down thread pools");
        fixedThreadPool.shutdown();
        cachedThreadPool.shutdown();
        scheduledPool.shutdown();
        try {
            if (!fixedThreadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                fixedThreadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            fixedThreadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
