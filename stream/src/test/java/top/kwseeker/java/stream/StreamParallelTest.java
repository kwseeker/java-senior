package top.kwseeker.java.stream;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 看到有人提到一个关于stream的一个问题
 * 大概意思如下：
 *  即如下测试代码，为何注释掉下面两行后，执行完耗时是大概5s,不注释的话耗时却是大概1s
 *      //.collect(Collectors.toList())
 *      //.stream()
 *
 * 我猜测是因为 stream() 后的操作都当作一个不可分割的任务执行的，
 * 去掉上面两行，则 .map().map().collect() 是当成一个任务，第一个map中异步提交任务，但是第二个map中同步等待任务执行结果，相当于还是同步执行，而且stream()就是单线程迭代，最终就是5个任务串行
 *  iterate(map -> map -> collect)
 * 而去掉上面两行，则 分成了两阶段的任务, 第一阶段迭代任务并发收集Future(任务是异步自行的)，第二阶段迭代任务同步等待获取结果
 *  iterate(map -> collect) -> iterate(map -> collect)
 *
 * 其实一般不会像Demo中这么使用，异步迭代的话应该使用 parallelStream(), 如 testParallel2() 中所示
 */
public class StreamParallelTest {

    @Test
    public void testParallel() throws Exception {
        List<Callable<Integer>> tasks = new ArrayList<>();
        Callable<Integer> task = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(1000);
                return ThreadLocalRandom.current().nextInt(100);
            }
        };
        tasks.add(task);
        tasks.add(task);
        tasks.add(task);
        tasks.add(task);
        tasks.add(task);

        long startTs = System.currentTimeMillis();
        List<Object> results = tasks.stream()
                .map(tk -> {
                    try {
                        System.out.println("start ts: " + System.currentTimeMillis() + ", thread: " + Thread.currentThread().getName());
                        return CompletableFuture.supplyAsync(() -> {
                            try {
                                return tk.call();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return -1;
                        });
                    } finally {
                        System.out.println("end   ts: " + System.currentTimeMillis() + ", thread: " + Thread.currentThread().getName());
                    }
                })
                //下面两行去掉后，会发现上面map每隔1s执行一次
                //.collect(Collectors.toList())
                //.stream()
                //.map(CompletableFuture::join)
                .map(future -> {
                    try {
                        System.out.println("wait start ts: " + System.currentTimeMillis() + ", thread: " + Thread.currentThread().getName());
                        return future.get(20, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        System.out.println("wait end   ts: " + System.currentTimeMillis() + ", thread: " + Thread.currentThread().getName());
                    }
                    return -1;
                })
                .collect(Collectors.toList());
        long endTs = System.currentTimeMillis();
        System.out.println("cost: " + (endTs - startTs));
        results.forEach(System.out::println);
    }

    @Test
    public void testParallel2() throws Exception {
        List<Callable<Integer>> tasks = new ArrayList<>();
        Callable<Integer> task = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(1000);
                return ThreadLocalRandom.current().nextInt(100);
            }
        };
        tasks.add(task);
        tasks.add(task);
        tasks.add(task);
        tasks.add(task);
        tasks.add(task);

        long startTs = System.currentTimeMillis();
        List<Object> results = tasks.parallelStream()
                .map(tk -> {
                    try {
                        System.out.println("start ts: " + System.currentTimeMillis() + ", thread: " + Thread.currentThread().getName());
                        try {
                            return tk.call();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return -1;
                    } finally {
                        System.out.println("end   ts: " + System.currentTimeMillis() + ", thread: " + Thread.currentThread().getName());
                    }
                })
                .collect(Collectors.toList());
        long endTs = System.currentTimeMillis();
        System.out.println("cost: " + (endTs - startTs));
        results.forEach(System.out::println);
    }
}
