package hello.advanced.app.v2;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV1;
import hello.advanced.trace.hellotrace.HelloTraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV2 {

    private final HelloTraceV2 trace;

    public void save(TraceStatus status, String itemId) {
        // 저장 로직
        TraceStatus newStatus = null;
        try {
            newStatus = trace.beginSync(status.getTraceId(), "OrderRepository.save()");
            if (itemId.equals("ex")) {
                throw new IllegalStateException("예외 발생!");
            }
            sleep(newStatus, 1000);
            trace.end(newStatus);
        } catch (IllegalStateException e) {
            trace.exception(newStatus, e);
            throw e; // 예외를 던지지 않으면 예외를 먹어버리기 때문에, 다시 던져 줌
        }
    }

    private void sleep(TraceStatus status, int millis) {

        TraceStatus newStatus = null;
        try {
            newStatus = trace.beginSync(status.getTraceId(), "OrderRepository.sleep()");
            Thread.sleep(millis);
            trace.end(newStatus);
        } catch (IllegalStateException e) {
            trace.exception(newStatus, e);
            throw e; // 예외를 던지지 않으면 예외를 먹어버리기 때문에, 다시 던져 줌
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
