package interview.base;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;

/**
 * <p></p>
 *
 * @author liaoyl
 * @version 1.0 2020/03/14 15:26
 **/
public class ListOperation {


    public static void main(String[] args) {

        LinkedList<String> strings = new LinkedList<>();
        strings.add("1");
        strings.add("2");
        Iterator<String> iterator = strings.iterator();
//        1.8之后出现了新的操作，strings.removeIf(next -> Objects.equals("1", next));
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (Objects.equals("1", next)) {
                iterator.remove();
            }
        }

        System.out.println(strings.size());

    }
}
