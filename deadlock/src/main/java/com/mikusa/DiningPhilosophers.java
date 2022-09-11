package com.mikusa;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophers {
    public static void main(String[] args) throws InterruptedException {
        if (args.length != 0) {
            System.out.println("USAGE:");
            System.out.println("    java com.mikusa.DiningPhilosophers");
            System.out.println();
            System.exit(1);
        }

        Instant start = Instant.now();
        Lock[] locks = new Lock[] { new ReentrantLock(), new ReentrantLock(), new ReentrantLock(), new ReentrantLock(),
                new ReentrantLock() };
        List<Thread> philosophers = new ArrayList<>();

        philosophers.add(new Philosopher(locks[0], locks[1]));
        philosophers.add(new Philosopher(locks[1], locks[2]));
        philosophers.add(new Philosopher(locks[2], locks[3]));
        philosophers.add(new Philosopher(locks[3], locks[4]));
        philosophers.add(new Philosopher(locks[4], locks[0]));

        philosophers.stream().forEach(t -> t.start());

        Thread.sleep(5 * 1000);

        philosophers.stream().forEach(t -> t.interrupt());

        System.out.println("Acutal run time: " + Duration.between(start, Instant.now()));
    }
}
