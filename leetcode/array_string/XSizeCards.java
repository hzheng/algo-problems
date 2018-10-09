import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC914: https://leetcode.com/problems/x-of-a-kind-in-a-deck-of-cards/
//
// In a deck of cards, each card has an integer written on it.
// Return true if and only if you can choose X >= 2 such that it is possible to
// split the entire deck into 1 or more groups of cards, where:
// Each group has exactly X cards.
// All the cards in each group have the same integer.
//
// Note:
// 1 <= deck.length <= 10000
// 0 <= deck[i] < 10000
public class XSizeCards {
    // beats 72.00%(15 ms for 69 tests)
    public boolean hasGroupsSizeX(int[] deck) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int card : deck) {
            count.put(card, count.getOrDefault(card, 0) + 1);
        }
        int factor = 0;
        for (int v : count.values()) {
            if ((factor = gcd(factor, v)) == 1) return false;
        }
        return true;
    }

    private int gcd(int a, int b) {
        return (a == 0) ? b : gcd(b % a, a);
    }

    // beats 99.42%(6 ms for 69 tests)
    public boolean hasGroupsSizeX2(int[] deck) {
        int MAX = 10000;
        int[] count = new int[MAX];
        for (int card : deck) {
            count[card]++;
        }
        int factor = -1;
        for (int i = 0; i < MAX; i++) {
            if (count[i] > 0) {
                factor = (factor < 0) ? count[i] : gcd(factor, count[i]);
            }
        }
        return factor >= 2;
    }

    void test(int[] deck, boolean expected) {
        assertEquals(expected, hasGroupsSizeX(deck));
        assertEquals(expected, hasGroupsSizeX2(deck));
    }

    @Test
    public void test() {
        test(new int[] {1, 2, 3, 4, 4, 3, 2, 1}, true);
        test(new int[] {1, 1, 1, 2, 2, 2, 3, 3}, false);
        test(new int[] {1}, false);
        test(new int[] {1, 1}, true);
        test(new int[] {1, 1, 2, 2, 2, 2}, true);
        test(new int[] {1, 1, 1, 1, 2, 2, 2, 2, 2, 2}, true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
