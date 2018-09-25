import java.util.*;

import java.lang.reflect.Constructor;

import org.junit.Test;
import static org.junit.Assert.*;

// LC911: https://leetcode.com/problems/online-election/
//
// In an election, the i-th vote was cast for persons[i] at time times[i].
// Implement the following query function: TopVotedCandidate.q(int t) will
// return the number of the person that was leading the election at time t.
// Votes cast at time t will count towards our query.  In the case of a tie, the
// most recent vote (among tied candidates) wins.
// Note:
// 1 <= persons.length = times.length <= 5000
// 0 <= persons[i] <= persons.length
// times is a strictly increasing array with all elements in [0, 10^9].
// TopVotedCandidate.q is called at most 10000 times per test case.
// TopVotedCandidate.q(int t) is always called with t >= times[0].
public class TopVotedCandidate {
    // SortedMap
    // beats %(232 ms for 97 tests)
    class TopVotedCandidate1 {
        private NavigableMap<Integer, Integer> map = new TreeMap<>();

        public TopVotedCandidate1(int[] persons, int[] times) {
            int n = persons.length;
            int[] count = new int[n];
            int max = 0;
            for (int i = 0; i < n; i++) {
                int p = persons[i];
                if (++count[p] >= count[max]) {
                    map.put(times[i], max = p);
                }
            }
        }

        public int q(int t) {
            return map.floorEntry(t).getValue();
        }
    }

    // Binary Search + Hash Table
    // beats %(227 ms for 97 tests)
    class TopVotedCandidate2 {
        private List<int[]> leaders = new ArrayList<>();

        public TopVotedCandidate2(int[] persons, int[] times) {
            Map<Integer, Integer> count = new HashMap<>();
            for (int i = 0, leader = -1, max = 0; i < persons.length; i++) {
                int person = persons[i];
                int time = times[i];
                int c = count.getOrDefault(person, 0) + 1;
                count.put(person, c);
                if (c < max) continue;

                if (person != leader) {
                    leader = person;
                    leaders.add(new int[]{leader, time});
                }
                max = c;
            }
        }
    
        public int q(int t) {
            int low = 0;
            for (int high = leaders.size(); low < high; ) {
                int mid = (low + high) >>> 1;
                if (leaders.get(mid)[1] <= t) {
                    low = mid + 1;
                } else {
                    high = mid;
                }
            }
            return leaders.get(low - 1)[0];
        }
    }

    // Binary Search + Hash Table
    // beats %(253 ms for 97 tests)
    class TopVotedCandidate3 {
        private Map<Integer, Integer> leaders = new HashMap<>();
        private int[] times;

        public TopVotedCandidate3(int[] persons, int[] times) {
            this.times = times;
            Map<Integer, Integer> count = new HashMap<>();
            count.put(-1, 0);
            for (int i = 0, leader = -1; i < persons.length; i++) {
                int person = persons[i];
                int c = count.getOrDefault(person, 0) + 1;
                count.put(person, c);
                if (c >= count.get(leader)) {
                    leader = person;
                }
                leaders.put(times[i], leader);
            }
        }
    
        public int q(int t) {
            int i = Arrays.binarySearch(times, t);
            return leaders.get(times[i >= 0 ? i : -i - 2]);
        }
    }

    void test1(String className) throws Exception {
        Object outerObj =
            new Object() {}.getClass().getEnclosingClass().newInstance();
        test(new String[] { className, "q", "q", "q", "q", "q", "q" },
             new Object[][] {
                 new Object[] { outerObj, new int[] { 0, 1, 1, 0, 0, 1, 0 },
                 new int[] { 0, 5, 10, 15, 20, 25, 30 } },
                 { 3 }, { 12 }, { 25 }, { 15 }, { 24 }, { 8 } },
            new Integer[] { null, 0, 1, 1, 0, 0, 1 });
        test(new String[] { className, "q", "q", "q", "q", "q", "q", "q", "q",
                            "q", "q" },
             new Object[][] {
                 new Object[] { outerObj, new int[] { 0, 0, 0, 0, 1 },
                 new int[] { 0, 6, 39, 52, 75 } },
                 { 45 }, { 49 }, { 59 }, { 68 }, { 42 }, { 37 }, { 99 },
                 { 26 }, { 78 }, { 43 } },
            new Integer[] { null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
    }

    void test(String[] methods, Object[][] args,
              Integer[] expected) throws Exception {
        final String name =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        final Object[] VOID = new Object[] {};
        Class<?> clazz = Class.forName(name + "$" + methods[0]);
        Constructor<?> ctor = clazz.getConstructors()[0];
        Object obj = ctor.newInstance(args[0]);
        for (int i = 1; i < methods.length; i++) {
            Object[] arg = args[i];
            Object res = null;
            if (arg.length == 0) {
                res = clazz.getMethod(methods[i]).invoke(obj);
            } else if (arg.length == 1) {
                res = clazz.getMethod(methods[i], int.class).invoke(obj, arg);
            } else if (arg.length == 2) {
                res = clazz.getMethod(methods[i], int.class, int.class).invoke(
                    obj, arg);
            }
            if (expected[i] != null) {
                assertEquals(expected[i], res);
            }
        }
    }

    @Test
    public void test1() {
        try {
            test1("TopVotedCandidate1");
            test1("TopVotedCandidate2");
            test1("TopVotedCandidate3");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
