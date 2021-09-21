import org.junit.Test;

import static org.junit.Assert.*;

// LC1946: https://leetcode.com/problems/largest-number-after-mutating-substring/
//
// You are given a string num, which represents a large integer. You are also given a 0-indexed
// integer array change of length 10 that maps each digit 0-9 to another digit. More formally, digit
// d maps to digit change[d].
// You may choose to mutate a single substring of num. To mutate a substring, replace each digit
// num[i] with the digit it maps to in change (i.e. replace num[i] with change[num[i]]).
// Return a string representing the largest possible integer after mutating (or choosing not to) a
// single substring of num.
// A substring is a contiguous sequence of characters within the string.
//
// Constraints:
// 1 <= num.length <= 10^5
// num consists of only digits 0-9.
// change.length == 10
// 0 <= change[d] <= 9
public class LargestNumberAfterMutation {
    // time complexity: O(N), space complexity: O(N)
    // 17 ms(60.12%), 63.7 MB(27.17%) for 279 tests
    public String maximumNumber(String num, int[] change) {
        char[] res = num.toCharArray();
        boolean changed = false;
        for (int i = 0; i < res.length; i++) {
            int cur = res[i] - '0';
            int cand = change[cur];
            if (cur < cand) {
                res[i] = (char)(cand + '0');
                changed = true;
            } else if (cur > cand && changed) {
                break;
            }
        }
        return String.valueOf(res);
    }

    private void test(String num, int[] change, String expected) {
        assertEquals(expected, maximumNumber(num, change));
    }

    @Test public void test() {
        test("132", new int[] {9, 8, 5, 0, 3, 6, 4, 2, 6, 8}, "832");
        test("021", new int[] {9, 4, 3, 5, 7, 2, 1, 9, 0, 6}, "934");
        test("5", new int[] {1, 4, 7, 5, 3, 2, 5, 6, 9, 4}, "5");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
