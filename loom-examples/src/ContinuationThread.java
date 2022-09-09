import jdk.internal.vm.Continuation;
import jdk.internal.vm.ContinuationScope;

public class ContinuationThread {

// javac --enable-preview --source 19 --add-exports java.base/jdk.internal.vm=ALL-UNNAMED ContinuationThread.java
// java --enable-preview --source 19 --add-opens=java.base/jdk.internal.vm=ALL-UNNAMED ContinuationThread

    public static void main(String[] args) {

        var scope = new ContinuationScope("hello");
        var continuation =
              new Continuation(
                    scope,
                    () -> {
                        System.out.println("C1: " + Thread.currentThread());
                        Continuation.yield(scope);
                        System.out.println("C2: " + Thread.currentThread());
                    });

        System.out.println("Start");
        continuation.run();
        System.out.println("I'm back");
        continuation.run();
        System.out.println("Done");
    }
}
