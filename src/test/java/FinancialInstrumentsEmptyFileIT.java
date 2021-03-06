import com.infusion.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.SQLException;

@RunWith(JUnit4.class)
public class FinancialInstrumentsEmptyFileIT {
    private static final String PATH_TO_FILE = "src/test/resources/empty_file.txt";
    //todo make it work with the custom paths to input file
    @Test
    public void runApp(){
        System.out.println("Running app with non-existing file");
        try {
            App.setPathToFile(PATH_TO_FILE);
            App.main(new String[]{});
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
