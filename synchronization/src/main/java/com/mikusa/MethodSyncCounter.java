package com.mikusa;

public class MethodSyncCounter implements SyncCounter {
    private int count = 0;

    public synchronized void increment() {
        count += 1;
    }

    public synchronized void decrement() {
        count -= 1;
    }

    public synchronized int getCount() {
        return count;
    }
}
