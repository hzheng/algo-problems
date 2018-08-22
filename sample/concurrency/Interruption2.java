import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.Random;

// Interrupt thread pool example
public class Interruption2 {
    private static Random ran = new Random();

    public static double compute() {
        double res = 0;
        for (int i = 0; i < 1E8; i++) {
            if (Thread.currentThread().isInterrupted()) { // if (Thread.interrupted()) {
                System.out.println("interrupted");
                break;
            }
            res = Math.sin(ran.nextDouble());
        }
        return res;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Starting.");
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<?> future = executor.submit(() -> compute());
        executor.shutdown();
        Thread.sleep(500);
        future.cancel(true); // or interrupt by: executor.shutdownNow();
        executor.awaitTermination(1, TimeUnit.DAYS);
        System.out.println("Finished");
    }
}
