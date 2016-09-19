import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC402: https://leetcode.com/problems/remove-k-digits/
//
// Given a non-negative integer num represented as a string, remove k digits
// from the number so that the new number is the smallest possible.
// Note:
// The length of num is less than 10 ^ 5 and will be ≥ k.
// The given num does not contain any leading zero.
public class RemoveKDigits {
    // beats N/A(15 ms)
    public String removeKdigits(String num, int k) {
        int len = num.length();
        if (k >= len) return "0";

        if (k <= 0) return num;

        StringBuilder sb = new StringBuilder(num);
        for (int i = 0; k > 0 && i < sb.length() - 1; ) {
            if (sb.charAt(i) <= sb.charAt(i + 1)) {
                i++;
            } else {
                sb.deleteCharAt(i);
                if (i > 0) {
                    i--;
                }
                k--;
            }
        }
        if (k > 0) {
            len = sb.length();
            sb.delete(len - k, len);
        }
        while (sb.length() > 1 && sb.charAt(0) == '0') {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    // Stack
    // beats N/A(10 ms)
    public String removeKdigits2(String num, int k) {
        int len = num.length();
        if (k >= len) return "0";

        if (k <= 0) return num;

        Stack<Character> stack = new Stack<>();
        char[] chars = num.toCharArray();
        int i = 0;
        while (k > 0 && i < chars.length) {
            char c = chars[i];
            if (stack.isEmpty() || c >= stack.peek()) {
                stack.push(c);
                i++;
            } else {
                stack.pop();
                k--;
            }
        }
        while (k-- > 0) {
            stack.pop();
        }
        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            sb.insert(0, stack.pop());
        }
        sb.append(num.substring(i));
        while (sb.length() > 1 && sb.charAt(0) == '0') {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    void test(String num, int k, String expected) {
        assertEquals(expected, removeKdigits(num, k));
        assertEquals(expected, removeKdigits2(num, k));
    }

    @Test
    public void test1() {
        test("10", 1, "0");
        test("10", 2, "0");
        test("113", 1, "11");
        test("231", 1, "21");
        test("178", 1, "17");
        test("1178", 2, "11");
        test("7951", 2, "51");
        test("103200", 2, "200");
        test("10200", 1, "200");
        test("10000200", 1, "200");
        test("1234567890", 9, "0");
        test("9111003", 4, "3");
        test("911003", 3, "3");
        test("111362", 3, "111");
        test("41111111", 4, "1111");
        test("1111111", 3, "1111");
        test("1432219", 3, "1219");
        test("17837605025683516120010270503807569928178393773076879170028133735"
             + "644386709353014199231161016834202821655655947833118094662725822"
             + "436120397735236953596117990380023771485471207752920307347213671"
             + "242946302334923503764912432055700309914206103364063868546679428"
             + "867424925671680526032099860583026255808370544562607516226496473"
             + "439016990783116948597588838766623253667716587248969590351938547"
             + "681019783978607010026047562010092976543940682873432990239799550"
             + "710461153834255732312639561941973686912576394182153793380462425"
             + "867680992707616498637668115274622735232014853260668113966532173"
             + "703357431717064856689072299136043201776374633287565311907763102"
             + "068659145456425775667706742465636096265155225620954708476056834"
             + "360598143586342230201494961702397022909032525343629831061617524"
             + "029740549732503118933345637593796835637332517547616659271620123"
             + "917033661075709601596433920904019940562966413498015263253249142"
             + "412825157794131174843234468459564274711666179597226827742043712"
             + "4162608605233609109801621173197610016138622991716783609514815",
             100,
             "10946627258224361203977352369535961179903800237714854712077529203"
             + "073472136712429463023349235037649124320557003099142061033640638"
             + "685466794288674249256716805260320998605830262558083705445626075"
             + "162264964734390169907831169485975888387666232536677165872489695"
             + "903519385476810197839786070100260475620100929765439406828734329"
             + "902397995507104611538342557323126395619419736869125763941821537"
             + "933804624258676809927076164986376681152746227352320148532606681"
             + "139665321737033574317170648566890722991360432017763746332875653"
             + "119077631020686591454564257756677067424656360962651552256209547"
             + "084760568343605981435863422302014949617023970229090325253436298"
             + "310616175240297405497325031189333456375937968356373325175476166"
             + "592716201239170336610757096015964339209040199405629664134980152"
             + "632532491424128251577941311748432344684595642747116661795972268"
             + "277420437124162608605233609109801621173197610016138622991716783"
             + "609514815");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RemoveKDigits");
    }
}
