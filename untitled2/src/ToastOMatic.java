import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class Toast {
    public enum Status {DRY, BUTTERED, JAMMED}

    private Status status = Status.DRY;
    private final int id;

    public Toast(int i) {
        id = i;
    }

    public void butter() {
        status = Status.BUTTERED;
    }

    public void jam() {
        status = Status.JAMMED;
    }

    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Toast{" +
                "status=" + status +
                ", id=" + id +
                '}';
    }
}

class ToastQueue extends LinkedBlockingQueue<Toast> {
}

class Toaster implements Runnable {
    private ToastQueue toastQueue;
    private int count = 0;
    private Random random = new Random(47);


    public Toaster(ToastQueue toastQueue) {
        this.toastQueue = toastQueue;
    }

    @Override
    public void run() {

        try {
            while (!Thread.interrupted()) {
                TimeUnit.MILLISECONDS.sleep(100 + random.nextInt(500));
                Toast toast = new Toast(++count);
                System.out.println(toast);
                toastQueue.put(toast);
            }
        } catch (InterruptedException e) {
            System.out.println("Toaster InterruptedException");

        }
        System.out.println("Toaster off");
    }
}

class Butterer  implements Runnable{
    private ToastQueue dryQueue,butterQueue;

    public Butterer(ToastQueue dryQueue, ToastQueue butterQueue) {
        this.dryQueue = dryQueue;
        this.butterQueue = butterQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                Toast toast = dryQueue.take();
                toast.butter();
                System.out.println(toast);
                butterQueue.put(toast);
            }
        } catch (InterruptedException e) {
            System.out.println("butterer InterruptedException");

        }
        System.out.println("butterer off");
    }
}

class Jammer implements Runnable{
    private ToastQueue butterQueue,jammerQueue;

    public Jammer(ToastQueue butterQueue, ToastQueue jammerQueue) {
        this.butterQueue = butterQueue;
        this.jammerQueue = jammerQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                Toast toast = butterQueue.take();
                toast.jam();
                System.out.println(toast);
                jammerQueue.put(toast);
            }
        } catch (InterruptedException e) {
            System.out.println("Jammer InterruptedException");
        }
        System.out.println("Jammer off");
    }
}

class Easter implements Runnable{
    private ToastQueue jammerQueue;
    private int counert=0;
    public Easter(ToastQueue jammerQueue) {
        this.jammerQueue = jammerQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                Toast toast = jammerQueue.take();
                if (toast.getId()!=++counert||toast.getStatus()!=Toast.Status.JAMMED){
                    System.out.println(">>>>>>error: "+toast);
                    System.exit(1);
                }else {
                    System.out.println("Chomp! "+toast);
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Eater InterruptedException");

        }
        System.out.println("Eater off");
    }
}

public class ToastOMatic {
    public static void main(String[] args) throws InterruptedException {
        ToastQueue dry = new ToastQueue(),
                butterer = new ToastQueue(),
                finish = new ToastQueue();
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(new Toaster(dry));
        service.execute(new Butterer(dry,butterer));
        service.execute(new Jammer(butterer,finish));
        service.execute(new Easter(finish));
        TimeUnit.SECONDS.sleep(1);
        service.shutdownNow();
    }
}
