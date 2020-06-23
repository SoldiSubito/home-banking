package soldiSubito.home_banking.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.config.PropertyVisibilityStrategy;

public class Activity {
	
	private int id;
	private int id_conto;
	private double amount;
	private String recipient;
	private String description;
	private String operation_type;
	private Date create_at;
	private Date validated_at;
	private String status;
	private String currency;
	public Activity(int id_conto, double amount, String recipient, String description, String operation_type,
			Date create_at, Date validated_at, String status, String currency) {
		super();
		this.id_conto = id_conto;
		this.amount = amount;
		this.recipient = recipient;
		this.description = description;
		this.operation_type = operation_type;
		this.create_at = create_at;
		this.validated_at = validated_at;
		this.status = status;
		this.currency = currency;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId_conto() {
		return id_conto;
	}
	public void setId_conto(int id_conto) {
		this.id_conto = id_conto;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOperation_type() {
		return operation_type;
	}
	public void setOperation_type(String operation_type) {
		this.operation_type = operation_type;
	}
	public Date getCreate_at() {
		return create_at;
	}
	public void setCreate_at(Date create_at) {
		this.create_at = create_at;
	}
	public Date getValidated_at() {
		return validated_at;
	}
	public void setValidated_at(Date validated_at) {
		this.validated_at = validated_at;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
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
	
	

}
