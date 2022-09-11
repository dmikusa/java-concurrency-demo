package com.mikusa;

public class BlockSyncCounter implements SyncCounter {
    private int count;

    @Override
    public void increment() {
        synchronized (this) {
            count += 1;
        }
    }

    @Override
    public void decrement() {
        synchronized (this) {
            count -= 1;
        }
    }

    @Override
    public int getCount() {
        synchronized (this) {
            return count;
        }
    }

}
