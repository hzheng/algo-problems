import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.function.Function;

// LC767: https://leetcode.com/problems/reorganize-string/
//
// Given a string S, check if the letters can be rearranged so that two
// characters that are adjacent to each other are not the same.
// If possible, output any possible result. Otherwise, return the empty string.
public class ReorganizeString {
    // Priority Queue
    // beats %(21 ms for 62 tests)
    public String reorganizeString(String S) {
        Map<Character, Integer> freq = new HashMap<>();
        int max = 0;
        for (char c : S.toCharArray()) {
            int count = freq.getOrDefault(c, 0) + 1;
            freq.put(c, count);
            if (count > max) {
                max = count;
            }
        }
        int len = S.length();
        if (max > (len + 1) / 2) return "";

        PriorityQueue<Character> pq =
            new PriorityQueue<>(new Comparator<Character>() {
            public int compare(Character a, Character b) {
                return freq.get(b) - freq.get(a);
            }
        });
        for (char c : freq.keySet()) {
            pq.offer(c);
        }
        char[] res = new char[len];
        while (!pq.isEmpty()) {
            char c = pq.poll();
            int count = freq.get(c);
            for (int i = 0; i < len; i++) {
                if (res[i] == 0 && (i == 0 || res[i - 1] != c)) {
                    res[i] = c;
                    break;
                }
            }
            if (count > 1) {
                freq.put(c, count - 1);
                pq.offer(c);
            }
        }
        return new String(res);
    }

    // Priority Queue
    // beats %(10 ms for 62 tests)
    public String reorganizeString2(String S) {
        int n = S.length();
        int[] freq = new int[26];
        for (int i = 0; i < n; i++) {
            freq[S.charAt(i) - 'a']++;
        }
        PriorityQueue<Integer> pq =
            new PriorityQueue<>(new Comparator<Integer>() {
            public int compare(Integer a, Integer b) {
                return freq[b] - freq[a];
            }
        });
        // PriorityQueue<Integer> pq =
        //     new PriorityQueue<Integer>((a, b) -> freq[b] - freq[a]);
        for (int i = 0; i != 26; i++) {
            if (freq[i] > 0) {
                pq.offer(i);
            }
        }
        int q = -1;
        StringBuffer sb = new StringBuffer();
        while (!pq.isEmpty()) {
            int p = pq.poll();
            sb.append((char)(p + 'a'));
            freq[p]--;
            if (q >= 0 && freq[q] > 0) {
                pq.offer(q);
            }
            q = p;
        }
        return (sb.length() == n) ? sb.toString() : "";
    }

    // Sort
    // beats %(6 ms for 62 tests)
    public String reorganizeString3(String S) {
        int n = S.length();
        int[] counts = new int[26];
        for (char c : S.toCharArray()) {
            counts[c - 'a'] += 100;
        }
        for (int i = 0; i < 26; i++) {
            counts[i] += i;
        }
        Arrays.sort(counts);
        char[] res = new char[n];
        int pos = 1;
        for (int code : counts) {
            int count = code / 100;
            if (count > (n + 1) / 2) return "";

            char ch = (char) ('a' + (code % 100));
            for (int i = 0; i < count; i++, pos += 2) {
                if (pos >= n) {
                    pos = 0;
                }
                res[pos] = ch;
            }
        }
        return String.valueOf(res);
    }

