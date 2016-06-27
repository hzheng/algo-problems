import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.function.Function;

// https://leetcode.com/problems/repeated-dna-sequences/
//
// All DNA is composed of a series of nucleotides abbreviated as A, C, G, and T,
// for example: "ACGAATTCCG". When studying DNA, it is sometimes useful to
// identify repeated sequences within the DNA.
// Write a function to find all the 10-letter-long sequences (substrings) that
// occur more than once in a DNA molecule.
public class RepeatedDNA {
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

    // beats 63.66%(40 ms)
    public List<String> findRepeatedDnaSequences2(String s) {
        List<String> res = new ArrayList<>();
        final int size = 10;
        if (s.length() < size) return res;

        Map<Integer, Integer> counts = new HashMap<>();
        int[] charMap = new int['T' - 'A' + 1];
        charMap['C' - 'A'] = 1;
        charMap['G' - 'A'] = 2;
        charMap['T' - 'A'] = 3;

        int seq = 0;
        for (int i = 0; i < size; i++) {
            seq <<= 2;
            seq |= charMap[s.charAt(i) - 'A'];
        }
        counts.put(seq, 1);

        for (int i = size; i < s.length(); i++) {
            seq <<= 2;
            seq |= charMap[s.charAt(i) - 'A'];
            seq &= ~(0x300000);
            if (!counts.containsKey(seq)) {
                counts.put(seq, 1);
            } else {
                int count = counts.get(seq);
                if (count == 1) {
                    res.add(s.substring(i - size + 1, i + 1));
                }
                counts.put(seq, ++count);
            }
        }
        return res;
    }

    void test(Function<String, List<String>> find, String s, String... expected) {
        assertArrayEquals(expected, find.apply(s).toArray(new String[0]));
    }

    void test(String s, String... expected) {
        RepeatedDNA r = new RepeatedDNA();
        test(r::findRepeatedDnaSequences, s, expected);
        test(r::findRepeatedDnaSequences2, s, expected);
    }

    @Test
    public void test1() {
        test("AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT", "AAAAACCCCC", "CCCCCAAAAA");
        test("AAAAAAAAAAAA", "AAAAAAAAAA");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RepeatedDNA");
    }
}
