package com.mikusa;

import java.util.Random;
import java.util.concurrent.locks.Lock;

class Philosopher extends Thread {
    private static int num = 0;

    private Lock left;
    private Lock right;
    private Random random = new Random();

    public Philosopher(Lock left, Lock right) {
        super("Philosopher-" + num);
        synchronized (Philosopher.class) {
            num++;
        }
        this.left = left;
        this.right = right;
    }

    private int randomSleepTime() {
        return random.nextInt(51) + 100;
    }

    @Override
    public void run() {
        System.out.println("[" + Thread.currentThread().getName() + "] I think therefore I am");
        try {
            while (true) {
                try {
                    left.lockInterruptibly();
                    Thread.yield();
                    right.lockInterruptibly();
                    System.out.println(
                            "[" + Thread.currentThread().getName() + "] I have the power and both forks! Time to eat.");
                    Thread.sleep(randomSleepTime());
                } finally {
                    System.out.println("[" + Thread.currentThread().getName() + "] I'm done eating");
                    left.unlock();
                    right.unlock();
                }
                System.out.println("[" + Thread.currentThread().getName() + "] I'm hungry again");
            }
        } catch (InterruptedException | IllegalMonitorStateException ex) {
            System.err
                    .println("[" + Thread.currentThread().getName() + "] Interrupted while getting a fork, how rude!");
            return;
        }
    }
}
