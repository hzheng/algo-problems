import java.util.*;
import java.util.stream.Collectors;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.collection.IsIn.*;

import common.Utils;

// LC1125: https://leetcode.com/problems/smallest-sufficient-team/
//
// In a project, you have a list of required skills req_skills, and a list of people. The i-th
// person people[i] contains a list of skills that person has. Consider a sufficient team: a set of
// people such that for every required skill in req_skills, there is at least one person in the team
// who has that skill. We can represent these teams by the index of each person: for example,
// team = [0, 1, 3] represents the people with skills people[0], people[1], and people[3]. Return
// any sufficient team of the smallest possible size, represented by the index of each person.
// You may return the answer in any order.
//
// Constraints:
//
// 1 <= req_skills.length <= 16
// 1 <= people.length <= 60
// 1 <= people[i].length, req_skills[i].length, people[i][j].length <= 16
// Elements of req_skills and people[i] are (respectively) distinct.
// req_skills[i][j], people[i][j][k] are lowercase English letters.
// Every skill in people[i] is a skill in req_skills.
// It is guaranteed a sufficient team exists.
public class SmallestSufficientTeam {
    // 2-D Dynamic Programming(Bottom-Up) + Bit Manipulation + Hash Table
    // time complexity: O(M^2*2^N), space complexity: O(M*2^N)
    // 375 ms(16.51%), 104.5 MB(5.66%) for 38 tests
    public int[] smallestSufficientTeam(String[] req_skills, List<List<String>> people) {
        int n = req_skills.length;
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0, power = 1; i < n; i++, power <<= 1) {
            map.put(req_skills[i], power);
        }
        int target = (1 << n) - 1;
        int m = people.size();
        int[] persons = new int[m + 1];
        int index = 1;
        for (List<String> skills : people) {
            int val = 0;
            for (String skill : skills) {
                val |= map.get(skill);
            }
            persons[index++] = val;
        }
        int[][] dp = new int[m + 1][target + 1];
        int[][] prev = new int[m + 1][target + 1];
        for (int[] a : dp) {
            Arrays.fill(a, m + 1);
        }
        dp[0][0] = 0;
        int minIndex = -1;
        for (int j = 1, minValue = m + 1; j <= m; j++) {
            for (int i = 0; i < j; i++) {
                for (int t = 0, p = persons[j]; t <= target; t++) {
                    int newVal = t | p;
                    if (newVal != t && dp[j][newVal] > dp[i][t] + 1) {
                        dp[j][newVal] = dp[i][t] + 1;
                        prev[j][newVal] = (i << n) | t;
                    }
                }
            }
            if (dp[j][target] < minValue) {
                minValue = dp[j][target];
                minIndex = j;
            }
        }
        List<Integer> res = new ArrayList<>();
        for (int i = minIndex, t = target; i > 0; ) {
            res.add(i - 1);
            int p = prev[i][t];
            i = p >> n;
            t = p & target;
        }
        return res.stream().mapToInt(Integer::intValue).toArray();
    }

    // 1-D Dynamic Programming(Bottom-Up) + Bit Manipulation + Hash Table
    // time complexity: O(M*2^N), space complexity: O(2^N)
    // 20 ms(73.58%), 42.2 MB(47.17%) for 38 tests
    public int[] smallestSufficientTeam2(String[] req_skills, List<List<String>> people) {
        int n = req_skills.length;
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            map.put(req_skills[i], i);
        }
        List<Integer>[] dp = new List[1 << n];
        dp[0] = new ArrayList<>();
        for (int i = 0, m = people.size(); i < m; i++) {
            int skills = 0;
            for (String skill : people.get(i)) {
                skills |= 1 << (map.get(skill));
            }
            for (int j = 0; j < dp.length; j++) {
                if (dp[j] == null) { continue; }

                int newSkill = j | skills;
                if (newSkill == j) { continue; }

                List<Integer> team = dp[j];
                if (dp[newSkill] == null || dp[newSkill].size() > team.size() + 1) {
                    List<Integer> newTeam = new ArrayList<>(team);
                    newTeam.add(i);
                    dp[newSkill] = newTeam;
                }
            }
        }
        return dp[dp.length - 1].stream().mapToInt(Integer::intValue).toArray();
    }

    // 1-D Dynamic Programming(Bottom-Up) + Bit Manipulation + Hash Table
    // time complexity: O(M*2^N), space complexity: O(2^N)
    // 15 ms(82.08%), 39 MB(80.66%) for 38 tests
    public int[] smallestSufficientTeam3(String[] req_skills, List<List<String>> people) {
        int n = req_skills.length;
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            map.put(req_skills[i], i);
        }
        int[][] dp = new int[1 << n][];
        dp[0] = new int[] {0, -1, -1}; // count, cur, prev
        for (int i = 0, m = people.size(); i < m; i++) {
            List<String> person = people.get(i);
            int skills = 0;
            for (String skill : person) {
                skills |= 1 << (map.get(skill));
            }
            for (int j = 0; j < dp.length; j++) {
                if (dp[j] == null) { continue; }

                int newSkill = j | skills;
                if (newSkill != j && (dp[newSkill] == null || dp[newSkill][0] > dp[j][0] + 1)) {
                    dp[newSkill] = new int[] {dp[j][0] + 1, i, j};
                }
            }
        }
        List<Integer> res = new ArrayList<>();
        for (int[] value = dp[dp.length - 1]; value[1] >= 0; ) {
            res.add(value[1]);
            value = dp[value[2]];
        }
        return res.stream().mapToInt(Integer::intValue).toArray();
    }

    // Dynamic Programming(Top-Down) + BackTracking + Bit Manipulation + Hash Table
    // time complexity: O(M*2^N), space complexity: O(2^N)
    // 8 ms(87.74%), 38.1 MB(83.49%) for 38 tests
    public int[] smallestSufficientTeam4(String[] req_skills, List<List<String>> people) {
        Map<String, Integer> map = new HashMap<>();
        int n = 0;
        for (String skill : req_skills) {
            map.put(skill, n++);
        }
        int[] persons = new int[people.size()];
        for (int i = persons.length - 1; i >= 0; i--) {
            for (String skill : people.get(i)) {
                persons[i] |= 1 << map.get(skill);
            }
        }
        List<Integer> res = new ArrayList<>();
        dfs(res, 0, persons, new ArrayList<>(), (1 << n) - 1);
        return res.stream().mapToInt(Integer::intValue).toArray();
    }

    private void dfs(List<Integer> res, int cur, int[] persons, List<Integer> team, int target) {
        int size = res.isEmpty() ? Integer.MAX_VALUE : res.size();
        if (cur == target) {
            if (team.size() < size) {
                res.clear();
                res.addAll(team);
            }
            return;
        }
        if (team.size() >= size) { return; }

        int last0bit = 0;
        for (; ((cur >> last0bit) & 1) == 1; last0bit++) {}
        for (int i = 0; i < persons.length; i++) {
            int skills = persons[i];
            if (((skills >> last0bit) & 1) != 0) { // avoid duplicate than (cur | skill) != cur
                team.add(i);
                dfs(res, cur | skills, persons, team, target);
                team.remove(team.size() - 1);
            }
        }
    }

    @FunctionalInterface interface Function<A, B, C> {
        C apply(A a, B b);
    }

    private void test(Function<String[], List<List<String>>, int[]> smallestSufficientTeam,
                      String[] req_skills, List<List<String>> people, int[]... expected) {
        int[] res = smallestSufficientTeam.apply(req_skills, people);
        Arrays.sort(res);
        List<Integer> resList = Arrays.stream(res).boxed().collect(Collectors.toList());
        List<List<Integer>> expectedList = new ArrayList<>();
        for (int[] e : expected) {
            expectedList.add(Arrays.stream(e).boxed().collect(Collectors.toList()));
        }
        assertThat(resList, in(expectedList));
    }

    private void test(String[] req_skills, String[][] persons, int[]... expected) {
        SmallestSufficientTeam s = new SmallestSufficientTeam();
        List<List<String>> people = Utils.toList(persons);
        test(s::smallestSufficientTeam, req_skills, people, expected);
        test(s::smallestSufficientTeam2, req_skills, people, expected);
        test(s::smallestSufficientTeam3, req_skills, people, expected);
        test(s::smallestSufficientTeam4, req_skills, people, expected);
    }

    @Test public void test() {
        test(new String[] {"java", "nodejs", "reactjs"},
             new String[][] {{"java"}, {"nodejs"}, {"nodejs", "reactjs"}}, new int[] {0, 2});
        test(new String[] {"algorithms", "math", "java", "reactjs", "csharp", "aws"},
             new String[][] {{"algorithms", "math", "java"}, {"algorithms", "math", "reactjs"},
                             {"java", "csharp", "aws"}, {"reactjs", "csharp"}, {"csharp", "math"},
                             {"aws", "java"}}, new int[] {1, 2});
        test(new String[] {"hfkbcrslcdjq", "jmhobexvmmlyyzk", "fjubadocdwaygs", "peaqbonzgl",
                           "brgjopmm", "x", "mf", "pcfpppaxsxtpixd", "ccwfthnjt", "xtadkauiqwravo",
                           "zezdb", "a", "rahimgtlopffbwdg", "ulqocaijhezwfr", "zshbwqdhx",
                           "hyxnrujrqykzhizm"},
             new String[][] {{"peaqbonzgl", "xtadkauiqwravo"},
                             {"peaqbonzgl", "pcfpppaxsxtpixd", "zshbwqdhx"}, {"x", "a"}, {"a"},
                             {"jmhobexvmmlyyzk", "fjubadocdwaygs", "xtadkauiqwravo", "zshbwqdhx"},
                             {"fjubadocdwaygs", "x", "zshbwqdhx"}, {"x", "xtadkauiqwravo"},
                             {"x", "hyxnrujrqykzhizm"}, {"peaqbonzgl", "x", "pcfpppaxsxtpixd", "a"},
                             {"peaqbonzgl", "pcfpppaxsxtpixd"}, {"a"}, {"hyxnrujrqykzhizm"},
                             {"jmhobexvmmlyyzk"},
                             {"hfkbcrslcdjq", "xtadkauiqwravo", "a", "zshbwqdhx"},
                             {"peaqbonzgl", "mf", "a", "rahimgtlopffbwdg", "zshbwqdhx"},
                             {"xtadkauiqwravo"}, {"fjubadocdwaygs"},
                             {"x", "a", "ulqocaijhezwfr", "zshbwqdhx"}, {"peaqbonzgl"},
                             {"pcfpppaxsxtpixd", "ulqocaijhezwfr", "hyxnrujrqykzhizm"},
                             {"a", "ulqocaijhezwfr", "hyxnrujrqykzhizm"},
                             {"a", "rahimgtlopffbwdg"}, {"zshbwqdhx"},
                             {"fjubadocdwaygs", "peaqbonzgl", "brgjopmm", "x"},
                             {"hyxnrujrqykzhizm"}, {"jmhobexvmmlyyzk", "a", "ulqocaijhezwfr"},
                             {"peaqbonzgl", "x", "a", "ulqocaijhezwfr", "zshbwqdhx"},
                             {"mf", "pcfpppaxsxtpixd"}, {"fjubadocdwaygs", "ulqocaijhezwfr"},
                             {"fjubadocdwaygs", "x", "a"}, {"zezdb", "hyxnrujrqykzhizm"},
                             {"ccwfthnjt", "a"}, {"fjubadocdwaygs", "zezdb", "a"}, {},
                             {"peaqbonzgl", "ccwfthnjt", "hyxnrujrqykzhizm"},
                             {"xtadkauiqwravo", "hyxnrujrqykzhizm"}, {"peaqbonzgl", "a"},
                             {"x", "a", "hyxnrujrqykzhizm"}, {"zshbwqdhx"}, {},
                             {"fjubadocdwaygs", "mf", "pcfpppaxsxtpixd", "zshbwqdhx"},
                             {"pcfpppaxsxtpixd", "a", "zshbwqdhx"}, {"peaqbonzgl"},
                             {"peaqbonzgl", "x", "ulqocaijhezwfr"}, {"ulqocaijhezwfr"}, {"x"},
                             {"fjubadocdwaygs", "peaqbonzgl"}, {"fjubadocdwaygs", "xtadkauiqwravo"},
                             {"pcfpppaxsxtpixd", "zshbwqdhx"},
                             {"peaqbonzgl", "brgjopmm", "pcfpppaxsxtpixd", "a"},
                             {"fjubadocdwaygs", "x", "mf", "ulqocaijhezwfr"},
                             {"jmhobexvmmlyyzk", "brgjopmm", "rahimgtlopffbwdg",
                              "hyxnrujrqykzhizm"},
                             {"x", "ccwfthnjt", "hyxnrujrqykzhizm"},
                             {"hyxnrujrqykzhizm"},
                             {"peaqbonzgl", "x", "xtadkauiqwravo", "ulqocaijhezwfr",
                              "hyxnrujrqykzhizm"},
                             {"brgjopmm", "ulqocaijhezwfr", "zshbwqdhx"},
                             {"peaqbonzgl", "pcfpppaxsxtpixd"},
                             {"fjubadocdwaygs", "x", "a", "zshbwqdhx"},
                             {"fjubadocdwaygs", "peaqbonzgl", "x"}, {"ccwfthnjt"}},
             new int[] {13, 32, 50, 51, 56, 59}, new int[] {13, 26, 27, 31, 32, 51},
             new int[] {13, 14, 25, 32, 49, 52});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
