package soldiSubito.home_banking;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.config.PropertyVisibilityStrategy;

public class User {
	private String name;
	private String surname;
	private Date dateOfBirth;
	private String token;
	private Gender gender;
	private String birthPlace;
	private String livingPlace;
	private String cf;
	private String phoneNumber;
	private String eMail;
	private String identityId;
	private String password;
	private Integer id;
	
	//ToRegister
	@JsonbCreator


	public User(@JsonbProperty("name") String name, @JsonbProperty("surname") String surname,@JsonbProperty("dateOfBirth") String dateOfBirth, @JsonbProperty("gender") Gender gender,@JsonbProperty("birth_place") String birthPlace, @JsonbProperty("living_place") String livingPlace,
			@JsonbProperty("cf") String cf,@JsonbProperty("phone_number") String phoneNumber, @JsonbProperty("email") String eMail, @JsonbProperty("identityId") String identityId, @JsonbProperty("password") String password) throws ParseException {
		this.name = name;
		this.surname = surname;
		Date date = Date.valueOf(dateOfBirth);
		this.dateOfBirth = date;
		this.password = password;
		this.gender = gender;
		this.birthPlace = birthPlace;
		this.livingPlace = livingPlace;
		this.cf = cf;
		this.phoneNumber = phoneNumber;
		this.eMail = eMail;
		this.identityId = identityId;
		this.gender = gender;
		
	}
	

	public User(@JsonbProperty("living_place") String livingPlace, @JsonbProperty("email") String eMail,
			@JsonbProperty("phone_number") String phoneNumber, @JsonbProperty("id_user") Integer id_user) {
		this.livingPlace = livingPlace;
		this.eMail = eMail;
		this.phoneNumber = phoneNumber;
		this.id = id_user;
	}
	
	public User(@JsonbProperty("name") String name, @JsonbProperty("surname") String surname,@JsonbProperty("dateOfBirth") Date dateOfBirth, @JsonbProperty("token") String token, @JsonbProperty("cf") String cf) {
		this.name = name;
		this.surname = surname;
		this.dateOfBirth = dateOfBirth;
		this.token = token;
		this.cf = cf;
		
	}
	
	public User() {
	}

	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getBirthPlace() {
		return birthPlace;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public String getLivingPlace() {
		return livingPlace;
	}

	public void setLivingPlace(String livingPlace) {
		this.livingPlace = livingPlace;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public String getIdentityId() {
		return identityId;
	}

	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	/*
	 * 
	 * 
	 * {response:{
	 * 				message : "Nome non corretto"}
	 * }
	 * 
	 */
	
	
	
}
