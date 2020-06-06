import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1044: https://leetcode.com/problems/longest-duplicate-substring/
//
// Given a string S, consider all duplicated substrings: (contiguous) substrings of S that occur 2
// or more times.  (The occurrences may overlap.) Return any duplicated substring that has the
// longest possible length.  (If S does not have a duplicated substring, the answer is "".)
// Note:
//
// 2 <= S.length <= 10^5
// S consists of lowercase English letters.
public class LongestDupSubstring {
    private static final int MOD = 1_000_000_007;
    private static final int BASE = 26;

    // Binary Search + Rabin–Karp Algorithm(Rolling Hashing)
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 328 ms(78.54%), 60.9 MB(55.82%) for 16 tests
    public String longestDupSubstring(String S) {
        char[] cs = S.toCharArray();
        int low = 1;
        int index = -1;
        for (int high = cs.length; low + 1 < high; ) {
            int mid = (low + high) >>> 1;
            int res = duplicate(cs, mid);
            if (res >= 0) {
                low = mid;
                index = res;
            } else {
                high = mid;
            }
        }
        return (index >= 0) ? S.substring(index, index + low) : "";
    }

    private int duplicate(char[] s, int k) {
        long hash = 0;
        long power = 1;
        for (int i = 0; i < k; i++) {
            hash = (hash * BASE + (s[i] - 'a')) % MOD;
            if (i > 0) {
                power = power * BASE % MOD;
            }
        }
        Map<Long, Integer> map = new HashMap<>();
        map.put(hash, 0);
        for (int start = 1, end = k; end < s.length; start++, end++) {
            hash = (MOD + hash - (s[start - 1] - 'a') * power % MOD) % MOD;
            hash = (hash * BASE % MOD + (s[end] - 'a')) % MOD;

            Integer prev = map.put(hash, start);
            if (prev != null && equals(s, start, prev, k)) { return start; }
        }
        return -1;
    }

    private boolean equals(char[] s, int start1, int start2, int len) {
        for (int i = start1, j = start2; i < start1 + len; i++, j++) {
            if (s[i] != s[j]) { return false; }
        }
        return true;
    }

    // TODO: Suffix array

    void test(String S, String expected) {
        assertEquals(expected, longestDupSubstring(S));
    }

