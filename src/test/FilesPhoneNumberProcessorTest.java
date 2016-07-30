package test;

import com.naiyarv.matcher.Matcher;
import com.naiyarv.matcher.PhoneNumberMatcher;
import com.naiyarv.processor.FilesPhoneNumberProcessor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.HashSet;
import static com.naiyarv.constants.StringConstants.TEST_PHONE_NUMBER_FILE_PATH;
import static com.naiyarv.constants.StringConstants.TEST_PHONE_NUMBER_FILE_PATH_1;

/**
 * Created by vikasnaiyar on 28/07/16.
 */
public class FilesPhoneNumberProcessorTest {

    private Matcher matcher = null;

    // Collection of all the file paths
    private Collection<String> filePaths = null;

    @Before
    public void doSetUp() {
        filePaths = new HashSet<>();
        filePaths.add(TEST_PHONE_NUMBER_FILE_PATH);
        filePaths.add(TEST_PHONE_NUMBER_FILE_PATH_1);

        matcher = Mockito.mock(Matcher.class);
    }

    // Test whether all the files are being read properly
    @Test
    public void testProcessPhoneNumbers() {
        FilesPhoneNumberProcessor processor = new FilesPhoneNumberProcessor(filePaths, matcher);
        processor.processPhoneNumbers();
    }
}
