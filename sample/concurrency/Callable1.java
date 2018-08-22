import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.Random;

// Callable-Future example
public class Callable1 {
    public static int square(int num) throws InterruptedException {
        Random random = new Random();
        int time = 4000;
        int duration = random.nextInt(time);
        if (duration > time / 2) {
            throw new RuntimeException("computed too long(" + duration + " ms)");
        }
        System.out.println("start computing...");
        Thread.sleep(duration);
        System.out.println("Finished.");
        return num * num;
    }

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newCachedThreadPool();
        int num = new Random().nextInt(100);
        System.out.println("try to compute square of " + num + "...");
        Future<Integer> future = executor.submit(() -> square(num));
        executor.shutdown();
        try {
            System.out.println("Result is " + future.get());
        } catch (InterruptedException e) {
            System.out.println(e);
        } catch (ExecutionException e) {
            System.out.println(e.getMessage());
        }
    }
}
