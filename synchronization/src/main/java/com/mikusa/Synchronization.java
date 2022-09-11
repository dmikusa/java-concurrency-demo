package com.mikusa;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Synchronization {
    public static void main(String[] args) throws InterruptedException {
        if (args.length == 0) {
            System.out.println("USAGE:");
            System.out.println("    java com.mikusa.Synchronization <type>");
            System.out.println();
            System.out.println("Types: `no-sync`, `volatile`, `sync-method` or `sync-block`");
            System.out.println();
            System.exit(1);
        }

        Instant start = Instant.now();

        switch (args[0]) {
            case "sync-method":
                runSyncMethod();
                break;
            case "sync-block":
                runSyncBlock();
                break;
            case "no-sync":
                runNoSync();
                break;
            case "volatile":
                runVolatileSync();
                break;
            default:
                System.out.println("Invalid option [" + args[0] + "]");
        }

        System.out.println("Acutal run time: " + Duration.between(start, Instant.now()));
    }

    /*
     * Uses a method-based synchronization counter
     * 
     */
    private static void runSyncMethod() {
        MethodSyncCounter counter = new MethodSyncCounter();
        runCounter(counter);
    }

    /*
     * Uses a block-based synchronization counter
     *
     */
    private static void runSyncBlock() {
        BlockSyncCounter counter = new BlockSyncCounter();
        runCounter(counter);
    }

    /*
     * Uses no synchronization in the counter
     *
     */
    private static void runNoSync() {
        NoSyncCounter counter = new NoSyncCounter();
        runCounter(counter);
    }

    /*
     * Uses volatile on the synchronization counter
     *
     */
    private static void runVolatileSync() {
        VolatileSyncCounter counter = new VolatileSyncCounter();
        runCounter(counter);
    }

    /*
     * Count to a million across 10 threads
     * 
     * Uses a counter which synchronizes on method calls.
     *
     */
    private static void runCounter(SyncCounter counter) {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(() -> {
                for (int j = 0; j < 100_000; j++) {
                    counter.increment();
                }
            });
            t.start();
            threads.add(t);
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
            }
        }

        System.out.println("Total Count: " + counter.getCount());
    }
}
