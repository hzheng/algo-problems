import org.junit.Test;
import static org.junit.Assert.*;

// LC481: https://leetcode.com/problems/magical-string/
//
// A magical string S consists of only '1' and '2' and obeys the following rules:
// The string S is magical because concatenating the number of contiguous
// occurrences of characters '1' and '2' generates the string S itself.
// The first few elements of string S is the following: S = "1221121221221121122……"
// If we group the consecutive '1's and '2's in S, it will be:
// 1 22 11 2 1 22 1 22 11 2 11 22 ......
// and the occurrences of '1's or '2's in each group are:
// 1 2	2 1 1 2 1 2 2 1 2 2 ......
// You can see that the occurrence sequence above is the S itself.
// Given an integer N as input, return the number of '1's in the first N number
// in the magical string S.
// Note: N will not exceed 100,000.
public class MagicalString {
    // beats 66.77%(15 ms for 65 tests)
    public int magicalString(int n) {
        if (n < 2) return n;

        int[] seq = new int[n];
        seq[0] = 1;
        int count = 1;
        for (int i = 1, stream = 1, group = 0; i < n; i++) {
            if (stream < seq[group]) {
                stream++;
                seq[i] = seq[i - 1];
            } else {
                stream = 1;
                seq[i] = 3 - seq[i - 1];
                group++;
            }
            if (seq[i] == 1) {
                count++;
            }
        }
        return count;
    }

    // beats 84.96%(12 ms for 65 tests)
    public int magicalString2(int n) {
        if (n < 2) return n;

        int[] seq = new int[n + 1];
        seq[0] = 1;
        seq[1] = seq[2] = 2;
        int count = 1;
        for (int head = 2, tail = 3, num = 1; tail < n; head++, num ^= 3) {
            for (int i = 0; i < seq[head]; i++, tail++) {
                seq[tail] = num;
                if (num == 1 && tail < n) {
                    count++;
                }
            }
        }
        return count;
    }

    void test(int n, int expected) {
        assertEquals(expected, magicalString(n));
        assertEquals(expected, magicalString2(n));
    }

    @Test
    public void test() {
        test(0, 0);
        test(1, 1);
        test(2, 1);
        test(3, 1);
        test(6, 3);
        test(7, 4);
        test(9, 4);
        test(10, 5);
        test(12, 5);
        test(13, 6);
        test(14, 7);
        test(15, 7);
        test(16, 8);
        test(17, 9);
        test(18, 9);
        test(18, 9);
        test(19, 9);
        test(20, 10);
        test(21, 10);
        test(22, 11);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MagicalString");
    }
}
