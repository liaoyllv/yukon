package interview.lock;

/**
 * <p></p>
 *
 * @author liaoyl
 * @version 1.0 2020/03/17 13:45
 **/
public class LockDemo {

    public static String staticAttr = "11";
    public  String nonStaticAttr = "22";

    public static void staticFun() {
        System.out.println("staticFun");
    }

    public void nonStaticFun() {
        System.out.println("nonStaticFun");
    }

    public static void main(String[] args) {
        LockDemo lockDemo = new LockDemo();
        new Thread(() -> {
            synchronized (lockDemo) {
                System.out.println(Thread.currentThread() + "get resource1");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "waiting get resource2");
                synchronized (lockDemo) {
                    System.out.println(Thread.currentThread() + "get resource2");
                }
            }
        }, "线程 1").start();

        new Thread(() -> {
            synchronized (lockDemo) {
                System.out.println(Thread.currentThread() + "get resource2");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "waiting get resource1");
                synchronized (lockDemo) {
                    System.out.println(Thread.currentThread() + "get resource1");
                }
            }
        }, "线程 2").start();



    }


}
