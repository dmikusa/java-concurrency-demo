package com.mikusa;

public class CountRunnableTask implements Runnable {

    @Override
    public void run() {
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
    }

}
