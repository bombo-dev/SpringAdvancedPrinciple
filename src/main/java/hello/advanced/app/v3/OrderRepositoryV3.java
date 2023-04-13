package hello.advanced.app.v3;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV3 {

    private final LogTrace trace;

    public void save(String itemId) {
        // 저장 로직
        TraceStatus status = null;
        try {
            status = trace.begin("OrderRepository.save()");
            if (itemId.equals("ex")) {
                throw new IllegalStateException("예외 발생!");
            }
            sleep(1000);
            trace.end(status);
        } catch (IllegalStateException e) {
            trace.exception(status, e);
            throw e; // 예외를 던지지 않으면 예외를 먹어버리기 때문에, 다시 던져 줌
        }
    }

    private void sleep(int millis) {

        TraceStatus status = null;
        try {
            status = trace.begin("OrderRepository.sleep()");
            Thread.sleep(millis);
            trace.end(status);
        } catch (IllegalStateException e) {
            trace.exception(status, e);
            throw e; // 예외를 던지지 않으면 예외를 먹어버리기 때문에, 다시 던져 줌
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
