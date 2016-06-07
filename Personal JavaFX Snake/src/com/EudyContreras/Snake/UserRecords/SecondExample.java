package com.EudyContreras.Snake.UserRecords;

//STEP 1. Import required packages
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class SecondExample {
	static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	// get current date time with Date()
	static java.util.Date date = new java.util.Date();
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/user_scores";

	// Database credentials
	static final String USER = "guess_account";
	static final String PASS = "";

	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		String selectSql;
	//	String insertSql;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to a selected database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Connected database successfully...");

			// STEP 4: Execute a query
			System.out.println("Inserting records into the table...");
			stmt = conn.createStatement();
			selectSql = "SELECT id, name, email, age, score, date, country FROM user_record";
//			insertSql = "INSERT INTO user_record "+"VALUES (0, 'Markus Vasdekis', 'Markus@gmail.com', 28, 350,'"+dateFormat.format(date)+"', 'Sweden')";
//			stmt.executeUpdate(insertSql);
			// insertSql = "INSERT INTO user_record "+"VALUES (0, 'Jessica
			// Wernberg', 'JessicaWernberg@gmail.com', 25, 120,
			// '"+dateFormat.format(date)+"', 'Sweden')";
			// stmt.executeUpdate(insertSql);
			System.out.println("Inserted records into the table...");
			ResultSet rs = stmt.executeQuery(selectSql);
			// stmt.executeUpdate(sql2);
			while (rs.next()) {
				// Retrieve by column name
				int id = rs.getInt("id");
				int age = rs.getInt("age");
				int score = rs.getInt("score");
				String name = rs.getString("name");
				String country = rs.getString("country");
				String date = rs.getString("date");
				String email = rs.getString("email");

				// Display values
				System.out.print("ID: " + id);
				System.out.print(", Age: " + age);
				System.out.print(", score: " + score);
				System.out.print(", Name: " + name);
				System.out.print(", Date: " + date);
				System.out.print(", Country: " + country);
				System.out.println(", Email: " + email);
			}
			// STEP 6: Clean-up environment
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			} // do nothing
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		} // end try
		System.out.println("Goodbye!");
	}// end main
}// end JDBCExample