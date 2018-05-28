import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC843: https://leetcode.com/problems/guess-the-word/
//
// We are given a word list of unique words, each word is 6 letters long, and
// one word in this list is chosen as secret. You may call master.guess(word) to
// guess a word. The guessed word should have type string and must be from the
// original list with 6 lowercase letters. The function returns an integer type,
// representing the number of exact matches(value and position) of your guess to
// the secret word. Also, if your guess is not in the given wordlist, it will
// return -1 instead. For each test case, you have 10 guesses to guess the word.
// At the end of any number of calls, if you have made 10 or less calls to
// master.guess and at least one of these guesses was the secret, you pass.
// Besides the example test case, there will be 5 additional test cases, each
// with 100 words in the word list. The letters of each word in those testcases
// were chosen independently at random from 'a' to 'z', such that every word in
// the given word lists is unique.
public class FindSecretWord {
    // HashSet
    // beats %(4 ms for 6 tests) (may fail)
    public void findSecretWord(String[] wordlist, Master master) {
        Set<String> cand = new HashSet<>();
        for (String word : wordlist) {
            cand.add(word);
        }
        for (int maxMatch = wordlist[0].length(); ;) {
            Iterator<String> itr = cand.iterator();
            String target = itr.next();
            int res = master.guess(target);
            if (res == maxMatch) return;

            for (itr.remove(); itr.hasNext(); ) {
                if (match(target, itr.next()) != res) {
                    itr.remove();
                }
            }
        }
    }

    private int match(String a, String b) {
        int res = 0;
        for (int i = a.length() - 1; i >= 0; i--) {
            res += (a.charAt(i) == b.charAt(i)) ? 1 : 0;
        }
        return res;
    }

