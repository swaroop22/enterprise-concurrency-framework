package com.enterprise.concurrency;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Service
public class AsyncTaskService {

    private final Executor taskExecutor;
    private final Executor heavyTaskExecutor;

    public AsyncTaskService(
            @Qualifier("taskExecutor") Executor taskExecutor,
            @Qualifier("heavyTaskExecutor") Executor heavyTaskExecutor) {
        this.taskExecutor = taskExecutor;
        this.heavyTaskExecutor = heavyTaskExecutor;
    }

    @Async("taskExecutor")
    public CompletableFuture<String> executeAsyncTask(String taskName) {
        log.info("Starting async task: {} on thread: {}", taskName, Thread.currentThread().getName());
        try {
            Thread.sleep(2000);
            String result = "Task " + taskName + " completed";
            log.info("Completed async task: {}", taskName);
            return CompletableFuture.completedFuture(result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Task interrupted: {}", taskName, e);
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async("heavyTaskExecutor")
    public CompletableFuture<Long> executeHeavyTask(long iterations) {
        log.info("Starting heavy task with {} iterations on thread: {}", iterations, Thread.currentThread().getName());
        long result = 0;
        for (long i = 0; i < iterations; i++) {
            result += i;
        }
        log.info("Completed heavy task");
        return CompletableFuture.completedFuture(result);
    }

    public void executeTaskSync(String taskName) {
        log.info("Executing sync task: {} on thread: {}", taskName, Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Sync task interrupted", e);
        }
    }
}
