import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Return all possible combinations of k numbers out of 1 ... n.
public class Combination {
    // beats 30.91%
    public List<List<Integer> > combine(int n, int k) {
        List<List<Integer> > res = new ArrayList<>();
        combine(n, k, res, new ArrayList<Integer>());
        return res;
    }

    private void combine(int n, int k, List<List<Integer> > res,
                         List<Integer> cur) {
        int count = cur.size();
        if (count == k) {
            res.add(new ArrayList<Integer>(cur));
            return;
        }

        int next = (count > 0) ? cur.get(count - 1) + 1 : 1;
        for (int i = next; i <= n; i++) {
            cur.add(i);
            combine(n, k, res, cur);
            cur.remove(count);
        }
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<Integer, Integer, List<List<Integer>>> combine,
     String name, int n, int k, Integer[][] expected) {
        List<List<Integer>> res = combine.apply(n, k);
        Integer[][] resArray = res.stream().map(
            a -> a.toArray(new Integer[0])).toArray(Integer[][]::new);
        sort(resArray);
        assertArrayEquals(expected, resArray);
    }

    Integer[][] sort(Integer[][] lists) {
        Arrays.sort(lists, (a, b) -> {
            // Arrays.sort(a);
            // Arrays.sort(b);
            int len = Math.min(a.length, b.length);
            int i = 0;
            for (; i < len && (a[i] == b[i]); i++);
            if (i == len) return a.length - b.length;
            return a[i] - b[i];
        });
        return lists;
    }

    void test(int n, int k, Integer[][] expected) {
        Combination c = new Combination();
        test(c::combine, "combine", n, k, expected);
    }

    @Test
    public void test1() {
        test(4, 3, new Integer[][] {
            {1, 2, 3}, {1, 2, 4}, {1, 3, 4}, {2, 3, 4}
        });
        test(5, 3, new Integer[][] {
            {1, 2, 3}, {1, 2, 4}, {1, 2, 5}, {1, 3, 4}, {1, 3, 5},
            {1, 4, 5}, {2, 3, 4}, {2, 3, 5}, {2, 4, 5}, {3, 4, 5}
        });
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Combination");
    }
}
