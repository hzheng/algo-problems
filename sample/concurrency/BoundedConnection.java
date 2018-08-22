import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

// Semaphore example
public class BoundedConnection {
    private static BoundedConnection instance = new BoundedConnection();

    private int connections;

    private Semaphore sem = new Semaphore(10, true);

    public static BoundedConnection getInstance() {
        return instance;
    }

    public void connect() {
        try {
            sem.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            doConnect();
        } finally {
            sem.release();
        }
    }

    private void doConnect() {
        synchronized(this) {
            connections++;
            System.out.println("Current connnections: " + connections);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized(this) {
            connections--;
        }
    }

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 100; i >= 0; i--) {
            executor.submit(() -> BoundedConnection.getInstance().connect());
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.DAYS);
    }
}
