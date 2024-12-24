
import Model.Employee;
import Util.ConnectionUtil;
import Util.FileUtil;
import org.junit.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Lab4Test {
    private static Connection conn;

    @Test
    public void testActivityFindEmployeesOr() {
        List<Employee> expectedList = new ArrayList<>();
        expectedList.add(new Employee(2, "Alexa", "Smith", 42500));
        expectedList.add(new Employee(4, "Brandon", "Smith", 120000));
        String sql = FileUtil.parseSQLFile("src/main/lab4.sql");
        if(sql.isBlank()){
            Assert.fail("No sql statement exists in lab4.sql.");
        }
        //The following code will execute your statement on the database
        List<Employee> resultsList = new ArrayList<>();
        try {
            Connection connection = ConnectionUtil.getConnection();
            Statement s = connection.createStatement();
            ResultSet rs =s.executeQuery(sql);

            while(rs.next()) {
                resultsList.add(new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4)));
            }
        } catch (SQLException e) {
            Assert.fail("There was an issue executing the SQL statement in lab4: "+e.getMessage());
        }
        assertEquals("The result of lab4 should match expected", expectedList, resultsList);
    }

    @BeforeClass
    public static void beforeAll() {
        conn = ConnectionUtil.getConnection();
    }

    @Before
    public void beforeEach() {
        try {
            conn = ConnectionUtil.getConnection();

            String createTable = "CREATE TABLE employee (" +
                    "id SERIAL PRIMARY KEY," +
                    "first_name VARCHAR(255)," +
                    "last_name VARCHAR(255)," +
                    "salary DOUBLE PRECISION" +
                    ");";
            PreparedStatement createTableStatement = conn.prepareStatement(createTable);
            createTableStatement.executeUpdate();

            String insertData = "INSERT INTO employee (first_name, last_name, salary) VALUES" +
                    "('Steve', 'Garcia', 67400.00)," +
                    "('Alexa', 'Smith', 42500.00)," +
                    "('Steve', 'Jones', 99890.99)," +
                    "('Brandon', 'Smith', 120000)," +
                    "('Adam', 'Jones', 55050.50)," +
                    "('Steve', 'Brown', 90000.50);";
            PreparedStatement insertDataStatement = conn.prepareStatement(insertData);
            insertDataStatement.executeUpdate();

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @After
    public void afterEach() {
        try {
            conn = ConnectionUtil.getConnection();

            String dropTable = "DROP TABLE IF EXISTS employee";
            PreparedStatement createTableStatement = conn.prepareStatement(dropTable);
            createTableStatement.executeUpdate();

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void afterAll() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
