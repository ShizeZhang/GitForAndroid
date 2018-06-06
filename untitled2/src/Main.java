import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;

public class Main implements Runnable {
    private String name;

    public Main(String name) {
        this.name = name;
    }
    public void sy() throws InterruptedException {
        synchronized (this) {

            System.out.println("wax off!");
            notifyAll();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Main m = new Main("diyi");
        System.out.println(true|false);
//        BlockingQueue<Runnable> d = new LinkedBlockingQueue<Runnable>();
//        ExecutorService s = Executors.newSingleThreadExecutor();
//        ThreadPoolExecutor n = new ThreadPoolExecutor(1,2,3, TimeUnit.MILLISECONDS,d);
//        n.execute();
    }


    @Override
    public void run() {
        synchronized (this) {
            try {
                for (int i = 0; i < 10; i++) {
                    System.out.println(i);
                    System.out.println("wax odddddddff!");
                    wait();
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

