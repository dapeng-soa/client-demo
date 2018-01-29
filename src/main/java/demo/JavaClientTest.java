package demo;


import com.github.dapeng.core.InvocationContextImpl;
import com.github.dapeng.core.SoaException;
import com.github.dapeng.hello.JavaAsyncHelloServiceAsyncClient;
import com.github.dapeng.hello.JavaSyncHelloServiceClient;
import com.github.dapeng.hello.domain.Hello;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class JavaClientTest {

    public static void main(String[] args) throws InterruptedException, ExecutionException, SoaException {
        asyncTest(1);
        Thread.sleep(2000);
//        for (int j = 0; j < 10; j++) {
        long begin = System.currentTimeMillis();
        int i = 0;
        while (true) {
            asyncTest(++i);
            if ((i % 800) == 0) {
                Thread.sleep(100);
            }
            if (i >= 100000) break;

        }
        Thread.sleep(10000);
        System.out.println("success:" + success.get() + "/failed: " + failed.get());
        System.out.println(i + " times cost:" + (System.currentTimeMillis() - begin));
//        }
    }

    private static void syncTest(int id) {
        JavaSyncHelloServiceClient client = new JavaSyncHelloServiceClient();
        Hello request = new Hello();
        request.name = "Today";
        request.id = id;
        request.message = Optional.of("便利店");
        try {
            System.out.println(client.sayHello(request));
        } catch (SoaException e) {
            System.out.println(e.getMsg());
        }
    }

    static JavaAsyncHelloServiceAsyncClient asyncClient = new JavaAsyncHelloServiceAsyncClient();
    static AtomicInteger success = new AtomicInteger(0);
    static AtomicInteger failed = new AtomicInteger(0);

    private static void asyncTest(int id) {
        Hello request = new Hello();
        request.name = "Today";
        request.id = id;
        request.message = Optional.of("便利店");
        try {
            InvocationContextImpl.Factory.createNewInstance();
            CompletableFuture<String> future = asyncClient.sayHello(request, 2000);
            future.exceptionally(ex -> {
                failed.incrementAndGet();
                return null;
            });
            future.thenAccept(resp -> {
//                        System.out.println(resp);
                        success.incrementAndGet();
                    }
            );
        } catch (SoaException e) {
            e.printStackTrace();
        } finally {
            InvocationContextImpl.Factory.createNewInstance();

        }
    }
}