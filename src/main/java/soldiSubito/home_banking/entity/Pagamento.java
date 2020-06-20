package soldiSubito.home_banking.entity;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;

public class Pagamento {
	private String ibanPagante, ibanRicevente;
	private double soldi;
	
	@JsonbCreator
	public Pagamento(@JsonbProperty("ibanP") String ibanPagante,
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
}
