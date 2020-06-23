package soldiSubito.home_banking;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import soldiSubito.home_banking.api.ContoApi;
import soldiSubito.home_banking.api.FindCountResponse;
import soldiSubito.home_banking.api.FindUsersResponse;
import soldiSubito.home_banking.api.PagamentoApi;
import soldiSubito.home_banking.api.UserApi;
import soldiSubito.home_banking.database.ContoDAO;
import soldiSubito.home_banking.database.UserDAO;
import soldiSubito.home_banking.entity.Conto;
import soldiSubito.home_banking.entity.Pagamento;
import soldiSubito.home_banking.entity.User;

@Path("/conto")
public class ContoController {

	@Path("/bonifico")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response bonifico(PagamentoApi pagamentoApi) {
		if (pagamentoApi.getSoldi() <= 0)
			throw new IllegalArgumentException("Il bonifico non può essere inferiore od uguale a 0€");
		if (pagamentoApi.getIbanPagante().contentEquals(pagamentoApi.getIbanRicevente()))
			return Response.status(400, "I due iban coincidono").build();


		Pagamento pagam = Pagamento.from(pagamentoApi);
		if(ContoDAO.bonifico(pagam)) return Response.ok("Bonifico eseguito correttamente").build();
		else return Response.status(400, "Errore nell'operazione").build();

	}

	@Path("/create")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response save(ContoApi contoApi) throws ParseException {

		Conto conto = Conto.from(contoApi);

		if (ContoDAO.saveCount(conto))
			return Response.ok().build();
		else
			return Response.status(403).build();

	}

	@Path("/findByID")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public static Response findById(@QueryParam("id") int id) {

		Conto conto = null;

		if (ContoDAO.findById(id) != null) {
			conto = ContoDAO.findById(id);
			return Response.ok(conto.toJson()).build();
		} else
			return Response.status(400, "Non esiste un conto con questo id").build();
	}

	@Path("/findAll")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public static Response findAll() throws ParseException {

		List<Conto> conto = null;
		conto = ContoDAO.findAll();
		if (conto != null) {
			List<ContoApi> cApi = new ArrayList<>();

			for (Conto c : conto) {
				cApi.add(ContoApi.from(c));
			}

			FindCountResponse fConto = new FindCountResponse(cApi);

			return Response.ok(fConto.toJson()).build();

		} else
			return Response.status(404, "Conto not found").build();

	}

	@Path("/findByOwner")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public static Response findByOwner(@QueryParam("owner") int owner) throws ParseException {
		List<Conto> conto = null;

		conto = ContoDAO.findByOwner(owner);

		if (conto != null) {
			List<ContoApi> cApi = new ArrayList<>();

			for (Conto c : conto) {
				cApi.add(ContoApi.from(c));
			}

			FindCountResponse fConto = new FindCountResponse(cApi);

			return Response.ok(fConto.toJson()).build();
		} else
			return Response.status(404, "Conto not found").build();
	}

	// Per path param usa:
	// @Path("/findByIBAN/{iban}") e pathParam
	@Path("/findByIBAN")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public static Response findByIban(@QueryParam("iban") String iban) {

		Conto conto = ContoDAO.findByIban(iban);

		if (conto != null) {

			return Response.ok(conto.toJson()).build();
		} else
			return Response.status(400, "Non esiste un account con questo iban").build();

	}

}
