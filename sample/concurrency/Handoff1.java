import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

// import org.junit.Test;
import static org.junit.Assert.*;

// https://www.baeldung.com/java-synchronous-queue
public class Handoff1 {
    static void handoff() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        AtomicInteger sharedState = new AtomicInteger();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Runnable producer = () -> {
            try {
                Thread.sleep(1000);
                int num = ThreadLocalRandom.current().nextInt(0, 100);
                System.out.println("putting number: " + num);
                sharedState.set(num);
                countDownLatch.countDown();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        };
        Runnable consumer = () -> {
            try {
                countDownLatch.await();
                Integer num = sharedState.get();
                System.out.println("received number: " + num);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        };
        executor.execute(producer);
        executor.execute(consumer);
        executor.awaitTermination(2, TimeUnit.SECONDS);
        executor.shutdown();
        assertEquals(0, countDownLatch.getCount());
    }

    public static void main(String[] args) throws Exception {
        handoff();
        // String clazz =
        //     new Object() {}.getClass().getEnclosingClass().getSimpleName();
        // org.junit.runner.JUnitCore.main(clazz);
    }
}