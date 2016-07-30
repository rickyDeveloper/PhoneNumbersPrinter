package test;

import com.naiyarv.matcher.PhoneNumberMatcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * Created by vikasnaiyar on 28/07/16.
 */
public class PhoneNumberMatcherTest {

    Map<String, Set<String>> dictionary = null;
    @Before
    public void doSetUp() {
        dictionary = new HashMap<>();
        Set<String> words = new HashSet<>();
        words.add("AD");
        dictionary.put("23", words);

        words = new HashSet<>();
        words.add("HELLO");
        dictionary.put("43556", words);

        words = new HashSet<>();
        words.add("WORLD");
        dictionary.put("96753", words);
    }


    // Test that correct patterns are getting generated
    @Test
    public void testProcessPhoneNumbers() {
        PhoneNumberMatcher matcher = new PhoneNumberMatcher(dictionary);
        Collection<String> words = matcher.findMatchingWords("223");

        Assert.assertTrue(words != null && words.size() > 0);
        Assert.assertTrue(words.size() == 1);
        Assert.assertTrue(words.contains("2-AD"));

        words = matcher.findMatchingWords("143556196753");

        Assert.assertTrue(words != null && words.size() > 0);
        Assert.assertTrue(words.size() == 1);
        Assert.assertTrue(words.contains("1-HELLO-1-WORLD"));
    }

}
