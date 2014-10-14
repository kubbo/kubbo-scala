package com.kubbo.proxy.http.netty;

import com.kubbo.demo.EchoService;
import com.kubbo.rpc.Ref;
import com.kubbo.rpc.akka.Reference;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <title>com.kubbo.proxy.http.netty.ProviderTest</title>
 * <p></p>
 *
 * @author zhuwei
 *         2014/10/11
 */
public class ProviderTest {


    public  static  void test(EchoService echoService) throws InterruptedException {
        Config config = ConfigFactory.parseURL(Thread.currentThread().getContextClassLoader().getResource("benchmark.conf"));
        System.out.println(config);
        final int concurrent = config.getInt("benchmark.concurrent");

        final CountDownLatch latch = new CountDownLatch(concurrent);

        final int MAX = config.getInt("benchmark.max");

        final AtomicInteger COUNTER = new AtomicInteger(MAX);

        final AtomicLong times = new AtomicLong(0);

        final ExecutorService executor = Executors.newFixedThreadPool(concurrent);
        try {
            long start = System.currentTimeMillis();
            List<String> methods = new ArrayList<String>();
            Collections.addAll(methods, "sync", "async", "void");
            String method = config.getString("benchmark.method");
            final int index = methods.indexOf(method);
            for (int i = 0; i < concurrent; i++) {
                executor.execute(new Runnable() {
                    public void run() {
                        while (COUNTER.getAndDecrement() > 0) {
                            long s = System.nanoTime();
                            switch (index) {
                                case 0:
                                    echoService.syncEcho("hello");
                                    break;
                                case 1:
                                    echoService.asyncEcho("hello");
                                    break;
                                case 2:
                                    echoService.voidEcho("hello");
                                    break;
                            }
                            long e = System.nanoTime();
                            times.addAndGet(e - s);
                        }
                        latch.countDown();
                    }
                });
            }
            latch.await();
            long end = System.currentTimeMillis();
            System.out.println("cost:" + (end - start) + " ms,ops:" + MAX * (1000) / (end - start) + " /s");
            System.out.println((times.get() / MAX) + " ns/opt");
        } finally {
            executor.shutdown();

        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {



        Ref ref = Reference.get();
        final EchoService echoService = ref.getRef(EchoService.class, "test", "1.0.0");
        Thread.sleep(5000);
        String test = echoService.syncEcho("test");
        System.out.println(test);

        for (; ; ) {
            test(echoService);
            System.out.print("go?");

            System.in.read();
        }

    }


}