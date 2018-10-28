import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC929: https://leetcode.com/problems/unique-email-addresses/
//
// Every email consists of a local name and a domain name, separated by the @.
// If you add periods ('.') between some characters in the local name part of an 
// email address, mail sent there will be forwarded to the same address without 
// dots in the local name. If you add a plus ('+') in the local name, everything
// after the first plus sign will be ignored. Given a list of emails, we send
// one email to each address in the list.  How many different addresses actually 
// receive mails? 
public class UniqueEmailAddresses {
    // beats %(36 ms for 173 tests)
    public int numUniqueEmails(String[] emails) {
        Set<String> set = new HashSet<>();
        for (String email : emails) {
            int at = email.indexOf('@');
            String domain = email.substring(at);
            String name = email.substring(0, at);
            name = name.split("\\+")[0].replace(".", "");
            // or: name = name.split("\\+")[0].replaceAll("\\.", "");
            set.add(name + domain);
        }
        return set.size();
    }

    void test(String[] emails, int expected) {
        assertEquals(expected, numUniqueEmails(emails));
    }

    @Test
    public void test() {
        test(new String[] {"test.email+alex@leetcode.com", "test.e.mail+bob.cathy@leetcode.com",
                "testemail+david@lee.tcode.com"}, 2);
        test(new String[] {"test.email+alex@leetcode.com", "test.e.mail+bob.cathy@leetcode.com",
                "testemail+david@leetcode.com"}, 1);
        test(new String[] {"test.email+alex@leetcode.com", "test.f.mail+bob.cathy@leetcode.com",
                "testemail+david@lee.tcode.com"}, 3);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
