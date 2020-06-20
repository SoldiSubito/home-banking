package soldiSubito.home_banking.database;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.bouncycastle.util.encoders.Hex;

import soldiSubito.home_banking.DBConnection;
import soldiSubito.home_banking.Gender;
import soldiSubito.home_banking.api.EditUserApi;
import soldiSubito.home_banking.api.UserApi;
import soldiSubito.home_banking.entity.ErrorFounded;
import soldiSubito.home_banking.entity.User;

public class UserDAO {

	public static List<User> findUsers() {

		String myQuery = "SELECT * FROM user,generics WHERE user.contact = generics.id";
		List<User> allUsers = new ArrayList<>();
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement(myQuery)) {
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {

				allUsers.add(new User(rs.getString("NAME"), rs.getString("SURNAME"), rs.getString("BIRTH_DATE"),
						Gender.valueOf(rs.getString("GENDER")), rs.getString("BIRTH_PLACE"),
						rs.getString("LIVING_PLACE"), rs.getString("FISCAL_CODE"), rs.getString("PHONE_NUMBER"),
						rs.getString("EMAIL"), rs.getString("IDENTITY_ID"), rs.getString("PASSWORD")));
			}

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("VendorError: " + e.getErrorCode());
			e.printStackTrace();
		}

		return allUsers;
	}

	public static int saveGenerics(String lp, String em, String pn, String bp) {

		String myQuery = "INSERT INTO generics(living_place, email, phone_number, birth_place) VALUES (?,?,?,?)";

		int generatedKey = -1;
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement(myQuery,
						Statement.RETURN_GENERATED_KEYS);) {
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

	public static void saveUser(User user) {

		int contactId = saveGenerics(user.getLivingPlace(), user.geteMail(), user.getPhoneNumber(),
				user.getBirthPlace());
		;
		if (contactId == -1) {
			throw new RuntimeException("Errore salvataggio generics");
		}

		String myQuery = "INSERT INTO user(name, surname, fiscal_code, password, birth_date, contact, create_at, update_at,gender)"
				+ " VALUES (?,?,?,?,?,?,?,?,?)";

		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement(myQuery);) {

			String encoded = "";

			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(user.getPassword().getBytes(StandardCharsets.UTF_8));
			encoded = new String(Hex.encode(hash));
			System.out.println(encoded);

			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getSurname());
			preparedStatement.setString(3, user.getCf());
			preparedStatement.setString(4, encoded);
			// Date date=Date.valueOf(data[4]);
			preparedStatement.setDate(5, user.getDateOfBirth());
			preparedStatement.setInt(6, contactId);
			preparedStatement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
			preparedStatement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));

			preparedStatement.setString(9, user.getGender().toString());

			preparedStatement.execute();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean login(String username, String password) {

		User user = null;

		String myQuery = "SELECT * FROM user WHERE fiscal_code = ? AND password = ?";
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement(myQuery);) {
			preparedStatement.setString(1, username);

			String encoded = "";

			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
			encoded = new String(Hex.encode(hash));
			System.out.println(encoded);

			preparedStatement.setString(2, encoded);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				StringBuilder sb = new StringBuilder();
				sb.append(rs.getString("NAME") + "\n");
				sb.append(rs.getString("SURNAME") + "\n");
				sb.append(rs.getString("FISCAL_CODE") + "\n");
				sb.append(rs.getString("PASSWORD") + "\n");
				sb.append(rs.getDate("BIRTH_DATE") + "\n");
				sb.append(rs.getInt("CONTACT") + "\n");

				return true;
			} else {

				return false;
			}

		} catch (SQLException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public boolean deleteUser() {
		return false;
	}

	public static boolean editUser(EditUserApi user) {

		String myQuery = " UPDATE generics SET living_place=?, eMail = ?,phone_number=? WHERE id = ?";
		try {
			Connection myConnection = DBConnection.connect();
			PreparedStatement preparedStatement = myConnection.prepareStatement(myQuery);
			preparedStatement.setString(1, user.getLivingPlace());
			preparedStatement.setString(2, user.geteMail());
			preparedStatement.setString(3, user.getPhoneNumber());
			preparedStatement.setInt(4, user.getId());

			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("VendorError: " + ex.getErrorCode());
			return false;
		}
	

	}

}
