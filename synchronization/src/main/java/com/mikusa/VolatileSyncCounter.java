package com.mikusa;

public class VolatileSyncCounter implements SyncCounter {
    private volatile int counter = 0;

    @Override
    public void increment() {
        counter += 1;
    }

    @Override
    public void decrement() {
        counter -= 1;
    }

    @Override
    public int getCount() {
        return counter;
    }

}
