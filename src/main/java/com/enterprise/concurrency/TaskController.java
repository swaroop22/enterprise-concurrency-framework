package com.enterprise.concurrency;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.CompletableFuture;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final AsyncTaskService asyncTaskService;
    private final ThreadPoolManager threadPoolManager;
    private final ConcurrentDataStructuresUtil dataStructuresUtil;

    public TaskController(
            AsyncTaskService asyncTaskService,
            ThreadPoolManager threadPoolManager,
            ConcurrentDataStructuresUtil dataStructuresUtil) {
        this.asyncTaskService = asyncTaskService;
        this.threadPoolManager = threadPoolManager;
        this.dataStructuresUtil = dataStructuresUtil;
    }

    @PostMapping("/async")
    public ResponseEntity<Map<String, String>> submitAsyncTask(@RequestParam String taskName) {
        log.info("Received async task: {}", taskName);
        CompletableFuture<String> result = asyncTaskService.executeAsyncTask(taskName);
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "Task submitted");
        response.put("task", taskName);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/heavy")
    public ResponseEntity<Map<String, Object>> submitHeavyTask(@RequestParam long iterations) {
        log.info("Received heavy task with {} iterations", iterations);
        asyncTaskService.executeHeavyTask(iterations);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "Heavy task submitted");
        response.put("iterations", iterations);
        response.put("activeThreads", threadPoolManager.getActiveTaskCount());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getTaskStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("activeTasks", threadPoolManager.getActiveTaskCount());
        response.put("counterValue", dataStructuresUtil.getCounterValue());
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cache")
    public ResponseEntity<Map<String, String>> cacheData(
            @RequestParam String key,
            @RequestParam String value) {
        dataStructuresUtil.putCache(key, value);
        
        Map<String, String> response = new HashMap<>();
        response.put("key", key);
        response.put("value", value);
        response.put("status", "Cached");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cache/{key}")
    public ResponseEntity<Map<String, Object>> getCachedData(@PathVariable String key) {
        Object value = dataStructuresUtil.getCache(key);
        
        Map<String, Object> response = new HashMap<>();
        response.put("key", key);
        response.put("value", value);
        return ResponseEntity.ok(response);
    }
}
