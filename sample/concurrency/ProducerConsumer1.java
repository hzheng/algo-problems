
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import org.junit.Test;
import static org.junit.Assert.*;

// Producer-Consumer example by use of wait/notify
public class ProducerConsumer1 {
    private static class Producer implements Runnable {
        private final Queue<Integer> queue;
        private int maxSize;
        private Random rand = new Random();
        private int id;

        public Producer(Queue<Integer> queue, int maxSize, int id) {
            this.queue = queue;
            this.maxSize = maxSize;
            this.id = id;
        }

        public int produce() {
            return rand.nextInt(100);
        }

        public void run() {
            while (true) {
                synchronized (queue) {
                    try {
                        while (queue.size() == maxSize) {
                            System.out.println(
                                "queue is full, waiting for consumer");
                            queue.wait();
                        }
                        Thread.sleep(100);
                        int num = produce();
                        System.out.println(
                            "producer " + id + " is producing " + num);
                        queue.offer(num);
                        queue.notifyAll();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static class Consumer implements Runnable {
        private final Queue<Integer> queue;
        private Random rand = new Random();
        private int id;

        public Consumer(Queue<Integer> queue, int id) {
            this.queue = queue;
            this.id = id;
        }

        public void consume(int num) {
            System.out.println(
                "consumer " + id + " is consuming " + num + ";queue.size()=" +
                queue.size());
        }

        public void run() {
            try {
                while (true) {
                    Thread.sleep(100);
                    if (rand.nextInt(50) == 0) {
                        synchronized (queue) {
                            while (queue.isEmpty()) {
                                System.out.println("waiting for producer...");
                                queue.wait();
                            }
                            consume(queue.poll());
                            queue.notifyAll();
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static void test1() {}

    @Test
    public void test() throws Exception {
        Queue<Integer> queue = new LinkedList<>();
        Producer producer = new Producer(queue, 10, 1);
        Consumer consumer1 = new Consumer(queue, 1);
        Consumer consumer2 = new Consumer(queue, 2);
        Thread t1 = new Thread(producer);
        t1.start();
        Thread t2 = new Thread(consumer1);
        t2.start();
        Thread t3 = new Thread(consumer2);
        t3.start();
        t1.join();
        t2.join();
        t3.join();
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
