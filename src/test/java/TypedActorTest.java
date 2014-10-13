import akka.actor.ActorSystem;
import akka.actor.TypedActor;
import akka.actor.TypedActorExtension;
import akka.actor.TypedProps;
import akka.japi.Creator;
import com.kubbo.demo.EchoService;
import com.kubbo.demo.EchoServiceImpl;
import com.typesafe.config.ConfigFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <title>TypedActorTest</title>
 * <p></p>
 *
 * @author zhuwei
 *         2014/10/10
 */
public class TypedActorTest {

    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("typedActor", ConfigFactory.empty());
        TypedActorExtension typed = TypedActor.get(system);
        EchoService echoService = typed.typedActorOf(new TypedProps<EchoService>(EchoService.class, new Creator<EchoService>() {
            @Override
            public EchoService create() throws Exception {
                return new EchoServiceImpl(0, false);
            }
        }));
        int count = 100000;
        final AtomicInteger COUNT = new AtomicInteger(count);
        int threadNum = 32;
        final CountDownLatch latch = new CountDownLatch(threadNum);
        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        long start = System.currentTimeMillis();
        final AtomicLong times = new AtomicLong(0);
        for (int i = 0; i < threadNum; i++) {
            executor.execute(() -> {
                while (COUNT.getAndDecrement() > 0) {
                    long s = System.nanoTime();
                    echoService.voidEcho("hello world");
                    long e = System.nanoTime();
                    times.addAndGet(e - s);
                }
                latch.countDown();
            });
        }

        latch.await();
        long end = System.currentTimeMillis();
        System.out.println("cost:" + (end - start) + " ms,ops:"+ count*(1000) / (end - start) + " /s");
        System.out.println((times.get()/count)+" ns/opt");
        System.exit(0);
    }
}