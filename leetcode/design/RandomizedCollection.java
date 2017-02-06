import java.awt.geom.Arc2D;
import java.util.*;
import java.util.Random;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Interval;

// LC381: https://leetcode.com/problems/insert-delete-getrandom-o1-duplicates-allowed/
//
// Design a data structure that supports all following operations in average O(1) time.

// Note: Duplicate elements are allowed.
// insert(val): Inserts an item val to the collection.
// remove(val): Removes an item val from the collection if present.
// getRandom: Returns a random element from current collection of elements. The
// probability of each element being returned is linearly related to the number
// of same value the collection contains.
public class RandomizedCollection {
    static interface IRandomizedCollection {
        /** Inserts a value to the collection. Returns true if the collection
         did not already contain the specified element. */
        public boolean insert(int val);

        /** Removes a value from the collection. Returns true if the collection
        contained the specified element. */
        public boolean remove(int val);

        /** Get a random element from the collection. */
        public int getRandom();
    }

    // Solution of Choice
    // Hash Table + List
    // beat 95.43%(127 ms for 28 tests)
    static class RandomizedCollection1 implements IRandomizedCollection {
        private Random rand = new Random();
        private Map<Integer, Set<Integer>> map = new HashMap<>();
        private List<Integer> list = new ArrayList<>();

        public boolean insert(int val) {
            boolean res = !map.containsKey(val);
            if (res) {
                map.put(val, new HashSet<>());
                // map.put(val, new LinkedHashSet<>()); // maybe better
            }
            map.get(val).add(list.size());
            list.add(val);
            return res;
        }

        public boolean remove(int val) {
            if (!map.containsKey(val)) return false;

            Set<Integer> set = map.get(val);
            Iterator<Integer> indices = set.iterator();
            int seq = indices.next();
            indices.remove();
            if (set.isEmpty()) {
                map.remove(val);
            }
            int lastIndex = list.size() - 1;
            int last = list.remove(lastIndex);
            if (seq != lastIndex) {
                list.set(seq, last);
                set = map.get(last);
                set.remove(lastIndex);
                set.add(seq);
            }
            return true;
        }

        public int getRandom() {
            return list.get(rand.nextInt(list.size()));
        }
    }

    void test(IRandomizedCollection set, int[] insertions, int[] removals,
              int[] range) {
        for (int i : insertions) {
            set.insert(i);
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
        test(new RandomizedCollection1(), insertions, removals, range);
    }

    @Test
    public void test1() {
        test(new int[] {4, 3, 4, 2, 4}, new int[] {4, 3, 4, 4}, new int[] {2});
        test(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
             new int[] {2, 4, 6, 8, 10},
             new int[] {1, 3, 5, 7, 9});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RandomizedCollection");
    }
}
