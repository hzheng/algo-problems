import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC811: https://leetcode.com/problems/subdomain-visit-count/
//
// A website domain like "discuss.leetcode.com" consists of various subdomains.
// At the top level, we have "com", at the next level, we have "leetcode.com",
// and at the lowest level, "discuss.leetcode.com". When we visit a domain like
// "discuss.leetcode.com", we will also visit the parent domains "leetcode.com"
// and "com" implicitly.
// Now, call a "count-paired domain" to be a count (representing the number of
// visits this domain received), followed by a space, followed by the address.
// We are given a list cpdomains of count-paired domains. We would like a list
// of count-paired domains, (in the same format as the input, and in any order),
// that explicitly counts the number of visits to each subdomain.
public class SubdomainVisits {
    // beats %(32 ms for 52 tests)
    public List<String> subdomainVisits(String[] cpdomains) {
        Map<String, Integer> countMap = new HashMap<>();
        for (String cpdomain : cpdomains) {
            String[] parts = cpdomain.split(" ");
            int count = Integer.valueOf(parts[0]);
            String[] domains = parts[1].split("\\.");
            String domain = "";
            for (int i = domains.length - 1; i >= 0; i--) {
                if (i < domains.length - 1) {
                    domain = "." + domain;
                }
                domain = domains[i] + domain;
                countMap.put(domain, countMap.getOrDefault(domain, 0) + count);
            }
        }
        List<String> res = new ArrayList<>();
        for (String domain : countMap.keySet()) {
            res.add(countMap.get(domain) + " " + domain);
        }
        return res;
    }

    void test(String[] cpdomains,  String[] expected) {
        Set<String> expectedSet = new HashSet<>();
        for (String e : expected) {
            expectedSet.add(e);
        }
        Set<String> actualSet = new HashSet<>();
        actualSet.addAll(subdomainVisits(cpdomains));
        assertEquals(expectedSet, actualSet);
    }

    @Test
    public void test() {
        test(new String[] {"900 google.mail.com", "50 yahoo.com",
                           "1 intel.mail.com", "5 wiki.org"},
             new String[] { "901 mail.com", "50 yahoo.com",
                            "900 google.mail.com", "5 wiki.org",
                            "5 org", "1 intel.mail.com", "951 com"});
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
