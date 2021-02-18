import java.util.*;

import java.lang.reflect.Constructor;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1157: https://leetcode.com/problems/online-majority-element-in-subarray/
//
// Implementing the class MajorityChecker, which has the following API:
// MajorityChecker(int[] arr) constructs an instance of MajorityChecker with the given array arr;
// int query(int left, int right, int threshold) has arguments such that:
// 0 <= left <= right < arr.length representing a subarray of arr;
// 2 * threshold > right - left + 1, ie. the threshold is always a strict majority of the length of
// the subarray
// Each query(...) returns the element in arr[left], arr[left+1], ..., arr[right] that occurs at
// least threshold times, or -1 if no such element exists.
//
// Constraints:
//
// 1 <= arr.length <= 20000
// 1 <= arr[i] <= 20000
// For each query, 0 <= left <= right < len(arr)
// For each query, 2 * threshold > right - left + 1
// The number of queries is at most 10000
public class MajorityChecker {
    // Segment Tree + Hash Table
    // 2021 ms(31.48%), 102.9 MB(11.11%) for 27 tests
    static class MajorityChecker1 {
        private SegmentTreeNode root;

        public MajorityChecker1(int[] arr) {
            int n = arr.length;
            root = SegmentTreeNode.build(0, n);
            for (int i = 0; i < n; i++) {
                root.update(i, arr[i]);
            }
        }

        public int query(int left, int right, int threshold) {
            Map<Integer, Integer> count = root.query(left, right);
            for (int key : count.keySet()) {
                if (count.get(key) >= threshold) { return key; }
            }
            return -1;
        }

        private static class SegmentTreeNode {
            private final int min;
            private final int max;
            private final Map<Integer, Integer> countMap = new HashMap<>();
            private SegmentTreeNode left, right;

            public SegmentTreeNode(int start, int end) {
                min = start;
                max = end;
            }

            public static SegmentTreeNode build(int start, int end) {
                SegmentTreeNode root = new SegmentTreeNode(start, end);
                if (start == end) { return root; }

                int mid = start + (end - start) / 2;
                root.left = build(start, mid);
                root.right = build(mid + 1, end);
                return root;
            }

            public void update(int index, int val) {
                if (min > index || max < index) { return; }

                if (min == index && max == index) {
                    countMap.put(val, getCount(val) + 1);
                    return;
                }
                int mid = min + (max - min) / 2;
                if (index <= mid) {
                    left.update(index, val);
                } else {
                    right.update(index, val);
                }
                countMap.put(val, left.getCount(val) + right.getCount(val));
            }

            private int getCount(int val) {
                return countMap.getOrDefault(val, 0);
            }

            public Map<Integer, Integer> query(int start, int end) {
                if (min == start && max == end) { return countMap; }

                int mid = min + (max - min) / 2;
                if (end <= mid) { return left.query(start, end); }
                if (start > mid) { return right.query(start, end); }

                Map<Integer, Integer> res = new HashMap<>(left.query(start, mid));
                right.query(mid + 1, end).forEach((key, val) -> res.merge(key, val, Integer::sum));
                return res;
            }
        }
    }

    private static int majority(int[] nums, int start, int end) {
        int major = -1;
        int count = 0;
        for (int i = start; i <= end; i++) {
            int num = nums[i];
            if (count == 0 || major == num) {
                major = num;
                count++;
            } else {
                count--;
            }
        }
        return major;
    }

    // Segment Tree + Hash Table
    // 710 ms(44.45%), 53.8 MB(25.00%) for 27 tests
    static class MajorityChecker2 {
        private int[] data;
        private Map<Integer, List<Integer>> map = new HashMap<>();
        private SegmentTreeNode root;

        public MajorityChecker2(int[] arr) {
            this.data = arr;
            int n = arr.length;
            for (int i = 0; i < n; i++) {
                map.computeIfAbsent(arr[i], x -> new ArrayList<>()).add(i);
            }
            root = SegmentTreeNode.build(0, n);
        }

        public int query(int left, int right, int threshold) {
            int[] res = root.query(left, right, data, map);
            return (res[0] < 0 || res[1] < threshold) ? -1 : res[0];
        }

        private static class SegmentTreeNode {
            private static final int[] NONE = new int[] {-1, 0};

            private final int min;
            private final int max;
            private SegmentTreeNode left, right;

