# Java Concurrency Demos

This repository contains a set of various Java related concurrency demos.

The go with the Slides from the Cleveland Java User Group presentation [Java Concurrency Deep Dive](https://docs.google.com/presentation/d/1fgK0pmDTFuiwUjM63-7mwT1dtaAZ79OOErF5RUMoA5E/edit#slide=id.p).

## Demos

The demos are broken down into groups. There may be multiple demos under each sub project.

1. Basic Threads - demos with core Java threads
2. Synchronization - demos using basic Java synchronization
3. Producer/Consumer - demos using Java wait/notify
4. Deadlock - demo that intentionally creates a deadlock
5. Java Util Concurrent - demos that use `java.util.concurrent`
6. Project Loom - demos that use Project Loom & Virtual Threads

Note that the Project Loom demos are under the `loom` branch. This is because at the time of publishing Loom has not been fully released and requires a bit of additional setup.
