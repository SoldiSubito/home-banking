package soldiSubito.home_banking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

	Statement stmt = null;
	ResultSet rs = null;

	public static Connection connect() {
		Connection conn = null;
		try {

			conn = DriverManager.getConnection("jdbc:mysql://sql7.freemysqlhosting.net/sql7347791?user=sql7347791&password=124D8LD19u");
		} catch (SQLException ex) {

			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			return null;

		}
		return conn;
	}
	
}
