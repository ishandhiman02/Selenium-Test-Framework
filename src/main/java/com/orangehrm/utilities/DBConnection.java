package com.orangehrm.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.orangehrm.base.BaseClass;

//Data Base COnnectivity 
public class DBConnection {

	private static final String DB_URL = "jdbc:mysql://localhost:3306/orangehrm";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "";
	private static final Logger logger = BaseClass.logger;

	//to make the connection with DB
	public static Connection getDBConnection() {
		try {
//			System.out.println("Starting DB Connection....");
			logger.info("Starting DB Connection....");
			Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
//			System.out.println("DB Connection Successfull");
			logger.info("DB Connection Successfull");
			return conn;
		} catch (SQLException e) {
//			System.out.println("Error while establisig the DB Connection");
			logger.error("Error while establisig the DB Connection");
			e.printStackTrace();
			return null;
		}
	}
	
	// to get the employee details  /data from DB and store in a Map
	public static Map<String,String> getEmployeeDetails(String employee_id) {
		String query = "SELECT emp_firstname , emp_middle_name, emp_lastname FROM hs_hr_employee WHERE employee_id = "+employee_id;
		
		Map<String,String> empolyeeDetails = new HashMap<>();
		
		try(Connection conn = getDBConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)){
//			System.out.println("Executing query:" +query);
			logger.info("Executing query:" +query);
			if(rs.next()) {
				String firstName = rs.getString("emp_firstname");
				String middleName = rs.getString("emp_middle_name");
				String lastName = rs.getString("emp_lastname");

				//Store in a Map
				empolyeeDetails.put("firstName", firstName);
				empolyeeDetails.put("middleName", middleName!=null? middleName:"");
				empolyeeDetails.put("lastName", lastName);
				
//				System.out.println("Query Executed Successfully");
//				System.out.println("Employee Data Fetched:" +empolyeeDetails);
				logger.info("Query Executed Successfully");
				logger.info("Employee Data Fetched:" +empolyeeDetails );
			}else {
//				System.out.println("Employee Not Found");
				logger.error("Employee Not Found");
			}				
		}
		catch (Exception e) {
//			System.out.println("Erre while executing query");
			logger.info("Erre while executong query");
			e.printStackTrace();
		}
		return empolyeeDetails;
		
		

		
	}
}