    @Test public void test() {
        test("banana", "ana");
        test("abcd", "");
        test("aaaaa", "aaaa");
        test("shabhlesyffuflsdxvvvoiqfjacpacgoucvrlshauspzdrmdfsvzinwdfmwrapbzuyrlvulpalqltqsha"
             + "skpsoiispneszlcgzvygeltuctslqtzeyrkfeyohutbpufxigoeagvrfgkpefythszpzpxjwgklrdbypyar"
             + "epdeskotoolwnmeibkqpiuvktejvbejgjptzfjpfbjgkorvgzipnjazzvpsjxjscekiqlcqeawsdydpsuqe"
             + "wszlpkgkrtlwxgozdqvyynlcxgnskjjmdhabqxbnnbflsscammppnlwyzycidzbhllvfvheujhnxrfujwmh"
             + "wiamqplygaujruuptfdjmdqdndyzrmowhctnvxryxtvzzecmeqdfppsjczqtyxlvqwafjozrtnbvshvxshp"
             + "etqijlzwgevdpwdkycmpsehxtwzxcpzwyxmpawwrddvcbgbgyrldmbeignsotjhgajqhgrttwjesrzxhvte"
             + "tifyxwiyydzxdqvokkvfbrfihslgmvqrvvqfptdqhqnzujeiilfyxuehhvwamdkkvfllvdjsldijzkjvloo"
             + "jspdbnslxunkujnfbacgcuaiohdytbnqlqmhavajcldohdiirxfahbrgmqerkcrbqidstemvngasvxzdjjq"
             + "kwixdlkkrewaszqnyiulnwaxfdbyianmcaaoxiyrshxumtggkcrydngowfjijxqczvnvpkiijksvridusfe"
             + "asawkndjpsxwxaoiydusqwkaqrjgkkzhkpvlbuqbzvpewzodmxkzetnlttdypdxrqgcpmqcsgohyrsrlqct"
             + "gxzlummuobadnpbxjndtofuihfjedkzakhvixkejjxffbktghzudqmarvmhmthjhqbxwnoexqrovxolfkxd"
             + "izsdslenejkypyzteigpzjpzkdqfkqtsbbpnlmcjcveartpmmzwtpumbwhcgihjkdjdwlfhfopibwjjsiky"
             + "qawyvnbfbfaikycrawcbkdhnbwnhyxnddxxctwlywjcisgqfsctzatdgqqauuvgclicdrpjcphysqdjaflp"
             + "dbmvnhqggixxzcmpsysbwfkzwxzjictnngufpqhcxlbkodyrqlfomlkiefbmcfenugzqnyqqvgpxonmizkp"
             + "jdlaqyyowjagzkzrzvcrupfyofeftyfvoqorzvxphhdhydnqiyiczfcgzsecxzsoaobwrixcajabjnvtoer"
             + "zwayjowahrmuixmmkbtchogfizmvbjnpespxngxjxntohzatlpkcmpphmewevpteharnszafbpbexrvnbed"
             + "ieojezdhnyooiivhnhakilvkobxepbksnqrtxxuqhalvtjspyvporalbliiwjciamlhttaydhxoelimuorj"
             + "nfvebjhcocbkrgbguwdncodskzzoqrzgavsbjcippetltqaxjhkqacwlgmsbxezqubyzeznnsoqegkykzlx"
             + "ohvitbmjcxllbrvgdijyovpjyeaojlyxqwnheyblznwoyikhqiutotpfukyqkvatxotulvlqzfcvskdccui"
             + "xthzqrwymzccosjmjqjigehcnfphjuuybaxxukconatzseljyirycbhucxmwwftulfwfmyqyprlnsmxzyfm"
             + "gjctgeunuuexhbrbsaaingqxqrjvpuhbvcmyztmkgenhonajrkzfrqjinjrbmjyinhwvlcmmxvbgvjgfmao"
             + "liosmxbonvlzoiqvkxxtoposygcgkcotohcrauivxxvmrghuauadwojxjligrgstczirnvhqpzwgjbvqzlk"
             + "xltqnqrfxieggnuriytavbnouwhuamdtlspednyckixkhxedjmotiuucewllthhducwgwmgzxsbkqzfnqfy"
             + "nwisvsctyqdoaiypjivtxkxgoyhwhccklbdjoqykaqzljejlizgbehekmkfetvgfstmypmfnyoundudqlor"
             + "cogbzoznddfalthwpmiewkmvogmzirbprjftbtffjrkrfminnechitfyfaujgtugadqbrskulsjbaunonxo"
             + "lauvsifevpdyurvfocxtkizflcuvltzuhwyhlbxaphifhtgkfktfnnmocpenrlujsuppbbuorvtubuiysza"
             + "wzftijwhwgdyubjmmodzybiyunksuixnkariubegpdgctbayaynfskkuyhjvegsjwsbppodvhpjdjlzhxix"
             + "swdncapxyfjspxeqxdfkhockvrzoisikaymoiqzqbjyoscwegfomlnurwboesfiszetebjblaolnovgvfcp"
             + "nbemwambkhwcgdbhvkoluhjfxlfrfaeedocdilaboesauplmttewlbojkocklhsbzrtzeyhqtmgroupbzly"
             + "mupmupsvlkzchclujuozzmngjvktzstsvocxrziuxelruwojzaleyrkjkdleavwqxwgjdbtiywqtdtaamrl"
             + "cjogxufhgvoqpqkgopbtyqchzhexqgecheahjzxapqjdylzjqhzlzssbjmokncxalgasexztnlzfisxxpee"
             + "rywlrjdohprewwnlwdbtwmfnnxnoolfhqqxzcvoymdbvmaoliedpvwzyvgyrwjguvoqnxrnaeqwvcfrqkwj"
             + "mlvxovptultyfuxerelpfgctnpdxluqeruxkxqntosggfjqmrnlnkhhilznpycdrnemnktcsmzufpqgirap"
             + "hzmgfhevzejhavsypohpttnnowfahpxfwmvxgwfuomxemdkzdlzldesmowzmhwoydnsovwykxqyllbmcurl"
             + "vtwcfwxvvkxfknwwcwfjkzjtonalgijdsulcfagehiptszrcltbbypopdbmdfkyofelmrdmdbceguyxnkhe"
             + "qqtbletpqmjugpckmjyuuvsbqhyzmposwcgscnishluuhnwkyrkujefpgtsqrmcoortgitpdoagdncxlofk"
             + "qozgngbtmlyyoyodcmcwetdtltupjrtemrjswekkfjvfecmvagyptjjuwsqpjwlxxosqhpssdvjraaicjfw"
             + "vesyqfbumjjbqytkinpldxopxjzmvpigmberobyzyxwvwmlmbziduqhmbescgkvhgqtalmaxfsjlysmvriz"
             + "gvrudstiwmaahtqehfbofvqwgqygvseykmgmhgjbxcrtdjqvojvyhohallyewqelzhjeuqmmsqhkluvqsfm"
             + "xzbqqokehfoqrlqmwpnwojfowqpqebnuggeuvsszgfywceolvabyvbrwatuyherijsdqvpyyhdyradbammm"
             + "chqkvdbxpbrxzrpfrsiiezvowrfqejibvociujtcwbygvfwojgfnvvwqlqqgipxhrogppzghtnweodaxuqx"
             + "knnqnajlnsvheiycsvifvoljsncgnunsqcymnyoeeslrjflpprvtksimffvnuvakskdakvmlkpowfpfzdrc"
             + "fctikhvvbagrvjlzjydnlmspzyynyjjfxnozpjjgjelipswsmfroitqphzsuqgumlnkxksbzhrsvcnfwufo"
             + "fhurmhksvvfjzggbtgrezkrkqmhduyqgwuwxoxaiifemtwrbilftiuhcgpjvqxldrnlzphdffncevlcyrxl"
             + "pbwuswjfdegexeoooshdfqtqithpfocyowaqeedikssptyvkabhtaeotcwxccgguuotqvypugpcbwzalxwq"
             + "bjdcokoxjnqhggpbbfeyjiellsikiqqtxpvzmjsfleepjpbxpeicxfcwbpprzgcrjgjaxshewradetsqsvf"
             + "mcxptmksecfpynqzpctqpogcgokzrkltsbmwxkmynasohpkzjupapngusnvdjfqezqhyikllgkelewwwhhb"
             + "djvxdagnnxscjkotbbmhzkqbjwuwidrnvmztikmqjcxmcpgkoudhydmdvberfuvjnhlnfcsbpzmuquvrgog"
             + "tfwefhqzkmxxgadtvjpxvurxprbsssihviypclwkjfaatzjxtvlzwaacqlwnqetgkldqaqghuihrgxbbpmj"
             + "fsvaigqrhiiskkfibaeilqptkdsqqfwxeixuxgkiboaqnuaeutjcydnxyxnmattjrrxmthwvyipgazaxgrr"
             + "jcvdnyxpktsldhluhicyqprxhljyfhawuvoonrwyklcdlmdvsgqrwqqomisksftsfyeifmupvylkjbagzyc"
             + "tuifbsrugqsbrkvskmundmczltpamhmgqespzgrkxebsvubrlmkwyqhjyljnkeqvdxtjxjvzlrubsiiahci"
             + "wefwsswgssxmvyvgjrobvubcbgjomqajmotbcgqjudneovfbjtjzwqtsovzshmxeqofssukkvcdwlsdtypl"
             + "rlgwtehnwvhhegtwkwnqqdiajpcaajsylesadaiflruewhrbrogbujbppunsqgytgnyuhnkejhccavaptby"
             + "dtqhvyatftxcaaljyhhkkadzdhhzawgndunwwgknnbtqaddpszqgummmnomfqmdxqtwjexsbadfdqhnyixj"
             + "slsaisscocbabivzokkgiinqqzsrtfpzjmxfkqmuzzlelqjtjidjarkwbwlcqrefokrlwdmuzyffdtajnqo"
             + "imlzzpcgpjjwlqkusefzbgznhexzojxnzxmmedobgvdabtdoiskozrdrjscxwivaekrkyyfynuktmgyzite"
             + "avdxfctvkfkrmsdwpaywzbkeojeycwdkectydojttisizruilwokhepscqdnjygiakomkhyujaffxjyxqmv"
             + "kemqihpcdygprdeaxgjbkonfvgtzayfbmgwsskoyxjlknwwtehhhpjllhkcblyaxnbekoidbbyqvdqqsyfc"
             + "emylmqskpxifcnhmemkkitqtbfwhmyemkzightkjbhlquticivpeeclpamsqoztxvdtcqbsonxyecnhcadt"
             + "ghkjckhrcdfggnqlwurydzbeybqkcfnnbwkciwaqdzgmcrbltvcuftxsqfpxnoombsbeoqxivgtkrjklxat"
             + "gcorkdrvmngwlekeopgecefzxtprcoajoopxviijxilxfiwuajsbtcctfcqqgzhyjmonwdbyjlnneidyaqh"
             + "hothzpzaxcthvbxpdcqofaeamxbqjwhunnqwclhcqhagawjsxygorgbuqryzickyplbaivkabbrkibqzqac"
             + "abbwmnpndaqvbknbqcjuywxjrdbznndomwbbqfgulwczydrhrocebhygriiwxmwtjjyqqiqrjblxuamddls"
             + "iocdoysdaacuovljtpgocephnxuugdyggsnhcqiqhulyhlxiwzvhrtbmvjnhacwunckqzhpcthqgsupptdw"
             + "kfeprbg", "maoli");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
