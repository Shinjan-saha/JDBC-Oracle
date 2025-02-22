import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Connect {
    public static void main(String[] args) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
    "jdbc:oracle:thin:@//localhost:1521/orclpdb1", "SYS as sysdba", "mypassword1"
);


            Statement smt = con.createStatement();
            smt.executeUpdate("create table emp(eno number, ename varchar(12), esal number)");

            System.out.print("Table Created Successfully...");
            con.close();
        } catch (Exception e) {
            System.out.print(e);
        }
    }
}



