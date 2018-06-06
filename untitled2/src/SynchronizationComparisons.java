import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

abstract class Accumulator{
    public static long cycles = 5L;
    private static final int N= 4;
    public static ExecutorService exec = Executors.newFixedThreadPool(N*2);
    private static CyclicBarrier barrier = new CyclicBarrier(N*2+1);
    protected volatile int index = 0;
    protected volatile int value = 0;
    protected long duration = 0;
    protected String id = "error";
    protected final static int SIZE = 10;
    protected static int[] preLoaded = new int[SIZE];
    static {
        Random rand = new Random(47);
        for (int i = 0;i<cycles;i++)
            preLoaded[i] = rand.nextInt();
    }
    public abstract void accumulate();
    public abstract long read();
    private class Modifier implements Runnable{
        @Override
        public void run() {
            for (long i =0;i<cycles;i++)
                accumulate();

            try {
                barrier.await();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    private class Reader implements Runnable{
        private volatile long value;
        @Override
        public void run() {
            for (long i = 0;i<cycles;i++){
                value = read();
            }
            try {
                barrier.await();
            }catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void timedTest(){
        long start = System.nanoTime();
        for (int i=0;i<N;i++){
            exec.execute(new Modifier());
            exec.execute(new Reader());
        }
        try {
            barrier.await();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        duration = System.nanoTime()-start;
        System.out.print(String.format("%-13s : %-13d\n",id,duration));
    }
    public static void report(Accumulator acc1,Accumulator acc2){
        System.out.print(String.format("%-22s : %.2f\n",acc1.id+"/"+acc2.id,
                (double)acc1.duration/(double)acc2.duration));
    }
}
class BaseLine extends Accumulator{
    { id = "BaseLine"; }
    @Override
    public void accumulate() {
        value+= preLoaded[index++];
        if (index>=SIZE)index=0;
    }

    @Override
    public long read() {
        return value;
    }
}
class SynchronizedTest extends Accumulator{
    {id="synchronized";}
    @Override
    public synchronized void accumulate() {
        value+= preLoaded[index++];
        if (index>=SIZE)index=0;
    }

    @Override
    public synchronized long read() {
        return value;
    }
}
class LockTest extends Accumulator{
    {id="LockTest";}
    private Lock lock = new ReentrantLock();
    @Override
    public void accumulate() {
        lock.lock();
        try{
            value+= preLoaded[index++];
            if (index>=SIZE)index=0;
        }finally {
            lock.unlock();
        }

    }

    @Override
    public long read() {
        lock.lock();
        try{
            return value;
        }finally {
            lock.unlock();
        }

    }
}
class AtomicTest extends Accumulator{
    {id="Atomic";}
    private AtomicInteger index = new AtomicInteger(0);
    private AtomicLong value = new AtomicLong(0);
    @Override
    public void accumulate() {
        int i = index.getAndIncrement();
        System.out.println(Thread.currentThread().toString()+i);
        value.getAndAdd(preLoaded[i]);
        if(++i>=SIZE){
            index.set(0);
        }
    }

    @Override
    public long read() {
        return value.get();
    }
}
class Call implements Callable<AtomicTest>{

    @Override
    public AtomicTest call() throws Exception {
        return null;
    }
}
public class SynchronizationComparisons {
    public static void printf(String s){
        System.out.print(s);
    }
    static BaseLine baseLine = new BaseLine();
    static SynchronizedTest synchronizedTest = new SynchronizedTest();
    static LockTest lockTest = new LockTest();
    static AtomicTest atomicLong = new AtomicTest();
    static void test(){
        System.out.println("====================================");
        printf(String.format("%-12s : %13d\n","Cycles",Accumulator.cycles));
//        baseLine.timedTest();
//        synchronizedTest.timedTest();
//        lockTest.timedTest();
        atomicLong.timedTest();
//        Accumulator.report(synchronizedTest,baseLine);
//        Accumulator.report(lockTest,baseLine);
//        Accumulator.report(atomicLong,baseLine);
//        Accumulator.report(synchronizedTest,lockTest);
//        Accumulator.report(synchronizedTest,atomicLong);
//        Accumulator.report(lockTest,atomicLong);
    }
    public static void main(String[] args){
        int iterations = 5;
        if(args.length>0){
            iterations = new Integer(args[0]);

        }
        Call call = new Call();
        Thread thread = new Thread();
        thread.start();
        System.out.println("Warmup");
        for (int i=0;i<2;i++){
            test();
            Accumulator.cycles*=2;

        }
        Accumulator.exec.shutdownNow();
    }
}
