import org.junit.Test;

import static org.junit.Assert.*;

// LC1717: https://leetcode.com/problems/maximum-score-from-removing-substrings/
//
// You are given a string s and two integers x and y. You can perform two types of operations any
// number of times.
// Remove substring "ab" and gain x points.
// For example, when removing "ab" from "cabxbae" it becomes "cxbae".
// Remove substring "ba" and gain y points.
// For example, when removing "ba" from "cabxbae" it becomes "cabxe".
// Return the maximum points you can gain after applying the above operations on s.
//
// Constraints:
// 1 <= s.length <= 105
// 1 <= x, y <= 104
// s consists of lowercase English letters.
public class MaximumGain {
    // Greedy
    // time complexity: O(N), space complexity: O(1)
    // 28 ms(94.68%), 40.3 MB(83.64%) for 76 tests
    public int maximumGain(String s, int x, int y) {
        int res = 0;
        final String seq = (x > y) ? "ab" : "ba";
        int[] count = new int[2];
        int max = Math.max(x, y);
        int min = Math.min(x, y);
        for (int i = 0, len = s.length(); i <= len; i++) {
            char c = (i == len) ? ' ' : s.charAt(i);
            int index = seq.indexOf(c);
            if (index < 0) { // clear
                res += Math.min(count[0], count[1]) * min;
                count[0] = count[1] = 0;
            } else if (index > 0 && count[0] > 0) { // greedily consume
                count[0]--;
                res += max;
            } else {
                count[index]++;
            }
        }
        return res;
    }

    // Greedy
    // Time Limit Exceeded
    public int maximumGain2(String s, int x, int y) {
        final String[] seq = new String[] {"ab", "ba"};
        final int[] score = new int[] {x, y};
        StringBuilder sb = new StringBuilder(s);
        int i = x > y ? 0 : 1;
        return replace(sb, seq[i], score[i]) + replace(sb, seq[1 - i], score[1 - i]);
    }

    private int replace(StringBuilder sb, String seq, int score) {
        for (int res = 0; ; ) {
            int index = sb.indexOf(seq);
            if (index < 0) { return res; }

            res += score;
            sb.delete(index, index + 2);
        }
    }

    private void test(String s, int x, int y, int expected) {
        assertEquals(expected, maximumGain(s, x, y));
        assertEquals(expected, maximumGain2(s, x, y));
    }

    @Test public void test() {
        test("cdbcbbaaabab", 4, 5, 19);
        test("aabbaaxybbaabb", 5, 4, 20);
        test("tababpbsbdbbbaabaabpaaubnaabiajxbbbaugbabaabbbabobabewaabbibaaxbbabbabababbaabobabbaaacbaqbbbbabbbababbbbaabaaaavbabbbnbbmxbbaababbaaslcgabdbaaabbamaaaaaaababaabbbbbxbbabusbabadabbbkacgbzabfapbaxbjabbbaaadbbahbhwoarbufabbbbykabvbafbabbdzxbabahbaabaabwabbbwjbhbbbdblaaaabbbabbabababebbraraaabbaaaaabababebfnbdraabmbdbebbabbpbvbbbadabbaaaxbbffbabanovbabdabbabckiibaabbabbfbbbbzabxaaaauaaabcabbaefbaabaapbbzabxabagababaaeahnadnkyxbaarbkmdbbauuabsaalbabtpbapabbababaaaabbabababfbfbbbbabaaahaacabababasabaaskbabbaoawbahcabsbsanbckopjqbaqbbacbxbebbakbiaebaabambbaabbabaaabebaatbubbgbaabapparanlbbaabbnbanfbapbaabfbdcaxbfbbbbaaawaabaqaaabvabbbgaanaabbbabgaaagbaabbahpbbaakbbabaabahbbsbbbaebbbaabalbbhbhaaybbbbbpbatbaaaqsbaaabbhbgbbabtqbbaabbbaaxaabbbbaapbaqaagaacajajcbbababbbbwbaatabbbyaaaaaboabaabbbabsbbbabbzfafbabbaababbbaablabkaiaaeaaaaaaabbaarahabbbqbbbabbbtaaaecbambbaabvajbkuaaacbiaihbbaaealaabbdababbbaabobbbaabaaabaazbhebbaaabybarwbvbycoaabanbabbsbaababbbamyabbbbaaaobybweabbibababababaabrbsvaaebaaabardpacaakbaravbabaaaaagwagababksstyvaahycbauxbaabababbebbabbamabvnanaabnaaabacbbbeababbatubbbababcavvbbaaabbaaablbbcbbzabfaababaafofbabbbaaabeababatbababbndabbboaaabbabaaaqaawabadbbboaaaabaziabbawlabbtbzfabaqaabbabzaabbakbbbmbrhdialbmdpeabaaaabbaabpbaabawbwbbbabbabbbbabbablabaiaabattaabpbbbfbaaaebbaaabacanbaabjabaaaiacpbaabbbabaqbaaoaabaaablaaacajbbbbaaabmaaaranbabvbkabaaabaibxaaaqtaambbaacmabfaybbbavmtbbbaafaarayaarbvbjbazabbtabbbbaawwbbbalbbbaaatbbbabqagrabayboabababfbbbcbaabbbbbbpbrbbtrbbababoabbbbaocaajaaaabaaabaaabbaaababuazzbraabaaaapbqbarayaaagbabbaabaaasobazbaabgacabavayjaubbabbbaaabeabzbbbabibfbbaaaaaabybbaaxmababhbabaabnbaxbbbbfbaaaabawkjbbaboabsaabbaababbbixhkiabbgbmabaiaabbcaabbabmoubawbmbbbbvdauabyabaabbbbfrwaauaapbmabaacahbwabaxvyurazvarltbaababfxbabp",
             10, 11, 4530);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
