package com.mikusa;

public class Mailbox {
    // the message to be shared
    private String message;

    // indicates who last updated the message
    private Actor lastUpdatedBy = Actor.Nobody;

    public synchronized void put(String message) throws InterruptedException {
        // wait until this switches to Consumer or Nobody, then we can continue
        // that means the consumer has read the last message and we can continue
        while (lastUpdatedBy == Actor.Producer) {
            System.out
                    .println(Thread.currentThread().getName() + " - waiting, lastUpdatedBy: " + lastUpdatedBy);
            wait();
            System.out.println(Thread.currentThread().getName() + " - notified, trying again");
        }

        // toggle last updated to Producer, since we consumed the message
        lastUpdatedBy = Actor.Producer;
        // notify threads may attempt to proceed
        notifyAll();
        // store the message
        this.message = message;
    }

    public synchronized String take() throws InterruptedException {
        // wait until this switches to Producer
        // that means the producer has stored a message, then we can continue
        while (lastUpdatedBy != Actor.Producer) {
            System.out.println(Thread.currentThread().getName() + " - waiting, lastUpdatedBy: " + lastUpdatedBy);
            wait();
            System.out.println(Thread.currentThread().getName() + " - notified, trying again");
        }

        // toggle last updated to Consumer, since we consumed the message
        lastUpdatedBy = Actor.Consumer;
        // notify threads may attempt to proceed
        notifyAll();
        // return the actual message
        return message;
    }
}
