package hello.advanced.trace.hellotrace;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HelloTraceV2 {

    enum Prefix {
        START_PREFIX("-->"), COMPLETE_PREFIX("<--"), EX_PREFIX("<X-");

        private final String prefix;

        Prefix(String prefix){
            this.prefix = prefix;
        }
    }

    public TraceStatus begin(String message) {
        TraceId traceId = new TraceId();
        long startTimeMs = System.currentTimeMillis();
        //로그 출력
        log.info("[{}] {}{}", traceId.getId(), addSpace(Prefix.START_PREFIX.prefix, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    // V2 에서 추가
    public TraceStatus beginSync(TraceId beforeTraceId, String message) {
        TraceId nextId = beforeTraceId.createNextId();
        long startTimeMs = System.currentTimeMillis();
        //로그 출력
        log.info("[{}] {}{}", nextId.getId(), addSpace(Prefix.START_PREFIX.prefix, nextId.getLevel()), message);
        return new TraceStatus(nextId, startTimeMs, message);
    }

    public void end(TraceStatus status) {
        complete(status, null);
    }

    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e) {
        long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        TraceId traceId = status.getTraceId();

        if (e == null) {
            log.info("[{}] {}{} time = {}ms", traceId.getId(),
                    addSpace(Prefix.COMPLETE_PREFIX.prefix, traceId.getLevel()), status.getMessage(), resultTimeMs);
        } else {
            log.info("[{}] {}{} time = {}ms ex = {}", traceId.getId(),
                    addSpace(Prefix.EX_PREFIX.prefix, traceId.getLevel()), status.getMessage(), resultTimeMs, e.toString());
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
