package soldiSubito.home_banking.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.glassfish.jersey.internal.util.Property;

import soldiSubito.home_banking.CountType;
import soldiSubito.home_banking.DBConnection;
import soldiSubito.home_banking.StatusConto;
import soldiSubito.home_banking.entity.Activity;
import soldiSubito.home_banking.entity.Conto;
import soldiSubito.home_banking.entity.Pagamento;

public class ContoDAO {

	public static boolean saveCount(Conto conto) {
		String myQuery = "INSERT INTO conto(owner, total_amount, iban, status, count_type, created_at) VALUES (?,?,?,?,?,?)";
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement(myQuery);) {
			preparedStatement.setString(1, conto.getOwner());
			preparedStatement.setDouble(2, conto.getTotalAmount());
			preparedStatement.setString(3, conto.getIban());
			preparedStatement.setString(4, conto.getStatus().toString());
			preparedStatement.setInt(5, conto.getCountType().getValue());
			preparedStatement.setDate(6, Date.valueOf(LocalDate.now()));
			preparedStatement.execute();
			return true;
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return false;
	}

	public static Conto findById(int id) {
		Conto conto = null;
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection
						.prepareStatement("SELECT * FROM conto WHERE id = ?");) {
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {

				conto = new Conto(rs.getString("OWNER"), rs.getDouble("TOTAL_AMOUNT"), rs.getString("IBAN"),
						StatusConto.valueOf(rs.getString("STATUS")), CountType.getEnumValue(rs.getInt("COUNT_TYPE")));
				conto.setId(rs.getInt("ID"));
			}
			return conto;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Conto> findAll() {
		Conto conto = null;
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement("SELECT * FROM conto");) {

			ResultSet rs = preparedStatement.executeQuery();
			List<Conto> contiTrovati = createFromRS(rs);
			return contiTrovati;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Conto> findByOwner(int owner) {
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection
						.prepareStatement("SELECT * FROM conto WHERE owner LIKE ?");) {
			preparedStatement.setString(1, "%*" + owner + "*%");
			ResultSet rs = preparedStatement.executeQuery();
			List<Conto> contiTrovati = createFromRS(rs);
			return contiTrovati;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Conto findByIban(String iban) {

		Conto conto = null;
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection
						.prepareStatement("SELECT * FROM conto WHERE iban = ?");) {
			preparedStatement.setString(1, iban);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {

				conto = new Conto(rs.getString("OWNER"), rs.getDouble("TOTAL_AMOUNT"), rs.getString("IBAN"),
						StatusConto.valueOf(rs.getString("STATUS")), CountType.getEnumValue(rs.getInt("COUNT_TYPE")));
				conto.setId(rs.getInt("ID"));
			}
			return conto;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
	public static boolean bonifico(Pagamento pagamento) {

		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement1 = myConnection
						.prepareStatement("SELECT * FROM conto WHERE iban = ?");
				PreparedStatement preparedStatement2 = myConnection
						.prepareStatement("SELECT * FROM conto WHERE iban = ?");
				PreparedStatement preparedStatement3 = myConnection
						.prepareStatement("UPDATE conto SET total_amount = total_amount - ? WHERE iban = ?");) {

			preparedStatement1.setString(1, pagamento.getIbanPagante());
			ResultSet conto1 = preparedStatement1.executeQuery();
			preparedStatement2.setString(1, pagamento.getIbanRicevente());
			ResultSet conto2 = preparedStatement2.executeQuery();
			if (!conto1.next())
				Response.status(400, "L'IBAN del mandante non è corretto").build();
			if (!conto2.next())
				Response.status(400, "L'IBAN del ricevente non è corretto").build();
			if (conto1.getDouble("TOTAL_AMOUNT") < pagamento.getSoldi())
				Response.status(400, "Non ci sono abbastanza soldi sul conto per questa operazione").build();
			preparedStatement3.setDouble(1, pagamento.getSoldi());
			preparedStatement3.setString(2, pagamento.getIbanPagante());
			preparedStatement3.execute();

			preparedStatement3.setDouble(1, -pagamento.getSoldi());
			preparedStatement3.setString(2, pagamento.getIbanRicevente());
			preparedStatement3.execute();
			boolean ok = ContoDAO.saveActvity(conto1.getInt("ID"), conto2.getInt("ID"), pagamento.getSoldi());
			if (ok)
				return true;
			else
				return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean saveActvity(int id_p, int id_r, double amount) {

		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement4 = myConnection.prepareStatement(
						"INSERT INTO activity(id_conto,total,recipient,description, operation_type,create_at,status,currency) VALUES(?,?,?,?,?,?,?,?)");) {

			preparedStatement4.setInt(1, id_p); // id_conto mandante
			preparedStatement4.setDouble(2, amount); // amount
			preparedStatement4.setInt(3, id_r); // ricevente
			preparedStatement4.setString(4, "Esempio di causale"); // descrizione
			preparedStatement4.setString(5, "Bonifico"); // tipo operazione
			preparedStatement4.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now())); // create
			preparedStatement4.setString(7, "Pending"); // status
			preparedStatement4.setString(8, "EUR"); // currency
			preparedStatement4.execute();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	public static List<Activity> findActivityByContoID(int id) {

		List<Activity> activities = null;
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection
						.prepareStatement("SELECT * FROM activity WHERE id_conto = ?");) {
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			activities = createActivityFromRS(rs);
			return activities;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static List<Activity> findActivities() {
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection
						.prepareStatement("SELECT * FROM activity");) {
			ResultSet rs = preparedStatement.executeQuery();
			
			
			List<Activity> activities = createActivityFromRS(rs);
			return activities;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static List<Activity> createActivityFromRS(ResultSet rs) throws SQLException {
		List<Activity> activities = new ArrayList<Activity>(0);
		while (rs.next()) {
			Activity a = new Activity(rs.getInt("ID_CONTO"), rs.getDouble("TOTAL"), rs.getString("RECIPIENT"),
					rs.getString("DESCRIPTION"), rs.getString("OPERATION_TYPE"), rs.getDate("CREATE_AT"),
					rs.getDate("VALIDATED_AT"), rs.getString("STATUS"), rs.getString("CURRENCY"));
			a.setId(rs.getInt("ID"));
			
			
			activities.add(a);
		}
		return activities;
	}
	
	
	private static List<Conto> createFromRS(ResultSet rs) throws SQLException {
		List<Conto> contiTrovati = new ArrayList<Conto>(0);
		while (rs.next()) {
			Conto contoTrovato = new Conto(rs.getString("OWNER"), rs.getDouble("TOTAL_AMOUNT"), rs.getString("IBAN"),
					StatusConto.valueOf(rs.getString("STATUS")), CountType.getEnumValue(rs.getInt("COUNT_TYPE")));
			contoTrovato.setId(rs.getInt("ID"));
			System.out.println(contoTrovato.toString());
			contiTrovati.add(contoTrovato);
		}
		return contiTrovati;
	}

}