package soldiSubito.home_banking.api;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.config.PropertyVisibilityStrategy;

import soldiSubito.home_banking.entity.Pagamento;
import soldiSubito.home_banking.entity.PagamentoApi;

public class PagamentoApi {
	private String ibanPagante, ibanRicevente;
	private double soldi;
	
	@JsonbCreator
	public PagamentoApi(@JsonbProperty("ibanP") String ibanPagante,
			@JsonbProperty("ibanR") String ibanRicevente, @JsonbProperty("amount") double soldi) {
		this.ibanPagante = ibanPagante;
		this.ibanRicevente = ibanRicevente;
		this.soldi = soldi;
	}

	public String getIbanPagante() {
		return ibanPagante;
	}

	public String getIbanRicevente() {
		return ibanRicevente;
	}

	public double getSoldi() {
		return soldi;
	}
	

	public String toJson() {
		JsonbConfig config = new JsonbConfig().withPropertyVisibilityStrategy(new PropertyVisibilityStrategy() {
			
			@Override
			public boolean isVisible(Method arg0) {
				return false;
			}
			
			@Override
			public boolean isVisible(Field arg0) {
				return true;
			}
		});
		return JsonbBuilder.newBuilder().withConfig(config).build().toJson(this);
	}
	
	public static PagamentoApi from(Pagamento u) {

		/*User user = new User(u.getName(), u.getSurname(), u.getDateOfBirth().toString(), u.getGender(),
				u.getBirthPlace(), u.getLivingPlace(), u.getCf(), u.getPhoneNumber(), u.geteMail(), u.getIdentityId(),
				u.getPassword());*/
		
		//PagamentoApi pagamentoApi = new Pagame..

		return null;

	}
}
