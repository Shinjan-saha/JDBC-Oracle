import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class InsertData {
    public static void main(String[] args) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@//localhost:1521/orclpdb1", "SYS as sysdba", "mypassword1"
            );
            
            String query = "INSERT INTO emp (eno, ename, esal) VALUES (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            
          
            pstmt.setInt(1, 101);
            pstmt.setString(2, "John Doe");
            pstmt.setDouble(3, 50000.0);
            
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Data inserted successfully...");
            }
            
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
