package soldiSubito.home_banking.entity;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;

import soldiSubito.home_banking.api.PagamentoApi;
import soldiSubito.home_banking.api.UserApi;

public class Pagamento {
	private String ibanPagante, ibanRicevente;
	private double soldi;

	public Pagamento(String ibanPagante,
			String ibanRicevente, double soldi) {
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
	
	
	public static Pagamento from(PagamentoApi u) {

		/*User user = new User(u.getName(), u.getSurname(), u.getDateOfBirth().toString(), u.getGender(),
				u.getBirthPlace(), u.getLivingPlace(), u.getCf(), u.getPhoneNumber(), u.geteMail(), u.getIdentityId(),
				u.getPassword());*/
		
		//Pagamento pagamento = new Pagame..

		return null;

	}
}
