package soldiSubito.home_banking;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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


@Path("/conto")
public class ContoDAO {

	@Path("/create")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response save(Conto conto) {
			String myQuery = "INSERT INTO conto(owner, total_amount, iban, status, count_type, created_at) VALUES (?,?,?,?,?,?)";
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement(myQuery);){
			preparedStatement.setString(1, conto.getOwner());
			preparedStatement.setDouble(2, conto.getTotalAmount());
			preparedStatement.setString(3, conto.getIban());
			preparedStatement.setString(4, conto.getStatus().toString());
			preparedStatement.setInt(5, conto.getCountType().getValue());
			preparedStatement.setDate(6, Date.valueOf(LocalDate.now()));
			preparedStatement.execute();
			return Response.ok().build();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return Response.status(400).build();
	}

	
	
	@Path("/findByID")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public static Response findById(@QueryParam("id") int id) {
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement("SELECT * FROM conto WHERE id = ?");) {
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			List<Conto> contiTrovati = createFromRS(rs);
			if(contiTrovati.size() == 1 && contiTrovati.get(0) != null)
			return Response.ok(createFromRS(rs).get(0).toJson()).build();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Response.status(400, "Non esiste un conto con questo id").build();
	}

	@Path("/findByOwner")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public static Response findByOwner(@QueryParam("owner") String owner) {
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement("SELECT * FROM conto WHERE owner LIKE ?");) {
			preparedStatement.setString(1, "%*" + owner + "*%");
			ResultSet rs = preparedStatement.executeQuery();
			List<Conto> contiTrovati = createFromRS(rs);
			if(contiTrovati.size()>=1){
				String sumJSON = "" ;
				for(Conto c :contiTrovati) {
					sumJSON+=c.toJson();
					}
				return Response.ok(sumJSON).build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Response.status(400, "Non esiste un conto con questo owner").build();
	}

	//Per path param usa:
	//@Path("/findByIBAN/{iban}") e pathParam
	@Path("/findByIBAN")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public static Response findByIban(@QueryParam("iban") String iban) {
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement("SELECT * FROM conto WHERE iban = ?");) {
			preparedStatement.setString(1, iban);
			ResultSet rs = preparedStatement.executeQuery();
			List<Conto> contiTrovati = createFromRS(rs);
			if(contiTrovati.size() == 1 && contiTrovati.get(0) != null)
				return Response.ok(contiTrovati.get(0).toJson()).build();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Response.status(400, "Non esiste un account con questo iban").build();
	}

	public static List<Conto> createFromRS(ResultSet rs) throws SQLException {
		List<Conto> contiTrovati = new ArrayList<Conto>(0);
		while(rs.next()) {
			Conto contoTrovato = new Conto(rs.getString("OWNER"), rs.getDouble("TOTAL_AMOUNT"),
					rs.getString("IBAN"), StatusConto.valueOf(rs.getString("STATUS")), CountType.getEnumValue(rs.getInt("COUNT_TYPE")));
			contoTrovato.setId(rs.getInt("ID"));
			System.out.println(contoTrovato.toString());
			contiTrovati.add(contoTrovato);
		}
		return contiTrovati;
	}

	@Path("/bonifico")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response bonifico(Pagamento pagamento) {
		if(pagamento.getSoldi() <= 0) throw new IllegalArgumentException("Il bonifico non può essere inferiore od uguale a 0€");
		if(pagamento.getIbanPagante().contentEquals(pagamento.getIbanRicevente())) return Response.status(400, "I due iban coincidono").build();
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement1 = myConnection.prepareStatement("SELECT * FROM conto WHERE iban = ?");
				PreparedStatement preparedStatement2 = myConnection.prepareStatement("SELECT * FROM conto WHERE iban = ?");
				PreparedStatement preparedStatement3 = myConnection.prepareStatement("UPDATE conto SET total_amount = total_amount - ? WHERE iban = ?");) {
			preparedStatement1.setString(1, pagamento.getIbanPagante());
			ResultSet conto1 = preparedStatement1.executeQuery();
			preparedStatement2.setString(1, pagamento.getIbanRicevente());
			ResultSet conto2 = preparedStatement2.executeQuery();
			if (!conto1.next()) throw new IllegalArgumentException("L'IBAN del mandante non è corretto");
			if (!conto2.next()) throw new IllegalArgumentException("L'IBAN del ricevente non è corretto");
			if (conto1.getDouble("TOTAL_AMOUNT") < pagamento.getSoldi()) throw new IllegalArgumentException("Non ci sono abbastanza soldi sul conto per questa operazione");
			preparedStatement3.setDouble(1, pagamento.getSoldi());
			preparedStatement3.setString(2, pagamento.getIbanPagante());
			preparedStatement3.execute();

			preparedStatement3.setDouble(1, -pagamento.getSoldi());
			preparedStatement3.setString(2, pagamento.getIbanRicevente());
			preparedStatement3.execute();
			return Response.ok("Il bonifico è andato a buon fine").build();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(400).build();
	}
}