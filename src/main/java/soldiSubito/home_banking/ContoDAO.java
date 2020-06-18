package soldiSubito.home_banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContoDAO {

	public static void save(Conto conto/*String owner, double totalAmount, String iban, String status, int tipoConto, Date dateCreazione*/) {
			String myQuery = "INSERT INTO conto(owner, total_amount, iban, status, count_type, created_at) VALUES (?,?,?,?,?,?)";
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement(myQuery);){
			preparedStatement.setString(1, conto.getOwner());
			preparedStatement.setDouble(2, conto.getTotalAmount());
			preparedStatement.setString(3, conto.getIban());
			preparedStatement.setString(4, conto.getStatus().toString());
			preparedStatement.setInt(5, conto.getCountType().getValue());
			preparedStatement.setDate(6, conto.getCreatedAt());
			preparedStatement.execute();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public static void findById(int id) {
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement("SELECT * FROM conto WHERE id = ?");) {
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			printUtenteFromRS(rs);
			myConnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void findByOwner(String owner) {
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement("SELECT * FROM conto WHERE owner LIKE ?");) {
			preparedStatement.setString(1, "%*" + owner + "*%");
			ResultSet rs = preparedStatement.executeQuery();
			printUtenteFromRS(rs);
			myConnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void findByIban(String iban) {
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement("SELECT * FROM conto WHERE iban = ?");) {
			preparedStatement.setString(1, iban);
			ResultSet rs = preparedStatement.executeQuery();
			printUtenteFromRS(rs);
			myConnection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void printUtenteFromRS(ResultSet rs) throws SQLException {
		while(rs.next()) {
			StringBuilder sb = new StringBuilder();
			sb.append("OWNER: " + rs.getString("OWNER") + "\n");
			sb.append("TOTAL_AMOUNT: " + rs.getDouble("TOTAL_AMOUNT") + "\n");
			sb.append("IBAN: " + rs.getString("IBAN") + "\n");
			sb.append("STATUS: " + rs.getString("STATUS") + "\n");
			sb.append("COUNT_TYPE: " + rs.getInt("COUNT_TYPE") + "\n");
			sb.append("CREATED_AT: " + rs.getDate("CREATED_AT") + "\n");
			System.out.println(sb.toString());
		}
	}

	public static void bonifico(String ibanPagante, String ibanRicevente, double soldi) {
		if(soldi <= 0) throw new IllegalArgumentException("Il bonifico non può essere inferiore od uguale a 0€");
		if(ibanPagante.contentEquals(ibanRicevente)) return;
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement1 = myConnection.prepareStatement("SELECT * FROM conto WHERE iban = '" + ibanPagante + "'");
				PreparedStatement preparedStatement2 = myConnection.prepareStatement("SELECT * FROM conto WHERE iban = '" + ibanRicevente + "'");
				PreparedStatement preparedStatement3 = myConnection.prepareStatement("UPDATE conto SET total_amount = total_amount - ? WHERE iban = ?");) {
			ResultSet conto1 = preparedStatement1.executeQuery();
			ResultSet conto2 = preparedStatement2.executeQuery();
			if (!conto1.next()) throw new IllegalArgumentException("L'IBAN del mandante non è corretto");
			if (!conto2.next()) throw new IllegalArgumentException("L'IBAN del ricevente non è corretto");
			if (conto1.getDouble("TOTAL_AMOUNT") < soldi) throw new IllegalArgumentException("Non ci sono abbastanza soldi sul conto per questa operazione");
			preparedStatement3.setDouble(1, soldi);
			preparedStatement3.setString(2, ibanPagante);
			preparedStatement3.execute();

			preparedStatement3.setDouble(1, -soldi);
			preparedStatement3.setString(2, ibanRicevente);
			preparedStatement3.execute();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
