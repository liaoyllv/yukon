package interview.ds;

import java.util.LinkedList;

/**
 * <p>
 * 栈应该有如下几个实现：创建一个空栈，插入一个元素，弹出最顶层的元素，栈的大小
 * </p>
 *
 * @author liaoyl
 * @version 1.0 2020/04/12 1:45 PM
 **/
public class MyStack<T> {

    private LinkedList<T> values;

    public MyStack() {
        values = new LinkedList<>();
    }

    void push(T t) {
        values.push(t);
    }

    T pop() {
        return values.pop();
    }

    int size() {
        return values.size();
    }


    public static void main(String[] args) {
        LinkedList<Integer> integers = new LinkedList<>();
        integers.push(1);
        integers.push(2);
        integers.forEach(System.out::print);

        integers.pop();
        integers.forEach(System.out::print);


    }

}
