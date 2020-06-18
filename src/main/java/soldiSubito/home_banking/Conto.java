package soldiSubito.home_banking;

import java.sql.Date;

public class Conto{
	private int id;
	private String owner;
	private double totalAmount;
	private String iban;
	private StatusConto status;
	private CountType countType;
	private Date createdAt;
	
	public Conto(String owner, double totalAmount, String iban, StatusConto status, CountType countType,
			Date createdAt) {
		this.owner = owner;
		this.totalAmount = totalAmount;
		this.iban = iban;
		this.status = status;
		this.countType = countType;
		this.createdAt = createdAt;
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
	public Date getCreatedAt() {
		return createdAt;
	}
}