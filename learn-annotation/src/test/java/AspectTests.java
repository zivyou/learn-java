import com.zivyou.house.House;
import com.zivyou.house.MyHouse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Slf4j
@SpringBootTest(classes = AspectTests.TestConfig.class)
public class AspectTests {

    @Autowired
    private House house;

    @Test
    public void test01() {
        log.info("hello world?");
        house.open();
        log.info("hello world!");
    }

    @Configuration
    @ComponentScan("com.zivyou")
    @EnableAspectJAutoProxy
    static class TestConfig {

        @Bean
        public House myHouse() {
            return new MyHouse(-1, 0);
        }
    }
}
