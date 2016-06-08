import java.util.*;
import java.lang.reflect.Array;

import org.junit.Test;
import static org.junit.Assert.*;

// Given a string S and a string T, count the number of distinct subsequences of
// T in S.
public class DistinctSubseq {
    private List<Integer[]> getIndexListSeq(String s, String t) {
        boolean[] targetChars = new boolean[256];
        Map<Character, List<Integer> > indexMap = new HashMap<>();

        for (char c : t.toCharArray()) {
            targetChars[c] = true;
        }

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (targetChars[c]) {
                if (!indexMap.containsKey(c)) {
                    indexMap.put(c, new ArrayList<>());
                }
                indexMap.get(c).add(i);
            }
        }

        List<Integer[]> indexListSeq = new ArrayList<>(t.length());
        for (char c : t.toCharArray()) {
            if (!indexMap.containsKey(c)) return null;

            indexListSeq.add(indexMap.get(c).toArray(new Integer[0]));
        }
        return indexListSeq;
    }

    private int nextIndex(Integer[] list, int x) {
        int index = Arrays.binarySearch(list, x);
        if (index >= 0) { // same array
            index++;
        } else { // not found, insertion point
            index = -index - 1;
        }
        return (index == list.length) ? -1 : index;
    }

    // Time Limit Exceeded
    public int numDistinct(String s, String t) {
        List<Integer[]> indexListSeq = getIndexListSeq(s, t);
        if (indexListSeq == null) return 0;

        int seqLen = t.length();
        int count = 0;
        Stack<Integer> stack = new Stack<>();
        stack.push(-1);
        while (!stack.isEmpty()) {
            int pos = stack.pop();
            Integer[] indexList = null;
            while (!stack.isEmpty()) {
                indexList = indexListSeq.get(stack.size());
                if (pos < indexList[indexList.length - 1]) {
                    pos = indexList[nextIndex(indexList, pos)];
                    stack.push(pos);
                    break;
                }
                pos = stack.pop();
            }
            int i = stack.size();
            int lastPos = 0;
            while (true) {
                indexList = indexListSeq.get(i);
                pos = nextIndex(indexList, pos);
                lastPos = pos;
                if (++i == seqLen || pos < 0) break;
                pos = indexList[pos];
                stack.push(pos);
            }
            if (i < seqLen || pos < 0) continue;

            count += indexList.length - lastPos;
        }
        return count;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<String, String, Integer> distinct, String name,
              String s, String t, int expected) {
        long t1 = System.nanoTime();
        assertEquals(expected, (int)distinct.apply(s, t));
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
    }

    void test(String s, String t, int expected) {
        DistinctSubseq d = new DistinctSubseq();
        test(d::numDistinct, "numDistinct", s, t, expected);
    }

    @Test
    public void test1() {
        test("rabbbit","rabbit", 3);
        test("aabbcc","abc", 8);
        test("aabbccaa","abc", 8);
        test("abbccaacb","abc", 6);
        test("aabbccaac","abc", 12);
        test("aabbccaacb","abc", 12);
        test("aabbccabcaacb","abc", 22);
        test("abbccaacac", "abcac", 22);
        test("aabdbaabeeadcbbdedacbbeecbabebaeeecaeabaedadcbdbcdaabebdadbbaeab"
             + "dadeaabbabbecebbebcaddaacccebeaeedababedeacdeaaaeeaecbe",
             "bddabdcae", 10582116);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("DistinctSubseq");
    }
}
