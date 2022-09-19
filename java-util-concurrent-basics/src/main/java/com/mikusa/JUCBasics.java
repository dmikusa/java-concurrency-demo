package com.mikusa;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class JUCBasics {
    public static void main(String[] args) throws InterruptedException, IOException {
        if (args.length == 0) {
            System.out.println("USAGE:");
            System.out.println("    java com.mikusa.JUCBasics <type>");
            System.out.println();
            System.out.println(
                    "Types: `atomics`, `thread-pool`, `producer-consumer`, `parallel-stream`, and `schedule-task`");
            System.out.println();
            System.exit(1);
        }

        Instant start = Instant.now();

        switch (args[0]) {
            case "atomics":
                runAtomics();
                break;
            case "thread-pool":
                runAtomicsOnPool();
                break;
            case "producer-consumer":
                runConcurrentCollection();
                break;
            case "parallel-stream":
                runParallelStream();
                break;
            case "schedule-task":
                scheduleTask();
                break;
            default:
                System.out.println("Invalid option [" + args[0] + "]");
        }

        System.out.println("Total run time: " + Duration.between(start, Instant.now()));
    }

    /*
     * Count from many threads.
     * 
     * Same example as in basic-threads, but requires much less work.
     * 
     */
    private static void runAtomics() throws InterruptedException {
        AtomicInteger count = new AtomicInteger(0);

        ArrayList<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(() -> {
                for (int j = 0; j < 100_000; j++) {
                    count.incrementAndGet();
                }
            }));
        }

        threads.forEach(t -> t.start());

        waitForThreadsToStop(threads);

        System.out.println("Count: " + count.get());
    }

    private static void waitForThreadsToStop(List<Thread> threads) throws InterruptedException {
        for (Thread t : threads) {
            try {
                t.join(1000);
            } catch (InterruptedException ex) {
                t.interrupt();
                t.join();
            }
        }
    }

    /*
     * Count from many threads, but using a thread pool.
     * 
     * Same as `runAtomics()` but uses a thread pool so it's even easier.
     * 
     */
    private static void runAtomicsOnPool() throws InterruptedException {
        AtomicInteger count = new AtomicInteger(0);
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        CompletionService<String> completionService = new ExecutorCompletionService<>(threadPool);

        for (int i = 0; i < 10; i++) {
            completionService.submit(() -> {
                for (int j = 0; j < 100_000; j++) {
                    count.incrementAndGet();
                }

                return "DONE";
            });
        }

        for (int i = 0; i < 10; i++) {
            completionService.take(); // we don't care about the result here, but could get the return result
        }

        shutdownAndAwaitTermination(threadPool);

        System.out.println("Count: " + count.get());
    }

    private static final int MESSAGE_COUNT = 10;

    /*
     * Producer/consumer that uses concurrent collections.
     * 
     * Don't use concurrent collections all the time, but if you need to access a
     * collection across threads, then concurrent collections are a big help.
     * 
     */
    private static void runConcurrentCollection() throws InterruptedException {
        // max size of queue is delibrately low, you wouldn't normally do that
        ArrayBlockingQueue<String> mailbox = new ArrayBlockingQueue<>(3);

        Thread producer = new Thread(() -> {
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                try {
                    mailbox.put("Count : " + i);
                } catch (InterruptedException ex) {
                    break;
                }
            }
        });

        Thread consumer = new Thread(() -> {
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                try {
                    String msg = mailbox.take();
                    System.out.println("Took [" + msg + "]");
                } catch (InterruptedException ex) {
                    break;
                }
            }
        });

        producer.start();
        Thread.sleep(50); // wait for the queue to fill up
        consumer.start(); // order you start these in doesn't matter either

        producer.join(1000);
        producer.interrupt();
        producer.join();

        consumer.join(1000);
        consumer.interrupt();
        consumer.join();
    }

    /*
     * Simple use of parallel streams.
     * 
     * Streams can be made parallel. You need to be careful with this. While being
     * parallel can be helpful/faster in some cases, it can also be slower in some
     * cases.
     * 
     * Certain stream operations may be more costly in when done in parallel, see
     * the Javadocs for the operations to confirm. There is also a small amount of
     * overhead for making the stream parallel, so if you only have a small amount
     * of data it can actually be slower on a parallel stream.
     *
     */
    private static void runParallelStream() throws IOException, InterruptedException {
        List<String> urls = List.of(
                "https://www.mikusa.com",
                "https://www.alsdjflasjdfljaljsdflasdjf.com", // should fail
                "https://httpbin.org/",
                "https://httpbin.org/delay/5"); // will take 5 seconds

        serialCount(urls);
        parallelCount(urls);
    }

    private static void serialCount(List<String> urls) {
        Instant start = Instant.now();

        UrlFetcher urlFetcher = new UrlFetcher();
        long totalCharacters = 0;

        for (String url : urls) {
            String body = urlFetcher.fetch(url);
            totalCharacters += body.chars().count();
        }

        System.out.println("Character count: " + totalCharacters);
        System.out.println("Serial run time: " + Duration.between(start, Instant.now()));
    }

    private static void parallelCount(List<String> urls) throws IOException {
        Instant start = Instant.now();
        UrlFetcher urlFetcher = new UrlFetcher();

        long totalCharacters = urls.stream()
                .parallel()
                .map(url -> {
                    return urlFetcher.fetch(url);
                })
                .map(body -> body.chars().count())
                .reduce(0l, Long::sum);

        System.out.println("Character count: " + totalCharacters);
        System.out.println("Parallel run time: " + Duration.between(start, Instant.now()));
    }

    /*
     * Schedule a thread to be called later.
     * 
     */
    private static void scheduleTask() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

        // schedule to run 1 second later
        scheduler.schedule(() -> System.out.println("I ran later on thread -> " + Thread.currentThread().getName()), 1,
                TimeUnit.SECONDS);

        // schedule repeatedly
        scheduler.scheduleAtFixedRate(
                () -> System.out.println("I run every 1/2 second on thread -> " + Thread.currentThread().getName()),
                250,
                500,
                TimeUnit.MILLISECONDS);

        // schedule repeatedly
        scheduler.scheduleWithFixedDelay(
                () -> {
                    System.out.println(
                            "I run 1/2 second after previous run on thread -> " + Thread.currentThread().getName());
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                    }
                },
                400, 500,
                TimeUnit.MILLISECONDS);

        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
        }

        shutdownAndAwaitTermination(scheduler);
    }

    /*
     * The <a href=
     * "https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html"
     * >documented process form Oracle</a>
     * 
     * You would normally wait longer for the pool to shudown, but this gives you an
     * idea.
     */
    private static void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(1, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }
}
