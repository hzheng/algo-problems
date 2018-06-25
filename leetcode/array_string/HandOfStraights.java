import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC846: https://leetcode.com/problems/hand-of-straights/
//
// Alice has a hand of cards, given as an array of integers. Now she wants to
// rearrange the cards into groups so that each group is size W, and consists of
// W consecutive cards. Return true if and only if she can.
public class HandOfStraights {
    // SortedMap
    // time complexity: O(N * log(N)), time complexity: O(N)
    // beats %(89 ms for 65 tests)
    public boolean isNStraightHand(int[] hand, int W) {
        if (hand.length % W != 0) return false;

        SortedMap<Integer, Integer> map = new TreeMap<>();
        for (int h : hand) {
            map.put(h, map.getOrDefault(h, 0) + 1);
        }
        while (!map.isEmpty()) {
            int key = map.firstKey();
            int count = map.get(key);
            map.remove(key);
            for (int nextKey = key + 1; nextKey < key + W; nextKey++) {
                int nextCount = map.getOrDefault(nextKey, 0);
                if (nextCount < count) return false;

                if (nextCount == count) {
                    map.remove(nextKey);
                } else {
                    map.put(nextKey, nextCount - count);
                }
            }
        }
        return true;
    }

    // SortedMap
    // time complexity: O(N * log(N)), time complexity: O(N)
    // beats %(97 ms for 65 tests)
    public boolean isNStraightHand2(int[] hand, int W) {
        SortedMap<Integer, Integer> map = new TreeMap<>();
        for (int h : hand) {
            map.put(h, map.getOrDefault(h, 0) + 1);
        }
        while (!map.isEmpty()) {
            int first = map.firstKey();
            for (int card = first; card < first + W; card++) {
                if (!map.containsKey(card)) return false;

                int c = map.get(card);
                if (c == 1) {
                    map.remove(card);
                } else {
                    map.replace(card, c - 1);
                }
            }
        }
        return true;
    }

    // SortedMap + Queue
    // time complexity: O(N * log(N)), time complexity: O(N)
    // beats %(88 ms for 65 tests)
    public boolean isNStraightHand3(int[] hand, int W) {
        SortedMap<Integer, Integer> map = new TreeMap<>();
        for (int h : hand) {
            map.put(h, map.getOrDefault(h, 0) + 1);
        }
        Queue<Integer> starts = new LinkedList<>();
        int lastCard = -1;
        int opened = 0;
        for (int i : map.keySet()) {
            int count = map.get(i);
            if (opened > 0 && i > lastCard + 1 || opened > count) return false;

            starts.offer(count - opened);
            lastCard = i;
            opened = count;
            if (starts.size() == W) {
                opened -= starts.poll();
            }
        }
        return opened == 0;
    }

    void test(int[] hand, int W, boolean expected) {
        assertEquals(expected, isNStraightHand(hand, W));
        assertEquals(expected, isNStraightHand2(hand, W));
        assertEquals(expected, isNStraightHand3(hand, W));
    }

    @Test
    public void test() {
        test(new int[] { 1, 1, 2, 2, 3, 3 }, 2, false);
        test(new int[] { 1, 2, 3, 6, 2, 3, 4, 7, 8 }, 3, true);
        test(new int[] { 1, 2, 3, 4, 5 }, 4, false);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
