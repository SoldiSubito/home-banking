package soldiSubito.home_banking.api;

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
import soldiSubito.home_banking.entity.Conto;
import soldiSubito.home_banking.entity.User;

public class ContoApi {
	private int id;
	private String owner;
	private double totalAmount;
	private String iban;
	private StatusConto status;
	private CountType countType;

	@JsonbCreator
	public ContoApi(@JsonbProperty("owner") String owner, @JsonbProperty("totalAmount") double totalAmount,
			@JsonbProperty("iban") String iban, @JsonbProperty("status") String status,
			@JsonbProperty("countType") String countType) {
		this.owner = owner;
		this.totalAmount = totalAmount;
		this.iban = iban;
		this.status = StatusConto.valueOf(status);
		this.countType = CountType.valueOf(countType);
	}

	@Override
	public String toString() {
		return "\n\nID = " + id + "\nOwner = " + owner + "\nTotal amount = " + totalAmount + "\nIban = " + iban
				+ "\nStatus = " + status + "\nCount type = " + countType;
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

	public static ContoApi from(Conto c) throws ParseException {

		ContoApi contoApi = new ContoApi(c.getOwner(), c.getTotalAmount(), c.getIban(), c.getStatus().toString(),
				c.getCountType().toString());

		return contoApi;

	}

}