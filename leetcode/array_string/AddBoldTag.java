import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC616: https://leetcode.com/problems/add-bold-tag-in-string/
//
// Given a string s and a list of strings dict, you need to add a closed pair of
// bold tag <b> and </b> to wrap the substrings in s that exist in dict. If two
// such substrings overlap, you need to wrap them together by only one pair of
// closed bold tag. Also, if two substrings wrapped by bold tags are
// consecutive, you need to combine them.
// Note:
// The given dict won't contain duplicates, and its length won't exceed 100.
// All the strings in input have length in range [1, 1000].
public class AddBoldTag {
    // Set
    // time complexity: O(|s| ^ 3)
    // space complexity: O(|s| + |dict| * |average string length of dict|)
    // Time Limit Exceeded
    public String addBoldTag(String s, String[] dict) {
        List<int[]> ranges = new ArrayList<>();
        Set<String> set = new HashSet<>();
        int maxLen = 0;
        for (String word : dict) {
            set.add(word);
            maxLen = Math.max(maxLen, word.length());
        }
        int len = s.length();
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j <= Math.min(len, i + maxLen); j++) {
                if (set.contains(s.substring(i, j))) {
                    int last = ranges.size() - 1;
                    if (last >= 0 && ranges.get(last)[1] >= i) {
                        ranges.get(last)[1] = Math.max(ranges.get(last)[1], j);
                    } else {
                        ranges.add(new int[] {i, j});
                    }
                }
            }
        }
        return convert(s, ranges);
    }

    private String convert(String s, List<int[]> ranges) {
        if (ranges.isEmpty()) return s;

        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (int[] range : ranges) {
            sb.append(s.substring(i, range[0])).append("<b>")
            .append(s.substring(range[0], range[1])).append("</b>");
            i = range[1];
        }
        sb.append(s.substring(i));
        return sb.toString();
    }

    // Trie
    // time complexity: O(|s| ^ 3)
    // space complexity: O(|s| + |dict| * |average string length of dict|)
    // beats 8.81%(164 ms for 70 tests)
    public String addBoldTag2(String s, String[] dict) {
        List<int[]> ranges = new ArrayList<>();
        TrieNode trie = new TrieNode();
        for (String word : dict) {
            trie.insert(word);
        }
        int len = s.length();
        for (int i = 0; i < len; i++) {
            int j = i + 1;
            if (!ranges.isEmpty()) {
                j = Math.max(j, ranges.get(ranges.size() - 1)[1] + 1);
            }
            for (; j <= len; j++) {
                Boolean matched = trie.search(s, i, j);
                if (matched == null) break;
                if (matched) {
                    int last = ranges.size() - 1;
                    if (last >= 0 && ranges.get(last)[1] >= i) {
                        ranges.get(last)[1] = Math.max(ranges.get(last)[1], j);
                    } else {
                        ranges.add(new int[] {i, j});
                    }
                }
            }
        }
        return convert(s, ranges);
    }

    private static class TrieNode {
        private Map<Character, TrieNode> children = new HashMap<>();
        // private TrieNode[] children = new TrieNode[256]; // Memory Limit Exceeded
        private boolean isWord;

        public Boolean search(String word, int start, int end) {
            TrieNode cur = this;
            for (int i = start; i < end; i++) {
                cur = cur.children.get(word.charAt(i));
                if (cur == null) return null;
            }
            return cur.isWord;
        }

        public void insert(String word) {
            TrieNode cur = this;
            for (char c : word.toCharArray()) {
                TrieNode node = cur.children.get(c);
                if (node == null) {
                    cur.children.put(c, node = new TrieNode());
                }
                cur = node;
            }
            cur.isWord = true;
        }
    }

    // Heap
    // time complexity: O(|s| * |dict| * |average string length of dict|)
    // space complexity: O(|s| * |dict|)
    // beats 39.88%(49 ms for 70 tests)
    public String addBoldTag3(String s, String[] dict) {
        PriorityQueue<int[]> intervals =
            new PriorityQueue<int[]>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return a[0] == b[0] ? a[1] - b[1] : a[0] - b[0];
            }
        });
        int len = s.length();
        for (String word : dict) {
            int w = word.length();
            outer : for (int i = 0; i <= len - w; i++) { // or use startsWith
                for (int j = 0; j < w; j++) {
                    if (s.charAt(i + j) != word.charAt(j)) continue outer;
                }
                intervals.offer(new int[] {i, i + w});
            }
        }
        List<int[]> ranges = new ArrayList<>();
        while (!intervals.isEmpty()) {
            int[] interval = intervals.poll();
            int last = ranges.size() - 1;
            if (last >= 0 && ranges.get(last)[1] >= interval[0]) {
                ranges.get(last)[1] = Math.max(ranges.get(last)[1], interval[1]);
            } else {
                ranges.add(interval);
            }
        }
        return convert(s, ranges);
    }

    // time complexity: O(|s| * |dict| * |average string length of dict|)
    // space complexity: O(|s|)
    // beats 61.02%(28 ms for 70 tests)
    public String addBoldTag4(String s, String[] dict) {
        int len = s.length();
        boolean[] bold = new boolean[len];
        for (int i = 0, end = 0; i < len; i++) {
            for (String word : dict) {
                if (s.startsWith(word, i)) {
                    end = Math.max(end, i + word.length());
                }
            }
            bold[i] = end > i;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            if (!bold[i]) {
                sb.append(s.charAt(i));
                continue;
            }
            int j = i;
            for (; j < len && bold[j]; j++) {}
            sb.append("<b>" + s.substring(i, j) + "</b>");
            i = j - 1;
        }
        return sb.toString();
    }
    
    // TODO: KMP algorithm

    void test(String s, String[] dict, String expected) {
        assertEquals(expected, addBoldTag(s, dict));
        assertEquals(expected, addBoldTag2(s, dict));
        assertEquals(expected, addBoldTag3(s, dict));
        assertEquals(expected, addBoldTag4(s, dict));
    }

    @Test
    public void test() {
        test("aaabbcc", new String[] {"aaa", "aab", "bc", "aaabbcc"},
             "<b>aaabbcc</b>");
        test("abcxyz123", new String[] {"abc", "123"},
             "<b>abc</b>xyz<b>123</b>");
        test("aaabbcc", new String[] {"aaa", "aab", "bc"}, "<b>aaabbc</b>c");
        test(
            "ajbccohukfglihmryfuizxqwsvkdivzhtyhzdmopogyygnxwkfqsxnrdbuemmajitgkqquainemgioajzxrdcdexuxohxhlxffgbswbdhzrzjykltpruvtffnhgfuswtuygjihgfrlociluxvtudbostrxbtnnxoehheyimlskfsyxfyewagehfsnjblslkjnduzgzsitfrmeepptxnesuesosrupmdnewpryrexureqraufdcgaiihwbmtvxgyqberatgpjcsnabecsyopeptwotxfwybjtzdcwtowhaavytwnvbzuznvdojsxmpefclsylfeshwxgpfadityivdigpsyyodrpzlekrectzrzbnyowlepykhdczrnnjcxxcstvinzwgyifyksobxseavzizxshatrftuvearvzzekzoyxjltveetccfpgjgndqdbrkqfwruqlqiegntvzqbhggmzmlmmgfpmbuakeoswhpiiprajhpvwcnjdvfzsiinbkenpogxfkrfxpgaxszwhclkwsvbjuyorjrbcfstqeqpjaiqgbtckcsvbavxpimfmznlenhjobmxoefackrsmwsgsjkjyizvtapadhyddogajjzcbvgjkctpgbgjmmfmjqilmlovojfzttufttmxjuabkocmmkdbfadapwtzmdriedrosmfoxyekugzmxpewuwqpwntzuxgulblokoerqthtphmqnmefmnvlihhuibfznvymqlfvuztixbelwdnqujxvdojeduxhvwnsyeyhcrntjvixxsquww",
            new String[] {"aj", "bc", "co", "hu", "kf", "gl", "ih", "mr", "yf", "ui", "zx", "qw", "sv", "kd", "iv", "zh", "ty"},
            "<b>ajbccohukfglihmryfuizxqwsvkdivzhty</b>hzdmopogyygnxw<b>kf</b>qsxnrdbuemm<b>aj</b>itgkqquainemgio<b>ajzx</b>rdcdexuxohxhlxffgbswbdhzrzjykltpruvtffnhgfuswtuygj<b>ih</b>gfrlociluxvtudbostrxbtnnxoehheyimls<b>kf</b>syxfyewagehfsnjblslkjnduzgzsitfrmeepptxnesuesosrupmdnewpryrexureqraufdcgai<b>ih</b>wbmtvxgyqberatgpjcsnabecsyopeptwotxfwybjtzdcwtowhaavytwnvbzuznvdojsxmpefclsylfeshwxgpfadi<b>tyiv</b>digpsyyodrpzlekrectzrzbnyowlepykhdczrnnjcxxcstvinzwgyifyksobxseavzi<b>zx</b>shatrftuvearvzzekzoyxjltveetccfpgjgndqdbrkqfwruqlqiegntvzqbhggmzmlmmgfpmbuakeoswhpiipr<b>aj</b>hpvwcnjdvfzsiinbkenpogxfkrfxpgaxszwhclkw<b>sv</b>bjuyorjr<b>bc</b>fstqeqpjaiqgbtckc<b>sv</b>bavxpimfmznlenhjobmxoefackrsmwsgsjkjyizvtapadhyddog<b>aj</b>jzcbvgjkctpgbgjmmfmjqilmlovojfzttufttmxjuabkocmm<b>kd</b>bfadapwtzmdriedrosmfoxyekugzmxpewuwqpwntzuxgulblokoerqthtphmqnmefmnvl<b>ihhui</b>bfznvymqlfvuztixbelwdnqujxvdojeduxhvwnsyeyhcrntjvixxsquww");
        test(
            "KJCbnHAbiERYGOwa6JERirw7YKlMX5WWXVrsTFOfDGPuzobcs2CIaUCbcpRyFfKUwQPHjRlNZfPEVcZZmjrxkHc1RUfw6FKKHiTXSgLiNVA1sKkQL7d8BLoAvbFepQV1yBeB1IypWfqYZPRJ8PZnWw2jVREQn3N2nb48jzBkJ5EF48MSjCkLbs8jdUaboYn0TP90zeXWQxMTa9xgCfryg6EBmzW8iaYBZIuXCR3ygfxnuHcccUrA8OZTBQArh5HTljftDeRZntphN2VdEaBwOZS7VxsaHYK1i1QEfOArLTtuIydQanMD6u1Nr3iHmxjsKx3jQKRQAzVT1W9Ite8qJ4DGrbnoSsXTysfC05WqMbmpRQnf0U4jUurJ6rhQFl2k2YogTQhLGKAm7vv98y1tfAb3RBYVTOUHsC71utT0iQIL1pFMZa8jm8gvSCK1k6d221aDHBfhPAGpVHvwBS1n70spmvIJNYHXOsIXcU7l6K7mYREzHinAwV9f8mdFzpr75rIChor8Ul4KIECm5KnL6avDR3w71u0OhCecibzRqgsHoHeFoEfHabNIClqpErZ5NRj85L0RJymeiucT7fTbFA7sYdpKHrCO3lbvNpawoYkGF5iHK3Ig0sTqOp1AmhhNkews5pvv7SZeGRZxMdTMbS7APGpq5jhGkx2SoKcB0DYza3mWlvOXBymgCSjRLroNjjXE4HJT0GKruOwQwaeekdxGqdE4rCcv8q3Ynxkki7QgR718Ytbq2P5Nqynsn0sBeCHniDZ0c8hiQ0QXaXS66Pvc6IRWbKM7TnzIX9t6lmSrSlOLfb4cDrZQNgPapasgt2Qc68rLRsavGQZfqEXvK7jplaJHz7a3acjgpimRqclHQH98RshAt8XRXeffwv3fwXsfi8fEQOMiCAU3iQzZhp0FXTewnvzhqbDm",
            new String[] {"sIXcU7l6K7mYREzHinAwV9f8mdFzpr75rIChor8Ul4KIECm5KnL6avDR3w71u0OhCecibzRqgsHoHeFoEfHabNIClqpErZ5NRj85L0RJymeiucT7fTbFA7sYdpKHrCO3lbvNpawoYkGF5iHK3Ig0sTqOp1AmhhNkews5pvv7SZeGRZxMdTMbS7APGpq5jhGkx2SoKcB0DYza3mWlvOXBymgCSjRLroNjjXE4HJT0GKruOwQwaeekdxGqdE4rCcv8q3Ynxkki7QgR718Ytbq2P5Nqynsn0sBeCHniDZ0c8hiQ0QXaXS66Pvc6IRWbKM7TnzIX9t6lmSrSlOLfb4cDrZQNgPapasgt2Qc68rLRs",
                          "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",
                          "Sxn8wz8Fk5OWUm0Sg3sdDd5RElGxxYGOnmqIedQP9CADedFFU5cOBTnKDuivamMc7hZBu0UlZnO1HvGNpvUXSsQefuQb2mvmivgSLhHfYzz34VlUAzOJS4K3PE1dd6JrfNPTKBe8hI6dgLlxe4iWq6Gak3V2G5EMc8tsz6JjKMfCPcRQIB9PECI0yGCMT2YeU5Zk4t8eMTi0ncOQR4kqCO0ciC9rqRUaxNTiTNOChlWVvp99bP774JoNMvblJkWrXHkvwYn4sv1wwObZjjCRZVWFFnIHCaZeuGECOBazOadeQn72hQDbYOsXFhcRFNMCRA3w9XytvhDaLVXzFpXcQjC8YkOex3wDufmFGKd2tFlYbF6McwL2DdBcXFCZPbDptBeJ4WWvG2Xkz9tVe3T9alyoFEENEOKT3eoCXaQhZk1ttNOxuDkRiFBu1BETgOeOW1sWkLn4nIfViJyNR2A7Bos",
                          "n3N2nb48jzBkJ5EF48MSjCkLbs8jdUaboYn0TP90zeXWQxMTa9xgCfryg6",
                          "fEQOMiCAU3i", "cj",
                          "bq2P5Nqynsn0sBeCHniDZ0c8hiQ0QXaXS66Pvc6IRWbKM7TnzIX9t6lmSrSlOLfb4cDrZQNgPapasgt2Qc68rLRsavGQZfqEXvK7jplaJHz7a3acjgpimRqclHQH98RshAt8XRXeffwv3fwXsfi8fEQOMiCAU3iQzZhp0FXTewnvzhqbDm",
                          "354TsXJMAhP2F3lpkLj1H75d9VHERN0gFl9eeG0wQjrXE3P1wMd3icdAslfgQORlABAfIdAEmu5M8ypeHpqVy10F",
                          "50FroQBK6EarOHKWdU8Oo2Q3HOFgqHsupyx9RDWW1s5cb6AqFLBhQoVyaQwhk29vPKHhCQJ8vJujtx5Htk6JRHNxnaKGSX29oiNSpu4RQyLyRHwoW6lLTdfICFRLsGAjptCJXdxS32BrSn3p7FIlCHZdUVm0x39ftoBaOjuIZB3PoII3Ubt3BBF4UD0cqdGuCwjdqj25sKNyCKyUc5M9Vq8VFl8vQIwGR3ZFlzryqxL4ClIKwcnNfjKq6rqsScR1j4pt8DawYWhoDBydr3pRf6qrQMgie52RLJ4FHbOGQUtkKe236TgKsDKKGMpj41mGUhkSCJPgHP4i598zKgMJDTVtK4DSlVSmnXTx5QcJisX4jRXt0Kouz6qSMgdbmGWuKUz8VFWrVQSmIrh86FB2JHVcmpWBVS9YOlRYHIPiDsbkY27yNEm1brkDgSZlX0qi4xj5dDsNeStemuiLzfTnRoWaTDf2igAauzDKRogMd8YArAH9byizoQeKjCLAfFGnFfn5S9fnMvdtrTp29En5LMDTv8NdEdHr1dUTvqLWTXbTm4XVCkQU5phmjRShbqyo6mw53CjmubdXpNAXWM1KEw0iWLbGZFZaC5RJXOWO85hwugSST1hzy2Poizt06z8qFx4PFALMw4j8nFa2h2XGRFsWZV1l0AaW5i2vLIIl02XAVAxo6hFtBUynn8S4S2xHivpMpk3K9o3GJ7gf8xJQdTJ6ROyzYvnUhkF6vme55CoqY0O2Dn9ISGDubaX36FQbX4",
                          "IaUCbcpRyFfKUwQPHjRlNZfPEVcZZmjrxkHc1RUfw6FKKHiTXSgLiNVA1sKkQL7d8BLoAvbFepQV1yBeB1IypWfqYZPRJ8PZnWw2jVREQn3N2nb48jzBkJ5EF48MSjCkLbs8jdUaboYn0TP90zeXWQxMTa9xgCfryg6EBmzW8iaYBZIuXCR3y",
                          "V9BlafdYgT6jq3JfOhaBMRZ1tO9eR5Me3hGaApupkIcbNsjvSRQHnrByNJgqzyumJ59mxqNX9CRez4BBFU69wR89q64Ox9Hc3mDRFfWQ5T3KNl5f5IZl7jqt6v1",
                          "91xQ8f7WQqHJ9aXaW6bAF2UlWn0RxcX7ct2Ym7bVd9SXymiuamdM56Gf6Xj0r9nFP8gfqEuMICllZr9Ygv",
                          "ZntphN2VdEaBwOZS7VxsaHYK1i1QEfOArLTtuIydQanMD6u1Nr3iHmxjsKx3jQKRQAzVT1W9Ite8qJ4DGrbnoSsXTysfC05WqMbmpRQnf0U4jUurJ6rhQFl2k2YogTQhLGKAm7vv98y1tfAb3RBYVTOUHsC71utT0iQIL1pFMZa8jm8gvSCK1k6d221aDHBfhPAGpVHvwBS1n70spmvIJNYHXOsIXcU7l6K7mYREzHinAwV9f8mdFzpr75rIChor8Ul4KIECm5KnL6avDR3w71u0OhCecibzRqgsHoHeFoEfHabNIClqpErZ5NRj85L0RJymeiucT7fTbFA7sYdpKHrCO3lbvNpawoYkGF5iHK3Ig0sTqOp1Amh",
                          "Zs2VBG9jJbp3DMz2WOWSbPH49othqrsp8TbdikNUU7r0iiIjXAhL5zJK4er3esx2SEs0L45BgmjzPGtApgx0XtgfkxE1tCltqoJOHMuSlL0gy9nRyKQlSMawlxde11Dnd2wGsh9Nukj2Gt6rhC8yqVcfsQu6eXdjgzYC8A4INrmcTeaPupaQzklhehfyoN2XKREKqHgQjooEdQ9fKj74VIOBnNxk5f85M1IHYMWxXbFBSlSvpHfoR6JyYOXTxAws41rAVvwDdBMVuNtImjX7dkcb9JeBRuTLIuQW9ElGtr0cvHQYrRt3XlcXT5JxmfMV9fvnMTNW7anUAobU36mjk7pIcG6oXIs63KnzWIXJYMlGjURfcg1qfvROdU6VsNuWZldd5QSx1MEY3glPEDUgCPqb9Y4BOBmvgM8VJOVxaAEmzSH0QODXNFFhltUKwpscVWMAZpif3JtwurMD72XR9L8YSIuKxt09yWm9e8XvKTXQBqzkjGt9jepOF0t02P5SnW7D6",
                          "J4zL4osKAtUg1SL7mf1jpG070Vqjd7WEyFIWORsHStLCqWgK1k0Xqsss8OJRU8vgVhtkwugvC1CR49GvkbhraQyPltrFXgksZlr6GO2ZWNHjEsVUjwnXQtIHp1jVTSKOawLig760U9UvJd9C3dcV764Bhei9DJSMbjfV7ciJfOqaM34QDeEZi85RC0D3BD7DVRHjxjYftFvg2vWKgsqSkz3YLaLbX55vhLYJzjsNHBh12bSTNjTTRsL1VzHFpbJwHySqCfJ5peGL4JSE4LTA0TMOdtWwmPTzPyX7ZL2lxHMpN0LHkj4mZOgVnJ7INL4s0TeDLsd3rEOvRoj4rYNkzzYpKvvrSdhosZQ83i5RqVx98Rkno9Q49UqryT8e3MvBDp5vm1wQuzXnxWNUGzvrpZ4lAJaXoUVt806zqzRpL3q3gK8O1LJ9iQ4g0kCWdQ7mTW8H16xlVM7n2zeNQ1PzrJbRUoQjh31AOkLJUl1IhrsQDBqcpr0eGzdBTRsphPrjUze2AkQGNdPsyUYPjSWDETk6aXzrXqqFmg09fTayd0OhI53cf6VMUKQdHlhqL9B1nzMa5146WJVenp3p81ikwDutjdiYaXzRnHfEeXhmDbNk44T2tl8hNpFKMMj7bAmg9AvqT722UOJmZl3ltR4ZgBGKkxNGlMfNOBYIU9xG1Zj76LdVy9Pw8fLhcWkBMLQdO5HAnG0SSgnPXhZeJA8E2TMxH4jzf0rIPWqRd0FW9x4NTDh4nuPdYuXTQFqN5p6CFLn3OJymPIx2wXa0uSOVjAEowZjk39hyLojUlMxWWg1FBx9cKf9HDSyw0O1rVqH6xYVqUoyZ1QoYLhDPTJgaRxVq3HC9wko1PJASsbKys6hnVmSqlMJAKw7x2P4RDKwonsNXL9FdZNSXxJ2uEmCUqP4Lh1",
                          "cjWU33CEdx0R1kV7WrqaLjIjoLY5gBnK1cOrmFCMwN4bbNgFjDTpwd5IQpJ1Pc3JXrANBR5nG5smELQHM0hrAs1eaA5aOMYgbaaWnIDLIgVL9vMdayD8S5nDDlvZwVwyvr0I56XUVeLsDp3NxQ2IhgPS3H90wGoUh9mpnjSrK6gaeb7n3Is8dsHKNy3poVjuvwI4lI1tH0vKiaalyQwwEPTdiMwOpe7OTg61l4qqSkJ1nbYVLlYdLp2YihSUgGsxoD3vlWmmcpOOtnTzXMc9BincHfurArBGuOdlne858uBMhyYA5gQvmY2fBMyP5BG2QTiNDEgEj8VRIfOmQblvPTJmvQNG8m8LzhAe2PZNxcghEyaGyx3WLtfAkdjcKMhnMqbn5jqcsFMpV58lmWRmJCfY9gmLXhhw00RWnKR8eMBpScKhCoUmaIGpiGr6Jfj5Y9JfZwFiB04cptuGjR1SylL9d0nXvzkC35awnvdAchShGfQ5TO1HwmVcf7csgdyudRwhUPi3KhbrLpWxDHQQyXR7doMAAy4pC4tcSQGa7vRhFbFzxucdiZPKPwKj7KxcDywSjSkuAkhkGrEjyrD19iTJbWSLTOgIcOKa31q6K5Kx3xoMfiM3s1VVlcEG9BWzhfwibnn3QDcJuTJwJBKwpfW9OoK2LUYBD0920wRjZ8HPYORMHRm3QvVRYX38WdSFxDBOKWKb0pBbCrVBVGw1Q2LBaOD8VefgoJx7wu1Z7ANqSul2XBHwXLIafJDSYAcbGCqx0bw7qj4ZAM4Ing9Bx1UfafUSvPRZqcmIUU",
                          "68rLRsavGQZfqEXvK7jplaJHz7a3acjgpimRqc",
                          "cDrZQNgPa",
                          "g6xe2LorSciYPWhHQdZ1kwYvtgnzgkgiG9GdEY1rveCUGB2LnssR8E6AIk8RnR6HfH03zhl6yuItdcCIRbkcTbwKfFODu0e7y1VgS2ejB7ZXfWWizKW7IeugJX9KQQQXIEahuGeVzIYIOJxJkt8AnGhna1J4bogSeu0MMvHZZGP0qQNLRNwAWSKhFAaHne8difG8FcC8PhHsrKKVcTEN4L6muIJu2Wy15mXDKO3ONGRRdBmXCcEX79Uz7cTts8zxNVTbDmHrIorWJXLpL0TZs3h1DG35Uki2Zf9wYy1LWLbnOvfCFJJhxUioe4oJ7yFMbBE9zOtOZhRnmuvgI3tOHadlT6KBbVDYf1T9HBql1cgvJRSeit7FQTiPWx5JE2hOGPxcEDUNd5wZFN7nBAubpfh42kWdxWxLnEBsgLeoHWfPUlPHOQzH4tVikwdmjSlm1j5F1sSSsX8X9KOqOdralJGnGSY5AW5u74EG96qGQ4KCzHaoVDnJWKjmPKWFW8TkhfRbINd5mDuj8sprM6RsN7AmpxVCza5o1KbrNgayyIT6cSlX4foJzIcsPXy21aa2olrFAcZZWhcaLXIXyE",
                          "chFQpXDdaLdqi0Q3J3JZpT7yZGp4YnaLhRs3gx8M0mVGNOIehUsb2dw6qOaf4LFbGhotRAvSyia74rVjTg4moYChxdjXWrdyMN7qXLg0QkjGGMeQAiFkOGoHvatIwLHWJbNok12gjZOkdQ5ozOow4eTX1CQSg87IkxlvmpBrUwIxf685pmu1Shubkaamg2zxzDQxGKnjxCXZfeMsqVlnHxNTKWS7SNxjPXrf3AxbY3TX0tt2wskYb0tStQ0ICU53lkidvIvgU5CYiY0GhikCsMkSQ6hpKuAQfaWsMUjfrKSFIAPmIUeaAqxVv3lmWyjrtik4I5UcXLF0DNAhqzYuXzorvIAE3MclFvBNv9q6yk0scuynK0Sl2QhEPgSoDTLdA8P1IJaZcyCRm",
                          "1i1QEfOArLTtuIydQanMD6u1Nr3iHmxjsKx3jQKRQAzVT1W9Ite8qJ4DGrbnoSsXTysfC05WqMbmpRQnf0U4jUurJ6rhQFl2k2YogTQhLGKAm7vv98y1tfAb3RBYVTOUHsC71utT0iQIL1pFMZa8jm8gvSCK1k6d221aDHBfhPAGpVHvwBS1n70spmvIJNYHXOsIXcU7l6K7mYREzHinAwV9f8mdFzpr75rIChor8Ul4KIECm5KnL6avDR3w71u0OhCecibzRqgsHoHeFoEfHabNIClqpErZ5NRj85L0RJymeiucT7fTbFA7sYdpKHrCO3lbvNpawoYkG",
                          "8OZTBQArh5HTljftDeRZntphN2VdEaBwOZS7VxsaHYK1i1QEfOArLTtuIydQanMD6u1Nr3iHmxjsKx3jQKRQAzVT1W9Ite8qJ4DGrbnoSsXTysfC05WqMbmpRQnf0U4jUurJ6rhQFl2k2YogTQhLGKAm7vv98y1tfAb3RBYVTOUHsC71utT0iQIL1pFMZa8jm8gvSCK1k6d221aDHBfhPAGpVHvwBS1n70spmvIJNYHXOsIXcU7l6K7mYREzHinAwV9f8mdFzpr75rIChor8Ul4KIECm5KnL6avDR3w71u0OhCecibzRqgsHoHeFoEfHabNIClqpErZ5NRj85L0RJymeiucT7fTbFA7sYdpKHrCO3lbvNpawoYkGF5iHK3Ig0sTqOp1AmhhNkews5pvv7SZeGRZxMdTMbS7APGpq5jhGkx2SoKcB0DYza3mWlvOXBymgCSjRL",
                          "Y13gaSmqr1h5cluUk7V60PbHzqTRhsKeEJ7QS9S6TQMiv6BASpG0uIUmSNnVxv1gW7o6wtfrCIUumRqvEGiQ0tf15AErCX8OFO6UlYLR2pPBdkOYLz19bcOq8o9cOJJf5SXXPkxGW4WHWHqr5mwT94XM4Tlr1egcXGEeF2PSPtVPJeVp6NHdKJ2xqhIeqHpLCSsXw8b82QGQoQhQryLKs4BW3W5kKQWPpveAQt4KJd3bLSRgWSbbQ387JSjYKr7pPVxRptoWg5BB6Qs2ADx44ziNsdZLGrTO7YxO5z8APE8bZH5ZvQEEjitCX8wdx4wtEpkJ8jvtBaErjC6K9XCgK9pKuWsKUrM0dTTgkSJZjj3lhlic51V9vZ8ZtXO22gPTJfLugbrpoNg4e648LPYsIDL50MKGWZevsosnxxC5IqV6a6LdV4Kg7JSuKTabNpbwA9RQaomFcv0oEmrP438zmoCWpr5RJF5Q0aDiHxGujgYQvqrD93kPuFSzEAjj6NCGt2tlWpFy7f1ERueRVBlEEpm7xhz8U8bQQKB3FqHCwJrjtEWglRLaBnocj090zAOoQTvKqDM8B100sg8OnTfXa4LWeyFWVx5nmy1BqYyEBPLBcmHRpTovFQseiHeWbGVFlQmTkxTTMH5gwqLADKhWcG9ITb8qzhRx73uKosgHy8afBhtL6BZyEbcMZkhGwPvaQqazBAWjmtUDzWihlnWecgyOv76SapAoNDFsChh7QMQOMlo2DIIyVaT32pWicMbMo0TdPXAPS4LpwVCgHiCaitgryUfU7hofJipeUFaABjpAYae1CMGo6vfc9TPyulPhWYtJ1MpzdJgbe3FIhJIpxLnzpQckleSd3fhWSiWXwzCrnY1ZBgl8kV3WMthSvQysWrzIa2bYq9ufhGvc7oaOMrWeCQxDeAJVLFrHJ129MkmzRMLh",
                          "J4xwUAbPgLImjnEQO6kobMYkJGiQgH0b8MiuphhPskVzy2LmaV94chtACVJ3PPaj6pzP6JAzNa9BnMB7GUBYSiJ3fy95EoLSiB8S2L00NSsBAfKYTIQwokOpTOUCWBA0",
                          "oAzONybHFJVgPtx1tcLWGMjVz6XzbsTkzyZjAxRMY0yakwyfHsuERv2eFaMplrMFXo6OvSiMaphZn8g4GGwRIoW4CUPie0C4u42cWsZiVdNxT2Iom5o67u1uF3dItfiKIgXy4Bulrdsn57HHnxCN99gP7PM7fu764q4sxcURAgyce83wMtCM82ww8Nm2MduUEbdoMXX2cLxWgOukZ1lVQqEwHenWm7cNIS4d7X4VknCD84xGMG9NehgKWMrYzEshvaHlqZCSmmCUdyVdD3TuCwT",
                          "u1Nr3iHmxjsKx3jQKRQAzVT1W9Ite8qJ4DGrbnoSsXTysfC05WqMbmpRQnf0U4jUurJ6rhQFl2k2YogTQhLGKAm7vv98y1tfAb3RBYVTOUHsC71utT0iQIL1pFMZa8jm8gvSCK1k6d221aDHBfhPAGpVHvwBS1n70spmvIJNYHXOsIXcU",
                          "8q3Ynxkki7QgR718Ytbq2P5Nqynsn0sBeCHniDZ0c8hiQ0QXaXS66Pvc6IRWbKM7TnzIX9t6lmSrSlOLfb4cDrZQNgPapasgt2Qc68rLRsavGQZfqEXvK7jplaJHz7a3acjgpimRqclHQH98RshAt8XRXeffwv3fwXsfi8fEQOMiCAU3iQz",
                          "d4HvIauLbzRTQc4wF2QkmW79udhVvOvwRKmxQa74v7mn8TDRr8tBwW0qBr4trWUh0iIx0fAsbqHaslvwlX2KqLA0SDXcRB3Yv5y75PXbaehHOn0jecKI0ub4ETCHFeYMzbqU41UR0rV0UbmJl2dakHM5ARAlEZzhucbGzmScAUq6PA3kYtaQw3CVjRs4qnuRTPuybcvpj53OZEyK8SsiXtNbXd3nnxbH84vRKJ5nVwOVkc8psC7JQVnUD0IOSEwjI9BH1pwd8mXP3cnRyIvk8HkEVpd8rom9oK1Fh4YQKw2S5MN0BSJp6VtzvAwQF4Tx16WF3Jp41j3CcTNHxagOkeb0fFLMTqoRcU3mRxh3rUqj4NZabIP04aV5na3zsi1WfDw62BeYYY8EcHyvixU07xTAWwpZTo9O6qZ69oLqHf0wP0JoS2Kyn0I1cD9TR7klxVLSf3UKug03Gr4w",
                          "9sdoyKWSpqwW6nWNnr1faYXmNUFbXWvJubvaE13hTnvZhK4tdjNOrt9kt43RigJZK4574h7FWafu8Xhe78JMxujw0pgzfpbLMjk5luTXGaEBP5MUoShOPbc4zMzBXbrXk6r0SOUkUwJe79Ssin7eU4RLohDMg0fPfEk4t8a4ERVxzAHLyu1rdkeAJrEcKlnURJHBmJXaWT7cwuUJeUpXmERB6BGXs9mspzyUjd8V5wgugGNyUC311fVGQrCleR9f9EcRTazurJFTCxyGnvDK4oxoOuTQIxSON9UdV9d3xNQDJ8ywWiF2IpFNCFm1PB2O5p4WmcTMIu5UIsB0FZ3oG5tJI",
                          "cU7l6K7mYREzHinAwV9f8mdFzpr75rIChor8Ul4KIECm5KnL6avDR3w71u0OhCecibzRqgsHoHeFoEfHabNIClqpErZ5NRj85L0RJymeiucT7fTbFA7sYdpKHrCO3lbvNpawoYkGF5iHK3Ig0sTqOp1AmhhNkews5pvv7SZeGRZxMdTMbS7APGpq5jhGkx2SoKcB0DYza3mWlvOXBymgCSjRLroNjjXE4HJT0GKruOwQwaeekdxGqdE4rCcv8q3Ynxkki7QgR718Ytbq2P5Nqynsn0sBeCHniDZ0c8hiQ0QXaXS66Pvc6IRWbKM7TnzIX9t6lmSrSlOLfb4cDrZQNgPapasgt2Qc68rLRsavGQZfqEXvK7jplaJHz7a3acjgpimRqclHQH98RshAt8XRXeffwv3fwXsfi8fEQO",
                          "2RmRWVtGx1aGdJ0aq2DGxy7rrHbalpYXjy83p6vdZqgm1FHZpnLdgOXGNrHsqtKbcBma1KEe6lkof85cbJ3MAYi9ssGYuhFQZOQe7AnS161QD51VnOMAgUNQFboIkplkbSTEXN0nhpNXO7RnSd3IFjv03LlOBQMT60zBpXSPANNIWRSQJ5WhuszFp1XBNCO4eJjhQ2eVvqiA0B5cIxUn6m4Kzz5hNWM8bPQQx0zZ575GQOusqDJrTAfRygNhlzrzou1yxADMEJAkZsLG9gSXb7frexGUsazNwb2VaC2I2c0iUD6j58BX13dyTsghvIDRCQIBwTaI7WxHhlxeNbfSwLxfEQGBH06NPUFKirutpuEJQpEIMDtCiEbgKiTMWHhe52y8tAQ65uBBcM1SQ8Ke8jWzfw1TXSd6a0gF6k7T77wWuu7myz2LxfemY8I3wWnp2flwoiXdJWwtBSyuWHdntuZicnivmdIe6MLkDd4XI02dzBZ7uQ2IYL6XCLIBGHnTw5KjPD8nYXwuzKqn9Bh6qwJvkX7eV4BHTOEybS5G6Hx1AQDoXT8RReDTAujOykwjJlUvMOd2Kmd4navCDURHlPBbvlm91P2pXxKnODV5tyRM6F4CYsvZn7DWO13ht6UVNs5DBkURaEaxI92lQHg40cKouS3zYI9I5UCTl8LDdmknKqCHaOXn6d6caeRQu0OY2AfrNX951eOM7LuDNJezQ795XjrhwroKgrqTgLvA7zNSyGpUPsE2Bwv71H3jaDEk4JNNv4BYgAzxbgIlg0NddTvGxLshQMVZWw7cPq2KyyN2c8hroTecCfOnrimSfTWpevoZkH0qoBYFxTE",
                          "fwXsfi8f",
                          "OfDGPuzobcs2CIaUCbcpRyFfKUwQPHjRlNZfPEVcZZmjrxkHc1RUfw6FKKHiTXSgLiNVA1sKkQL7d8BLoAvbFepQV1yBeB1IypWfqYZPRJ8PZnWw2jVREQn3N2nb48jzBkJ5EF48MSjCkLbs8jdUaboYn0TP90zeXWQxMTa9xgCfryg6EBmzW8iaYBZIuXCR3ygfxnuHcccUrA8OZTBQArh5HTljftDeRZntphN2VdEaBwOZS7VxsaHYK1i1QEfO",
                          "foZa63b0KQJoZNYgxuAyd3ySa0EURygGAYVQ0eRDbkl5yIhxvEQSRWSdJich16QHJiarxPw4DYPyvOiRREpxKcL902AVl07G9WxigFtRemeYYEZGyYclLRFKdavVTkycHbQDsuTfkJvjdgOizX3c5tDKfbpal5HUCcJjsJf5JgBS2U5f9WjUmb1ESPcu2qwERdkYASBG6QTCMpfwXin8WEWLoF7guZmN4PqQCdY2Dsg1sI0J4EJRv4INosGpQJ5pcpF95OTiK8SAcOGk5A65Cv57sasbeKbr3TltDFV1qTHhSomJo8WsnHEVdwaSSfbP9SEJdlJDXSWeoXw4YhEayWSvRJGFHIVX6VEvE4WSPODfENNpKsvkbxeK9PJR2WLPg3qpNBfu05CcY9QDAaqHd695pUOmX9u4u4ZOVpLMclitKlNSBNDeDKTgsHuY5idMoQccC9UMFoTmI2lVY8lhEaS13BDHN08yqLkvOKrHAgkyefHXCGdYJPHbNy4mdFW8TM7woVtPIBKl69Fb8zZTGyTr6oGCSVvLSiEEhXo8KC6pU3ALoN122gQrPFI",
                          "DclMXABZM3TR0zEuLUjYbWZXRZU8X2xKMZXGFNGZxyYxLrj2rLzafF5iDbIuk5BCj0vo7olKuiU8fi5M032ypj0Oiw4jrivWTMgGelZ0FXnCCtPDQK6PjPc1NTWBeSCjkpxPxKPgWRfLGKYrkVe9xl15byyjRmLZI3g7MjUmFAKIPIbWVs0w5bgJxs5u9urQ7BUz8LX1Qiv9ppYNfSmiBeAtwI7Mabfm4NtK3MLnquEQieOSaRYA5zaP5oagVx96EtEo2lL00tssfBfWZ3L9Gv00GUmRqJG5SfoNRDNdveXWvWMt9AOovCx45hWFTTwjs10vgyCB3dLG8FwvckD9ugHbZjjHoNZglw2RUgWxjO79dSe4ybMLHXhwIr4VKvXDJ8nn4YRm4PQUSMSbCugS79Ew8ApU85WVAWptNpQdo4KECJwMsaTZpn5LaVz5yzKfTltENlpdxldLdMxdCjddMZQ1fT3Pbmqz6d7IPJEtD1U5g9k864jFlQ9vR2xqthsiIjaLzKTrT4hgZ4NXvWyMDPEdifGGdTzcHNtTBpWtjWfLv3sZXOf4K2mSXhAJWFbmFoOZs29DmhLu98MhcDYwVleGhzCVp3bWdb7GXB60oLZnzgNS5uLGhyABr64ktyLSftDtGHStFF0UtvWfieMm5ocRQ7fMG6jWngGs2DIfHA0j7DMLIMZjFPPsMTGlQRGog6frrkvDoSTsZhBgONeAYdIlFpGxzvhXkIQ3yhGnQVV",
                          "Xc92PRPpgrfLVIxOllZlII480SukhP1GbmcHODoId05fo2xMXpAIwoVu15oOHOxDcmmzKSJXJg61vz7W5GaJd",
                          "7APGpq5jhGkx2SoKcB0DYza3mWlvOXBymgCSjRLroNjjXE4HJT0",
                          "rxkHc1RUfw6FKKHiTXSgLiNVA1sKkQL7d8BLoAvbFepQV1yBeB1IypWfqYZPRJ8PZnWw2jVREQn3N2nb48jzBkJ5EF48MSjCkLbs8jdUaboYn0TP90zeXWQxMTa9xgCfryg6EBmzW8iaYBZIuXCR3ygfxnuHcccUrA8OZTBQArh5HTljftDeRZn",
                          "JS1LJHqi3oQGyIXZL6fHTCOZsxSC3XK0v4O5Gsr6m4RHsrQkJHk8Sa7Pvk8QuJWrzQ8Mj72UVAc8GBlobqIsMmJ26fKi0yLZOGhkSUUZDdcqaiHWsiUgL0Xd77dzULc7pCBhE7hdEmNrRuo7Z8R0mGCEjrdyu6MxGLmtrxYpTQ99Duook9WhTj1fKmdLOXBSQCBU8EW8Nzcpy2lU1iVpNIhyyAO4R9XenO2BMOqbp7LvFSWCegWQlxxfYmacSrvG54RAn45aYxVIbEpf3qYEkZ29eOVLtboz44qM3H4Qf1VlSCJhdFpLmVJsAXvND1PBM3dA7bAYcdaYprfC8uqwZY1ldEVK1YHaxX21HVQumbf72Mvz8QE1O8xpiOPnND9z41usULczLSG0CQbUhN1JbZjcyZmUKDzdxdCd73Vm9aoIM9e16uvyTZ2YsrQwqja2f1pnuoGJEqzm04CBLcp0TxJLqyqk5sEMMNYMhXyorHXlk7BoASC5qrYSPSJJACuYH6nVBJfL8Y3a0g0q7tKAutGmCaxiUdTrJWtjsRkYcQRoADIQScTYtJOZ7WpJSjjCQ2nEQurFwMfnUFpJaXDSxLiH1Zd3kgxWEFoBKGzfJ3sYMztcQRu7KXKHCwK4tkJM5YEB2g4YM4c0TRPnvAuK2W6Dw1nB67P5FVF0r3CKft0YOabXkyV86SC8x8YRbGxRdBl1A6hYO7ficTXXNQUHoHGe4hG6G0y9Kmt9p0Kbpv39BpwMgBet7QItvWOZN4y58cN5YNAoXrTdmYD5ZfXGut",
                          "clHQH98RshAt8XRXeffwv3fwXsfi8fEQOMiCAU3iQzZhp0FXTewnvzhqbDm",
                          "KJ0Vqm29G8mFWVkLwzz9XTTXZ1xePYQI2VwfPMNp8O8MV6D9Letn93V7QFJUaUaRcZ9cKIf5DUXMU36vZisWec2L7UKtOcvQZKRMfx6T6BbuI95dCRRuYWXo4weJemNlQhBxThZiSzgpwsUACrAw9VgH248H80SUwwFJ2Bkg6n6mqhhPD8SI6nnIS7TeDLwhIlYCQnOjzLdIKtX1rQStlKkHUMpZMnl57fkNzwinv9ro7kTVbh0mXTj9QZXc18ZziFjUfbrxmISQQIiyTnoH2lz7iF0PkmENOfpnQQSSXZTx6waz2hQYUmmXMFVXdQLDOfok18o1tDo8EV3p7oefElgDJwNb9rFN4PNdUC1n0GI078fvnSFPgDWkZC8ZW7lQfTFEoMAmKnuLW9lPBuxLMF4AIHiun3k564zOZcfjkrhsABwvIGbkfPUEOgD5JHaJNCATnJgIRltbNwkx1CHgi2g3NFg1vEGimQPpxDh11j2hxTlqXGnb2F5rHdEOK3dQBb7yONtzNVd0uh4tf3G7lZK7DzBf2OnxOv6U0WvdLmyzxNmnxFTykjgkyHTPBljNPDXYu1tae6HEKFUyxo2gPEeNVnqiW45xswgL6sTyLuKIKMbUtHuwjrcqcLPxGbKZiCJFRR9e94oosd0WXVG7QJNSonzdaqjJUr942mn13ZsT",
                          "ArLTtuIydQanMD6u1Nr3iHmxjsKx3jQKRQAzVT1W9Ite8qJ4DGrbnoSsXTysfC05WqMbmpRQnf0U4jUurJ6rhQFl2k2YogTQhLGKAm7vv98y1tfAb3RBYVTOUHsC71utT0iQIL1pFMZa8jm8gvSCK1k6d221aDHBfhPAGpVHvwBS1n70spmvIJNYHXOsIXcU7l6K7mYREzHinAwV9f8mdFzpr75rIChor8Ul4KIECm5KnL6avDR3w71u0OhCecibzRqgsHoHeFoEfHabNIClqpErZ5NRj85L0RJymeiucT7fTbFA7sYdpKHrCO3lbvNpawoYkGF5iHK3Ig0sTqOp1AmhhNkews5pvv7SZeGRZxMdTMbS7APGpq5jhGkx2SoKcB0DYza3mWlvOXBymgCSjRLroNjjXE4HJT0GKruOwQwaeekdxGqdE4rCcv8q3Ynxkki7QgR718Ytbq2P5Nqynsn0sBeCHniDZ0c8hiQ0QXaXS66Pvc6IRWbKM7TnzIX",
                          "R3w71u0OhCecibzRqgsHoHeFoEfHabNIClqpErZ5NRj85L0RJymeiucT7fTbFA7sYdpKHrCO3lbvNpawoYkGF5iHK3Ig0sTqOp1AmhhNkews5pvv7SZeGRZxMdTMbS7APGpq5jhGkx2SoKcB0DYza3mWlvOXBymgCSjRLroNjjXE4HJT0GKruOwQwaeekdxGqdE4rCcv8q3Ynxkki7QgR71",
                          "z7wKRSVIkZbFzheaTjLiRj9xRyNXDXNyTSYgrJXTMmoKODCkTwYqq13fGb97OCeAAtInL7hp9zxQp7WMHDltRQO1hnxxNeN121MSOJ0eSCU529CnowOehMom5zCRNtjRF6efTyC8xvLLY3NfsFbkj6frQrluPEIgmFuUtcd8tZOW91XakeQLsHwNH0vvFho6IwbgBaPSrt4Ncey7Fc6E8kDgidAoY5X9zIflhKvRxwQHFcdIVFWT12Rn0jhK69rHWEtVT6XJz8zUKCMUvFzpHIqLQS4VRVjvnRAht9KBDdOXdNvCSKuuo7iTur3vL8FOBT7Xdl5zkUKoc0sIjl4Bca5GYE0nt2H07KL7H5CzuGX",
                          "DR3w71u0OhCecibzRqgsHoHeFoEfHabNIClqpErZ5NRj85L0RJymeiucT7fTbFA7sYdpKHrCO3lbvNpawoYkGF5iHK3Ig0sTqOp1AmhhNkews5pvv7SZeGRZxMdTMbS7APGpq5jhGkx2SoKcB0DYza3mWlvOXBymgCSjRLroNjjXE4HJT0GKruOwQwaeekdxGqdE4rCcv8q3Ynxkki7QgR718Ytbq2P5Nqynsn0sBeCHniDZ0c8hiQ0QXaXS66Pvc6IRWbKM7TnzIX9t6lmSrSlOLfb4cDrZQNgPapasgt2Qc68rLRsavGQZfqEXvK7jplaJHz7a",
                          "80Ly2NON8N1bFUvvBvzADvU7QwCKRYVYluSwyZvOlkksycfmPrpNmzWDIBwsA3o9UBcDqeQ3p3WXfpjStdqx0i9J0nS3EnaqbwIDA8JZ6a5064fLNDd5uN5yf4IMgOWQaSlkAx4UqROOKNWsquXA9bSLXhUNO8pA2zNW8fmOeKPxWJKLwGeBcLNkVUozE9IScrV4hofri22tdHZc0USGIq9vFOaVQWUHlWx4BNcxQ02wPGkxY56PwcRoP9DVz530FonFBcXU8yNp",
                          "hWMjOGjZe42PJbGdAivXwXIvDBEaaHdKTFGSiJZixxGM6qcuT1ltne0QltsmaPZk0UXlAhVUF3e631k42y7EbfPY2KSExbRv4jPZfekX2SJf7UU5WloCYuX9LHgcarCEMgRD4slkvCwfPnKcBolYqHCwNwkpdaODzISx6gpeSJ3I43A6KILcosEhiIXJ8iXJvEJRU29HCx6Hb52bhwggwLJuB0gBvOsmHC7LV18x5jP6hW1xZ1eWQELLRbAD8b09qpFVYsmQNwyHUZ6OAdsUEslSiKIdxc2s9sK8mB7htMuAJhx1P16EWWQXuyqypW5oOELhWCYe1KGFqpqtBBZQmAMzHf4j0aYtejZrCoxseUKoO9yz4QWbn8oVEPq26RPfkeWPVvog8MqeEFw04csosFrxVQfvKcYy2WxDX7ME64FqZ6MfAdxuKM2Kep",
                          "j5mgVbmi81t5GUG01o5BMi1kOGQ5JP2rMpUE75IirHhlEWovMdYoQmBxgaVmcixMBlzLSeMhIrez3ZA8rgGrPF2bBXGzi0AkxHm7MsQHcI1NB9mF5jOJJX7LtJSOYdwu9fhUQYfjJF0qjWohcRCb6oDKiSb1tj4CZHbivY1i48ZcM32bfgYtfftLGm67lFl7ikyDvPzg5XSiPFtUdQ1RPSXlmxhH2pKpc6GgW6Mec7Et20Y0ly1IdzDyQY1iuHtV6THc1PESLlNwO5M4PySfM8jZyvtdn6IAAYngFbYKhuqIThOHnYcVkFHyIWIuj7WcccCv1NnVbhCQln3n1ffojLYLWoSS1yhDTs8RyAN6EtIk9I70nzuRlU5DWqcTf0tqIQZJw56Swg98U3Y7zDd35323nE7w0iZWUUjxf1Wi6jfJqAdOwPJ8xBI5jxmrs45HPn0eRqhRR8ylCzlb7EiJl3a943HXhiwBUiYO77A53uq1NoQRz57c2GvLqS2Syd0v97fpic6MOTcEvI5ZhcPT5O3vhSfVbqs09LtwCl5eI4PYiM48Kkeja1Io3hfBIPCCUYvUanJEHhuKcFzaMM8o7pZQESZK2nEm3hGx07y2S4h9LjkUAp6aWj4wRS0pvtKgxiJbwDWQlYWKc8YpnrnyjHkE1bWWqo44u9QGqxiyz3E1GEITutAdvUUa1LY0v29nt3gUoNCElQLfx8ML6HwD7rwkTeq98dRhe0kuSOqsh5irk943pFkcQTjC2uIlAfmBHHOidChR111R0Eo5KkDJV1duY2C32N5iCOgfhBmzDkNtdo44zxEAaxo7R2ecO1W6U",
                          "lDsBaybuqTlilSVFq0edt9BI1aAdK2mqSWEgGeQ7RN3tvEC6hnuhASMJ2uYVDIx3McyWPWms2bAdjMSmRckiloJHgGmHHk87CObE4MOwRFH5iGT1ELMdefyZDdM1szOix0EjU7tRe7L4moq6T3ijc9rv8WQ7XYlEMTM5te8PG5FcwB51hNq3ZwTEzhRwf7kJdxBW5YC5tpvV6zrRxUHzA39nejmUjcXxTuyWTX8NlnkqdABy9lF0NQsDur6F4OKohHtLR9K8nLxnGji52ZLCY50QVPc7RtXSz42fx2gKhdhdELBy6XyurzuEnbsMl2ODbsYRn4zTM98YCQAzRwMU5DreMIH2ErPOGehQj6gijV55r0PwAZkocmVZMogfGdIGvBcKIxGDPaXpsRVgI47Kp9AOmH7jkndPceEPF9z8ZrvTvo4wPNt0pl3OTRSxxaNNKOfzVCOfAUUosipApJOgtFNLfWDiVCIIMtzX4E9Jcj6YfEQ6vNTGP2G0iZl2eEXu9LspNNtMaPePQrHqgDw4uel6EAhG9e9MIv17a81PPmLwANAHUskYZ0j0xpwWrVJQ3C51ylgqd3bl9H31kOrulhLvBOVn2hyvPmwvQsX6a6oNLZxL8EhhPfL5GEoDM9pQQDW7EREEfCbm2VbbQpkNXxgMwsc4X8PqAmH8SN738cwC7fTMeEe9eZ8qXibASrwIchzCW2YLcmtKzaXoAXlxjshTf6dOjShgTmGaXbK80tDvjEleUndf9rrRmwFNGfeQG",
                          "AD3Gk7VxKJQh8DGpAlPXrz9lRcEHLjS2ZvuId1z9PVBgBdNDenwDZfNDlqJ7OCQEeapmGeRP1mNnAh55bzzboiLB14b7fCO6B84hGUrTjI26727MVNVPlEuXY9HMPwBG4NJZNCVoVNEOJkyriHh4haJFbiyCL6Tw",
                          "TaRRB0NDe99Rap97uBZykHHg0M0UFfnYw4isT0QVDAa4yxC6mqv7xIw2QPux3WWIA5SbTpTM4OnLlrwB6yir9ypxB3pWDZ2z3bZMa2cmWmrw02ejINHmY4Pfpqtys4JXJ1y4XqCnihJA61GevCZoJ0mIhS1xYZDvQIxzhsE6OvtQ0vGY3SoD8QAeKru1geSdhjq5In2h881qCwe0tzD4M90ne3t0NI6m6nEI957v94HSS35k2QtVgLFkvI2JYXdeDtnFM3jmqh7zoGCkl4uTF7BPVsDyKtd6qoLSrjRQy6cQkayQqTlnJ1BiDRqjLaHVkHZA5YoecFLhR52S9v9oRM8",
                          "ZnWw2jVREQn3N2nb48jzBkJ5EF48MSjCkLbs8jdUaboYn0TP90zeXWQxMTa9xgCfryg6EBmzW8iaYBZIuXCR3ygfxnuHcccUrA8OZTBQArh5HTljftDeRZntphN2VdEaBwOZS7VxsaHYK1i1QEfOArLTtuIydQanMD6u1Nr3iHmxjsKx3jQKRQAzVT1W9Ite8qJ4DGrbnoSsXTysfC05WqMbmpRQnf0U4jUurJ6rhQFl2k2YogTQhLGKAm7vv98y1tfAb3RBYVTOUHsC71utT0iQIL1pFMZa8jm8gvS"},
            "KJCbnHAbiERYGOwa6JERirw7YKlMX5WWXVrsTF<b>OfDGPuzobcs2CIaUCbcpRyFfKUwQPHjRlNZfPEVcZZmjrxkHc1RUfw6FKKHiTXSgLiNVA1sKkQL7d8BLoAvbFepQV1yBeB1IypWfqYZPRJ8PZnWw2jVREQn3N2nb48jzBkJ5EF48MSjCkLbs8jdUaboYn0TP90zeXWQxMTa9xgCfryg6EBmzW8iaYBZIuXCR3ygfxnuHcccUrA8OZTBQArh5HTljftDeRZntphN2VdEaBwOZS7VxsaHYK1i1QEfOArLTtuIydQanMD6u1Nr3iHmxjsKx3jQKRQAzVT1W9Ite8qJ4DGrbnoSsXTysfC05WqMbmpRQnf0U4jUurJ6rhQFl2k2YogTQhLGKAm7vv98y1tfAb3RBYVTOUHsC71utT0iQIL1pFMZa8jm8gvSCK1k6d221aDHBfhPAGpVHvwBS1n70spmvIJNYHXOsIXcU7l6K7mYREzHinAwV9f8mdFzpr75rIChor8Ul4KIECm5KnL6avDR3w71u0OhCecibzRqgsHoHeFoEfHabNIClqpErZ5NRj85L0RJymeiucT7fTbFA7sYdpKHrCO3lbvNpawoYkGF5iHK3Ig0sTqOp1AmhhNkews5pvv7SZeGRZxMdTMbS7APGpq5jhGkx2SoKcB0DYza3mWlvOXBymgCSjRLroNjjXE4HJT0GKruOwQwaeekdxGqdE4rCcv8q3Ynxkki7QgR718Ytbq2P5Nqynsn0sBeCHniDZ0c8hiQ0QXaXS66Pvc6IRWbKM7TnzIX9t6lmSrSlOLfb4cDrZQNgPapasgt2Qc68rLRsavGQZfqEXvK7jplaJHz7a3acjgpimRqclHQH98RshAt8XRXeffwv3fwXsfi8fEQOMiCAU3iQzZhp0FXTewnvzhqbDm</b>");
        test(
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
            new String[] {"a", "aa", "aaa", "aaaa", "aaaaa", "aaaaaa", "aaaaaaa", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"},
            "<b>aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa</b>");
        test(
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
            new String[] {"a", "aa", "aaa", "aaaa", "aaaaa", "aaaaaa", "aaaaaaa"},
            "<b>aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa</b>");
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
