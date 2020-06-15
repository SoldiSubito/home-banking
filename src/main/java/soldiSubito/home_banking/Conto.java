package soldiSubito.home_banking;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Conto {

	public static void createConto(String owner, double totalAmount, String iban, String status, int tipoConto, Date dateCreazione) {

		String myQuery = "INSERT INTO conto(owner, total_amount, iban, status, count_type, created_at)" + 
		" VALUES (?,?,?,?,?,?)";
		
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement(myQuery);){
			preparedStatement.setString(1, owner);
			preparedStatement.setDouble(2, totalAmount);
			preparedStatement.setString(3, iban);
			preparedStatement.setString(4, status);
			preparedStatement.setInt(5, tipoConto);
			preparedStatement.setDate(6, dateCreazione);
			preparedStatement.execute();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	private static void find(String myQuery) {
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement(myQuery);) {
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				StringBuilder sb = new StringBuilder();
				sb.append(rs.getString("OWNER") + "\n");
				sb.append(rs.getDouble("TOTAL_AMOUNT") + "\n");
				sb.append(rs.getString("IBAN") + "\n");
				sb.append(rs.getString("STATUS") + "\n");
				sb.append(rs.getInt("COUNT_TYPE") + "\n");
				sb.append(rs.getDate("CREATED_AT") + "\n");
				System.out.println(sb.toString());
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void findById(int id) {
		find("SELECT * FROM conto WHERE id = " + id);
	}
	public static void findByOwner(String owner) {
		find("SELECT * FROM conto WHERE owner LIKE " + "'%" + owner + "%'");
	}
}
