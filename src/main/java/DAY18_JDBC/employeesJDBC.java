package DAY18_JDBC;

import java.net.URL;
import java.sql.*;

public class employeesJDBC {
    private static final String URL="jdbc:mysql://localhost:3306/demo";
    private static final String USER="root";
    private static final String PASSWORD="admin";

    public static void main(String[] args) {
       try(Connection conn= DriverManager.getConnection(URL,USER,PASSWORD)){
           System.out.println("Connection Established");
//           insertemployee(conn,"Arnav","Senior Software Developer");
//           insertemployee(conn,"Aryan","CEO");
//           updateemployee(conn,"Arnav","Senior Software Developer",1);
//           searchemployee(conn);
           deleteemployee(conn,2);
       }
       catch (SQLException e){
           e.getStackTrace();
       }
        System.out.println("Connection Closed");
    }
    public static void insertemployee(Connection connection, String name,String Job_Title){
//        String sql="INSERT INTO employee (name,job_title) values ('"+name+"','"+Job_Title+"')";
        String sql="INSERT INTO employee (name,job_title) values (?,?)";

//        try(Statement statement = connection.createStatement()){
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1,name);
            ps.setString(2,Job_Title);
            int i = ps.executeUpdate();
            System.out.println("Rows Updated "+ i);
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }
    public static void updateemployee(Connection connection, String name,String Job_Title,int id){
        String sql="UPDATE employee set name=?,job_title=? where id="+id;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,Job_Title);
            int i=preparedStatement.executeUpdate();
            System.out.println("UPDATED ROWS :"+i);
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }
    public static void searchemployee(Connection connection){
        String sql="SELECT * FROM employee";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                int id=resultSet.getInt("id");
                String name=resultSet.getString("name");
                String Job_Title=resultSet.getString("job_title");
                System.out.println("ID : "+id+" | Name : "+name+" | Job_Title : "+Job_Title);
            }
        }
        catch(SQLException e){
            e.getStackTrace();
        }
    }
    public static void deleteemployee(Connection connection,int id){
        String sql="DELETE FROM employee WHERE id=?";
        try(PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setInt(1,id);
            int i = preparedStatement.executeUpdate();
            System.out.println("DELETED ROWS : "+i);
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }
}
