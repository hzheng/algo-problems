import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Utils;

// LC638: https://leetcode.com/problems/shopping-offers/
//
// There are some kinds of items to sell. Each item has a price. However, there
// are some special offers, and a special offer consists of one or more
// different kinds of items with a sale price. You are given the each item's
// price, a set of special offers, and the number we need to buy for each item.
//  The job is to output the lowest price you have to pay for exactly certain
// items as given, where you could make optimal use of the special offers.
// Each special offer is represented in the form of an array, the last number
// represents the price you need to pay for this special offer, other numbers
// represents how many specific items you could get if you buy this offer.
// You could use any of special offers as many times as you want.
// Note:
// There are at most 6 kinds of items, 100 special offers.
// For each item, you need to buy at most 6 of them.
// You are not allowed to buy more items than you want, even if that would lower
// the overall price.
public class ShoppingOffers {
    // Recursive + DFS + Backtracking
    // beats 79.05%(20 ms for 53 tests)
    public int shoppingOffers(List<Integer> price, List<List<Integer> > special,
                              List<Integer> needs) {
        return minPrice(price, special, needs.toArray(new Integer[0]), 0);
    }

    private int minPrice(List<Integer> price, List<List<Integer> > special,
                         Integer[] needs, int start) {
        int res = 0;
        for (int i = 0; i < needs.length; i++) {
            res += needs[i] * price.get(i);
        }
        if (start == special.size()) return res;

        List<Integer> specialList = special.get(start);
        int pay = specialList.get(specialList.size() - 1);
        outer : for (int i = 0;; i++) {
            Integer[] copy = needs.clone();
            for (int j = 0; j < needs.length; j++) {
                if ((copy[j] -= specialList.get(j) * i) < 0) break outer;
            }
            res = Math.min(res, i * pay + minPrice(
                               price, special, copy, start + 1));
        }
        return res;
    }

    // Recursive + DFS + Backtracking
    // beats 55.81%(24 ms for 53 tests)
    public int shoppingOffers2(List<Integer> price,
                               List<List<Integer> > special,
                               List<Integer> needs) {
        return shopping(price, special, needs);
    }

    private int shopping(List<Integer> price, List<List<Integer> > special,
                         List<Integer> needs) {
        int res = dotProduct(needs, price);
        for (List<Integer> s : special) {
            List<Integer> clone = new ArrayList<>(needs);
            int i = 0;
            for (i = 0; i < needs.size(); i++) {
                int diff = clone.get(i) - s.get(i);
                if (diff < 0) break;

                clone.set(i, diff);
            }
            if (i != needs.size()) continue;

            res = Math.min(res,
                           s.get(i) + shopping(price, special, clone));
        }
        return res;
    }

    // DFS + Dynamic Programming(Top-Down)
    // beats 69.57%(22 ms for 53 tests)
    public int shoppingOffers3(List<Integer> price,
                               List<List<Integer> > special,
                               List<Integer> needs) {
        return shopping(price, special, needs, new HashMap<>());
    }

    private int shopping(List<Integer> price,
                         List<List<Integer> > special,
                         List<Integer> needs,
                         Map<List<Integer>, Integer> memo) {
        if (memo.containsKey(needs)) return memo.get(needs);

        int res = dotProduct(needs, price);
        for (List<Integer> s : special) {
            List<Integer> clone = new ArrayList<>(needs);
            int i = 0;
            for (i = 0; i < needs.size(); i++) {
                int diff = clone.get(i) - s.get(i);
                if (diff < 0) break;

                clone.set(i, diff);
            }
            if (i != needs.size()) continue;

            res = Math.min(res,
                           s.get(i) + shopping(price, special, clone, memo));
        }
        memo.put(needs, res);
        return res;
    }

    private int dotProduct(List<Integer> a, List<Integer> b) {
        int res = 0;
        for (int i = a.size() - 1; i >= 0; i--) {
            res += a.get(i) * b.get(i);
        }
        return res;
    }

    void test(Integer[] prices, Integer[][] special,
              Integer[] need,
              int expected) {
        List<Integer> priceList = Arrays.asList(prices);
        List<List<Integer> > specialList = Utils.toList(special);
        List<Integer> needList = Arrays.asList(need);
        assertEquals(expected,
                     shoppingOffers(priceList, specialList, needList));
        assertEquals(expected,
                     shoppingOffers2(priceList, specialList, needList));
        assertEquals(expected,
                     shoppingOffers3(priceList, specialList, needList));
    }

    @Test
    public void test() {
        test(new Integer[] {2, 3, 4},
             new Integer[][] {{1, 1, 0, 4}, {2, 2, 1, 9}},
             new Integer[] {1, 2, 1}, 11);
        test(new Integer[] {2, 5}, new Integer[][] {{3, 0, 5}, {1, 2, 10}},
             new Integer[] {3, 2}, 14);
        test(new Integer[] {1, 1}, new Integer[][] {{1, 1, 2}, {1, 1, 3}},
             new Integer[] {6, 6}, 12);
        test(new Integer[] {500}, new Integer[][] {{2, 1}, {3, 2}, {4, 1}},
             new Integer[] {9}, 4);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
