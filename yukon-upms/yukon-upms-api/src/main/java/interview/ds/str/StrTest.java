package interview.ds.str;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class StrTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int lineSize = scanner.nextInt();


        LinkedList<String> allPackages = new LinkedList<>();
        LinkedList<String> rightPac = new LinkedList<>();
        LinkedList<String> packages2 = new LinkedList<>();
        LinkedList<String> packages3 = new LinkedList<>();
        LinkedList<String> packages4 = new LinkedList<>();
        for (int i = 0; i < lineSize; i++) {
            String str = scanner.nextLine();
            allPackages.add(str);
            rightPac.add(str.split("")[1]);
        }

    }




    private static String clear(String str) {
        String tmp = "../";
        if (str.contains(tmp)) {
            int index = str.lastIndexOf(tmp);
            String preStr  = str.substring(0, index);
            preStr = clear(preStr);
            if (preStr.length() < 2) {
                return str.substring(index + 3);
            }else {
                return preStr.substring(0, preStr.length() - 2) + str.substring(index + 3);
            }
        }else {
            return str;
        }
    }


    /**
     *
     * @param str 输入字符
     * @param start 为清除开始下标
     * @param end 清除结束下标
     */
//    private static String clear(String str, int start, int end) {
//        String tmp = "../";
//
//        if (!str.contains(tmp)) {
//            return str;
//        } else {
//            if (start == -1) {
//                // 从查到的位置开始设置下标
//                start = str.indexOf(tmp);
//                end = start + 3;
//            }
//            // 如果 ../ 前面只有两个字符
//            if (start<2) {
//                return str.substring(end);
//            }else {
//                return clear()
//
//
//            }
//
//            String substring = str.substring(0, index - 2);
//            return substring + str.substring(index + 3);
//        }
//    }
}
