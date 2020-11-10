import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.collection.IsIn.*;

// LC870: https://leetcode.com/problems/advantage-shuffle/
//
// Given two arrays A and B of equal size, the advantage of A with respect to B
// is the number of indices i for which A[i] > B[i].
// Return any permutation of A that maximizes its advantage with respect to B.
public class AdvantageShuffle {
    // Heap + Set + Hash Table
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 27.54%(108 ms for 67 tests)
    public int[] advantageCount(int[] A, int[] B) {
        Comparator<int[]> cmp = new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return a[0] - b[0];
            }
        };
        PriorityQueue<int[]> pqA = new PriorityQueue<>(cmp);
        PriorityQueue<int[]> pqB = new PriorityQueue<>(cmp);
        int i = 0;
        for (int a : A) {
            pqA.offer(new int[] { a, i++ });
        }
        i = 0;
        for (int b : B) {
            pqB.offer(new int[] { b, i++ });
        }
        Set<Integer> unmappedA = new HashSet<>();
        int n = A.length;
        for (i = 0; i < n; i++) {
            unmappedA.add(i);
        }
        Set<Integer> unmappedB = new HashSet<>();
        for (i = 0; i < n; i++) {
            unmappedB.add(i);
        }
        Map<Integer, Integer> map = new HashMap<>();
        outer : while (!pqB.isEmpty()) {
            int[] b = pqB.poll();
            while (true) {
                if (pqA.isEmpty()) break outer;

                int[] a = pqA.poll();
                if (a[0] > b[0]) {
                    map.put(b[1], a[1]);
                    unmappedA.remove(a[1]);
                    unmappedB.remove(b[1]);
                    break;
                }
            }
        }
        Iterator<Integer> itr = unmappedA.iterator();
        for (int k : unmappedB) {
            map.put(k, itr.next());
        }
        int[] res = new int[n];
        for (i = 0; i < n; i++) {
            res[i] = A[map.get(i)];
        }
        return res;
    }

    // Sort + Hash Table + Deque
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 82.26%(57 ms for 67 tests)
    public int[] advantageCount2(int[] A, int[] B) {
        int[] sortedA = A.clone();
        Arrays.sort(sortedA);
        int[] sortedB = B.clone();
        Arrays.sort(sortedB);

        // assigned[b] = list of a that are assigned to beat b
        Map<Integer, Deque<Integer> > assigned = new HashMap<>();
        for (int b : B) {
            assigned.put(b, new LinkedList<>());
        }
        Deque<Integer> remaining = new LinkedList<>();
        int j = 0;
        for (int a : sortedA) {
            if (a > sortedB[j]) {
                assigned.get(sortedB[j++]).add(a);
            } else {
                remaining.add(a);
            }
        }
        int[] res = new int[B.length];
        for (int i = 0; i < B.length; i++) {
            if (assigned.get(B[i]).size() > 0) {
                res[i] = assigned.get(B[i]).pop();
            } else {
                res[i] = remaining.pop();
            }
        }
        return res;
    }

    // SortedMap
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 27.54%(108 ms for 67 tests)
    public int[] advantageCount3(int[] A, int[] B) {
        NavigableMap<Integer, Integer> counts = new TreeMap<>();
        for (int a : A) {
            counts.put(a, counts.getOrDefault(a, 0) + 1);
        }
        int n = A.length;
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            Integer x = counts.higherKey(B[i]);
            if (x == null) {
                x = counts.firstKey();
            }
            counts.put(x, counts.get(x) - 1);
            if (counts.get(x) == 0) {
                counts.remove(x);
            }
            res[i] = x;
        }
        return res;
    }

    // Sort + Heap
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 82.75%(56 ms for 67 tests)
    public int[] advantageCount4(int[] A, int[] B) {
        Arrays.sort(A);
        int n = A.length;
        int[] res = new int[n];
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return b[0] - a[0];
            }
        });
        for (int i = 0; i < n; i++) {
            pq.offer(new int[] { B[i], i });
        }
        for (int low = 0, high = n - 1; !pq.isEmpty(); ) {
            int[] cur = pq.poll();
            int idx = cur[1];
            if (A[high] > cur[0]) {
                res[idx] = A[high--];
            } else {
                res[idx] = A[low++];
            }
        }
        return res;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        C apply(A a, B b);
    }

    void test(Function<int[], int[], int[]> f, int[] A, int[] B, Integer[]... expected) {
        Object[] expectedList = Stream.of(expected).map(Arrays::asList).toArray();
        int[] result = f.apply(A, B);
        List<Integer> resultList = Arrays.stream(result).boxed().collect(Collectors.toList());
        assertThat(resultList, in(expectedList));
    }

    void test(int[] A, int[] B, Integer[] ... expected) {
        AdvantageShuffle a = new AdvantageShuffle();
        test(a::advantageCount, A, B, expected);
        test(a::advantageCount2, A, B, expected);
        test(a::advantageCount3, A, B, expected);
    }

    @Test
    public void test() {
        test(new int[] { 12, 24, 8, 32 }, new int[] { 13, 25, 32, 11 },
             new Integer[] { 24, 32, 8, 12 });
        test(new int[] { 2, 7, 11, 15 }, new int[] { 1, 10, 4, 11 },
             new Integer[] { 2, 11, 7, 15 });
        test(new int[] { 2, 0, 4, 1, 2 }, new int[] { 1, 3, 0, 0, 2 },
             new Integer[] { 2, 0, 1, 2, 4 },
             new Integer[] { 2, 0, 2, 1, 4 }, new Integer[] { 2, 4, 1, 2, 0 });
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
