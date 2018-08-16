import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC649: https://leetcode.com/problems/dota2-senate/
//
// In the world of Dota2, there are two parties: the Radiant and the Dire. The
// Dota2 senate consists of senators coming from two parties. Now the senate
// wants to make a decision. The voting is a round-based procedure. In each
// round, each senator can exercise one of the two rights:
// Ban one senator's right: A senator can make another senator lose all his
// rights in this and all the following rounds.
// Announce the victory: If this senator found the senators who still have
// rights to vote are all from the same party, he can announce the victory.
// Given a string representing each senator's party belonging. The character 'R'
// and 'D' represent the Radiant party and the Dire party respectively. The
// round-based procedure starts from the first senator to the last senator in
// the given order. This procedure will last until the end of voting. Suppose
// every senator is smart enough and will play the best strategy for his own
// party, you need to predict which party will finally announce the victory.
public class Dota2Senate {
    // 2 Queues
    // time complexity: O(N), space complexity: O(N)
    // beats 30.68%(20 ms for 81 tests)
    public String predictPartyVictory(String senate) {
        Queue<Integer> r = new LinkedList<>();
        Queue<Integer> d = new LinkedList<>();
        int i = 0;
        for (char s : senate.toCharArray()) {
            if (s == 'R') {
                r.offer(i++);
            } else {
                d.offer(i++);
            }
        }
        int n = senate.length();
        while (!r.isEmpty() && !d.isEmpty()) {
            int firstR = r.poll();
            int firstD = d.poll();
            if (firstR < firstD) {
                r.offer(firstR + n);
            } else {
                d.offer(firstD + n);
            }
        }
        return r.isEmpty() ? "Dire" : "Radiant";
    }

    // 1 Queue
    // time complexity: O(N), space complexity: O(N)
    // beats 85.23%(14 ms for 81 tests)
    public String predictPartyVictory2(String senate) {
        Queue<Integer> q = new LinkedList<>();
        int[] people = new int[2];
        int[] bans = new int[2]; // voting debts(i.e people to be banned)
        for (char person : senate.toCharArray()) {
            int x = (person == 'R') ? 1 : 0;
            people[x]++;
            q.offer(x);
        }
        while (people[0] > 0 && people[1] > 0) {
            int x = q.poll();
            if (bans[x] > 0) {
                bans[x]--;
                people[x]--;
            } else {
                bans[x ^ 1]++;
                q.offer(x);
            }
        }
        return people[1] > 0 ? "Radiant" : "Dire";
    }

    // time complexity: O(N), space complexity: O(N)
    // beats 10.23%(493 ms for 81 tests)
    public String predictPartyVictory3(String senate) {
        int[] people = new int[2];
        char[] s = senate.toCharArray();
        for (char c : s) {
            people[(c == 'R') ? 0 : 1]++;
        }
        int n = s.length; 
        for (int i = 0; people[0] > 0 && people[1] > 0; i = (i + 1) % n) {
            char ban = 'R';
            if (s[i] == 'R') {
                ban = 'D';
                people[1]--;
            } else if (s[i] == 'D') {
                people[0]--;
            } else continue; // s[i] == ' '

            int j = (i + 1) % n;
            for (; s[j] != ban; j = (j + 1) % n) {}
            s[j] = ' ';
        }
        return people[1] == 0 ? "Radiant" : "Dire";
    }

    void test(String senate, String expected) {
        assertEquals(expected, predictPartyVictory(senate));
        assertEquals(expected, predictPartyVictory2(senate));
        assertEquals(expected, predictPartyVictory3(senate));
    }

    @Test
    public void test() {
        test("RD", "Radiant");
        test("RDD", "Dire");
        test("RRDDDRDRDR", "Radiant");
        test("RDRDDDRDRDRD", "Dire");
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
