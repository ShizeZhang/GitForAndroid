import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Meal {
    private final int orderNum;

    public Meal(int orderNum) {
        this.orderNum = orderNum;
    }

    public String toString() {
        return "Meal " + orderNum;
    }
}

class WaitPerson implements Runnable {
    private Restaurant restaurant;

    public WaitPerson(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (this) {
                    while (restaurant.meal == null) {
                        wait();
                    }
                }
                System.out.println("服务员得到肉"+restaurant.meal);
                synchronized (restaurant.chef){
                    restaurant.meal=null;
                    restaurant.chef.notify();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("WaitPerson InterruptedException");
        }
    }
}
class Chef implements Runnable{
    private Restaurant restaurant;
    private int count = 0;
    public Chef(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (this) {
                    while (restaurant.meal != null) {
                        wait();
                    }
                }
                if(++count==10){
                    System.out.println("out of food");
                    restaurant.service.shutdownNow();
                }
                synchronized (restaurant.waitPerson){
                    restaurant.meal = new Meal(count);
                    restaurant.waitPerson.notify();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Chef InterruptedException");
        }
    }
}
public class Restaurant {
    Meal meal;
    ExecutorService service = Executors.newCachedThreadPool();
    Chef chef = new Chef(this);
    WaitPerson waitPerson= new WaitPerson(this);
    public Restaurant(){
        service.execute(chef);
        service.execute(waitPerson);
    }
    public static void main(String[] args){
        new Restaurant();
    }
}
