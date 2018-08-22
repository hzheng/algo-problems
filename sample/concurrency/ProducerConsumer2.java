import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.junit.Test;
import static org.junit.Assert.*;

// Producer-Consumer example by use of BlockingQueue
public class ProducerConsumer2 {
    private static class Producer implements Runnable {
        private final BlockingQueue<Integer> queue;
        private Random rand = new Random();
        private int id;

        public Producer(BlockingQueue<Integer> queue, int id) {
            this.queue = queue;
            this.id = id;
        }

        public int produce() {
            return rand.nextInt(100);
        }

        public void run() {
            try {
                while (true) {
                    Thread.sleep(100);
                    int num = produce();
                    System.out.println("producer " + id + " is producing " +
                                       num);
                    queue.put(num); // queue.add will throw IllegalStateException
                }
            } catch (InterruptedException e) {
                System.out.println("interrupted!");
            }
        }
    }

    private static class Consumer implements Runnable {
        private final BlockingQueue<Integer> queue;
        private Random rand = new Random();
        private int id;

        public Consumer(BlockingQueue<Integer> queue, int id) {
            this.queue = queue;
            this.id = id;
        }

        public void consume(int num) {
            System.out.println(
                "consumer " + id + " is consuming " + num + ";queue.size()="
                + queue.size());
        }

        public void run() {
            try {
                while (true) {
                    Thread.sleep(100);
                    if (rand.nextInt(50) == 0) {
                        consume(queue.take());
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("interrupted!");
            }
        }
    }

    @Test
    public void test() throws Exception {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
        Producer producer = new Producer(queue, 1);
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
