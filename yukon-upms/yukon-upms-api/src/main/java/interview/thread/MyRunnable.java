package interview.thread;

import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p></p>
 *
 * @author liaoyl
 * @version 1.0 2020/04/06 16:47
 **/
public class MyRunnable implements Runnable {

    private String command;

    public MyRunnable(String s) {
        this.command = s;
    }

    @Override
    public String toString() {
        return this.command;
    }

    private void processCommand() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ":start,time:" + new Date());
        processCommand();
        System.out.println(Thread.currentThread().getName() + ":end,time:" + new Date());
    }

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        String  s = "111";
    }


}
