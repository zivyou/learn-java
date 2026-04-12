import com.zivyou.countdownlatchdemo.Counter;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CountDownLatchTests {
    @Test
    public void test01() throws InterruptedException {
        Counter counter = new Counter();
        ScheduledExecutorService service1 = new ScheduledThreadPoolExecutor(2);
        ScheduledExecutorService service2 = new ScheduledThreadPoolExecutor(2);
        service1.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                int result = 0;
                try {
                    result = counter.decrease();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("service1 decrease counter: "+result);
            }
        }, 0, 1, TimeUnit.SECONDS);

        service2.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                int result = 0;
                try {
                    result = counter.increase();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("service2 increase counter: "+result);
            }
        }, 0, 1, TimeUnit.SECONDS);

        Thread.sleep(50*1000);
    }
}
