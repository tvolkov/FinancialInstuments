import com.infusion.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.SQLException;

@RunWith(JUnit4.class)
public class FinancialInstrumentsRegularFileIT {
    private static final String PATH_TO_FILE = "src/test/resources/example_input.txt";
    @Test
    public void runApp(){
        System.out.println("Running test with regular file");
        try {
            App.setPathToFile(PATH_TO_FILE);
            App.main(new String[]{});
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
