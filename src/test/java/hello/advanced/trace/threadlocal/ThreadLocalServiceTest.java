package hello.advanced.trace.threadlocal;

import hello.advanced.trace.threadlocal.code.ThreadLocalService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ThreadLocalServiceTest {

    private ThreadLocalService threadLocalService = new ThreadLocalService();

    @Test
    void field() {
        log.info("main start");

        Runnable userA = () -> threadLocalService.logic("userA");
        Runnable userB = () -> threadLocalService.logic("userB");

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start();
        sleep(2000); // 동시성 문제가 발생하지 않음.
//        sleep(100); // 동시성 문제가 발생
        threadB.start();
        // 이제는 각각 다른 쓰레드 전용 저장소를 사용하기 때문에 각 nameStore 에 값을 저장할 때 null 을 확인 할 수 있다.

        sleep(3000); // 메인 쓰레드가 종료하지 않도록 대기
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
