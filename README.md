# Enterprise Concurrency Framework

[![Java](https://img.shields.io/badge/Java-21+-ED8B00?style=flat&logo=java)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0+-6DB33F?style=flat&logo=spring-boot)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

Enterprise-grade multithreading framework demonstrating advanced concurrency patterns, thread pool management, async processing, and real-time data streaming using **Spring Boot**, **Executor Services**, and **concurrent data structures**.

## Overview

This framework showcases professional Java concurrency patterns used in enterprise applications:

- **Thread Pool Management**: Multiple executor services (fixed, cached, scheduled)
- **Asynchronous Processing**: CompletableFuture-based async task execution
- **Concurrent Data Structures**: ConcurrentHashMap, BlockingQueue, CopyOnWriteArrayList
- **Thread Synchronization**: ReadWriteLock, AtomicInteger for thread-safe operations
- **Spring Boot Integration**: Async support, REST APIs, and enterprise configuration

## Project Structure

```
src/
├── main/
│   ├── java/com/enterprise/concurrency/
│   │   ├── ConcurrencyApplication.java          # Spring Boot main application
│   │   ├── AsyncTaskService.java                # Async task execution service
│   │   ├── ThreadPoolManager.java               # Thread pool management
│   │   ├── ConcurrentDataStructuresUtil.java    # Concurrent utilities
│   │   └── TaskController.java                  # REST API endpoints
│   └── resources/
│       └── application.properties                # Spring Boot configuration
├── pom.xml                                       # Maven configuration
├── .gitignore
└── README.md
```

## Key Components

### 1. ConcurrencyApplication
Spring Boot application with executor bean configurations:
- `taskExecutor`: ThreadPoolTaskExecutor (10 core, 50 max threads)
- `heavyTaskExecutor`: For CPU-intensive operations (5 core, 20 max threads)

### 2. AsyncTaskService
Provides async task execution:
- `executeAsyncTask()`: Async task with CompletableFuture
- `executeHeavyTask()`: Heavy computational task
- `executeTaskSync()`: Synchronous task execution

### 3. ThreadPoolManager
Manages multiple executor services:
- **Fixed Thread Pool**: For regular tasks
- **Cached Thread Pool**: For variable workload
- **Scheduled Thread Pool**: For periodic tasks
- **ForkJoinPool**: For divide-and-conquer operations
- Task counting with AtomicInteger

### 4. ConcurrentDataStructuresUtil
Demonstrates thread-safe data structures:
- **ConcurrentHashMap**: Thread-safe caching
- **BlockingQueue**: Producer-consumer pattern
- **CopyOnWriteArrayList**: Event logging
- **AtomicInteger**: Atomic counter
- **ReadWriteLock**: Reader-writer synchronization

### 5. TaskController
REST API endpoints:
- `POST /api/tasks/async`: Submit async task
- `POST /api/tasks/heavy`: Submit heavy task
- `GET /api/tasks/status`: Get task status
- `POST /api/tasks/cache`: Cache data
- `GET /api/tasks/cache/{key}`: Retrieve cached data

## Technologies

- **Java 21**: Latest LTS version with virtual threads support
- **Spring Boot 3.2.0**: Enterprise framework
- **Maven**: Dependency management
- **Lombok**: Reduce boilerplate code
- **SLF4J**: Logging framework

## Building & Running

### Prerequisites
- Java 21 or higher
- Maven 3.8.1 or higher

### Build
```bash
mvn clean package
```

### Run
```bash
mvn spring-boot:run
```

The application starts on `http://localhost:8080`

## API Examples

### Submit Async Task
```bash
curl -X POST "http://localhost:8080/api/tasks/async?taskName=sample-task"
```

### Submit Heavy Task
```bash
curl -X POST "http://localhost:8080/api/tasks/heavy?iterations=1000000"
```

### Get Task Status
```bash
curl -X GET "http://localhost:8080/api/tasks/status"
```

### Cache Data
```bash
curl -X POST "http://localhost:8080/api/tasks/cache?key=user123&value=john_doe"
```

### Get Cached Data
```bash
curl -X GET "http://localhost:8080/api/tasks/cache/user123"
```

## Concurrency Patterns Demonstrated

1. **Thread Pool Pattern**: Managing fixed/dynamic thread pools
2. **Executor Service Pattern**: Task submission and execution
3. **CompletableFuture Pattern**: Non-blocking async operations
4. **Producer-Consumer Pattern**: BlockingQueue implementation
5. **Reader-Writer Pattern**: ReadWriteLock synchronization
6. **Atomic Operations**: Thread-safe counters
7. **Copy-on-Write Pattern**: CopyOnWriteArrayList

## Configuration

Edit `src/main/resources/application.properties` for customization:

```properties
server.port=8080
spring.task.execution.pool.core-size=10
spring.task.execution.pool.max-size=50
logging.level.com.enterprise.concurrency=DEBUG
```

## Performance Considerations

- **Thread Pool Sizing**: Core threads = 2 × CPU cores (for I/O-bound) or CPU cores (for CPU-bound)
- **Queue Capacity**: Size based on expected workload and memory constraints
- **Thread Safety**: Use concurrent collections to avoid synchronization bottlenecks
- **Monitoring**: Use JMX or Micrometer for thread pool metrics

## Best Practices

1. Always shutdown executor services in application lifecycle
2. Use bounded queues to prevent memory exhaustion
3. Handle InterruptedException properly in thread code
4. Prefer concurrent collections over synchronized collections
5. Use CompletableFuture for non-blocking operations
6. Monitor thread pool metrics in production
7. Use appropriate thread naming for debugging

## Future Enhancements

- [ ] Distributed task scheduling with Quartz
- [ ] Reactive streaming with Project Reactor
- [ ] Virtual threads (Project Loom)
- [ ] Task monitoring dashboard
- [ ] Performance benchmarks
- [ ] Unit and integration tests

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Author

**Swaroop Devulapalli**
- Full Stack Java Developer
- Expertise in concurrent programming, microservices, and cloud technologies

## Contributing

Contributions are welcome! Please feel free to fork and submit pull requests.

## References

- [Java Concurrency in Practice](https://jcip.net/)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [ExecutorService JavaDoc](https://docs.oracle.com/javase/21/docs/api/java.base/java/util/concurrent/ExecutorService.html)
- [CompletableFuture Guide](https://docs.oracle.com/javase/21/docs/api/java.base/java/util/concurrent/CompletableFuture.html)
