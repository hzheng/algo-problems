import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.function.Function;

// LC187: https://leetcode.com/problems/repeated-dna-sequences/
//
// All DNA is composed of a series of nucleotides abbreviated as A, C, G, and T,
// for example: "ACGAATTCCG". When studying DNA, it is sometimes useful to
// identify repeated sequences within the DNA.
// Write a function to find all the 10-letter-long sequences (substrings) that
// occur more than once in a DNA molecule.
public class RepeatedDNA {
    // Hash Table
    // beats 63.66%(40 ms)
    public List<String> findRepeatedDnaSequences(String s) {
        List<String> res = new ArrayList<>();
        Map<String, Integer> counts = new HashMap<>();
        final int size = 10;
        int end = s.length() - size + 1;
        for (int i = 0; i < end; i++) {
            String seq = s.substring(i, i + size);
            if (!counts.containsKey(seq)) {
                counts.put(seq, 1);
            } else {
                int count = counts.get(seq);
                if (count == 1) {
                    res.add(seq);
                }
                counts.put(seq, ++count);
            }
        }
        return res;
    }

    //
    // Hash Table + Bit Manipulation
    // beats 51.73%(56 ms for 32 tests)
    public List<String> findRepeatedDnaSequences2(String s) {
        List<String> res = new ArrayList<>();
        Map<Integer, Integer> counts = new HashMap<>();
        int[] charMap = new int['T' - 'A' + 1];
        charMap['C' - 'A'] = 1;
        charMap['G' - 'A'] = 2;
        charMap['T' - 'A'] = 3;
        final int size = 10;
        final int bits = 2;
        final int mask = ((1 << (size * bits)) - 1); // ~(0x300000)
        int seq = 0;
        for (int i = 0; i < s.length(); i++, seq <<= 2) {
            seq |= charMap[s.charAt(i) - 'A'];
            if (i < size - 1) continue;

            seq &= mask;
            int count = counts.getOrDefault(seq, 0);
            if (count == 1) {
                res.add(s.substring(i - size + 1, i + 1));
            }
            if (count < 2) {
                counts.put(seq, ++count);
            }
        }
        return res;
    }

    // Hash Table + Bit Manipulation
    // beats 72.36%(50 ms for 32 tests)
    public List<String> findRepeatedDnaSequences3(String s) {
        final int size = 10;
        List<String> res = new ArrayList<>();
        Set<Integer> words = new HashSet<>();
        Set<Integer> doubleWords = new HashSet<>();
        int[] charMap = new int['T' - 'A' + 1];
        charMap['C' - 'A'] = 1;
        charMap['G' - 'A'] = 2;
        charMap['T' - 'A'] = 3;
        for (int i = 0; i <= s.length() - size; i++) {
            int seq = 0;
            for (int j = i; j < i + size; j++) {
                seq <<= 2;
                seq |= charMap[s.charAt(j) - 'A'];
            }
            if (!words.add(seq) && doubleWords.add(seq)) {
                res.add(s.substring(i, i + 10));
            }
        }
        return res;
    }

    // Hash Table + Bit Manipulation
    // https://discuss.leetcode.com/topic/8487/i-did-it-in-10-lines-of-c
    // beats 51.73%(56 ms for 32 tests)
    public List<String> findRepeatedDnaSequences4(String s) {
        Map<Integer, Integer> map = new HashMap<>();
        List<String> res = new ArrayList<>();
        final int size = 10;
        final int bits = 3;
        final int charMask = (1 << bits) - 1; // 7
        final int seqMask = (1 << size * bits) - 1; // 0x3FFFFFFF
        for (int hash = 0, i = 0; i < s.length(); i++) {
            hash = (hash << bits & seqMask) | (s.charAt(i) & charMask);
            int count = map.getOrDefault(hash, 0);
            if (count == 1) {
                res.add(s.substring(i - size + 1, i + 1));
            }
            if (count < 2) {
                map.put(hash, ++count);
            }
        }
        return res;
    }

    // Solution of Choice
    // Hash Table + Bit Manipulation
    // Rabin-Karp algorithm
    // beats 64.85%(52 ms for 32 tests)
    public List<String> findRepeatedDnaSequences5(String s) {
        final int size = 10;
        final int bits = 2;
        final int mask = (1 << size * bits) - 1; // 0x3FFFFFFF
        int[] charMap = new int['T' - 'A' + 1];
        charMap['C' - 'A'] = 1;
        charMap['G' - 'A'] = 2;
        charMap['T' - 'A'] = 3;
        Set<String> res = new HashSet<>();
        Set<Integer> hashes = new HashSet<>();
        for (int i = 0, hash = 0; i < s.length(); i++, hash <<= bits) {
            hash &= mask;
            hash |= charMap[s.charAt(i) - 'A'];
            if (i >= size - 1 && !hashes.add(hash)) {
                res.add(s.substring(i - size + 1, i + 1));
            }
        }
        return new ArrayList<>(res);
    }

    void test(Function<String, List<String> > find, String s, String ... expected) {
        assertArrayEquals(expected, find.apply(s).toArray(new String[0]));
    }

    void test(String s, String ... expected) {
        RepeatedDNA r = new RepeatedDNA();
        test(r::findRepeatedDnaSequences, s, expected);
        test(r::findRepeatedDnaSequences2, s, expected);
        test(r::findRepeatedDnaSequences3, s, expected);
        test(r::findRepeatedDnaSequences4, s, expected);
        test(r::findRepeatedDnaSequences5, s, expected);
    }

    @Test
    public void test1() {
        test("AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT", "AAAAACCCCC", "CCCCCAAAAA");
        test("AAAAAAAAAAAA", "AAAAAAAAAA");
        test("AAAAAAA");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RepeatedDNA");
    }
}
