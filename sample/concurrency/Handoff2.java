import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadLocalRandom;

// import org.junit.Test;
import static org.junit.Assert.*;

// https://www.baeldung.com/java-synchronous-queue
public class Handoff2 {
    static boolean done(int num) {
        return num == 50;
    }

    static void handoff() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        SynchronousQueue<Integer> queue = new SynchronousQueue<>();

        Runnable producer = () -> {
            try {
                while (true) {
                    Thread.sleep(1000);
                    int num = ThreadLocalRandom.current().nextInt(0, 100);
                    System.out.println("putting number: " + num);
                    queue.put(num);
                    if (done(num)) break;
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        };
        Runnable consumer = () -> {
            try {
                while (true) {
                    int num = queue.take();
                    System.out.println("received number: " + num);
                    if (done(num)) break;
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        };
        executor.execute(producer);
        executor.execute(consumer);
        executor.awaitTermination(100, TimeUnit.SECONDS);
        executor.shutdown();
        assertEquals(0, queue.size());
    }

    public static void main(String[] args) throws Exception {
        handoff();
        // String clazz =
        //     new Object() {}.getClass().getEnclosingClass().getSimpleName();
        // org.junit.runner.JUnitCore.main(clazz);
    }
}