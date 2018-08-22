import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.Random;

// Interrupt threads example
public class Interruption1 {
    private static Random ran = new Random();

    private static class Job1 implements Runnable {
        public void run() {
            for (int i = 0; i < 1E8; i++) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    System.out.println("job1 interrupted");
                    break;
                }
                Math.sin(ran.nextDouble());
            }
        }
    }

    private static class Job2 implements Runnable {
        public void run() {
            for (int i = 0; i < 1E8; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("job2 interrupted");
                    break;
                }
                Math.sin(ran.nextDouble());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Starting.");
        Thread t1 = new Thread(new Job1());
        t1.start();
        Thread t2 = new Thread(new Job2());
        t2.start();
        Thread.sleep(1000);
        t1.interrupt();
        Thread.sleep(1000);
        t2.interrupt();
        t1.join();
        t2.join();
        System.out.println("Finished");
    }
}
