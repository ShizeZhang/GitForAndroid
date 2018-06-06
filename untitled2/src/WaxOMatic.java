import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

import static com.sun.deploy.trace.Trace.*;

class Car {
    private boolean waxOn = false;
    public  synchronized void waxed(Car car){
//        waxOn= true;
//        notifyAll();
        car.buffed();
//        for (int i = 0;i<10;i++)
            System.out.println("waxed");
    }
    public synchronized void buffed(){
//        waxOn = false;
//        notifyAll();
//        for (int i = 0;i<10;i++)
            System.out.println("buffed");
    }
    public synchronized void waitForWaxing() throws InterruptedException {
        while (waxOn==false)wait();
    }
    public synchronized void waitForBuffing() throws InterruptedException {
        while (waxOn==true)wait();
    }
}
class WaxOn implements Runnable{
    private Car car1,car2;

    public WaxOn(Car car1, Car car2) {
        this.car1 = car1;
        this.car2 = car2;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()){
                System.out.println("wax on!");
                TimeUnit.MILLISECONDS.sleep(200);
                car1.waxed(car2);
//                car.waitForBuffing();
            }
        }catch (InterruptedException e){
            System.out.println("exiting via InterruptedException");
        }
        System.out.println("ending wax off task");
    }
}
class WaxOff implements Runnable{
    private Car car1,car2;

    public WaxOff(Car car1, Car car2) {
        this.car1 = car1;
        this.car2 = car2;
    }

    @Override
    public void run() {

        try {
            while (!Thread.interrupted()){
//                car.waitForWaxing();
                System.out.println("wax off!");
                TimeUnit.MILLISECONDS.sleep(200);
                car2.waxed(car1);
//                car.waitForWaxing();

            }
        } catch (InterruptedException e) {
            System.out.println("exiting via InterruptedException");
        }
        System.out.println("ending wax off task");

    }
}
public class WaxOMatic{
    public static void main(String[] args) throws InterruptedException {
        Car car1 = new Car();
        Car car2 = new Car();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new WaxOn(car1,car2));
        executorService.execute(new WaxOff(car1,car2));
        TimeUnit.SECONDS.sleep(5);
        executorService.shutdownNow();
        Lock lock = new ReentrantLock();
        lock.lock();
        lock.unlock();
        LockSupport.park();
//        Semaphore
//        LockSupport.unpark();
//        synchronized (){
//
//        }
    }
}