            public SegmentTreeNode(int start, int end) {
                min = start;
                max = end;
            }

            public static SegmentTreeNode build(int start, int end) {
                SegmentTreeNode root = new SegmentTreeNode(start, end);
                if (start == end) { return root; }

                int mid = start + (end - start) / 2;
                root.left = build(start, mid);
                root.right = build(mid + 1, end);
                return root;
            }

            public int[] query(int start, int end, int[] data, Map<Integer, List<Integer>> map) {
                if (min == start && max == end) {
                    int candidate = majority(data, start, end);
                    return query(start, end, start, end, new int[] {candidate, 0}, map);
                }

                int mid = min + (max - min) / 2;
                if (end <= mid) { return left.query(start, end, data, map); }
                if (start > mid) { return right.query(start, end, data, map); }

                int[] leftRes = left.query(start, mid, data, map);
                int[] res = query(start, end, mid + 1, end, leftRes, map);
                if (res != NONE) { return res; }

                int[] rightRes = right.query(mid + 1, end, data, map);
                return query(start, end, start, mid, rightRes, map);
            }

            private int[] query(int start, int end, int from, int to, int[] candidate,
                                Map<Integer, List<Integer>> map) {
                if (candidate[0] >= 0) {
                    List<Integer> indices = map.get(candidate[0]);
                    int total = count(indices, from, to) + candidate[1];
                    if (total * 2 > end - start + 1) { return new int[] {candidate[0], total}; }
                }
                return NONE;
            }

            private int count(List<Integer> indices, int start, int end) {
                int low = Collections.binarySearch(indices, start);
                if (low < 0) {
                    low = -low - 1;
                }
                int high = Collections.binarySearch(indices, end);
                if (high < 0) {
                    high = (-high - 1) - 1;
                }
                return high - low + 1;
            }
        }
    }

    // Segment Tree + Hash Table
    // 2609 ms(5.56%), 48.7 MB(87.96%) for 27 tests
    static class MajorityChecker3 {
        private int[] data;
        private Map<Integer, List<Integer>> map = new HashMap<>();
        private SegmentTreeNode root;

        public MajorityChecker3(int[] arr) {
            this.data = arr;
            int n = arr.length;
            for (int i = 0; i < n; i++) {
                map.computeIfAbsent(arr[i], x -> new ArrayList<>()).add(i);
            }
            root = SegmentTreeNode.build(0, n);
        }

        public int query(int left, int right, int threshold) {
            int[] res = root.query(left, right, data, map);
            return (res[0] < 0 || res[1] < threshold) ? -1 : res[0];
        }

        private static class SegmentTreeNode {
            private static final int[] NONE = new int[] {-1, 0};

            private final int min;
            private final int max;
            private SegmentTreeNode left, right;

            public SegmentTreeNode(int start, int end) {
                min = start;
                max = end;
            }

            public static SegmentTreeNode build(int start, int end) {
                SegmentTreeNode root = new SegmentTreeNode(start, end);
                if (start == end) { return root; }

                int mid = start + (end - start) / 2;
                root.left = build(start, mid);
                root.right = build(mid + 1, end);
                return root;
            }

            public int[] query(int start, int end, int[] data, Map<Integer, List<Integer>> map) {
                if (start > max || end < min) { return NONE; }

                if (start == end) { return new int[] {data[start], 1}; }
                if (start + 1 == end) { // will be TLE without this optimization
                    return data[start] == data[end] ? new int[] {data[start], 2} : NONE;
                }

                int mid = min + (max - min) / 2;
                if (end <= mid) { return left.query(start, end, data, map); }
                if (start > mid) { return right.query(start, end, data, map); }

                int[] leftRes = left.query(start, mid, data, map);
                int[] res = query(start, end, mid + 1, end, leftRes, map);
                if (res != NONE) { return res; }

                int[] rightRes = right.query(mid + 1, end, data, map);
                return query(start, end, start, mid, rightRes, map);
            }

            private int[] query(int start, int end, int from, int to, int[] candidate,
                                Map<Integer, List<Integer>> map) {
                if (candidate[0] >= 0) {
                    List<Integer> indices = map.get(candidate[0]);
                    int total = count(indices, from, to) + candidate[1];
                    if (total * 2 > end - start + 1) { return new int[] {candidate[0], total}; }
                }
                return NONE;
            }

