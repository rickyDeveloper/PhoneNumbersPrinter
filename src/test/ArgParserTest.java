package test;

import com.naiyarv.ArgsParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static com.naiyarv.constants.StringConstants.DEFAULT_PHONE_NUMBERS_DICTIONARY_FILE_PATH;
import static com.naiyarv.constants.StringConstants.PHONE_NUMBERS_DICTIONARY_ARG;

/**
 * Created by vikasnaiyar on 28/07/16.
 */
public class ArgParserTest {

    private ArgsParser argsParser = null;

    @Before
    public void doSetUp() {
       argsParser = new ArgsParser();
    }

    @Test
    public void testParseArgsMethodWithNoArgs() {
        String[] args = new String[0];
        argsParser.parseArgs(args);
        Assert.assertEquals("Default dictionary path expected", DEFAULT_PHONE_NUMBERS_DICTIONARY_FILE_PATH, argsParser.getDictionaryFilePath());
        Assert.assertEquals("File path size should be zero", 0,argsParser.getPhoneNumbersFilePaths().size());
    }

    @Test
    public void testParseArgsMethodWithDictionaryArgs() {
        String[] args = new String[2];
        args[0] = PHONE_NUMBERS_DICTIONARY_ARG;
        args[1] = DEFAULT_PHONE_NUMBERS_DICTIONARY_FILE_PATH;
        argsParser.parseArgs(args);

        Assert.assertEquals("Default dictionary path expected", DEFAULT_PHONE_NUMBERS_DICTIONARY_FILE_PATH, argsParser.getDictionaryFilePath());
        Assert.assertEquals("File path size should be zero", 0,argsParser.getPhoneNumbersFilePaths().size());
    }


    @Test
    public void testParseArgsMethodWithFileArgs() {
        String[] args = new String[2];
        args[0] = "phone_numbers.txt";
        args[1] = "phone_numbers_1.txt";
        argsParser.parseArgs(args);

        Assert.assertEquals("Default dictionary path expected", DEFAULT_PHONE_NUMBERS_DICTIONARY_FILE_PATH, argsParser.getDictionaryFilePath());
        Assert.assertEquals("File path size should be two", 2,argsParser.getPhoneNumbersFilePaths().size());
    }
}
