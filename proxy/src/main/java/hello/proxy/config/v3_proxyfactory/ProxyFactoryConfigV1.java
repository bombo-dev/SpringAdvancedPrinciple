package hello.proxy.config.v3_proxyfactory;

import hello.proxy.app.v1.*;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ProxyFactoryConfigV1 {

    @Bean
    public OrderControllerV1 orderController(LogTrace logTrace, OrderServiceV1 orderService) {
        OrderControllerV1 orderController = new OrderControllerV1Impl(orderService);
        ProxyFactory proxyFactory = new ProxyFactory(orderController);
        Advisor advisor = getAdvisor(logTrace);
        proxyFactory.addAdvisor(advisor);
        return (OrderControllerV1) proxyFactory.getProxy();
    }

    @Bean
    public OrderServiceV1 orderService(LogTrace logTrace, OrderRepositoryV1 orderRepository) {
        OrderServiceV1 orderService = new OrderServiceV1Impl(orderRepository);
        ProxyFactory proxyFactory = new ProxyFactory(orderService);
        Advisor advisor = getAdvisor(logTrace);
        proxyFactory.addAdvisor(advisor);
        return (OrderServiceV1) proxyFactory.getProxy();
    }

    @Bean
    public OrderRepositoryV1 orderRepository(LogTrace logTrace) {
        OrderRepositoryV1 orderRepository = new OrderRepositoryV1Impl();
        ProxyFactory proxyFactory = new ProxyFactory(orderRepository);
        Advisor advisor = getAdvisor(logTrace);
        proxyFactory.addAdvisor(advisor);
        return (OrderRepositoryV1) proxyFactory.getProxy();
    }

    private Advisor getAdvisor(LogTrace logTrace) {
        return new DefaultPointcutAdvisor(Pointcut.TRUE, new LogTraceAdvice(logTrace));
    }
}
