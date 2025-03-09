import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Parent Class - Course
class Course {
    protected int courseId;
    protected String courseName;
    protected String instructor;
    protected int credits;

    // Constructor
    public Course(int courseId, String courseName, String instructor, int credits) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.instructor = instructor;
        this.credits = credits;
    }

    // Method to display course details
    public void displayDetails() {
        System.out.println("Course ID: " + courseId);
        System.out.println("Course Name: " + courseName);
        System.out.println("Instructor: " + instructor);
        System.out.println("Credits: " + credits);
    }
}

// Subclass - OnlineCourse
class OnlineCourse extends Course {
    private String platform;
    private int duration; // in hours

    // Constructor
    public OnlineCourse(int courseId, String courseName, String instructor, int credits, String platform, int duration) {
        super(courseId, courseName, instructor, credits);
        this.platform = platform;
        this.duration = duration;
    }

    // Method to check certificate eligibility
    public boolean isEligibleForCertificate() {
        return duration >= 20; // Eligible if duration is 20+ hours
    }

    // Method to display course details
    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("Platform: " + platform);
        System.out.println("Duration: " + duration + " hours");
        System.out.println("Eligible for Certificate: " + (isEligibleForCertificate() ? "Yes" : "No"));
    }

    // Method to insert course details into Oracle DB
    public void saveToDatabase() {
        Connection con = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;

        try {
            // Step 1: Load Oracle JDBC Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Step 2: Establish Database Connection
            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//localhost:1521/orclpdb1", "username", "password");

            // Step 3: Insert into Courses Table
            String sql1 = "INSERT INTO Courses (course_id, course_name, instructor, credits) VALUES (?, ?, ?, ?)";
            ps1 = con.prepareStatement(sql1);
            ps1.setInt(1, courseId);
            ps1.setString(2, courseName);
            ps1.setString(3, instructor);
            ps1.setInt(4, credits);
            ps1.executeUpdate();

            // Step 4: Insert into OnlineCourses Table
            String sql2 = "INSERT INTO OnlineCourses (course_id, platform, duration) VALUES (?, ?, ?)";
            ps2 = con.prepareStatement(sql2);
            ps2.setInt(1, courseId);
            ps2.setString(2, platform);
            ps2.setInt(3, duration);
            ps2.executeUpdate();

            System.out.println("Course successfully added to the database.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps1 != null) ps1.close();
                if (ps2 != null) ps2.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to retrieve and display all courses from database
    public static void fetchAllCourses() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Step 1: Load Oracle JDBC Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Step 2: Establish Database Connection
            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//localhost:1521/orclpdb1", "username", "password");

            // Step 3: Query Data
            String sql = "SELECT c.course_id, c.course_name, c.instructor, c.credits, o.platform, o.duration " +
                         "FROM Courses c JOIN OnlineCourses o ON c.course_id = o.course_id";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            // Step 4: Display Data
            System.out.println("----- Online Courses List -----");
            while (rs.next()) {
                System.out.println("Course ID: " + rs.getInt("course_id"));
                System.out.println("Course Name: " + rs.getString("course_name"));
                System.out.println("Instructor: " + rs.getString("instructor"));
                System.out.println("Credits: " + rs.getInt("credits"));
                System.out.println("Platform: " + rs.getString("platform"));
                System.out.println("Duration: " + rs.getInt("duration") + " hours");
                System.out.println("------------------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

// Main Class
public class CourseManagement {
    public static void main(String[] args) {
        // Creating an Online Course Object
        OnlineCourse oc = new OnlineCourse(101, "Java Programming", "John Doe", 3, "Udemy", 25);
        
        // Display Course Details
        oc.displayDetails();

        // Save to Database
        oc.saveToDatabase();

        // Fetch and Display All Courses
        OnlineCourse.fetchAllCourses();
    }
}
