package hello.proxy.pureproxy.decorator;

import hello.proxy.pureproxy.decorator.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class DecoratorPatternTest {

    @Test
    void noDecorator() {
        Component realComponent = new RealComponent();
        DecoratorPatternClient client = new DecoratorPatternClient(realComponent);
        client.execute();
    }

    @Test
    void decorator1() {
        Component realComponent = new RealComponent();
        Component messageDecorator = new MessageDecorator(realComponent);
        DecoratorPatternClient client = new DecoratorPatternClient(messageDecorator);
        client.execute();
    }

    @Test
    void decorator2() {
        Component realComponent = new RealComponent();
        Component messageDecorator = new MessageDecorator(realComponent);
        Component timeDecorator = new TimeDecorator(messageDecorator);
        DecoratorPatternClient client = new DecoratorPatternClient(timeDecorator);
        // 클라이언트가 바라보는 시점에서의 순서 행동의 순서를 생각해야한다. Client -> time -> message
        // Client 입장에서는 time 을 알아야하기 떄문에 마지막에 time 이 먼저 온다. time 이 이제 message 로 보내야 하는데
        // time 은 message 가 무엇인지 알아야 한다. 객체간의 자율성으로 행동하기 때문에, 전달된 객체가 행동하도록 하기 위해서이다.
        client.execute();
    }
}
