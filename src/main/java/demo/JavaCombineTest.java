package demo;

import com.github.dapeng.core.SoaException;
import com.github.dapeng.hello.*;
import com.github.dapeng.hello.domain.Hello;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ever
 */

@FunctionalInterface
interface SyncTestFunction<T> {
    String doTest(T t) throws SoaException;
}

@FunctionalInterface
interface ASyncTestFunction<T> {
    CompletableFuture<String> doTest(T t) throws SoaException;
}

public class JavaCombineTest {
    static final boolean debug = false;
    // java sync->java sync
    static AtomicInteger jsjsSuccess = new AtomicInteger(0);
    static AtomicInteger jsjsFailed = new AtomicInteger(0);

    // java sync->java async
    static AtomicInteger jsjaSuccess = new AtomicInteger(0);
    static AtomicInteger jsjaFailed = new AtomicInteger(0);

    // java sync->scala sync
    static AtomicInteger jsssSuccess = new AtomicInteger(0);
    static AtomicInteger jsssFailed = new AtomicInteger(0);

    // java sync->scala async
    static AtomicInteger jssaSuccess = new AtomicInteger(0);
    static AtomicInteger jssaFailed = new AtomicInteger(0);

    // java async->java sync
    static AtomicInteger jajsSuccess = new AtomicInteger(0);
    static AtomicInteger jajsFailed = new AtomicInteger(0);

    // java async->java async
    static AtomicInteger jajaSuccess = new AtomicInteger(0);
    static AtomicInteger jajaFailed = new AtomicInteger(0);

    // java async->scala sync
    static AtomicInteger jassSuccess = new AtomicInteger(0);
    static AtomicInteger jassFailed = new AtomicInteger(0);

    // java async->scala async
    static AtomicInteger jasaSuccess = new AtomicInteger(0);
    static AtomicInteger jasaFailed = new AtomicInteger(0);

    static JavaSyncHelloServiceClient jsjsClient = new JavaSyncHelloServiceClient();
    static JavaAsyncHelloServiceClient jsjaClient = new JavaAsyncHelloServiceClient();
    static ScalaSyncHelloServiceClient jsssClient = new ScalaSyncHelloServiceClient();
    static ScalaAsyncHelloServiceClient jssaClient = new ScalaAsyncHelloServiceClient();
    static JavaSyncHelloServiceAsyncClient jajsClient = new JavaSyncHelloServiceAsyncClient();
    static JavaAsyncHelloServiceAsyncClient jajaClient = new JavaAsyncHelloServiceAsyncClient();
    static ScalaSyncHelloServiceAsyncClient jassClient = new ScalaSyncHelloServiceAsyncClient();
    static ScalaAsyncHelloServiceAsyncClient jasaClient = new ScalaAsyncHelloServiceAsyncClient();

    public static void main(String[] args) throws InterruptedException {
        syncTest(1);
        asyncTest(1);
        Thread.sleep(2000);
        long begin = System.currentTimeMillis();
        int i = 0;
        while (true) {
            syncTest(i);
            asyncTest(i);
//            if ((i % 500) == 0) {
//                Thread.sleep(100);
//            }
            if (++i >= 50000) break;

        }
        Thread.sleep(10000);
        System.out.println("java sync->java sync:success:" + jsjsSuccess.get() + "/failed: " + jsjsFailed.get());
        System.out.println("java sync->java async:success:" + jsjaSuccess.get() + "/failed: " + jsjaFailed.get());
        System.out.println("java sync->scala sync:success:" + jsssSuccess.get() + "/failed: " + jsssFailed.get());
        System.out.println("java sync->scala async:success:" + jssaSuccess.get() + "/failed: " + jssaFailed.get());
        System.out.println("java async->java sync:success:" + jajsSuccess.get() + "/failed: " + jajsFailed.get());
        System.out.println("java async->java async:success:" + jajaSuccess.get() + "/failed: " + jajaFailed.get());
        System.out.println("java async->scala sync:success:" + jassSuccess.get() + "/failed: " + jassFailed.get());
        System.out.println("java async->scala async:success:" + jasaSuccess.get() + "/failed: " + jasaFailed.get());

        System.out.println(i + " times cost:" + (System.currentTimeMillis() - begin));
    }

    private static void syncTest(int id) {
        Hello request = new Hello();
        request.name = "Today";
        request.id = id;
        request.message = Optional.of("便利店");

        _innerSyncTest((Hello hello) -> jsjsClient.sayHello(hello), request,
                jsjsSuccess, jsjsFailed);

        _innerSyncTest((Hello hello) -> jsjaClient.sayHello(hello), request,
                jsjaSuccess, jsjaFailed);

        _innerSyncTest((Hello hello) -> jsssClient.sayHello(hello), request,
                jsssSuccess, jsssFailed);

        _innerSyncTest((Hello hello) -> jssaClient.sayHello(hello), request,
                jssaSuccess, jssaFailed);

    }

    private static void asyncTest(int id) {
        Hello request = new Hello();
        request.name = "Today";
        request.id = id;
        request.message = Optional.of("便利店");

        _innerAsyncTest((Hello hello) -> jajsClient.sayHello(hello), request,
                jajsSuccess, jajsFailed);

        _innerAsyncTest((Hello hello) -> jajaClient.sayHello(hello), request,
                jajaSuccess, jajaFailed);

        _innerAsyncTest((Hello hello) -> jassClient.sayHello(hello), request,
                jassSuccess, jassFailed);

        _innerAsyncTest((Hello hello) -> jasaClient.sayHello(hello), request,
                jasaSuccess, jasaFailed);
    }


    private static void _innerSyncTest(SyncTestFunction<Hello> testMethod, Hello request,
                                       AtomicInteger succeed, AtomicInteger failed) {
        try {
            String result = testMethod.doTest(request);
            if (debug) System.out.println(result);
            succeed.incrementAndGet();
        } catch (Throwable e) {
            if (debug) {
                System.out.println(e.getMessage());
            }
            failed.incrementAndGet();
        }
    }

    private static void _innerAsyncTest(ASyncTestFunction<Hello> testMethod, Hello request,
                                        AtomicInteger succeed, AtomicInteger failed) {
        try {
            CompletableFuture<String> future = testMethod.doTest(request);
            future.exceptionally(ex -> {
                failed.incrementAndGet();
                if (debug) System.out.println(ex.getMessage());
                return null;
            });
            future.thenAccept(resp -> {
                if (debug) System.out.println(resp);
                succeed.incrementAndGet();
            });
        } catch (Throwable e) {
            if (debug) {
                System.out.println(e.getMessage());
            }
            failed.incrementAndGet();
        }

    }
}
