import java.util.*;

import common.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

// LC1773: https://leetcode.com/problems/count-items-matching-a-rule/
//
// You are given an array items, where each items[i] = [typei, colori, namei] describes the type,
// color, and name of the ith item. You are also given a rule represented by two strings, ruleKey
// and ruleValue.
// The ith item is said to match the rule if one of the following is true:
// ruleKey == "type" and ruleValue == typei.
// ruleKey == "color" and ruleValue == colori.
// ruleKey == "name" and ruleValue == namei.
// Return the number of items that match the given rule.
//
// Constraints:
// 1 <= items.length <= 10^4
// 1 <= typei.length, colori.length, namei.length, ruleValue.length <= 10
// ruleKey is equal to either "type", "color", or "name".
// All strings consist only of lowercase letters.
public class CountMatches {
    // time complexity: O(N), space complexity: O(1)
    // 3 ms(100.00%), 43.4 MB(100.00%) for 92 tests
    public int countMatches(List<List<String>> items, String ruleKey, String ruleValue) {
        int res = 0;
        List<String> ruleKeys = Arrays.asList("type", "color", "name");
        int index = ruleKeys.indexOf(ruleKey);
        for (List<String> item : items) {
            res += item.get(index).equals(ruleValue) ? 1 : 0;
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // 4 ms(33.33%), 43.4 MB(100.00%) for 92 tests
    public int countMatches2(List<List<String>> items, String ruleKey, String ruleValue) {
        int res = 0;
        var rule = Map.of("type", 0, "color", 1, "name", 2);
        for (List<String> item : items) {
            res += item.get(rule.get(ruleKey)).equals(ruleValue) ? 1 : 0;
        }
        return res;
    }

    private void test(String[][] items, String ruleKey, String ruleValue, int expected) {
        List<List<String>> itemList = Utils.toList(items);
        assertEquals(expected, countMatches(itemList, ruleKey, ruleValue));
        assertEquals(expected, countMatches2(itemList, ruleKey, ruleValue));
    }

    @Test public void test() {
        test(new String[][] {{"phone", "blue", "pixel"}, {"computer", "silver", "lenovo"},
                             {"phone", "gold", "iphone"}}, "color", "silver", 1);
        test(new String[][] {{"phone", "blue", "pixel"}, {"computer", "silver", "phone"},
                             {"phone", "gold", "iphone"}}, "type", "phone", 2);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
