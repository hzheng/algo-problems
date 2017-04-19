import java.util.*;
import java.math.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/5304486/dashboard#s=p2
// Round 1A 2017: Problem C - Play the Dragon
//
// You have Hd health points and an attack power of Ad, and the knight has Hk health points
// and an attack power of Ak. If your health drops to 0 or below at any point; you lose; if
// the knight's health drops to 0 or below at any point, you win! You will battle the knight
//in a series of turns. On each turn, you go first, and you can choose and execute any one
// of the following actions.
// Attack: Reduce the opponent's health by your own attack power.
// Buff: Increase your attack power by B for the rest of the battle.
// Cure: Your health becomes Hd.
// Debuff: Decrease the opponent's attack power by D for the rest of the battle. If a Debuff
// would cause the opponent's attack power to become less than 0, it instead sets it to 0.
// Then, if the knight's health is greater than 0 following your action, he will execute an
// Attack action. After that, the turn ends. Every buff adds an additional B to your attack power.
// Similarly, debuffs stack with each other.
// Determine the minimum number of turns in which you can defeat the knight.
// Input
// The first line of the input gives the number of test cases, T. T test cases follow. Each
// consists of one line with six integers Hd, Ad, Hk, Ak, B, and D, as described above.
// Output
// For each test case, output one line containing Case #x: y, where x is the test case number
// and y is either IMPOSSIBLE if it is not possible to defeat the knight, or the minimum
// number of turns needed to defeat the knight.
// Limits
// 1 ≤ T ≤ 100.
// Small dataset
// 1 ≤ Hd ≤ 100. 1 ≤ Ad ≤ 100. 1 ≤ Hk ≤ 100. 1 ≤ Ak ≤ 100. 0 ≤ B ≤ 100. 0 ≤ D ≤ 100.
// Large dataset
// 1 ≤ Hd ≤ 10 ^ 9. 1 ≤ Ad ≤ 10 ^ 9. 1 ≤ Hk ≤ 10 ^ 9. 1 ≤ Ak ≤ 10 ^ 9. 0 ≤ B ≤ 10 ^ 9. 0 ≤ D ≤ 10 ^ 9.
public class DragonGame {
    // BFS + Queue
    // Only works for SMALL dataset
    public static int play(int health1, int attack1, int health2, int attack2, int buff, int debuff) {
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[] {health1, attack1, health2, attack2});
        Set<String> visited = new HashSet<>();
        for (int level = 1; !queue.isEmpty(); level++) {
            for (int i = queue.size(); i > 0; i--) {
                int[] state = queue.poll();
                int h1 = state[0];
                int a1 = state[1];
                int h2 = state[2];
                int a2 = state[3];
                if (a1 >= h2) return level;

                if (h1 <= a2) {
                    int[] s = new int[] {health1 - a2, a1, h2, a2};
                    if (s[0] > 0 && visited.add(toKey(s))) {
                        queue.offer(s);
                    }
                    if (h1 <= a2 - debuff) continue;
                }
                if (debuff > 0) {
                    int a2new = Math.max(0, a2 - debuff);
                    int[] s = new int[] {h1 - a2new, a1, h2, a2new};
                    if (s[0] > 0 && visited.add(toKey(s))) {
                        queue.offer(s);
                    }
                    if (h1 <= a2) continue;
                }
                if (buff > 0) {
                    int[] s = new int[] {h1 - a2, a1 + buff, h2, a2};
                    if (visited.add(toKey(s))) {
                        queue.offer(s);
                    }
                }
                int[] s = new int[] {h1 - a2, a1, h2 - a1, a2};
                if (visited.add(toKey(s))) {
                    queue.offer(s);
                }
            }
        }
        return -1;
    }

    private static String toKey(int[] state) {
        return state[0] + "," + state[1] + "," + state[2] + "," + state[3];
    }

    // Only works for SMALL dataset
    // time complexity: O(N ^ 2)
    public static int play2(int health1, int attack1, int health2, int attack2, int buff, int debuff) {
        int maxBuffCount = 0;
        if (buff > 0) {
            maxBuffCount = health2 / buff + 1;
        }
        int maxDebuffCount = 0;
        if (debuff > 0) {
            maxDebuffCount = attack2 / debuff + 1;
        }
        int min = Integer.MAX_VALUE;
        for (int d = 0; d <= maxDebuffCount; d++) {
            for (int b = 0; b <= maxBuffCount; b++) {
                min = Math.min(min, play(health1, attack1, health2, attack2, buff, debuff, b, d));
            }
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }

    private static int play(int health1, int attack1, int health2, int attack2, int buff, int debuff,
                            int buffTimes, int debuffTimes) {
        int times = buffTimes + debuffTimes;
        int h1 = health1;
        for (int i = 0, j = 0, lastCure = -2; i < debuffTimes; i++, j++) {
            if (h1 > attack2 - debuff) {
                attack2 -= debuff;
                if (attack2 < 0) {
                    attack2 = 0;
                }
                h1 -= attack2;
            } else {
                if (lastCure == j - 1) return Integer.MAX_VALUE;

                lastCure = j;
                h1 = health1 - attack2;
                times++;
                i--;
            }
        }
        for (int i = 0, j = 0, lastCure = -2; i < buffTimes; i++, j++) {
            if ((h1 -= attack2) > 0) {
                attack1 += buff;
            } else {
                if (lastCure == j - 1) return Integer.MAX_VALUE;

                lastCure = j;
                h1 = health1 - attack2;
                times++;
                i--;
            }
        }
        for (int lastCure = -2; health2 > 0; times++) {
            if ((health2 -= attack1) <= 0) continue;

            boolean cure = false;
            if (h1 <= attack2) {
                if (lastCure == times - 1) return Integer.MAX_VALUE;

                lastCure = times;
                h1 = health1;
                health2 += attack1;
            }
            h1 -= attack2;
        }
        return times;
    }

    // time complexity: O(N ^ 1/2)
    public static int play3(int health1, int attack1, int health2, int attack2, int buff, int debuff) {
        int minAttackAndBuff = minAttack(attack1, health2, buff); // fixed the optimum
        State state = new State(0, 0, health1, attack2);
        for (int d = 0, lastD = -1, minDebuff = Integer.MAX_VALUE;; ) { // test only threshold debuff
            int debuffTimes = play(health1, debuff, d, minAttackAndBuff, state);
            if (debuffTimes == Integer.MAX_VALUE && lastD > 0) return -1;

            minDebuff = Math.min(minDebuff, debuffTimes);
            if (debuff == 0 || attack2 <= d * debuff || d >= minDebuff) {
                return minDebuff == Integer.MAX_VALUE ? -1 : minAttackAndBuff + minDebuff;
            }
            int cureCycle = 1 + (health1 - 1) / (attack2 - d * debuff);
            for (lastD = d, d = (attack2 - (health1 - 1) / cureCycle) / debuff; attack2 > debuff * d; d++) {
                if ((health1 - 1) / (attack2 - debuff * d) >= cureCycle) break;
            }
        }
    }

    private static class State {
        int prevDebuffs;
        int times;
        int h1;
        int a2;
        State(int prevDebuffs, int times, int h1, int a2) {
            this.prevDebuffs = prevDebuffs;
            this.times = times;
            this.h1 = h1;
            this.a2 = a2;
        }
    }

    private static int play(int health1, int debuff, int debuffs, int attackAndBuff, State state) {
        int lastTimes = state.times;
        int leftDebuffs = debuffs;
        state.times = debuffs;
        if (leftDebuffs > 0) {
            leftDebuffs -= state.prevDebuffs;
            state.times += lastTimes - state.prevDebuffs;
            int finalAttack2 = Math.max(0, state.a2 - (debuffs - state.prevDebuffs) * debuff);
            int more = Math.min((state.h1 - 1) / state.a2, leftDebuffs);
            if (more > 0) {
                more = Math.min(more, state.a2 / debuff);
                leftDebuffs -= more;
                state.h1 -= more * state.a2 - debuff * more * (more + 1) / 2;
                state.a2 -= more * debuff;
            }
            if (leftDebuffs > 0 && state.h1 > state.a2 - debuff) {
                leftDebuffs--;
                state.a2 = Math.max(0, state.a2 - debuff);
                state.h1 -= state.a2;
            }
            int recover = (state.a2 > 0) ? (health1 - 1) / state.a2 - 1 : 0;
            if (leftDebuffs > 1 && recover <= 0) return Integer.MAX_VALUE;

            more = (int)Math.floor(Math.max(0, leftDebuffs - 1) / (double)recover);
            if (more > 0) {
                state.times += more;
                leftDebuffs -= more * recover;
                state.a2 = Math.max(0, state.a2 - debuff * (more - 1) * recover);
                int m = Math.min(recover, state.a2 / debuff);
                state.h1 = health1 - (m + 1) * state.a2 - debuff * m * (m - 1) / 2;
            }
            state.a2 = Math.max(0, finalAttack2 + leftDebuffs * debuff);
            if (!simulate(leftDebuffs, health1, debuff, state)) return Integer.MAX_VALUE;
        }
        state.prevDebuffs = debuffs;
        if (state.a2 == 0) return state.times;

        attackAndBuff -= Math.max(0, state.h1 - 1) / state.a2 + 1;
        if (attackAndBuff <= 0) return state.times;

        int recover = (health1 - 1) / state.a2 - 1;
        return recover > 0 ? state.times + (int)Math.ceil(attackAndBuff / (double)recover) : Integer.MAX_VALUE;
    }

    private static boolean simulate(int round, int health1, int debuff, State state) {
        for (int r = round, i = 0, lastCure = -2; r > 0; r--, i++) {
            if (state.h1 > state.a2 - debuff) {
                state.a2 = Math.max(0, state.a2 - debuff);
                state.h1 -= state.a2;
            } else {
                if (lastCure == i - 1) return false;

                lastCure = i;
                state.h1 = health1 - state.a2;
                state.times++;
                r++;
            }
        }
        return true;
    }

    private static int minAttack(int attack1, int health2, int buff) {
        int last = (int)Math.ceil(((double)health2) / attack1);
        if (buff > 0) {
            for (int b = 1, cur;; b++, last = cur) {
                cur = b + (int)Math.ceil(((double)health2) / (attack1 + b * buff));
                if (cur > last) break;
            }
        }
        return last;
    }

    void test(int health1, int attack1, int health2, int attack2, int buff, int debuff, int expected) {
        if (health1 < 1000) {
            assertEquals(expected, play(health1, attack1, health2, attack2, buff, debuff));
            assertEquals(expected, play2(health1, attack1, health2, attack2, buff, debuff));
        }
        assertEquals(expected, play3(health1, attack1, health2, attack2, buff, debuff));
    }

    @Test
    public void test() {
        test(3, 1, 3, 2, 1, 0, -1);
        test(3, 1, 3, 2, 2, 0, 2);
        test(2, 1, 5, 1, 1, 1, 5);
        test(11, 5, 16, 5, 0, 0, 5);
        test(26, 1, 60, 13, 3, 2, 16);
        test(100, 3, 60, 50, 0, 1, 39);
        test(93, 1, 92, 48, 1, 7, 28);
        test(33, 1, 76, 52, 1, 39, 19);
        test(97, 1, 50, 48, 0, 2, 84);
        test(96, 1, 94, 47, 0, 1, 162);
        test(95, 1, 90, 47, 0, 1, 158);
        test(93, 1, 95, 47, 0, 1, 164);
    }

    @Test
    public void test2() {
        test(914161769, 1, 906996925, 457080883, 0, 1, 1592589947);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        PrintStream out = System.out;
        if (args.length == 0) {
            String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
            out.format("Usage: java %s input_file [output_file]%n%n", clazz);
            org.junit.runner.JUnitCore.main(clazz);
            return;
        }
        try {
            in = new Scanner(new File(args[0]));
            if (args.length > 1) {
                out = new PrintStream(args[1]);
            }
        } catch (Exception e) {
            System.err.println(e);
            return;
        }

        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            out.format("Case #%d: ", i);
            printResult(in, out);
        }
    }

    private static void printResult(Scanner in, PrintStream out) {
        int res = play3(in.nextInt(), in.nextInt(), in.nextInt(),
                        in.nextInt(), in.nextInt(), in.nextInt());
        out.println(res >= 0 ? res : "IMPOSSIBLE");
    }
}
