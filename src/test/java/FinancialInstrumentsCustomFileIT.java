import com.infusion.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.print.DocFlavor;
import java.sql.SQLException;

@RunWith(JUnit4.class)
public class FinancialInstrumentsCustomFileIT {
    private static final String INPUT_FILE_PROPERTY = "inputFile";
    private static final String PATH_TO_FILE = System.getProperty(INPUT_FILE_PROPERTY);
    //todo make it work with the custom paths to input file
    @Test
    public void runApp(){
        System.out.println("Running app with custom file " + PATH_TO_FILE);
        try {
            App.setPathToFile(PATH_TO_FILE);
            App.main(new String[]{});
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