    // Sort
    // beats %(10 ms for 62 tests)
    public String reorganizeString4(String S) {
        char[] cs = S.toCharArray();
        final int CHARS = 128;
        int[] freq = new int[CHARS];
        for (char c : cs) {
            freq[c]++;
        }
        int n = cs.length;
        for (int i = 0; i < CHARS; i++) {
            if (freq[i] > (n + 1) / 2) return "";
        }
        int[][] freqPairs = new int[CHARS][];
        for (int i = 0; i < CHARS; i++) {
            freqPairs[i] = new int[] {i, freq[i]};
        }
        Arrays.sort(freqPairs, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return b[1] - a[1];
            }
        });
        char[] res = new char[n];
        int pos = 0;
        for (int[] pair : freqPairs) {
            for (int i = 0; i < pair[1]; i++) {
                res[pos] = (char)pair[0];
                pos += 2;
                if (pos >= n) {
                    pos = 1;
                }
            }
        }
        return new String(res);
    }

    // Priority Queue
    // beats %(81 ms for 62 tests)
    public String reorganizeString5(String S) {
        int N = S.length();
        int[] count = new int[26];
        for (char c: S.toCharArray()) {
            count[c - 'a']++;
        }
        PriorityQueue<MultiChar> pq = new PriorityQueue<MultiChar>
        ((a, b) -> a.count == b.count ? a.ch - b.ch : b.count - a.count);
        for (int i = 0; i < 26; i++) {
            if (count[i] > 0) {
                if (count[i] > (N + 1) / 2) return "";
                pq.add(new MultiChar(count[i], (char) ('a' + i)));
            }
        }
        StringBuilder res = new StringBuilder();
        while (pq.size() >= 2) {
            MultiChar mc1 = pq.poll();
            MultiChar mc2 = pq.poll();
            if (res.length() == 0 ||
                mc1.ch != res.charAt(res.length() - 1)) {
                res.append(mc1.ch);
                res.append(mc2.ch);
                if (--mc1.count > 0) {
                    pq.add(mc1);
                }
                if (--mc2.count > 0) {
                    pq.add(mc2);
                }
            }
        }
        if (!pq.isEmpty()) {
            res.append(pq.poll().ch);
        }
        return res.toString();
    }

    class MultiChar {
        int count;
        char ch;
        MultiChar(int count, char ch) {
            this.count = count;
            this.ch = ch;
        }
    }

    void test(String S, boolean expected) {
        ReorganizeString r = new ReorganizeString();
        test(S, expected, r::reorganizeString);
        test(S, expected, r::reorganizeString2);
        test(S, expected, r::reorganizeString3);
        test(S, expected, r::reorganizeString4);
        test(S, expected, r::reorganizeString5);
    }

    void test(String S, boolean expected, Function<String, String> f) {
        String res = f.apply(S);
        if (!expected) {
            assertEquals("", res);
            return;
        }
        char[] c1 = S.toCharArray();
        Arrays.sort(c1);
        char[] c2 = res.toCharArray();
        Arrays.sort(c2);
        assertArrayEquals(c1, c2);
        for (int i = 1; i < S.length(); i++) {
            if (res.charAt(i) == res.charAt(i - 1)) {
                fail("Same characters " + res.charAt(i)
                     + " are adjacent at postion " + i);
            }
        }
    }

    @Test
    public void test() {
        test("aaccccbb", true);
        test("vvvlo", true);
        test("aab", true);
        test("aaab", false);
        test("tndsewnllhrtwsvxenkscbivijfqnysamckzoyfnapuotmdexzkkrpmppttfic"
             + "zerdndssuveompqkemtbwbodrhwsfpbmkafpwyedpcowruntvymxtyyejqtajk"
             + "cjakghtdwmuygecjncxzcxezgecrxonnszmqmecgvqqkdagvaaucewelchsmeb"
             + "ikscciegzoiamovdojrmmwgbxeygibxxltemfgpogjkhobmhwquizuwvhfaiav"
             + "sxhiknysdghcawcrphaykyashchyomklvghkyabxatmrkmrfsppfhgrwywtlxeb"
             + "gzmevefcqquvhvgounldxkdzndwybxhtycmlybhaaqvodntsvfhwcuhvuccwcsx"
             + "elafyzushjhfyklvghpfvknprfouevsxmcuhiiiewcluehpmzrjzffnrptwbuhn"
             + "yahrbzqvirvmffbxvrmynfcnupnukayjghpusewdwrbkhvjnveuiionefmnfxao",
             true);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
