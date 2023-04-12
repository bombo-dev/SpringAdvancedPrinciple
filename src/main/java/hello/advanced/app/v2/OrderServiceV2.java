package hello.advanced.app.v2;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV1;
import hello.advanced.trace.hellotrace.HelloTraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV2 {

    private final OrderRepositoryV2 orderRepository;
    private final HelloTraceV2 trace;

    public void orderItem(TraceStatus status, String itemId) {
        TraceStatus newStatus = null;
        try {
            newStatus = trace.beginSync(status.getTraceId(), "OrderService.orderItem()");
            orderRepository.save(newStatus, itemId);
            trace.end(newStatus);
        } catch (IllegalStateException e) {
            trace.exception(newStatus, e);
            throw e; // 예외를 던지지 않으면 예외를 먹어버리기 때문에, 다시 던져 줌
        }
    }


}
