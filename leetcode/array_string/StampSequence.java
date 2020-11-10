import java.util.*;

import java.util.stream.*;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.collection.IsIn.*;

// LC936: https://leetcode.com/problems/stamping-the-sequence/
//
// You want to form a target string of lowercase letters. At the beginning, your
// sequence is target.length '?' marks.  You also have a stamp of lowercase 
// letters. On each turn, you may place the stamp over the sequence, and replace
// every letter in the sequence with the corresponding letter from the stamp.
// You can make up to 10 * target.length turns.
// If the sequence is possible to stamp, then return an array of the index of
// the left-most letter being stamped at each turn.  If the sequence is not
// possible to stamp, return an empty array.
// Also, if the sequence is possible to stamp, it is guaranteed it is possible
// to stamp within 10 * target.length moves.  Any answers specifying more than 
// this number of moves will not be accepted.
// Note:
// 1 <= stamp.length <= target.length <= 1000
// stamp and target only contain lowercase letters.
public class StampSequence {
    // Stack
    // beats %(31 ms for 262 tests)
    public int[] movesToStamp(String stamp, String target) {
        Stack<Integer> stack = new Stack<>();
        char[] s = stamp.toCharArray();
        char[] t = target.toCharArray();
        char[] allStar = new char[t.length];
        Arrays.fill(allStar, '*');
        while (!Arrays.equals(t, allStar)) {
            int index = doStamp(s, t);
            if (index < 0) return new int[0];

            stack.push(index);
        }
        int[] res = new int[stack.size()];
        for (int i = 0; !stack.isEmpty(); i++) {
            res[i] = stack.pop();
        }
        return res;
    }

    private int doStamp(char[] stamp, char[] target) {
        outer: for (int i = 0, n = stamp.length, m = target.length; i <= m - n; i++) {
            boolean stamped = false;
            for (int j = 0, k = i; j < n; j++, k++) {
                if (target[k] != '*' && target[k] != stamp[j]) continue outer;

                stamped |= (target[k] == stamp[j]);
            }
            if (!stamped) continue;

            for (int j = 0; j < n; j++) {
                target[i + j] = '*';
            }
            return i;
        }
        return -1;
    }

    // Stack + Set + Queue
    // beats %(61 ms for 262 tests)
    public int[] movesToStamp2(String stamp, String target) {
        int n = stamp.length();
        int m = target.length();
        boolean[] done = new boolean[m];
        List<Window> windows = new ArrayList<>();
        Queue<Integer> stamped = new ArrayDeque<>();
        Stack<Integer> res = new Stack<>();
        for (int i = 0; i <= m - n; i++) {
            Set<Integer> made = new HashSet<>();
            Set<Integer> todo = new HashSet<>();
            for (int j = 0; j < n; j++) {
                if (target.charAt(i + j) == stamp.charAt(j)) {
                    made.add(i + j);
                } else {
                    todo.add(i + j);
                }
            }
            windows.add(new Window(made, todo));
            if (!todo.isEmpty()) continue;

            res.push(i);
            for (int j = i; j < i + n; ++j) {
                if (!done[j]) {
                    stamped.offer(j);
                    done[j] = true;
                }
            }
        }
        while (!stamped.isEmpty()) {
            int i = stamped.poll();
            // check each window that is potentially affected(i.e. intersected)
            for (int j = Math.max(0, i - n + 1); j <= Math.min(m - n, i); j++) {
                Set<Integer> todo = windows.get(j).todo;
                if (!todo.remove(i) || !todo.isEmpty()) continue;

                res.push(j);
                for (int k : windows.get(j).made) {
                    if (!done[k]) {
                        stamped.offer(k);
                        done[k] = true;
                    }
                }
            }
        }
        for (boolean b : done) {
            if (!b) return new int[0];
        }
        int[] resArray = new int[res.size()];
        for (int i = 0; !res.isEmpty(); i++) {
            resArray[i] = res.pop();
        }
        return resArray;
    }

    private static class Window {
        Set<Integer> made, todo;

        Window(Set<Integer> m, Set<Integer> t) {
            made = m;
            todo = t;
        }
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        C apply(A a, B b);
    }

    void test(Function<String, String, int[]> f, String stamp, String target, Integer[]... expected) {
        Object[] expectedList = Stream.of(expected).map(Arrays::asList).toArray();
        int[] result = f.apply(stamp, target);
        List<Integer> resultList = Arrays.stream(result).boxed().collect(Collectors.toList());
        assertThat(resultList, in(expectedList));
    }

    void test(String stamp, String target, Integer[]... expected) {
        StampSequence s = new StampSequence();
        test(s::movesToStamp, stamp, target, expected);
        test(s::movesToStamp2, stamp, target, expected);
    }

    @Test
    public void test() {
        test("abc", "ababc", new Integer[] {0, 2}, new Integer[] {1, 0, 2});
        test("abca", "aabcaca", new Integer[] {3, 0, 1}, new Integer[] {2, 3, 0, 1});
        test("abcac", "abcacbcaccaabcac", new Integer[] {7, 11, 5, 4, 0},
             new Integer[] {9, 8, 6, 7, 5, 3, 2, 1, 10, 4, 11, 0});
        test("bedaefaeddccbce", "bebedabebbedaefaeddccbced", new Integer[0]);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
