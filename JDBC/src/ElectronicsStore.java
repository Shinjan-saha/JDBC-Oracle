import java.sql.*;

class DBConnection {
    // JDBC Connection method
    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//localhost:1521/orclpdb1", 
                    "SYS as sysdba", 
                    "mypassword1"
            );
        } catch (Exception e) {
            System.out.println("Database Connection Error: " + e);
        }
        return con;
    }
}

// Parent Class: ElectronicsProduct
class ElectronicsProduct {
    protected int productID;
    protected String name;
    protected double price;

    public ElectronicsProduct(int productID, String name, double price) {
        this.productID = productID;
        this.name = name;
        this.price = price;
    }

    public void applyDiscount(double discountPercentage) {
        price -= price * (discountPercentage / 100);
    }

    public double getFinalPrice() {
        return price;
    }

    public void saveToDatabase() {
        try (Connection con = DBConnection.getConnection()) {
            Statement stmt = con.createStatement();
            // Create table if not exists
            String createTable = "CREATE TABLE Electronics ("
                    + "product_id NUMBER PRIMARY KEY, "
                    + "name VARCHAR2(50), "
                    + "price NUMBER(10,2), "
                    + "warranty_period NUMBER)";
            stmt.executeUpdate(createTable);

            // Insert product data
            String query = "INSERT INTO Electronics (product_id, name, price) VALUES (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, productID);
            pstmt.setString(2, name);
            pstmt.setDouble(3, price);
            pstmt.executeUpdate();
            System.out.println("Product saved to database.");
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e);
        }
    }
}

// Subclass: WashingMachine
class WashingMachine extends ElectronicsProduct {
    private int warrantyPeriod;

    public WashingMachine(int productID, String name, double price, int warrantyPeriod) {
        super(productID, name, price);
        this.warrantyPeriod = warrantyPeriod;
    }

    public void extendWarranty(int extraYears) {
        warrantyPeriod += extraYears;
    }

    public void updateWarrantyInDB() {
        try (Connection con = DBConnection.getConnection()) {
            String query = "UPDATE Electronics SET warranty_period = ? WHERE product_id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, warrantyPeriod);
            pstmt.setInt(2, productID);
            pstmt.executeUpdate();
            System.out.println("Warranty updated in database.");
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e);
        }
    }

    public void displayDetails() {
        System.out.println("Product ID: " + productID);
        System.out.println("Name: " + name);
        System.out.println("Price: $" + price);
        System.out.println("Warranty Period: " + warrantyPeriod + " years");
    }
}

// Main Class
public class ElectronicsStore {
    public static void main(String[] args) {
        try {
            WashingMachine wm = new WashingMachine(101, "Samsung Front Load", 60000, 2);
            wm.applyDiscount(10); // 10% discount
            System.out.println("Final Price: " + wm.getFinalPrice());
            wm.saveToDatabase(); // Save product details
            wm.extendWarranty(3); // Extend warranty by 3 years
            wm.updateWarrantyInDB(); // Update warranty in DB
            wm.displayDetails(); // Show details
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
