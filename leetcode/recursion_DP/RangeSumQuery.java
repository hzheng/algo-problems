import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/range-sum-query-immutable/
public class RangeSumQuery {
    interface INumArray {
        int sumRange(int i, int j);
    }

    // beats 18.28%(4 ms)
    class NumArray implements INumArray {
        private int[] sums;

        public NumArray(int[] nums) {
            int n = nums.length;
            sums = new int[n + 1];
            for (int i = 0; i < n; i++) {
                sums[i + 1] = sums[i] + nums[i];
            }
        }

        public int sumRange(int i, int j) {
            return sums[j + 1] - sums[i];
        }
    }

    static class Pair {
        int i, j;

        Pair(int i, int j) {
            this.i = i;
            this.j = j;
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof Pair) {
                Pair o = (Pair)other;
                return o.i == i && o.j == j;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(i, j);
        }

        public static Pair create(int i, int j) {
            return new Pair(i, j);
        }
    }

    // from leetcode
    //  Time Limit Exceeded
    class NumArray2 implements INumArray {
        private Map<Pair, Integer> map = new HashMap<>();

        public NumArray2(int[] nums) {
            for (int i = 0; i < nums.length; i++) {
                int sum = 0;
                for (int j = i; j < nums.length; j++) {
                    sum += nums[j];
                    map.put(Pair.create(i, j), sum);
                }
            }
        }

        public int sumRange(int i, int j) {
            return map.get(Pair.create(i, j));
        }
    }

    void test(INumArray obj, int[] ... queries) {
        for (int[] query : queries) {
            assertEquals(query[2], obj.sumRange(query[0], query[1]));
        }
    }

    void test(int[] nums, int[] ... queries) {
        test(new NumArray(nums), queries);
        test(new NumArray2(nums), queries);
    }

    @Test
    public void test1() {
        test(new int[] {-2, 0, 3, -5, 2, -1},
             new int[] {0, 2, 1}, new int[] {2, 5, -1}, new int[] {0, 5, -3});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RangeSumQuery");
    }
}
