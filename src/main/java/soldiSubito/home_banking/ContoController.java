package soldiSubito.home_banking;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import soldiSubito.home_banking.api.PagamentoApi;
import soldiSubito.home_banking.database.ContoDAO;
import soldiSubito.home_banking.entity.Pagamento;

public class ContoController {
	
	
	@Path("/bonifico")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	//PagamentoApi
	public static Response bonifico(PagamentoApi pagamento) {
		if(pagamento.getSoldi() <= 0) throw new IllegalArgumentException("Il bonifico non può essere inferiore od uguale a 0€");
		if(pagamento.getIbanPagante().contentEquals(pagamento.getIbanRicevente())) return Response.status(400, "I due iban coincidono").build();
		
		
		//da pagamentoApi creo pagamento ---> uso pagamento per la chiamata al database
		
		Pagamento pagam = Pagamento.from(pagamento);
		ContoDAO.bonifico2(pagam);
		
		
		return Response.ok().build();
	}

}