            private int count(List<Integer> indices, int start, int end) {
                int low = Collections.binarySearch(indices, start);
                if (low < 0) {
                    low = -low - 1;
                }
                int high = Collections.binarySearch(indices, end);
                if (high < 0) {
                    high = (-high - 1) - 1;
                }
                return high - low + 1;
            }
        }
    }

    // Binary Search + Randomization + Hash Table
    // 76 ms(83.33%), 48.7 MB(87.96%) for 27 tests
    static class MajorityChecker4 {
        private int[] data;
        private Map<Integer, List<Integer>> map = new HashMap<>();

        public MajorityChecker4(int[] arr) {
            this.data = arr;
            for (int i = 0; i < arr.length; i++) {
                map.computeIfAbsent(arr[i], x -> new ArrayList<>()).add(i);
            }
        }

        public int query(int left, int right, int threshold) {
            for (int i = 20; i > 0; i--) {
                int candidate = data[left + (int)(Math.random() * (right - left + 1))];
                List<Integer> indices = map.get(candidate);
                if (indices.size() < threshold) { continue; }

                int low = Collections.binarySearch(indices, left);
                if (low < 0) {
                    low = -low - 1;
                }
                int high = Collections.binarySearch(indices, right);
                if (high < 0) {
                    high = (-high - 1) - 1;
                }
                if (high - low + 1 >= threshold) { return candidate; }
            }
            return -1;
        }
    }

    // Moore's voting algorithm
    // 2251 ms(26.85%), 49.9 MB(11.11%) for 27 tests
    static class MajorityChecker5 {
        private int[] data;

        public MajorityChecker5(int[] arr) {
            this.data = arr;
        }

        public int query(int left, int right, int threshold) {
            int major = majority(data, left, right);
            for (int i = left, cnt = 0; i <= right; i++) {
                if (data[i] == major && ++cnt >= threshold) { return major; }
            }
            return -1;
        }
    }

    void test1(String className) throws Exception {
        test(new String[] {className, "query", "query", "query"},
             new Object[][] {{new int[] {1, 1, 2, 2, 1, 1}}, {0, 5, 4}, {0, 3, 3}, {2, 3, 2}},
             new Object[] {null, 1, -1, 2});
        test(new String[] {className, "query", "query", "query", "query", "query", "query", "query",
                           "query", "query", "query"},
             new Object[][] {{new int[] {2, 2, 1, 2, 1, 2, 2, 1, 1, 2}}, {2, 5, 4}, {0, 5, 6},
                             {0, 1, 2}, {2, 3, 2}, {6, 6, 1}, {0, 3, 3}, {4, 9, 6}, {4, 8, 4},
                             {5, 9, 5}, {0, 1, 2}},
             new Object[] {null, -1, -1, 2, -1, 2, 2, -1, -1, -1, 2});
        test(new String[] {className, "query", "query", "query", "query", "query", "query", "query",
                           "query", "query", "query"},
             new Object[][] {{new int[] {2, 1, 1, 1, 2, 1, 2, 1, 2, 2, 1, 1, 2}}, {2, 9, 7},
                             {9, 11, 2}, {2, 11, 7}, {3, 4, 2}, {0, 1, 2}, {6, 9, 3}, {3, 12, 7},
                             {3, 10, 6}, {7, 11, 4}, {0, 6, 4}},
             new Object[] {null, -1, 1, -1, -1, -1, 2, -1, -1, -1, 1});
    }

    void test(String[] methods, Object[][] args, Object[] expected) throws Exception {
        final String name = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        Class<?> clazz = Class.forName(name + "$" + methods[0]);
        Constructor<?> ctor = clazz.getConstructors()[0];
        Object obj = ctor.newInstance(args[0]);
        for (int i = 1; i < methods.length; i++) {
            Object[] arg = args[i];
            Object res = null;
            if (arg.length == 0) {
                res = clazz.getMethod(methods[i]).invoke(obj);
            } else if (arg.length == 3) {
                res = clazz.getMethod(methods[i], int.class, int.class, int.class).invoke(obj, arg);
            }
            if (expected[i] != null) {
                assertEquals(expected[i], res);
            }
        }
    }

    @Test public void test1() {
        try {
            test1("MajorityChecker1");
            test1("MajorityChecker2");
            test1("MajorityChecker3");
            test1("MajorityChecker4");
            test1("MajorityChecker5");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
