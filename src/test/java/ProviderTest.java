import com.kubbo.demo.EchoService;
import com.kubbo.rpc.Ref;
import com.kubbo.rpc.akka.Reference;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <title>ProviderTest</title>
 * <p></p>
 *
 * @author zhuwei
 *         2014/10/11
 */
public class ProviderTest {


    public static void main(String[] args) throws InterruptedException {
        Ref ref = Reference.get();
        EchoService echoService = ref.getRef(EchoService.class, "test", "1.0.0");
        Thread.sleep(2000);
        int size = 100000;
        int threadNum =1;
        final CountDownLatch latch = new CountDownLatch(threadNum);
        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        long start = System.currentTimeMillis();
        for (int i = 0; i < threadNum; i++) {
            executor.execute(()-> {
                for (int j = 0; j < size; j++) {
                    echoService.voidEcho("hello world");
                }
                latch.countDown();
            });
        }
        latch.await();
        long end = System.currentTimeMillis();
        System.out.println("cost:" + (end - start) + ",ops:" + (size * threadNum * 1000 / (end - start))+" /s");
        System.exit(0);

    }
}