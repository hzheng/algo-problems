import java.awt.geom.Arc2D;
import java.util.*;
import java.util.Random;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Interval;

// LC380: https://leetcode.com/problems/insert-delete-getrandom-o1/
//
// Design a data structure that supports all following operations in average O(1) time.

// insert(val): Inserts an item val to the set if not already present.
// remove(val): Removes an item val from the set if present.
// getRandom: Returns a random element from current set of elements. Each
// element must have the same probability of being returned.
public class RandomizedSet {
    static interface IRandomizedSet {
        /** Inserts a value to the set. Returns true if the set did not already
           contain the specified element. */
        public boolean insert(int val);

        /** Removes a value from the set. Returns true if the set contained the
           specified element. */
        public boolean remove(int val);

        /** Get a random element from the set. */
        public int getRandom();
    }

    // 2 Hash Tables
    // beat 52.68%(137 ms for 18 tests)
    static class RandomizedSet1 implements IRandomizedSet {
        private Random rand = new Random();
        private Map<Integer, Integer> valMap = new HashMap<>();
        private Map<Integer, Integer> seqMap = new HashMap<>();

        public boolean insert(int val) {
            if (valMap.containsKey(val)) return false;

            int maxSeq = valMap.size() + 1;
            valMap.put(val, maxSeq);
            seqMap.put(maxSeq, val);
            return true;
        }

        public boolean remove(int val) {
            if (!valMap.containsKey(val)) return false;

            int seq = valMap.remove(val);
            int maxSeq = seqMap.size();
            if (seq < maxSeq) {
                int lastVal = seqMap.get(maxSeq);
                valMap.put(lastVal, seq);
                seqMap.put(seq, lastVal);
            }
            seqMap.remove(maxSeq);
            return true;
        }

        public int getRandom() {
            return seqMap.get(rand.nextInt(valMap.size()) + 1);
        }
    }

    // Solution of Choice
    // Hash Table + List
    // beat 57.10%(135 ms for 18 tests)
    static class RandomizedSet2 implements IRandomizedSet {
        private Random rand = new Random();

        private Map<Integer, Integer> map = new HashMap<>();
        private List<Integer> list = new ArrayList<>();

        public boolean insert(int val) {
            if (map.containsKey(val)) return false;

            map.put(val, list.size());
            list.add(val);
            return true;
        }

        public boolean remove(int val) {
            if (!map.containsKey(val)) return false;

            int seq = map.remove(val);
            int last = list.remove(list.size() - 1);
            if (val != last) {
                list.set(seq, last);
                map.put(last, seq);
            }
            return true;
        }

        public int getRandom() {
            return list.get(rand.nextInt(list.size()));
        }
    }

    void test(IRandomizedSet set, int[] insertions, int[] removals,
              int[] range) {
        for (int i : insertions) {
            assertEquals(true, set.insert(i));
        }
        for (int i = 0; i < 100; i++) {
            int random = set.getRandom();
            assertTrue(Arrays.stream(insertions).anyMatch(k -> k == random));
        }

        for (int i : removals) {
            assertEquals(true, set.remove(i));
        }
        for (int i = 0; i < 100; i++) {
            int random = set.getRandom();
            assertTrue(Arrays.stream(range).anyMatch(k -> k == random));
        }
    }

    void test(int[] insertions, int[] removals, int[] range) {
        test(new RandomizedSet1(), insertions, removals, range);
        test(new RandomizedSet2(), insertions, removals, range);
    }

    @Test
    public void test1() {
        test(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
             new int[] {2, 4, 6, 8, 10},
             new int[] {1, 3, 5, 7, 9});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RandomizedSet");
    }
}
