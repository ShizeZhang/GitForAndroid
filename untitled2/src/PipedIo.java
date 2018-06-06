import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class Sender implements Runnable{
    private LinkedBlockingQueue<Character> linkedBlockingQueue;
    private Random random = new Random(47);

    public Sender(LinkedBlockingQueue linkedBlockingQueue) {
        this.linkedBlockingQueue = linkedBlockingQueue;
    }


    @Override
    public void run() {
        try{
            while (!Thread.interrupted()){
                for (char c = 'A';c<='z';c++) {
                    linkedBlockingQueue.put(c);
                    System.out.println("writer"+c);
                    TimeUnit.MILLISECONDS.sleep(10);
                }


            }
        }catch (InterruptedException e){
            System.out.println("Writer InterruptedException");
        }
    }
}
class Receiver implements Runnable{
    private LinkedBlockingQueue<Character> linkedBlockingQueue;

    public Receiver(LinkedBlockingQueue<Character> linkedBlockingQueue) {
        this.linkedBlockingQueue = linkedBlockingQueue;
    }

    @Override
    public void run() {
        try{
            while (true){
                System.out.println("Receiver "+(char)linkedBlockingQueue.take()+" , ");
            }
        }catch (InterruptedException e){
            System.out.println("Receiver InterruptedException");
        }
    }
}
public class PipedIo {
    public static void main(String[] args) throws InterruptedException, IOException {
        LinkedBlockingQueue<Character> linkedBlockingQueue = new LinkedBlockingQueue<Character>();
        Sender sender = new Sender(linkedBlockingQueue);
        Receiver receiver = new Receiver(linkedBlockingQueue);
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(sender);
//        service.execute(receiver);
//        TimeUnit.SECONDS.sleep(5);
//        service.shutdownNow();
    }
}
