package com.mikusa;

public class NoSyncCounter implements SyncCounter {
    private int counter = 0;

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
