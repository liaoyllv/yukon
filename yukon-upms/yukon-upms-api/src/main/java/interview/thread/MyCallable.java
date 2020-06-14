package interview.thread;

import java.util.concurrent.Callable;

/**
 * <p></p>
 *
 * @author liaoyl
 * @version 1.0 2020/04/06 20:22
 **/
public class MyCallable implements Callable {
    @Override
    public Object call() throws Exception {
        Thread.sleep(1000);
        return Thread.currentThread().getName();
    }
}
