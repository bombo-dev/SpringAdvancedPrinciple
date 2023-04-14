package hello.advanced.trace.strategy;

import hello.advanced.trace.strategy.code.strategy.ContextV1;
import hello.advanced.trace.strategy.code.strategy.Strategy;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic1;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV1Test {

    @Test
    void strategyV0() {
        logic1();
        logic2();
    }

    private void logic1() {
        long startTime = System.currentTimeMillis();
        // 비즈니스 로직 실행
        log.info("비즈니스 로직1 실행");
        // 비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

    private void logic2() {
        long startTime = System.currentTimeMillis();
        // 비즈니스 로직 실행
        log.info("비즈니스 로직2 실행");
        // 비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

    /**
     * 전략 패턴 사용
     */
    @Test
    void strategyV1() {
        ContextV1 context1 = new ContextV1(new StrategyLogic1());
        context1.execute();

        ContextV1 context2 = new ContextV1(new StrategyLogic2());
        context2.execute();
    }

    @Test
    void strategyV2() {
        // 인터페이스에 함수가 하나이므로 람다로 생성 가능
        Strategy strategyLogic1 = () -> log.info("비즈니스 로직 1 실행");
        Strategy strategyLogic2 = () -> log.info("비즈니스 로직 2 실행");

        ContextV1 context1 = new ContextV1(strategyLogic1);
        ContextV1 context2 = new ContextV1(strategyLogic2);
        context1.execute();
        context2.execute();
    }

    @Test
    void strategyV3() {
        // 좀 더 간추리기
        ContextV1 context1 = new ContextV1(() -> log.info("비즈니스 로직 1 실행"));
        ContextV1 context2 = new ContextV1(() -> log.info("비즈니스 로직 2 실행"));
        context1.execute();
        context2.execute();
    }
}
