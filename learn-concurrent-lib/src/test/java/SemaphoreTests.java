import com.zivyou.semaphoredemo.Counter;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SemaphoreTests {
    @Test
    public void test01() throws InterruptedException {
        ScheduledExecutorService executorService1 = new ScheduledThreadPoolExecutor(5);
        ScheduledExecutorService executorService2 = new ScheduledThreadPoolExecutor(5);
        executorService1.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    Counter.decrease();
                    int result = Counter.get();
                    System.out.println("executorService1 decreased: result: "+result);
                    Thread.sleep((long) (Math.random()*1000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },  0,1, TimeUnit.SECONDS);

        executorService2.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    Counter.increase();
                    int result = Counter.get();
                    System.out.println("executorService2 increased: result: "+result);
                    Thread.sleep((long) (Math.random()*1000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },  0,1, TimeUnit.SECONDS);

        Thread.sleep(60 * 1000);
    }
}
