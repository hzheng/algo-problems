import java.util.*;
import java.util.stream.IntStream;
import static java.util.stream.Collectors.*;
import static java.util.Comparator.*;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

// LC791: https://leetcode.com/problems/custom-sort-string/
//
// S and T are strings composed of lowercase letters. In S, no letter occurs
// more than once. S was sorted in some custom order previously. We want to
// permute the characters of T so that they match the order that S was sorted.
// Return any permutation of T (as a string) that satisfies this property.
public class CustomSortString {
    // Sort
    // beats 31.85%(11 ms for 39 tests)
    public String customSortString(String S, String T) {
        int[] order = new int[26];
        for (int i = 0; i < S.length(); i++) {
            order[S.charAt(i) - 'a'] = i;
        }
        int n = T.length();
        Character[] t = new Character[n];
        for (int i = 0; i < n; i++) {
            t[i] = T.charAt(i);
        }
        Arrays.sort(t, new Comparator<Character>() {
            public int compare(Character a, Character b) {
                return order[a - 'a'] - order[b - 'a'];
            }
        });
        StringBuilder sb = new StringBuilder();
        for (char c : t) {
            sb.append(c);
        }
        return sb.toString();
    }

    // Bucket Sort
    // beats 99.96%(4 ms for 39 tests)
    public String customSortString2(String S, String T) {
        int[] freq = new int[26];
        for (char c : T.toCharArray()) {
            freq[c - 'a']++;
        }
        StringBuilder sb = new StringBuilder();
        for (char c : S.toCharArray()) {
            while (freq[c - 'a']-- > 0) {
                sb.append(c);
            }
        }
        for (char c = 'a'; c <= 'z'; c++) {
            while (freq[c - 'a']-- > 0) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    // Sort
    // beats 1.82%(98 ms for 39 tests)
    public static String customSortString3(String S, String T) {
        Map<Character, Integer> map = new HashMap<>();
        IntStream.range(0, S.length()).forEach(i -> map.put(S.charAt(i), i));
        return T.chars().mapToObj(c -> (char)c)
               .sorted(comparingInt(c -> map.getOrDefault(c, 0)))
               .map(c -> String.valueOf((char)c)).collect(joining());
    }

    void test(String S, String T, String[] expected) {
        List<String> expectedList = Arrays.asList(expected);
        assertThat(expectedList, hasItem(customSortString(S, T)));
        assertThat(expectedList, hasItem(customSortString2(S, T)));
        assertThat(expectedList, hasItem(customSortString3(S, T)));
    }

    @Test
    public void test() {
        test("cba", "abcd", new String[]{"cdba", "cbad"});
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
