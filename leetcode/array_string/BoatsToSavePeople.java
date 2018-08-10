import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC885: https://leetcode.com/problems/boats-to-save-people/
//
// The i-th person has weight people[i], and each boat can carry a maximum
// weight of limit. Each boat carries at most 2 people at the same time,
// provided the sum of the weight of those people is at most limit. Return the
// minimum number of boats to carry every given person.  (It is guaranteed each
// person can be carried by a boat.)
public class BoatsToSavePeople {
    // Sort + Two Pointers
    // time complexity: O(N * log(N)), space complexity: O(log(N))
    // beats 94.22%(27 ms for 77 tests)
    public int numRescueBoats(int[] people, int limit) {
        int res = 0;
        Arrays.sort(people);
        for (int i = people.length - 1, j = 0; i >= j; i--, res++) {
            if (people[i] + people[j] <= limit) {
                j++;
            }
        }
        return res;
    }

    // Sort + Two Pointers
    // time complexity: O(N * log(N)), space complexity: O(log(N))
    // beats 94.22%(27 ms for 77 tests)
    public int numRescueBoats_2(int[] people, int limit) {
        Arrays.sort(people);
        int i = people.length - 1;
        for (int j = 0; i >= j; i--) {
            if (people[i] + people[j] <= limit) {
                j++;
            }
        }
        return people.length - 1 - i;
    }

    void test(int[] people, int limit, int expected) {
        assertEquals(expected, numRescueBoats(people, limit));
        assertEquals(expected, numRescueBoats_2(people, limit));
    }

    @Test
    public void test() {
        test(new int[] { 1, 2 }, 3, 1);
        test(new int[] { 3, 2, 2, 1 }, 3, 3);
        test(new int[] { 3, 5, 3, 4 }, 5, 4);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
