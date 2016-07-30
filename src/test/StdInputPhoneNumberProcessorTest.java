package test;

import com.naiyarv.matcher.Matcher;
import com.naiyarv.processor.StdInputPhoneNumberProcessor;
import static com.naiyarv.constants.StringConstants.EXIT_STRING;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import static com.naiyarv.constants.StringConstants.TEST_STD_INPUT_FILE_PATH;

/**
 * Created by vikasnaiyar on 28/07/16.
 */
public class StdInputPhoneNumberProcessorTest {

    private Scanner scanner = null;

    private Matcher matcher = null;

    @Before
    public void doSetUp() {
        File file = new File(TEST_STD_INPUT_FILE_PATH);
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
           System.err.format("Exception occured while reading file %s %n", TEST_STD_INPUT_FILE_PATH);
        }

        matcher = Mockito.mock(Matcher.class);
    }


    // test that phone numbers are being read and processed
    @Test
    public void testProcessPhoneNumbers() {
        StdInputPhoneNumberProcessor processor = new StdInputPhoneNumberProcessor(scanner, matcher);
        processor.processPhoneNumbers();
    }
}
