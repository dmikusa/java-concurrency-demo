public class ThreadBuilder {

    // --enable-preview

    public static void main(String[] args) throws InterruptedException {
        // platform thread
        var pthread = Thread.ofPlatform()
                .name("platform-", 0)
                .start(() -> {
                    System.out.println("Platform Thread: " + Thread.currentThread());
                });
        pthread.join();

        // virtual thread
        var vthread = Thread.ofVirtual()
                .name("virtual-", 0)
                .start(() -> {
                    System.out.println("Virtual Thread: " + Thread.currentThread());
                });
        vthread.join();
    }
}
