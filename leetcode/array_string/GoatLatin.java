import org.junit.Test;
import static org.junit.Assert.*;

// LC824: https://leetcode.com/problems/goat-latin/
//
// A sentence S is given, composed of words separated by spaces. Each word
// consists of lowercase and uppercase letters only. We would like to convert
// the sentence to "Goat Latin". The rules of Goat Latin are as follows:
// If a word begins with a vowel (a, e, i, o, or u), append "ma" to the end of
// the word. If a word begins with a consonant, remove the first letter and
// append it to the end, then add "ma". Add one letter 'a' to the end of each
// word per its word index in the sentence, starting with 1.
public class GoatLatin {
    // beats %(10 ms for 99 tests)
    public String toGoatLatin(String S) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String word : S.split(" ")) {
            switch (Character.toLowerCase(word.charAt(0))) {
            case 'a': case 'e': case 'i': case 'o': case 'u':
                // if ("AEIOUaeiou".indexOf(word.charAt(0)) >= 0) {
                sb.append(word);
                break;
            default:
                sb.append(word.substring(1)).append(word.charAt(0));
            }
            sb.append("ma");
            for (int j = ++i; j > 0; j--) {
                sb.append("a");
            }
            sb.append(" ");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    void test(String S, String expected) {
        assertEquals(expected, toGoatLatin(S));
    }

    @Test
    public void test() {
        test("I speak Goat Latin", "Imaa peaksmaaa oatGmaaaa atinLmaaaaa");
        test("The quick brown fox jumped over the lazy dog",
             "heTmaa uickqmaaa rownbmaaaa oxfmaaaaa umpedjmaaaaaa overmaaaaaaa "
             + "hetmaaaaaaaa azylmaaaaaaaaa ogdmaaaaaaaaaa");
        test("Each word consists of lowercase and uppercase letters only",
             "Eachmaa ordwmaaa onsistscmaaaa ofmaaaaa owercaselmaaaaaa "
             + "andmaaaaaaa uppercasemaaaaaaaa etterslmaaaaaaaaa "
             + "onlymaaaaaaaaaa");
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
