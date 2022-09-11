package com.mikusa;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class BasicThreads {
    public static void main(String[] args) throws InterruptedException {
        if (args.length == 0) {
            System.out.println("USAGE:");
            System.out.println("    java com.mikusa.BasicThreads <type>");
            System.out.println();
            System.out.println("Types: `direct`, `lambda`, `runnable` or `thread`");
            System.out.println();
            System.exit(1);
        }

        Instant start = Instant.now();
        Optional<Thread> t = Optional.empty();

        switch (args[0]) {
            case "direct":
                runDirect();
                break;
            case "runnable":
                t = Optional.of(runInThread());
                break;
            case "thread":
                t = Optional.of(runAThread());
                break;
            case "lambda":
                t = Optional.of(runWithLambda());
                break;
            default:
                System.out.println("Invalid option [" + args[0] + "]");
        }

        if (t.isPresent()) {
            Thread.sleep(2 * 1000);

            System.out.println("Interrupting");
            // We are requesting that the thread interrupt
            t.get().interrupt();

            // Then waiting until the thread actually stops
            // If it interrupts properly, that'll happen almost immediately
            // If it does not interrupt properly, it'll wait the full 100s instead of 2s
            t.get().join();
            System.out.println("Done");
        }

        System.out.println("Acutal run time: " + Duration.between(start, Instant.now()));
    }

    /*
     * Runs directly in the current thread.
     * 
     * When using `implement Runnable`, it runs in the current thread by default.
     * 
     */
    private static void runDirect() {
        new CountRunnableTask().run();
    }

    /*
     * Runs in a new thread, returning the thread.
     * 
     * This uses the `Runnable` but it wraps the `Runnable` in a thread.
     * 
     */
    private static Thread runInThread() {
        Thread t = new Thread(new CountRunnableTask());
        t.start();
        return t;
    }

    /*
     * Runs in a new thread, returning the thread.
     *
     * This uses a lambda as a `Runnable` and wraps the `Runnable` in a thread. You
     * can do this because `Runnable` is a Functional Interface (only one method).
     *
     */
    private static Thread runWithLambda() {
        Thread t = new Thread(() -> {
            System.out.println("Thread running [" + Thread.currentThread().getName() + "]");

            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Sleeping for 10s");
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    // If we remove this break, then the interrupt does nothing
                    break; // or return
                }
            }
        });
        t.start();
        return t;
    }

    /*
     * Automatically runs in a Thread, returning the thread.
     * 
     * It automatically runs in a Thread, since `CountThreadTask` extends `Thread`.
     * 
     */
    private static Thread runAThread() {
        Thread t = new CountThreadTask();
        t.start();
        return t;
    }
}
