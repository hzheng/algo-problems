import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC423: https://leetcode.com/problems/reconstruct-original-digits-from-english
//
// Given a non-empty string containing an out-of-order English representation of
// digits 0-9, output the digits in ascending order.
// Note:
// Input contains only lowercase English letters.
// Input is guaranteed to be valid and can be transformed to its original digits.
// Input length is less than 50,000.
public class OriginalDigits {
    // beast N/A(53 ms for 24 tests)
    public String originalDigits(String s) {
        char[] cs = s.toCharArray();
        int[] charCounts = new int[26];
        for (char c : cs) {
            charCounts[c - 'a']++;
        }
        String[] digits = {"zero", "one", "two", "three", "four", "five",
                           "six", "seven", "eight", "nine"};
        List<Integer> res = new ArrayList<>();
        int[] order = {0, 6, 2, 8, 7, 3, 4, 1, 9, 5};
        for (int i : order) {
            int[] digitCharCount = new int[26];
            for (char c : digits[i].toCharArray()) {
                digitCharCount[c - 'a']++;
            }
            while (greater(charCounts, digitCharCount)) {
                consume(charCounts, digitCharCount);
                res.add(i);
            }
        }
        Collections.sort(res);
        StringBuilder sb = new StringBuilder();
        for (int d : res) {
            sb.append(d);
        }
        return sb.toString();
    }

    private boolean greater(int[] count1, int[] count2) {
        for (int i = 0; i < count1.length; i++) {
            if (count1[i] < count2[i]) return false;
        }
        return true;
    }

    private void consume(int[] count1, int[] count2) {
        for (int i = 0; i < count1.length; i++) {
            count1[i] -= count2[i];
        }
    }

    // beast N/A(9 ms for 24 tests)
    public String originalDigits2(String s) {
        int[] charCounts = new int[26];
        for (char c : s.toCharArray()) {
            charCounts[c - 'a']++;
        }
        char[][] digits = {{'z', 'e', 'r', 'o'}, {'o', 'n', 'e'},
                           {'t', 'w', 'o'}, {'t', 'h', 'r', 'e', 'e'},
                           {'f', 'o', 'u', 'r'}, {'f', 'i', 'v', 'e'},
                           {'s', 'i', 'x'}, {'s', 'e', 'v', 'e', 'n'},
                           {'e', 'i', 'g', 'h', 't'}, {'n', 'i', 'n','e'}};
        char[] order = {'0', '6', '2', '8', '7', '3', '4', '1', '5', '9'};
        char[] uniqChars = {'z', 'x', 'w', 'g', 's', 'h', 'u', 'o', 'v', 'i'};
        char[][] original = new char[10][];
        for (int i = 0; i < order.length; i++) {
            int count = charCounts[uniqChars[i] - 'a'];
            int index = order[i] - '0';
            original[index] = new char[count];
            if (count > 0) {
                Arrays.fill(original[index], order[i]);
                for (char c : digits[index]) {
                    charCounts[c - 'a'] -= count;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (char[] digit : original) {
            sb.append(digit);
        }
        return sb.toString();
    }

    // beast N/A(9 ms for 24 tests)
    public String originalDigits3(String s) {
        int[] counts = new int[26];
        for (char c : s.toCharArray()) {
            counts[c - 'a']++;
        }
        int[] res = new int[10];
        res[0] = counts['z' - 'a'];
        res[2] = counts['w' - 'a'];
        res[6] = counts['x' - 'a'];
        res[7] = counts['s' - 'a'] - res[6];
        res[5] = counts['v' - 'a'] - res[7];
        res[4] = counts['f' - 'a'] - res[5];
        res[8] = counts['g' - 'a'];
        res[3] = counts['h' - 'a'] - res[8];
        res[9] = counts['i' - 'a'] - res[8] - res[5] - res[6];
        res[1] = counts['n' - 'a'] - 2 * res[9] - res[7];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < res[i]; ++j) {
                sb.append((char)(i + '0'));
            }
        }
        return sb.toString();
    }

    // https://discuss.leetcode.com/topic/63386/one-pass-o-n-java-solution-simple-and-clear
    public String originalDigits4(String s) {
        int[] count = new int[10];
        for (char c : s.toCharArray()) {
            if (c == 'z') count[0]++;
            if (c == 'w') count[2]++;
            if (c == 'x') count[6]++;
            if (c == 's') count[7]++; //7-6
            if (c == 'g') count[8]++;
            if (c == 'u') count[4]++;
            if (c == 'f') count[5]++; //5-4
            if (c == 'h') count[3]++; //3-8
            if (c == 'i') count[9]++; //9-8-5-6
            if (c == 'o') count[1]++; //1-0-2-4
        }
        count[7] -= count[6];
        count[5] -= count[4];
        count[3] -= count[8];
        count[9] -= count[8] + count[5] + count[6];
        count[1] -= count[0] + count[2] + count[4];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 9; i++) {
            for (int j = 0; j < count[i]; j++) {
                sb.append(i);
            }
        }
        return sb.toString();
    }

    void test(String s, String expected) {
        assertEquals(expected, originalDigits(s));
        assertEquals(expected, originalDigits2(s));
        assertEquals(expected, originalDigits3(s));
        assertEquals(expected, originalDigits4(s));
    }

    @Test
    public void test() {
        test("onezerozeronine", "0019");
        test("owoztneoer", "012");
        test("fviefuro", "45");
        test("fviefuro", "45");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("OriginalDigits");
    }
}