    // Minimax with Heuristic
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // beats %(27 ms for 6 tests)
    public void findSecretWord2(String[] wordlist, Master master) {
        int n = wordlist.length;
        int[][] matches = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int match = 0;
                for (int k = 0; k < 6; k++) {
                    if (wordlist[i].charAt(k) == wordlist[j].charAt(k)) {
                        match++;
                    }
                }
                matches[i][j] = matches[j][i] = match;
            }
        }
        List<Integer> cand = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            cand.add(i);
        }
        Set<Integer> tried = new HashSet<>();
        while (!cand.isEmpty()) {
            int guess = pick(cand, tried, matches);
            int res = master.guess(wordlist[guess]);
            if (res == wordlist[0].length()) return;

            List<Integer> cand2 = new ArrayList<>();
            for (int i : cand) {
                if (matches[guess][i] == res) {
                    cand2.add(i);
                }
            }
            cand = cand2;
            tried.add(guess);
        }
    }

    private int pick(List<Integer> cand, Set<Integer> tried, int[][] matches) {
        // The following will fail on test case 2(11>10)
        // if (true) return cand.get(0);
        if (cand.size() <= 2) return cand.get(0);

        List<Integer> group = cand;
        int res = -1;
        for (int i : cand) {
        // for (int i = 0; i < matches.length; i++) {
            // if (tried.contains(i)) continue;
            @SuppressWarnings("unchecked")
            List<Integer>[] groups = new ArrayList[7];
            for (int j = groups.length - 1; j >= 0; j--) {
                groups[j] = new ArrayList<>();
            }
            for (Integer j : cand) {
                groups[matches[i][j]].add(j);
            }
            List<Integer> maxGroup = groups[0];
            for (List<Integer> grp : groups) {
                if (grp.size() > maxGroup.size()) {
                    maxGroup = grp;
                }
            }
            if (maxGroup.size() < group.size()) {
                group = maxGroup;
                res = i;
            }
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(N ^ 2)
    // beats %(3 ms for 6 tests) (may fail)
    public void findSecretWord3(String[] wordlist, Master master) {
        for (int maxMatch = wordlist[0].length(); ;) {
            String guess = wordlist[new Random().nextInt(wordlist.length)];
            int res = master.guess(guess);
            if (res == maxMatch) return;

            List<String> wordlist2 = new ArrayList<>();
            for (String w : wordlist) {
                if (match(guess, w) == res) {
                    wordlist2.add(w);
                }
            }
            wordlist = wordlist2.toArray(new String[wordlist2.size()]);
        }
    }

    // Minimax with Heuristic + Hash Table
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // beats %(17 ms for 6 tests)
    public void findSecretWord4(String[] wordlist, Master master) {
        for (int maxMatch = wordlist[0].length(); ;) {
            Map<String, Integer> diffs = new HashMap<>();
            for (String w1 : wordlist) {
                for (String w2 : wordlist) {
                    if (match(w1, w2) == 0) {
                        diffs.put(w1, diffs.getOrDefault(w1 , 0) + 1);
                    }
                }
            }
            String minWord = null;
            int minDiff = Integer.MAX_VALUE;
            for (String w : wordlist) {
                int diff = diffs.getOrDefault(w, 0);
                if (diff < minDiff) {
                    minWord = w;
                    minDiff = diff;
                }
            }
            int res = master.guess(minWord);
            if (res == maxMatch) return;

            List<String> wordlist2 = new ArrayList<>();
            for (String w : wordlist) {
                if (match(minWord, w) == res) {
                    wordlist2.add(w);
                }
            }
            wordlist = wordlist2.toArray(new String[0]);
        }
    }

    class Master {
        String[] wordlist;
        String secret;
        int calls;

        Master(String[] wordlist, String secret) {
            this.wordlist = wordlist;
            this.secret = secret;
        }

        public int guess(String word) {
            int res = -1;
            for (String w : wordlist) {
                if (w.equals(word)) {
                    res = 0;
                    break;
                }
            }
            calls++;
            return (res >= 0) ? match(word, secret) : -1;
        }

        public boolean pass() {
            // System.out.println(calls);
            return calls <= 10;
        }

        public void reset() {
            calls = 0;
        }
    }

    void test(String[] wordlist, String secret) {
        Master master = new Master(wordlist, secret);
        findSecretWord(wordlist, master);
        assertTrue(master.pass());

        master.reset();
        findSecretWord2(wordlist, master);
        assertTrue(master.pass());

        master.reset();
        findSecretWord3(wordlist, master);
        assertTrue(master.pass());

        master.reset();
        findSecretWord4(wordlist, master);
        assertTrue(master.pass());
    }

    @Test
    public void test() {
        test(new String[] { "acckzz", "ccbazz", "eiowzz", "abcczz" }, "acckzz");
        test(new String[] { "wichbx", "oahwep", "tpulot", "eqznzs", "vvmplb",
                            "eywinm", "dqefpt", "kmjmxr", "ihkovg",
                            "trbzyb", "xqulhc", "bcsbfw", "rwzslk", "abpjhw",
                            "mpubps", "viyzbc", "kodlta", "ckfzjh", "phuepp",
                            "rokoro", "nxcwmo", "awvqlr", "uooeon", "hhfuzz",
                            "sajxgr", "oxgaix", "fnugyu", "lkxwru", "mhtrvb",
                            "xxonmg", "tqxlbr", "euxtzg", "tjwvad", "uslult",
                            "rtjosi", "hsygda", "vyuica", "mbnagm", "uinqur",
                            "pikenp", "szgupv", "qpxmsw", "vunxdn", "jahhfn",
                            "kmbeok", "biywow", "yvgwho", "hwzodo", "loffxk",
                            "xavzqd", "vwzpfe", "uairjw", "itufkt", "kaklud",
                            "jjinfa", "kqbttl", "zocgux", "ucwjig", "meesxb",
                            "uysfyc", "kdfvtw", "vizxrv", "rpbdjh", "wynohw",
                            "lhqxvx", "kaadty", "dxxwut", "vjtskm", "yrdswc",
                            "byzjxm", "jeomdc", "saevda", "himevi", "ydltnu",
                            "wrrpoc", "khuopg", "ooxarg", "vcvfry", "thaawc",
                            "bssybb", "ccoyyo", "ajcwbj", "arwfnl", "nafmtm",
                            "xoaumd", "vbejda", "kaefne", "swcrkh", "reeyhj",
                            "vmcwaf", "chxitv", "qkwjna", "vklpkp", "xfnayl",
                            "ktgmfn", "xrmzzm", "fgtuki", "zcffuv", "srxuus",
                            "pydgmq" }, "ccoyyo");
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
