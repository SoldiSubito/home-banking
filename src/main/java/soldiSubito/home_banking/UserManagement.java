package soldiSubito.home_banking;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.config.PropertyVisibilityStrategy;

public class UserManagement {

	//POST
	public static void login(String cf, String password) {
		String myQuery = "SELECT * FROM user WHERE fiscal_code = ? AND password = ?";
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement(myQuery);) {
			preparedStatement.setString(1, cf);
			preparedStatement.setString(2, password);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()) {
				StringBuilder sb = new StringBuilder();
				sb.append(rs.getString("NAME") + "\n");
				sb.append(rs.getString("SURNAME") + "\n");
				sb.append(rs.getString("FISCAL_CODE") + "\n");
				sb.append(rs.getString("PASSWORD") + "\n");
				sb.append(rs.getDate("BIRTH_DATE") + "\n");
				sb.append(rs.getInt("CONTACT") + "\n");
				System.out.println(sb.toString());
			}else {
				System.out.println("Nome utente o password non corrispondono.");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//risposta tutti i parametri per identificare l'utente (TOKEN NECESSARIO)
	}
	
	//POST
	public static void register(
		String name,
		String surname,
		Date dateOfBirth,
		Gender gender,
		String birthPlace,
		String livingPlace,
		String cf,
		String phoneNumber,
		String eMail,
		String identityId,
		String password) {
		//	long age = ChronoUnit.YEARS.between(dateOfBirth, Date.valueOf(date));
		//	if (age < 18) throw new IllegalArgumentException("Devi avere almeno 18 anni per creare un account.");
			if (name.isBlank()) throw new IllegalArgumentException("Il nome non può essere vuoto.");
			if (surname.isBlank()) throw new IllegalArgumentException("Il cognome non può essere vuoto.");
			if (birthPlace.isBlank()) throw new IllegalArgumentException("Il luogo di nascita non può essere vuoto.");
			if (livingPlace.isBlank()) throw new IllegalArgumentException("La residenza non può essere vuota.");
			//if (LocalDate.now() < dateOfBirth) throw new IllegalArgumentException("La data di nascita non può essere nel futuro.");
			if (!isValidFiscalCode(cf)) throw new IllegalArgumentException("Il codice fiscale non è corretto");
			if (!isValidNumeroFisso(phoneNumber.trim()) &&
					!isValidNumeroMobile(phoneNumber.trim())) throw new IllegalArgumentException("Il numero di telefono non è corretto");
			if (!isValidMail(eMail)) throw new IllegalArgumentException("L'email non è corretta");
			//forse controllo identity Id
			int generatedId = saveGenerics(livingPlace, eMail, phoneNumber, birthPlace);
		//	DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");  
		//	String strDate = dateFormat.format(dateOfBirth)
			saveUser(new String[] {name, surname, cf, password, Integer.toString(generatedId)}, dateOfBirth);
		}

	
		
		
	
	public void modifyPhoneNumber() {
		
	}
	public void identityId() {
		
	}
	
	public static void logout() {
		
	}
	
	public static boolean isValidFiscalCode(String cf) {
		String regex  = "^[A-Za-z]{6}[0-9]{2}[A-Za-z]{1}[0-9]{2}[A-Za-z]{1}[0-9]{3}[A-Za-z]{1}$";
		return cf.matches(regex);
	}
	private static boolean isValidMail(String email) {
		String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
		return email.matches(regex);
	}
	private static boolean isValidNumeroFisso(String numero) {
		String regex = "^[0-9]{9}$";
		return numero.matches(regex);
	}

	private static boolean isValidNumeroMobile(String numero) {
		String regex = "^[0-9]{10}$";
		return numero.matches(regex);
	}
	private static int saveGenerics(String lp, String em, String pn, String bp) {
		
		String myQuery = "INSERT INTO generics(living_place, email, phone_number, birth_place) VALUES (?,?,?,?)";
		
		int generatedKey = -1;
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement(myQuery, Statement.RETURN_GENERATED_KEYS);){
			preparedStatement.setString(1, lp);
			preparedStatement.setString(2, em);
			preparedStatement.setString(3, pn);
			preparedStatement.setString(4, bp);
			generatedKey = preparedStatement.executeUpdate();
		    ResultSet rs = preparedStatement.getGeneratedKeys();
		    rs.next();
		    generatedKey = rs.getInt(1);
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return generatedKey;
	}
	
	public static void deleteUserById(int id ) {
		//myQuery non funziona
		String myQuery = "delete from generics where id = (select contact from user where user.id = "+id+")";
		String myQuery2 = "delete from user where user.id = "+id;
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement(myQuery);
				PreparedStatement preparedStatement2 = myConnection.prepareStatement(myQuery2);
				){
			preparedStatement.execute();
			preparedStatement2.execute();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("VendorError: " + e.getErrorCode());
			e.printStackTrace();
		}
			
	}
	
	
	
	private static void saveUser(String[] data, Date dateOfBirth) {
		String myQuery = "INSERT INTO user(name, surname, fiscal_code, password, birth_date, contact, create_at, update_at)" + 
		" VALUES (?,?,?,?,?,?,?,?)";
		
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement(myQuery);){
			preparedStatement.setString(1, data[0]);
			preparedStatement.setString(2, data[1]);
			preparedStatement.setString(3, data[2]);
			preparedStatement.setString(4, data[3]);
			
			//Date date=Date.valueOf(data[4]); 
			preparedStatement.setDate(5, dateOfBirth);
			preparedStatement.setInt(6, Integer.parseInt(data[4]));
			preparedStatement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
			preparedStatement.setTimestamp(8,Timestamp.valueOf(LocalDateTime.now()));
			//preparedStatement.setInt(9, Integer.parseInt(data[6]));
			preparedStatement.execute();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
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
