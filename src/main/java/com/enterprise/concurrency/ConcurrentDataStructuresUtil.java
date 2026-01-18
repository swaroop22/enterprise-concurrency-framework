package com.enterprise.concurrency;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class ConcurrentDataStructuresUtil {

    private final ConcurrentHashMap<String, Object> concurrentCache;
    private final BlockingQueue<String> taskQueue;
    private final CopyOnWriteArrayList<String> eventLog;
    private final AtomicInteger counter;
    private final ReadWriteLock lock;

    public ConcurrentDataStructuresUtil() {
        this.concurrentCache = new ConcurrentHashMap<>();
        this.taskQueue = new LinkedBlockingQueue<>(100);
        this.eventLog = new CopyOnWriteArrayList<>();
        this.counter = new AtomicInteger(0);
        this.lock = new ReentrantReadWriteLock();
    }

    public void putCache(String key, Object value) {
        concurrentCache.put(key, value);
        log.info("Cached: {} = {}", key, value);
    }

    public Object getCache(String key) {
        return concurrentCache.get(key);
    }

    public boolean addTask(String task) {
        try {
            return taskQueue.offer(task, 5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Task insertion interrupted", e);
            return false;
        }
    }

    public String takeTask() throws InterruptedException {
        return taskQueue.take();
    }

    public void logEvent(String event) {
        eventLog.add(event);
        log.info("Event logged: {}", event);
    }

    public List<String> getEventLog() {
        return new ArrayList<>(eventLog);
    }

    public void incrementCounter() {
        counter.incrementAndGet();
    }

    public int getCounterValue() {
        return counter.get();
    }

    public void performReadOperation() {
        lock.readLock().lock();
        try {
            log.info("Reading with read lock");
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void performWriteOperation() {
        lock.writeLock().lock();
        try {
            log.info("Writing with write lock");
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
