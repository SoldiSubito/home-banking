package soldiSubito.home_banking.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;

import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.config.PropertyVisibilityStrategy;

import soldiSubito.home_banking.CountType;
import soldiSubito.home_banking.StatusConto;
import soldiSubito.home_banking.api.ContoApi;

public class Conto{
	private int id;
	private String owner;
	private double totalAmount;
	private String iban;
	private StatusConto status;
	private CountType countType;

	
	public Conto(String owner, double totalAmount, String iban,
			 StatusConto status, CountType countType) {
		this.owner = owner;
		this.totalAmount = totalAmount;
		this.iban = iban;
		this.status = status;
		this.countType = countType;
	}
	
	@Override
	public String toString() {
		return "\n\nID = " + id + "\nOwner = " + owner + "\nTotal amount = " + totalAmount + "\nIban = " + iban + "\nStatus = "
				+ status + "\nCount type = " + countType;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	public int getId() {
		return id;
	}
	
	
	public String getOwner() {
		return owner;
	}
	
	
	public double getTotalAmount() {
		return totalAmount;
	}
	
	
	public String getIban() {
		return iban;
	}
	
	
	public StatusConto getStatus() {
		return status;
	}
	
	
	public CountType getCountType() {
		return countType;
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
	
	
	public static Conto from(ContoApi c) throws ParseException {

		Conto conto = new Conto(c.getOwner(), c.getTotalAmount(), c.getIban(), c.getStatus(),
				c.getCountType());

		return conto;

	}
}