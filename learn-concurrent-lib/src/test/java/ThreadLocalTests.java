import com.zivyou.threadlocaldemo.ThreadLocalDemo;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

public class ThreadLocalTests {
    @Test
    public void test01() {
        System.out.println(ThreadLocalDemo.getThreadId());
        CompletableFuture.runAsync(() -> {
            System.out.println(ThreadLocalDemo.getThreadId());
        });
    }

}
