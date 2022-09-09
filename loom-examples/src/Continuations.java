import jdk.internal.vm.Continuation;
import jdk.internal.vm.ContinuationScope;

// import static java.lang.System.out;

public class Continuations {

    // javac --enable-preview --source 19 --add-exports java.base/jdk.internal.vm=ALL-UNNAMED Continuations.java
    // java --enable-preview --source 19 --add-opens=java.base/jdk.internal.vm=ALL-UNNAMED Continuations

    public static void main(String[] args) {
        new Continuations().continuations();
    }

    void continuations() {
    // The scope is a tool for creating nested continuations.
    // to enable.
        ContinuationScope scope = new ContinuationScope("demo");
        Continuation continuation = new Continuation(scope, () -> {
            System.out.println("Started ...");
    // the function is "frozen" here and gives control to the caller.
            Continuation.yield(scope);
            System.out.println("Done.");
        });
        continuation.run();
        System.out.println("Still running!");
    // the continuation can be moved from where it was stopped, be continued.
        continuation.run();
    // ...
    }
}
