package hello.advanced.trace.threadlocal;

import hello.advanced.trace.threadlocal.code.FieldService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class FieldServiceTest {

    private FieldService fieldService = new FieldService();

    @Test
    void field() {
        log.info("main start");

        Runnable userA = () -> fieldService.logic("userA");
        Runnable userB = () -> fieldService.logic("userB");

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start();
//        sleep(2000); // 동시성 문제가 발생하지 않음.
        sleep(100); // 동시성 문제가 발생
        // threadA는 최소 1초가 지나야 종료가 되는데, threadA가 종료되지 않은 상태에서 threadB 실행
        threadB.start();
        // 서로가 같은 critical section 에 있는 값을 사용하게 됨, 즉 threadB가 nameStore 를 변경하고, threadA는 변경된 값을 반환함.

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
