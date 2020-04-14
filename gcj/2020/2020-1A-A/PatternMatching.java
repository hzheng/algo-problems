import java.util.*;
import java.io.*;

// Qualification Round 2020: Problem A - Pattern Matching
//
// Many terminals use asterisks (*) to signify "any string", including the empty string. In this problem, formally, a
// pattern is a string consisting of only uppercase English letters and asterisks (*), and a name is a string consisting
// of only uppercase English letters. A pattern p matches a name m if there is a way of replacing every asterisk in p
// with a (possibly empty) string to obtain m. Notice that each asterisk may be replaced by a different string.
// Given N patterns, can you find a single name of at most 104 letters that matches all those patterns at once, or
// report that it cannot be done?
// Input
// The first line of the input gives the number of test cases, T. T test cases follow. Each test case starts with a line
// with a single integer N: the number of patterns to simultaneously match. Then, N lines follow, each one containing a
// single string Pi representing the i-th pattern.
// Output
// For each test case, output one line containing Case #x: y, where x is the test case number (starting from 1) and y is
// any name containing at most 104 letters such that each Pi matches y according to the definition above, or *
// (i.e., just an asterisk) if there is no such name.
// Limits
// Time limit: 20 seconds per test set.
// Memory limit: 1GB.
// 1 ≤ T ≤ 100.
// 2 ≤ N ≤ 50.
// 2 ≤ length of Pi ≤ 100, for all i.
// Each character of Pi is either an uppercase English letter or an asterisk (*), for all i.
// At least one character of Pi is an uppercase English letter, for all i.
// Test set 1 (Visible Verdict)
// Exactly one character of Pi is an asterisk (*), for all i.
// The leftmost character of Pi is the only asterisk (*), for all i.
// Test set 2 (Visible Verdict)
// Exactly one character of Pi is an asterisk (*), for all i.
// Test set 3 (Visible Verdict)
// At least one character of Pi is an asterisk (*), for all i.
public class PatternMatching {
    public static String solve(int N, String[] words) {
        List<String> prefixes = new ArrayList<>();
        List<String> postfixes = new ArrayList<>();
        Set<String> middles = new HashSet<>();
        for (String word : words) {
            int first = word.indexOf('*');
            int last = word.lastIndexOf('*');
            prefixes.add(word.substring(0, first));
            postfixes.add(word.substring(last + 1));
            if (first < last) {
                middles.add(word.substring(first + 1, last).replaceAll("\\*", ""));
            }
        }
        String prefix = findCommon(prefixes, true);
        if (prefix == null) return null;

        String postfix = findCommon(postfixes, false);
        if (postfix == null) return null;

        StringBuilder sb = new StringBuilder(prefix);
        for (String middle : middles) {
            sb.append(middle);
        }
        sb.append(postfix);
        return sb.toString();
    }

    private static String findCommon(List<String> words, boolean isPrefix) {
        int maxLen = 0;
        String maxStr = "";
        for (String word : words) {
            int len = word.length();
            if (len > maxLen) {
                maxLen = len;
                maxStr = word;
            }
        }
        for (String word : words) {
            int len = word.length();
            if (isPrefix) {
                if (!maxStr.substring(0, len).equals(word)) return null;
            } else {
                if (!maxStr.substring(maxLen - len).equals(word)) return null;
            }
        }
        return maxStr;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        PrintStream out = System.out;
        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            int N = in.nextInt();
            out.format("Case #%d: ", i);
            String[] words = new String[N];
            for (int j = 0; j < N; j++) {
                words[j] = in.next();
            }
            String res = solve(N, words);
            out.println(res == null ? "*" : res);
        }
    }
}
