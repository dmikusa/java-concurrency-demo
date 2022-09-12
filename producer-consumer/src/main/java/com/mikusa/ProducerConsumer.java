package com.mikusa;

import java.time.Duration;
import java.time.Instant;

public class ProducerConsumer {
    private static final int MESSAGE_COUNT = 10;

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 0) {
            System.out.println("USAGE:");
            System.out.println("    java com.mikusa.ProducerConsumer");
            System.out.println();
            System.exit(1);
        }

        Instant start = Instant.now();
        Mailbox mailbox = new Mailbox();

        Thread producer = new Thread(() -> {
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String msg = "Count : " + i;
                try {
                    mailbox.put(msg);
                    System.out.println("Put [" + msg + "]");
                } catch (InterruptedException e) {
                    System.out.println("Interrupted: exiting");
                    break;
                }
            }
        }, "producer");

        Thread consumer = new Thread(() -> {
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                try {
                    String msg = mailbox.take();
                    System.out.println("Took [" + msg + "]");
                } catch (InterruptedException e) {
                    System.out.println("Interrupted: exiting");
                    break;
                }
            }
        }, "consumer");

        producer.start();
        consumer.start();

        producer.join(5 * 1000);
        consumer.join(5 * 1000);

        producer.interrupt();
        consumer.interrupt();

        producer.join();
        consumer.join();

        System.out.println("Acutal run time: " + Duration.between(start, Instant.now()));
    }
}
