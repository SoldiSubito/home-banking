package soldiSubito.home_banking;

public class User {
	private String name;
	private String surname;
	private Date dateOfBirth;
	private String token;
	//private Gender gender;
	//private String birthPlace;
	//private String livingPlace;
	private String cf;
	//private String phoneNumber;
	//private String eMail;
	//private String identityId;
	public User(String name, String surname, Date dateOfBirth, String token, String cf) {
		this.name = name;
		this.surname = surname;
		this.dateOfBirth = dateOfBirth;
		this.token = token;
		this.cf = cf;
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
//	public void setDateOfBirth(Date dateOfBirth) {
//		
//		this.dateOfBirth = dateOfBirth;
//	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getCf() {
		return cf;
	}
	public void setCf(String cf) {
		this.cf = cf;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cf == null) ? 0 : cf.hashCode());
		result = prime * result + ((dateOfBirth == null) ? 0 : dateOfBirth.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (cf == null) {
			if (other.cf != null)
				return false;
		} else if (!cf.equals(other.cf))
			return false;
		if (dateOfBirth == null) {
			if (other.dateOfBirth != null)
				return false;
		} else if (!dateOfBirth.equals(other.dateOfBirth))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (surname == null) {
			if (other.surname != null)
				return false;
		} else if (!surname.equals(other.surname))
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "User [name=" + name + ", surname=" + surname + ", dateOfBirth=" + dateOfBirth + ", cf=" + cf + "]";
	}
	
	/*Domanda segreta (nome del tuo migliore amico)*/
	
	
	
	
	
}
