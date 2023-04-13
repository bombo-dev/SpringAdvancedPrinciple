package hello.advanced.trace.logtrace;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
public class FieldLogTrace implements LogTrace {
    enum Prefix {
        START_PREFIX("-->"), COMPLETE_PREFIX("<--"), EX_PREFIX("<X-");

        private final String prefix;

        Prefix(String prefix){
            this.prefix = prefix;
        }
    }

    private TraceId traceIdHolder; // traceId 동기화, 동시성 이슈 발생, 구성을 활용한 처리

    @Override
    public TraceStatus begin(String message) {
        TraceId traceId = syncTraceId();
        long startTimeMs = System.currentTimeMillis();
        //로그 출력
        log.info("[{}] {}{}", traceId.getId(), addSpace(FieldLogTrace.Prefix.START_PREFIX.prefix, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    private TraceId syncTraceId() {
        if (traceIdHolder == null) {
            traceIdHolder = new TraceId();
        } else {
            traceIdHolder = traceIdHolder.createNextId();
        }
        return traceIdHolder;
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e) {
        long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        TraceId traceId = status.getTraceId();

        if (e == null) {
            log.info("[{}] {}{} time = {}ms", traceId.getId(),
                    addSpace(FieldLogTrace.Prefix.COMPLETE_PREFIX.prefix, traceId.getLevel()), status.getMessage(), resultTimeMs);
        } else {
            log.info("[{}] {}{} time = {}ms ex = {}", traceId.getId(),
                    addSpace(FieldLogTrace.Prefix.EX_PREFIX.prefix, traceId.getLevel()), status.getMessage(), resultTimeMs, e.toString());
        }

        releaseTraceId();
    }

    private void releaseTraceId() {
        if (traceIdHolder.isFirstLevel()) {
            traceIdHolder = null; // destroy;
        } else {
            traceIdHolder = traceIdHolder.createPreviousId();
        }
    }

    private String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < level; i++) {
            sb.append( (i == level - 1) ? "|" + prefix : "|   ");
        }
        return sb.toString();
    }
}
