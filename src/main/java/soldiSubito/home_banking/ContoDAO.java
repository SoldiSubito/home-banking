package soldiSubito.home_banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContoDAO {

	public static void save(Conto conto) {
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

	public static Conto findById(int id) {
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement("SELECT * FROM conto WHERE id = ?");) {
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			return createFromRS(rs).get(0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static List<Conto> findByOwner(String owner) {
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement("SELECT * FROM conto WHERE owner LIKE ?");) {
			preparedStatement.setString(1, "%*" + owner + "*%");
			ResultSet rs = preparedStatement.executeQuery();
			return createFromRS(rs);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Conto findByIban(String iban) {
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement("SELECT * FROM conto WHERE iban = ?");) {
			preparedStatement.setString(1, iban);
			ResultSet rs = preparedStatement.executeQuery();
			return createFromRS(rs).get(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Conto> createFromRS(ResultSet rs) throws SQLException {
		List<Conto> contiTrovati = new ArrayList<Conto>();
		while(rs.next()) {
			Conto contoTrovato = new Conto(rs.getString("OWNER"), rs.getDouble("TOTAL_AMOUNT"),
					rs.getString("IBAN"), StatusConto.valueOf(rs.getString("STATUS")), CountType.getEnumValue(rs.getInt("COUNT_TYPE")), rs.getDate("CREATED_AT"));
			contoTrovato.setId(rs.getInt("ID"));
			contiTrovati.add(contoTrovato);
		}
		return contiTrovati;
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