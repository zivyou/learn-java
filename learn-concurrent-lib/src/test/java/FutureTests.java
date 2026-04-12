import com.zivyou.futuredemo.FutureDemo;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class FutureTests {
    @Test
    public void test01() throws ExecutionException, InterruptedException {
        FutureDemo demo = new FutureDemo();
        demo.demo1();
    }
    @Test
    public void test02() throws ExecutionException, InterruptedException {
        FutureDemo demo = new FutureDemo();
        demo.demo2();
    }
    @Test
    public void test03() throws ExecutionException, InterruptedException {
        FutureDemo demo = new FutureDemo();
        demo.demo3();
    }

    @Test
    public void test04() throws InterruptedException {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        int count = 10;
        final int[] i = {0};
        var f = executorService.scheduleAtFixedRate(()->{
            i[0]++;
            System.out.println("i = "+ i[0]);
            if (i[0] > 10) {
                System.out.printf("shutting down.");
                executorService.shutdown();
            }

        }, 1, 1, TimeUnit.SECONDS);

        executorService.scheduleAtFixedRate(()->{
            i[0]++;
            System.out.println("j = "+ i[0]);
            if (i[0] > 20) {
                System.out.printf("shutting down.");
                executorService.shutdown();
            }

        }, 1, 1, TimeUnit.SECONDS);

        executorService.scheduleAtFixedRate(()->{
            i[0]++;
            System.out.println("k = "+ i[0]);
            if (i[0] > 30) {
                System.out.printf("shutting down.");
                executorService.shutdown();
            }

        }, 1, 1, TimeUnit.SECONDS);
        Thread.sleep(50 * 1000);
    }

    @Test
    public void test05() {
    }
}
