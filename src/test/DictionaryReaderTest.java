package test;

import com.naiyarv.dictionary.DictionaryReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static com.naiyarv.constants.StringConstants.TEST_PHONE_NUMBERS_DICTIONARY_FILE_PATH;

/**
 * Created by vikasnaiyar on 28/07/16.
 */
public class DictionaryReaderTest {

    private String dictionaryFilePath = null;

    @Before
    public void doSetUp() {
        dictionaryFilePath = TEST_PHONE_NUMBERS_DICTIONARY_FILE_PATH;
    }

    @Test
    public void testParseArgsMethodWithNoArgs() {
        DictionaryReader reader = new DictionaryReader(dictionaryFilePath);
        reader.readDictionaryFile();
        Assert.assertEquals("6 words expected", 6 , reader.getDictionary().size());

        reader.getDictionary().values().stream().forEach(word -> {
            Assert.assertTrue("White space should not be present in word", !word.contains(" "));
            Assert.assertTrue("Punctuation characters should not be present in word", !word.contains("!"));
        });
    }
}
