import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class VirtualThreads {

    // java --enable-preview --source 19 VirtualThreads.java

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

        Set<String> poolNames = ConcurrentHashMap.newKeySet();
        Set<String> pThreadNames = ConcurrentHashMap.newKeySet();
        Pattern pool = Pattern.compile("ForkJoinPool-[\\d?]");
        Pattern worker = Pattern.compile("worker-[\\d?]");

        var threads = IntStream.range(0, 100)
              .mapToObj(i -> Thread.ofVirtual()
                    .unstarted(() -> {
                        try {
                            Thread.sleep(2_000);
                            String name = Thread.currentThread().toString();
                            Matcher poolMatcher = pool.matcher(name);
                            if (poolMatcher.find()) {
                                poolNames.add(poolMatcher.group());
                            }
                            Matcher workerMatcher = worker.matcher(name);
                            if (workerMatcher.find()) {
                                pThreadNames.add(workerMatcher.group());
                            }

                        } catch (InterruptedException e) {
                            throw new AssertionError(e);
                        }
                    }))
              .toList();
        Instant begin = Instant.now();
        for (var thread : threads) {
            thread.start();
        }
        for (var thread : threads) {
            thread.join();
        }
        Instant end = Instant.now();
        // System.out.println("# Cores: " + Runtime.getRuntime().availableProcessors());
        System.out.println("# Virtual Threads: " + threads.size());
        System.out.println("# Platform Threads: " + pThreadNames.size());
        System.out.println("# Pools: " + poolNames.size());
        System.out.println("# Time: " + Duration.between(begin, end).toMillis() + " ms");
        // pools.forEach(System.out::println);
        // new TreeSet<>(pThreadNames).forEach(System.out::println);
    }
}
