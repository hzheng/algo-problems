import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC825: https://leetcode.com/problems/friends-of-appropriate-ages/
//
// Person A will NOT friend request person B (B != A) if any of the following
// conditions are true:
// age[B] <= 0.5 * age[A] + 7
// age[B] > age[A]
// age[B] > 100 && age[A] < 100
// Otherwise, A will friend request B.
// How many total friend requests are made?
// Notes:
// 1 <= ages.length <= 20000.
// 1 <= ages[i] <= 120.
public class FriendRequests {
    // Sort + Binary Search
    // time complexity: O(N * log(N)), space complexity: O(log(N))
    // beats %(26 ms for 83 tests)
    public int numFriendRequests(int[] ages) {
        int n = ages.length;
        Arrays.sort(ages);
        int res = 0;
        for (int i = n - 1; i > 0; i--) {
            int cur = ages[i];
            int lowBound = (int)(cur * 0.5 + 7) + 1;
            int first = Arrays.binarySearch(ages, 0, i, lowBound);
            if (first < 0) {
                first = -first - 1;
            } else {
                for (; first > 0; first--) {
                    if (ages[first - 1] != lowBound) break;
                }
            }
            int diff = i - first;
            res += diff;
            for (; i > 0 && ages[i - 1] == cur; i--) {
                res += diff;
            }
        }
        return res;
    }

    // Sort + Binary Search
    // time complexity: O(N * log(N)), space complexity: O(log(N))
    // beats %(32 ms for 83 tests)
    public int numFriendRequests2(int[] ages) {
        int n = ages.length;
        double[] sortedAges = new double[n];
        for (int i = 0; i < n; i++) {
            sortedAges[i] = ages[i];
        }
        Arrays.sort(sortedAges);
        int res = 0;
        for (int i = n - 1; i > 0; i--) {
            double cur = sortedAges[i];
            double lowBound = (cur * 0.5 + 7) + 0.1;
            int first = -Arrays.binarySearch(sortedAges, 0, i, lowBound) - 1;
            int diff = i - first;
            res += diff;
            for (; i > 0 && sortedAges[i - 1] == cur; i--) {
                res += diff;
            }
        }
        return res;
    }

    // Bucket
    // time complexity: O(N + MaxAge ^ 2), space complexity: O(MaxAge)
    // beats %(10 ms for 83 tests)
    public int numFriendRequests3(int[] ages) {
        int maxAge = 120;
        int[] counts = new int[maxAge + 1];
        for (int age : ages) {
            counts[age]++;
        }
        int res = 0;
        for (int i = maxAge; i > 0; i--) {
            int count = counts[i];
            int lowBound = (int)(i * 0.5 + 7) + 1;
            for (int j = lowBound; j < i; j++) {
                res += count * counts[j];
            }
            if (i >= lowBound) {
                res += count * (count - 1);
            }
        }
        return res;
    }

    // Bucket
    // time complexity: O(N + MaxAge ^ 2), space complexity: O(MaxAge)
    // beats %(11 ms for 83 tests)
    public int numFriendRequests3_2(int[] ages) {
        int maxAge = 120;
        int[] counts = new int[maxAge + 1];
        for (int age : ages) {
            counts[age]++;
        }
        int res = 0;
        for (int i = maxAge; i > 0; i--) {
            for (int j = i / 2 + 8, count = counts[i]; j <= i; j++) {
                res += count * (counts[j] - ((i == j) ? 1 : 0));
            }
        }
        return res;
    }

    // Bucket
    // time complexity: O(N + MaxAge), space complexity: O(MaxAge)
    // beats %(10 ms for 83 tests)
    public int numFriendRequests4(int[] ages) {
        int maxAge = 120;
        int[] counts = new int[maxAge + 1];
        for (int age : ages) {
            counts[age]++;
        }
        for (int i = 1; i <= maxAge; i++) {
            counts[i] += counts[i - 1];
        }
        int res = 0;
        for (int i = maxAge;; i--) {
            int lowBound = (int)(i * 0.5 + 7) + 1;
            if (i < lowBound) break;

            res += (counts[i] - counts[lowBound - 1] - 1)
                   * (counts[i] - counts[i - 1]);
        }
        return res;
    }

    // Bucket
    // time complexity: O(N + MaxAge), space complexity: O(MaxAge)
    // beats %(11 ms for 83 tests)
    public int numFriendRequests5(int[] ages) {
        int maxAge = 120;
        int[] counts = new int[maxAge + 1];
        for (int age : ages) {
            counts[age]++;
        }
        int res = 0;
        for (int i = 15, minAge = i, sum = 0; i <= maxAge; i++) {
            for (; minAge <= 0.5 * i + 7; sum -= counts[minAge++]) {}
            sum += counts[i];
            res += counts[i] * (sum - 1);
        }
        return res;
    }

    void test(int[] ages, int expected) {
        assertEquals(expected, numFriendRequests(ages));
        assertEquals(expected, numFriendRequests2(ages));
        assertEquals(expected, numFriendRequests3(ages));
        assertEquals(expected, numFriendRequests3_2(ages));
        assertEquals(expected, numFriendRequests4(ages));
        assertEquals(expected, numFriendRequests5(ages));
    }

    @Test
    public void test() {
        test(new int[] {15, 16}, 0);
        test(new int[] {16, 16}, 2);
        test(new int[] {20, 30, 100, 110, 120}, 3);
        test(new int[] {16, 17, 18}, 2);
        test(new int[] {56, 117, 42, 55, 100, 27, 97, 113, 56, 57, 117, 13, 42,
                        119, 67}, 48);
        test(new int[] {73, 106, 39, 6, 26, 15, 30, 100, 71, 35, 46, 112, 6,
                        60, 110}, 29);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
