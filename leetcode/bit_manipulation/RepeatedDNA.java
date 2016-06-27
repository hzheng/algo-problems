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

    // beats 43.17%(46 ms)
    public List<String> findRepeatedDnaSequences2(String s) {
        List<String> res = new ArrayList<>();
        final int size = 10;
        if (s.length() < size) return res;

        Map<Integer, Integer> counts = new HashMap<>();
        int end = s.length() - size + 1;
        int seq = strToInt(s.substring(0, size));
        counts.put(seq, 1);
        int power = 1 << (size - 1) * 2;
        for (int i = size; i < s.length(); i++) {
            seq >>= 2;
            seq += (charToInt(s.charAt(i)) * power);
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

    private int charToInt(char c) {
        switch (c) {
            case 'A': return 0;
            case 'C': return 1;
            case 'G': return 2;
            case 'T': return 3;
        }
        return -1;
    }

    private int strToInt(String s) {
        int n = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
        // for (int i = 0; i < s.length(); i++) {
            n <<= 2;
            n += charToInt(s.charAt(i));
        }
        return n;
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
