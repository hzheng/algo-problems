import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/top-k-frequent-elements/
//
// Given a non-empty array of integers, return the k most frequent elements.
// Note:
// You may assume k is always valid, 1 ≤ k ≤ number of unique elements.
// Time complexity must be better than O(n log n), where n is the array's size.
public class TopKFrequent {
    // Hash table + Counting Sort
    // time complexity: O(N), space complexity: O(N)
    // beats 83.91%(29 ms)
    public List<Integer> topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int num : nums) {
            countMap.put(num, countMap.getOrDefault(num, 0) + 1);
        }

        int n = nums.length;
        int[] counts = new int[n + 1];
        for (int count : countMap.values()) {
            counts[count]++;
        }
        int base = n;
        for (int mostFreqs = 0; base >= 0; base--) {
            mostFreqs += counts[base];
            if (mostFreqs >= k) {
                // the following is needed when there is tie in count
                // counts[base] -= (mostFreqs - k);
                break;
            }
        }

        List<Integer> res = new ArrayList<>();
        for (int i : countMap.keySet()) {
            int count = countMap.get(i);
            if (count >= base) {
                // the following is needed when there is tie in count
                // if (count > base || (count == base && counts[base]-- > 0)) {
                res.add(i);
            }
        }
        return res;
    }

    // Hash table + Heap
    // time complexity: O(N * log(K)), space complexity: O(N)
    // beats 68.05%(34 ms)
    public List<Integer> topKFrequent2(int[] nums, int k) {
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int num : nums) {
            countMap.put(num, countMap.getOrDefault(num, 0) + 1);
        }

        // slow, have to abandon
        // Queue<int[]> heap = new PriorityQueue<int[]>((a, b) -> a[1] - b[1]);
        Queue<int[]> heap = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return a[1] - b[1];
            }
        });

        for (Map.Entry<Integer, Integer> count : countMap.entrySet()) {
            heap.offer(new int[] {count.getKey(), count.getValue()});
            if (heap.size() > k) {
                heap.poll();
            }
        }

        List<Integer> res = new ArrayList<>();
        while (heap.size() > 0) {
            res.add(heap.poll()[0]);
        }
        return res;
    }

    // Hash table + Heap
    // time complexity: O(N * log(K)), space complexity: O(N)
    // beats 65.15%(35 ms)
    public List<Integer> topKFrequent3(int[] nums, int k) {
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int num : nums) {
            countMap.put(num, countMap.getOrDefault(num, 0) + 1);
        }
        Queue<Map.Entry<Integer, Integer> > heap =
            new PriorityQueue<>(new Comparator<Map.Entry<Integer, Integer>>() {
            public int compare(Map.Entry<Integer, Integer> a, Map.Entry<Integer, Integer> b) {
                return a.getValue() - b.getValue();
            }
        });
        // heap.addAll(countMap.entrySet());
        for (Map.Entry<Integer, Integer> count : countMap.entrySet()) {
            heap.offer(count);
            if (heap.size() > k) {
                heap.poll();
            }
        }

        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            res.add(heap.poll().getKey());
        }
        return res;
    }

    // Hash table + Bucket Sort
    // time complexity: O(N), space complexity: O(N)
    // https://discuss.leetcode.com/topic/44237/java-o-n-solution-bucket-sort/2
    // beats 83.91%(29 ms)
    public List<Integer> topKFrequent4(int[] nums, int k) {
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int num : nums) {
            countMap.put(num, countMap.getOrDefault(num, 0) + 1);
        }

        int n = nums.length;
        @SuppressWarnings("unchecked")
        List<Integer>[] bucket = new List[n + 1];
        for (int key : countMap.keySet()) {
            int frequency = countMap.get(key);
            if (bucket[frequency] == null) {
                bucket[frequency] = new ArrayList<>();
            }
            bucket[frequency].add(key);
        }

        List<Integer> res = new ArrayList<>();
        for (int i = n; i >= 0 && res.size() < k; i--) {
            if (bucket[i] != null) {
                res.addAll(bucket[i]);
            }
        }
        return res;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<int[], Integer, List<Integer> > topKFrequent,
              int k, int[] nums, Integer ... expected) {
        List<Integer> res = topKFrequent.apply(nums, k);
        Collections.sort(res);
        assertArrayEquals(expected, res.toArray(new Integer[0]));
    }

    void test(int k, int[] nums, Integer ... expected) {
        TopKFrequent t = new TopKFrequent();
        test(t::topKFrequent, k, nums, expected);
        test(t::topKFrequent2, k, nums, expected);
        test(t::topKFrequent3, k, nums, expected);
        test(t::topKFrequent4, k, nums, expected);
    }

    @Test
    public void test1() {
        test(3, new int[] {1, 1, 1, 2, 2, 3, 3, 4, 5, 5, 4, 5, 1, 3}, 1, 3, 5);
        test(2, new int[] {1, 1, 1, 2, 2, 3}, 1, 2);
        test(2, new int[] {1, 1, 1, 2, 2, 3, 3, 4, 5, 4, 1, 3}, 1, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TopKFrequent");
    }
}
