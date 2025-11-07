package DAY19_TRANSACTION_COMMITS;

import javax.swing.*;
import java.sql.*;
import java.util.Scanner;

public class transferMoney {
    private static final String URL="jdbc:mysql://localhost:3306/demo";
    private static final String USER="root";
    private static final String PASSWORD="admin";

    public static void main(String[] args) {
        Scanner sc =  new Scanner(System.in);
        try(Connection connection = DriverManager.getConnection(URL,USER,PASSWORD)){//Trying to connect to SQL
            try {// trying to get balance
                connection.setAutoCommit(false);
                System.out.println("CONNECTIONS ESTABLISHED");
                int fromAcc=createAccount(connection,"Arnav",200);
                int toAcc=createAccount(connection,"Ayush",100);
                double transferingmoney=sc.nextDouble();
                double balance_in_fromAcc=getBalance(connection,fromAcc) ;
                if(balance_in_fromAcc<transferingmoney){
                    throw new InsufficiendfBalanceException(transferingmoney);
                }
                transfermoney(connection,fromAcc,toAcc,transferingmoney);
            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Operation RollBack Successfully");
                throw new RuntimeException(e);
            } catch (InsufficiendfBalanceException e) {
                System.out.println(e.getMessage());
                connection.rollback();
                System.out.println("Operation RollBack Successfully");
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.getStackTrace();
        }
        System.out.println("CONNECTIONS CLOSED");
    }

    private static double getBalance(Connection connection, int fromAcc) throws SQLException{
        try(PreparedStatement preparedStatement=connection.prepareStatement("SELECT balance from accounts where account_no = ?")){
//                    //trying to execute sql statement
            preparedStatement.setInt(1,fromAcc);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    double balance=resultSet.getDouble("balance");
                    return balance;
                }
                else{
                    throw new SQLException("Account not found :(");
                }
//                            try(PreparedStatement preparedStatementto=connection.prepareStatement("SELECT balance from accounts where id = ?")){
//                                preparedStatementto.setInt(1,2);
//                                if(resultSet.next()){
//                                    transfermoneyto=resultSet.getDouble("balance");
//                                }
//                            }
//                            catch (SQLException e){
//                                System.out.println("Account Not Fount at transfermoneyto");
//                                e.printStackTrace();
//                            }
            }
        }
    }

    private static void transfermoney(Connection connection, int fromAcc, int toAcc, double transfermoney) {
        try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO transfermoney (fromAcc,toAcc,transferamount) values (?,?,?)")){
            preparedStatement.setInt(1,fromAcc);
            preparedStatement.setInt(2,toAcc);
            preparedStatement.setDouble(3,transfermoney);
            int i1 = preparedStatement.executeUpdate();
            System.out.println("ROWS UPDATED IN TRANSFER MOEY TABLE :" +i1);
            try(PreparedStatement ps= connection.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE account_no = ?")) {
                ps.setDouble(1,transfermoney);
                ps.setInt(2,fromAcc);
                int i = ps.executeUpdate();
                System.out.println("ROWS UPDATED : "+i);
            }try(PreparedStatement ps= connection.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE account_no = ?")) {
                ps.setDouble(1,transfermoney);
                ps.setInt(2,toAcc);
                int i = ps.executeUpdate();
                System.out.println("ROWS UPDATED : "+i);
            }
            System.out.println("TRANSFERED SUCCESSFULLY");
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    private static int createAccount(Connection connection, String name, int balance) {
        try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ACCOUNTS (customer_name,balance) values(?,?)"
                ,Statement.RETURN_GENERATED_KEYS)){
            if(balance<0){
                throw new Exception("Enter a valid balnce");
            }
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,balance);
            int i = preparedStatement.executeUpdate();
            System.out.println("INSERTED ROWS :"+i);
            try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
                if(generatedKeys.next()){
                    return generatedKeys.getInt(1);
                }
                else{
                    throw new SQLException("Account not Opened");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.getStackTrace();
            return -1;
        }
    }

}
