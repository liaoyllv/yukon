package interview.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 我们在代码中模拟了 10 个任务，我们配置的核心线程数为 5 、等待队列容量为 100 ，
 * 所以每次只可能存在 5 个任务同时执行，剩下的 5 个任务会被放到等待队列中去。当前的 5 个任务执行完成后，才会执行剩下的 5 个任务。
 * </p>
 *
 * @author liaoyl
 * @version 1.0 2020/04/06 16:52
 **/
public class MyThreadPoolExecutor {

    /**
     * 线程池的基本大小，即在没有任务需要执行的时候线程池的大小，并且只有在工作队列满了的情况下才会创建超出这个数量的线程。
     */
    private static final int CORE_POOL_SIZE = 5;
    /**
     * 线程池中允许的最大线程数
     */
    private static final int MAX_POOL_SIZE = 10;
    private static final int QUEUE_CAPACITY = 2;
    private static final Long KEEP_ALIVE_TIME = 1L;

    public static void main(String[] args) {

        // 使用阿里巴巴推荐的创建线程池的方式
        // 通过ThreadPoolExecutor构造函数自定义参数创建
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(QUEUE_CAPACITY),
                new ThreadPoolExecutor.CallerRunsPolicy());

        for (int i = 0; i < 10; i++) {
            // 创建WorkerThread对象（WorkerThread类实现了Runnable 接口）
            Runnable worker = new MyRunnable("" + i);
            // 执行Runnable
            executor.execute(worker);
        }
        // 终止线程池
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads");
    }
}
