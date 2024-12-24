
import Model.Employee;
import Util.ConnectionUtil;
import Util.FileUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Lab1Test {

    /**
     * This test calls the problem1 method and then compares it to the hardcoded list here, if they are the same then
     * the test passes.
     */
    @Test
    public void problem1GetAllSmiths(){
        //arrange
        Employee user1 = new Employee(2,"Alexa", "Smith", 42500.00);
        Employee user2 = new Employee(4, "Brandon", "Smith", 120000.00);
        List<Employee> expectedResult = new ArrayList<>();
        expectedResult.add(user1);
        expectedResult.add(user2);

        //act
        String sql = FileUtil.parseSQLFile("src/main/lab1.sql");


        List<Employee> actualResult = new ArrayList<>();
        try {
            Connection connection = ConnectionUtil.getConnection();
            Statement s = connection.createStatement();
            ResultSet rs =s.executeQuery(sql);

            while(rs.next()){
                actualResult.add(new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4)));
            }

        } catch (SQLException e) {
            Assert.fail("There was an issue running the SQL statement in lab1.");
        }

        //assert
        Assert.assertEquals(expectedResult, actualResult);
    }
    /**
     * The @Before annotation runs before every test so that way we create the tables required prior to running the test
     */
    @Before
    public void beforeTest(){

        try {

            Connection connection = ConnectionUtil.getConnection();

            //Write SQL logic here
            String sql1 = "CREATE TABLE employee (id SERIAL PRIMARY KEY, first_name varchar(100), last_name varchar(100), salary DOUBLE PRECISION);";
            String sql2 = "INSERT INTO employee (first_name, last_name, salary) VALUES ('Steve', 'Garcia', 67400.00);";
            String sql3 = "INSERT INTO employee (first_name, last_name, salary) VALUES ('Alexa', 'Smith', 42500.00);";
            String sql4 = "INSERT INTO employee (first_name, last_name, salary) VALUES ('Steve', 'Jones', 99890.99);";
            String sql5 = "INSERT INTO employee (first_name, last_name, salary) VALUES ('Brandon', 'Smith', 120000.00);";
            String sql6 = "INSERT INTO employee (first_name, last_name, salary) VALUES ('Adam', 'Jones', 55050.50);";


            PreparedStatement ps = connection.prepareStatement(sql1 + sql2 + sql3 + sql4 + sql5 + sql6);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("failed creating table");
            e.printStackTrace();
        }
    }

    /**
     * The @After annotation runs after every test so that way we drop the tables to avoid conflicts in future tests
     */
    @After
    public void cleanup(){

        try {

            Connection connection = ConnectionUtil.getConnection();

            String sql = "DROP TABLE IF EXISTS employee;";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("dropping table");
        }
    }
}
