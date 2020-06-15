package soldiSubito.home_banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Scanner;

public class UserManagement {

	public static void login(String cf, String password) {
		
	}
	public static void register(
		String name,
		String surname,
		LocalDate dateOfBirth,
		Gender gender,
		String birthPlace,
		String livingPlace,
		String cf,
		String phoneNumber,
		String eMail,
		String identityId) {
			long age = ChronoUnit.YEARS.between(dateOfBirth, LocalDate.now());
			if (age < 18) throw new IllegalArgumentException("Devi avere almeno 18 anni per creare un account.");
			if (name.isBlank()) throw new IllegalArgumentException("Il nome non può essere vuoto.");
			if (surname.isBlank()) throw new IllegalArgumentException("Il cognome non può essere vuoto.");
			if (birthPlace.isBlank()) throw new IllegalArgumentException("Il luogo di nascita non può essere vuoto.");
			if (livingPlace.isBlank()) throw new IllegalArgumentException("La residenza non può essere vuota.");
			if (LocalDate.now().isBefore(dateOfBirth)) throw new IllegalArgumentException("La data di nascita non può essere nel futuro.");
			if (!isValidFiscalCode(cf)) throw new IllegalArgumentException("Il codice fiscale non è corretto");
			if (!isValidNumeroFisso(phoneNumber.trim()) ||
					!isValidNumeroMobile(phoneNumber.trim())) throw new IllegalArgumentException("Il numero di telefono non è corretto");
			if (!isValidMail(eMail)) throw new IllegalArgumentException("L'email non è corretta");
			//forse controllo identity Id
			int generatedId = saveGenerics(livingPlace, eMail, phoneNumber, birthPlace);
			saveUser(String[] {name, surname, cf, "token", dateOfBirth, generatedId});
		}

		
		
		
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
		Connection myConnection = DBConnection.connect();
		String myQuery = "INSERT INTO generics(livingPlace, eMail, phoneNumber, birthPlace) VALUES (?,?,?,?)";
		PreparedStatement preparedStatement = myConnection.prepareStatement(myQuery, Statement.RETURN_GENERATED_KEYS);
		int generatedKey = -1;
		try {
			preparedStatement.setString(1, lp);
			preparedStatement.setString(2, em);
			preparedStatement.setString(3, pn);
			preparedStatement.setString(4, bp);
			generatedKey = preparedStatement.execute();
		    ResultSet rs = stmt.getGeneratedKeys();
		    rs.next();
		    generatedKey = rs.getInt(1);
		} catch (SQLException e) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("VendorError: " + ex.getErrorCode());
		}finally {
			preparedStatement.close();
			myConnection.close();
		}
		return generatedKey;
	}
	private void saveUser(String[] data, int id) {
		Connection myConnection = DBConnection.connect();
		
		String myQuery = "INSERT INTO user(name, surname, fiscal_code, token, birth_date, contact, create_at, update_at)" + 
		" VALUES (?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = myConnection.prepareStatement(myQuery);
		try {
			preparedStatement.setString(1, data[0]);
			preparedStatement.setString(2, data[1]);
			preparedStatement.setString(3, data[2]);
			preparedStatement.setString(4, data[3]);
			preparedStatement.setDate(5, data[4]);
			preparedStatement.setInt(6, Integer.parseInt(data[5]));
			preparedStatement.setTimestamp(7, Timestamp.from(LocalDate.now()));
			preparedStatement.setTimestamp(8, Timestamp.from(LocalDate.now()));
			//preparedStatement.setInt(9, Integer.parseInt(data[6]));
			preparedStatement.execute();
		} catch (SQLException e) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("VendorError: " + ex.getErrorCode());
		}finally {
			preparedStatement.close();
			myConnection.close();
		}
	}
}
