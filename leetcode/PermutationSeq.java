import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Given n and k, return the kth permutation sequence.
public class PermutationSeq {
    // Time Limit Exceeded
    public String getPermutation(int n, int k) {
        int[] nums = new int[n];
        for (int i = 1; i <= n; i++) {
            nums[i - 1] = i;
        }
        for (int i = 1; i < k; i++) {
            nextPermutation(nums);
        }
        return toString(nums);
    }

    private String toString(int[] nums) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nums.length; i++) {
            sb.append(nums[i]);
        }
        return sb.toString();
    }

    private void nextPermutation(int[] nums) {
        int len = nums.length;
        int i = len - 1;
        for (; i > 0 && nums[i - 1] >= nums[i]; i--);

        if (i > 0) {
            int j = len - 1;
            for (; j >= 0 && nums[j] <= nums[i - 1]; j--);
            swap(nums, i - 1, j);
        }
        // reverse from i to the end
        for (int j = len - 1; i < j; i++, j--) {
            swap(nums, i, j);
        }
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    // beats 5.39%
    public String getPermutation2(int n, int k) {
        if (n == 0) return "";

        int[] factorials = new int[n];
        factorials[0] = 1;

        int[] perms = new int[n];
        List<Integer> nums = new ArrayList<>(n);
        for (int i = n; i > 0; i--) {
            nums.add(i);
        }

        int index = 0;
        for (; k > 0; n--) {
            int order = 1;
            while (order <= n && k >= getFactorial(factorials, order)) {
                order++;
            }
            order--; // order! <= k < (order+1)!
            if (order == n) break;

            for (int i = n - order - 1; i > 0; i--) {
                perms[index++] = nums.remove(nums.size() - 1);
                n--;
            }
            int factorial = getFactorial(factorials, order);
            int nextIndex = nums.size() - k / factorial - 1;
            k %= factorial;
            if (k == 0) {
                nextIndex++;
            }
            perms[index++] = nums.remove(nextIndex);
        }
        for (int i = 0; i < nums.size(); i++) {
            perms[index++] = nums.get(i);
        }

        return toString(perms);
    }

    private int getFactorial(int[] factorials, int n) {
        if (n == 0) return 1;

        int factorial = factorials[n - 1];
        if (factorial > 0) return factorial;

        factorial = n * getFactorial(factorials, n - 1);
        return factorials[n - 1] = factorial;
    }

    // beats 77.48%
    public String getPermutation3(int n, int k) {
        StringBuilder availables = new StringBuilder("123456789");
        availables.setLength(n);
        StringBuilder sb = new StringBuilder();
        int total = 1;
        for (int i = 1; i < n; i++) {
            total *= i;
        }

        for (int i = 0, j = k - 1; i + 1 < n; i++) {
            int index = j / total;
            sb.append(availables.charAt(index));
            availables.deleteCharAt(index);
            j %= total;
            total /= (n - 1 - i);
        }
        return sb.append(availables.charAt(0)).toString();
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<Integer, Integer, String> perm, String name,
              int n, int k, String expected) {
        long t1 = System.nanoTime();
        assertEquals(expected, perm.apply(n, k));
        System.out.format("%s %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
    }

    void test(int n, int k, String expected) {
        PermutationSeq p = new PermutationSeq();
        test(p::getPermutation, "getPermutation", n, k, expected);
        test(p::getPermutation2, "getPermutation2", n, k, expected);
        test(p::getPermutation3, "getPermutation3", n, k, expected);
    }

    @Test
    public void test1() {
        test(2, 2, "21");
        test(1, 1, "1");
        test(1, 1, "1");
        test(2, 1, "12");
        test(3, 2, "132");
        test(3, 6, "321");
        test(3, 4, "231");
        test(4, 4, "1342");
        test(8, 590, "12785364");
        test(8, 1590, "14352876");
        test(8, 8590, "26847351");
    }

    void test(int n, int k) {
        assertEquals(getPermutation(n, k), getPermutation2(n, k));
    }

    @Test
    public void test2() {
        int n = 6;
        for (int k = 1; k <= 720; k++) {
            test(n, k);
        }
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PermutationSeq");
    }
}
