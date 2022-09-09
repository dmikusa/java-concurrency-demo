To compile/run the Thread examples:

```
java --enable-preview --source 19 VirtualThreads.java
```


To compile/run the Continuation example:

```
javac --enable-preview --source 19 --add-exports java.base/jdk.internal.vm=ALL-UNNAMED Continuations.java

java --enable-preview --source 19 --add-opens=java.base/jdk.internal.vm=ALL-UNNAMED Continuations
```