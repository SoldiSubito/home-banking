package soldiSubito.home_banking;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;

public class Pagamento {
	private String ibanPagante, ibanRicevente;
	private double soldi;
	
	@JsonbCreator
	public Pagamento(@JsonbProperty("ibanPagante") String ibanPagante,
			@JsonbProperty("ibanRicevente") String ibanRicevente, @JsonbProperty("soldi") double soldi) {
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
