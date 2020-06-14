package interview.ds.str;

import java.util.HashSet;

/**
 * <p>
 *     最长回文串
 *     LeetCode: 给定一个包含大写字母和小写字母的字符串，找到通过这些字母构造成的最长的回文串。在构造过程中，请注意区分大小写。比如"Aa"不能当做一个回文字符串。注 意:假设字符串的长度不会超过 1010。
 *     输入:
 *      "abccccdd"
 *     输出:
 *      7
 *     解释:
 *      我们可以构造的最长的回文串是"dccaccd", 它的长度是 7。
 *
 * </p>
 *
 * @author liaoyl
 * @version 1.0 2020/05/18 10:57 AM
 **/
public class BuildLongestPalindrome {

    public  int longestPalindrome(String s) {
        if (s.length() == 0) {
            return 0;
        }
        // 用于存放字符
        HashSet<Character> hashset = new HashSet<>();
        char[] chars = s.toCharArray();
        int count = 0;
        for (char aChar : chars) {
            if (!hashset.contains(aChar)) {
                // 如果hashset没有该字符就保存进去
                hashset.add(aChar);
            } else {
                // 如果有,就让count++（说明找到了一个成对的字符），然后把该字符移除
                hashset.remove(aChar);
                count++;
            }
        }
        return hashset.isEmpty() ? count * 2 : count * 2 + 1;
    }

}
