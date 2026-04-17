import com.zivyou.h2odemo.H2O;
import com.zivyou.h2odemo.H2OCountDownLatch;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class H2OTests {
    @Test
    void test01() {
        H2O h2O = new H2O();
        ThreadPoolExecutor executor1 = new ThreadPoolExecutor(10, 20, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        ThreadPoolExecutor executor2 = new ThreadPoolExecutor(10, 20, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        int i = 0;
        while (true) {
            i++;
            if (i > 1000) break;
            executor1.execute(() -> {
                try {
                    h2O.hydrogen(() -> System.out.print("H"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            executor2.execute(() -> {
                try {
                    h2O.oxygen(() -> System.out.print("O"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Test
    public void test02() {
        H2OCountDownLatch h2o = new H2OCountDownLatch();
        ThreadPoolExecutor executor1 = new ThreadPoolExecutor(10, 20, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000));
        ThreadPoolExecutor executor2 = new ThreadPoolExecutor(10, 20, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000));
        int i=0;
        while (i <= 100) {
            executor1.execute(() -> {
                try {
                    h2o.hydrogen(() -> {
                        System.out.print('H');
                    });
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            executor2.execute(() -> {
                try {
                    h2o.oxygen(() -> {
                        System.out.print('O');
                    });
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            i++;
        }
    }
}
