/*
 *
 * FALL 2021 Validation Code (DO NOT DELETE):  bc88iSTzud84fMXo
 *
 * Java program to connect Oracle Database
 *
 * Author: Vishaka Reddy Koripally
 *
 * Date: 12/18/2022
 */

 import java.io.BufferedReader;
 import java.io.InputStreamReader;
 import java.sql.*;
 import java.util.Scanner;
 
 public class Online_Grocery_Management_Oracle_Java {
 
     public static Connection connection;
     public static Statement statement;
 
 //************************************************************************************************
 //database connection method
 //************************************************************************************************
 
     private static void setConnection() {
         String username = "ORA_vrk106";
         String password = "******";
         try {
             DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
             System.out.println("Registered the driver...");
             //setting the connection with database
             connection = DriverManager.getConnection("jdbc:oracle:thin:@oracle2.wiu.edu:1521/orclpdb1", username, password);
             System.out.println("logged into oracle as " + username);
             //it saves all the changes that have been done till that particular point
             connection.setAutoCommit(false);
             statement = connection.createStatement();
         } catch (Exception ex) {
             System.out.println(ex.getMessage());
         }
     }
 
 //************************************************************************************************
 //Menu method to choose the options for various purposes listed below in the method
 //************************************************************************************************
 
     public static void showMenu() {
         System.out.println("please choose from the below list");
         System.out.println("Enter 1 to display a count of activities at a camp");
         System.out.println("Enter 2 to see the customers for an activity");
         System.out.println("Enter 3 to update a customer");
         System.out.println("Enter 4 to show the Attendance Report");
         System.out.println("Enter 5 to exit");
     }
 
 //*******************************************************************************************************
 //Code for question 1 to retrieve the number of activities that have been scheduled for a particular camp
 //*******************************************************************************************************
 
     private static void displayActivitiesScheduledByCampName(String campName) {
         try {
             String sqlCommand = "select camps.camp_name,count(scheduled_activities.aname) as number_of_activities\n" +
                     "                    from camps\n" +
                     "                    left outer join scheduled_activities\n" +
                     "                    on scheduled_activities.camp_id = camps.camp_id\n" +
                     "                    where Camp_name ='" + campName + "'" +
                     "                    group by camps.camp_name\n" +
                     "                    order by number_of_activities";
             ResultSet resultSet = statement.executeQuery(sqlCommand);
 
             while (resultSet.next()) {
                 System.out.println(
                         campName + " has " + resultSet.getInt("number_of_activities") + " activities scheduled");
             }
             resultSet.close();
         } catch (Exception e) {
             System.out.println("could not perform the job");
         }
     }
 
 //************************************************************************************************
 //Code for question 2 to retrieve every customer who has participated in a particular activity
 //************************************************************************************************
 
     private static void customerInformation(String activity) {
         try {
             String sqlCommand = "SELECT DISTINCT NAME, ADDRESS, PHONE# \n" +
                     "FROM LabDataF21.Customers\n" +
                     "INNER JOIN\n" +
                     "LabDataF21.Participated_In\n" +
                     "ON Customers.CID = Participated_In.CID \n" +
                     "INNER JOIN\n" +
                     "LabDataF21.Scheduled_Activities\n" +
                     "ON Participated_In.SCHEDULE_ID = Scheduled_Activities.SCHEDULE_ID \n" +
                     "INNER JOIN\n" +
                     "LabDataF21.Activities \n" +
                     "on Scheduled_Activities.ANAME = Activities.ANAME \n" +
                     "WHERE Activities.ANAME = '" + activity + "'";
             ResultSet resultSet = statement.executeQuery(sqlCommand);
             System.out.println("--------\t\t-----------\t\t----------------- ");
             System.out.println("Name \t\t Address \t\t Phone# ");
             System.out.println("---------\t\t-----------\t\t----------------- ");
             while (resultSet.next()) {
                 System.out.println(resultSet.getString(1) + " \t " + resultSet.getString(2) + " \t " + resultSet.getString(3));
             }
             resultSet.close();
         } catch (Exception ex) {
             System.out.println(ex.getMessage());
         }
     }
 
 //************************************************************************************************
 //Code for question 3 to update a row in my copy of the Customers table
 //************************************************************************************************
 
     private static void updateCustomer(String sqlCommand) {
         try {
             int result = statement.executeUpdate(sqlCommand);
             connection.close();
             if (result > 0) {
                 System.out.println(result + " row updated");
             } else {
                 System.out.println("updating failed");
             }
         } catch (Exception ex) {
             System.out.println(ex.getMessage());
         }
     }
 
 //************************************************************************************************
 //Code for question 4 to print an “Attendance” report for the Camp Manager.
 //************************************************************************************************
 
     private static void customerAttendanceReport(String campLocation) throws SQLException {
         ResultSet resultSet = statement.executeQuery(
                 "SELECT Camps.CAMP_ID, Camps.CAMP_NAME FROM LabDataF21.Camps Camps WHERE CAMP_LOCATION ='" + campLocation + "'");
         while (resultSet.next()) {
             int camp_Id = resultSet.getInt("CAMP_ID");
             String camp_name = resultSet.getString("CAMP_NAME");
             System.out.println("camp ID : " + camp_Id + " and Camp Name : " + camp_name);
             System.out.println("\t Name \t\t Address \t\t Email");
             System.out.println("--------------------------------------");
             Statement st=connection.createStatement();
             ResultSet rset = st.executeQuery(
                     "SELECT Customers.NAME, Customers.ADDRESS, Customers.EMAIL FROM LabDataF21.Customers Customers WHERE Customers.CAMP_ID = '" + camp_Id + "'");
             while (rset.next()) {
 
                 System.out.println(rset.getString("NAME") + " \t " + rset.getString("ADDRESS") + " \t " + rset.getString("EMAIL"));
             }
             System.out.println("--------------------------------------");
         }
     }
 
 //************************************************************************************************
 //Main method
 //************************************************************************************************
 
     public static void main(String[] args) {
         setConnection();
         boolean isAlive = true;
         while (isAlive) {
             showMenu();
             try {
                 BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
                 Scanner sc = new Scanner(System.in);
                 int userChoice = 0;
                 userChoice = sc.nextInt();
                 switch (userChoice) {
                     case 1:
                         System.out.println("Enter the Camp Name");
                         String campName = keyboard.readLine();
                         displayActivitiesScheduledByCampName(campName);
                         break;
                     case 2:
                         System.out.println("Enter the activity Name");
                         String activityName = keyboard.readLine();
                         customerInformation(activityName);
                         break;
                     case 3:
                         System.out.println("Enter the  CID (Customer ID)");
                         String customer_ID = keyboard.readLine();
                         System.out.println("Enter the Column name");
                         String columnName = keyboard.readLine();
                         System.out.println("Enter the new value for " + columnName);
                         String value = keyboard.readLine();
                         String username = "ORA_vrk106";
                         String sqlCommand="";
                         if(columnName.equals("CAMP_ID")){
                             sqlCommand = "UPDATE " + username + ".Customers SET " + columnName + " = " + value + " WHERE CID = '" + customer_ID + "'";
                         }
                         else {
                             sqlCommand = "UPDATE " + username + ".Customers SET " + columnName + " = '" + value + "' WHERE CID = '" + customer_ID + "'";
                         }
                         updateCustomer(sqlCommand);
                         break;
                     case 4:
                         System.out.println("Enter the Camp Location");
                         String campLocation = keyboard.readLine();
                         customerAttendanceReport(campLocation);
                         break;
                     case 5:
                         System.out.println("Exiting program");
                         isAlive = false;
                         break;
                     default:
                         System.out.println("Invalid option");
 
                 }
             } catch (Exception ex) {
                 System.out.println(ex.getMessage());
             }
         }
     }
 }
 