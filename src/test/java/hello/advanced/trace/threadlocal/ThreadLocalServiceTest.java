package hello.advanced.trace.threadlocal;

import hello.advanced.trace.threadlocal.code.ThreadLocalService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ThreadLocalServiceTest {

    private ThreadLocalService threadLocalService = new ThreadLocalService();

    @Test
    void threadLocal() {
        log.info("main start");

        Runnable userA = () -> threadLocalService.logic("userA");
        Runnable userB = () -> threadLocalService.logic("userB");

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start();
//        sleep(2000); // 동시성 문제가 발생하지 않음.
        sleep(100); // 동시성 문제 발생 X, 각각 다른 저장소에서 값을 저장하고 가져오기 때문에 동시성 문제가 발생 절대 X
        threadB.start();

        sleep(3000); // 메인 쓰레드가 종료하지 않도록 대기
        log.info("main exit");
    }

    @Test
    void threadLocalPool() {
        log.info("main start");

        Runnable userA = () -> threadLocalService.logic("userA");
        Runnable userB = () -> threadLocalService.logic("userB");

        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(userA);
        service.execute(userB);
        sleep(1500);
        service.execute(userA);
        sleep(2000);
        service.shutdown();
        log.info("main exit");
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
