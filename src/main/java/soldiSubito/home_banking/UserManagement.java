package soldiSubito.home_banking;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
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

import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.config.PropertyVisibilityStrategy;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bouncycastle.util.encoders.Hex;

import soldiSubito.home_banking.entity.ErrorFounded;
import soldiSubito.home_banking.entity.LoginForm;
import soldiSubito.home_banking.entity.User;

@Path("/user")
public class UserManagement {

	
	
	@Path("/login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(LoginForm login) {

		// String cf ="NFFYMR95R26B354O";
		// String password = "token";
		// System.out.println(login.cf);
		// System.out.println(login.pwd);

		User user = null;
		String myQuery = "SELECT * FROM user WHERE fiscal_code = ? AND password = ?";
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement(myQuery);) {
			preparedStatement.setString(1, login.getCf());
			
			String encoded = "";
			try {
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
				byte[] hash = digest.digest(
				  login.getPwd().getBytes(StandardCharsets.UTF_8));
				encoded = new String(Hex.encode(hash));
				System.out.println(encoded);
			}catch(Exception e) {
				return Response.status(323, new ErrorFounded(406,e.toString()).toJson()).build();
			}
	
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

				// String name, String surname, Date dateOfBirth, String token, String cf
				user = new User(rs.getString("NAME"), rs.getString("SURNAME"), rs.getDate("BIRTH_DATE"),
						rs.getString("PASSWORD"), rs.getString("FISCAL_CODE"));
			} else {
				
				return Response.status(403, new ErrorFounded(403,"Nome utente o password non corrispondono.").toJson()).build();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Response.ok(user.toJson()).build();

	}

	@Path("/register")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response register(User user) {
		// long age = ChronoUnit.YEARS.between(dateOfBirth, Date.valueOf(date));
		// if (age < 18) throw new IllegalArgumentException("Devi avere almeno 18 anni
		// per creare un account.");
		if (user.getName().isBlank())
			return Response.status(323, new ErrorFounded(406,"Il nome non pu� essere vuoto.").toJson()).build();
		//dentro entity Error(status, message)
			//throw new IllegalArgumentException("Il nome non pu� essere vuoto.");
		if (user.getSurname().isBlank())
			return Response.status(323, new ErrorFounded(406,"Il cognome non pu� essere vuoto.").toJson()).build();
		if (user.getBirthPlace().isBlank())
			return Response.status(323, new ErrorFounded(406,"Il luogo di nascita non pu� essere vuoto.").toJson()).build();
		if (user.getLivingPlace().isBlank())
			return Response.status(323, new ErrorFounded(406,"La residenza non pu� essere vuota.").toJson()).build();
		// if (LocalDate.now() < dateOfBirth) throw new IllegalArgumentException("La
		// data di nascita non pu� essere nel futuro.");
		if (!isValidFiscalCode(user.getCf()))
			return Response.status(323, new ErrorFounded(406,"Il codice fiscale non � corretto").toJson()).build();
		if (!isValidNumeroFisso(user.getPhoneNumber().trim()) && !isValidNumeroMobile(user.getPhoneNumber().trim()))
			return Response.status(323, new ErrorFounded(406,"Il numero di telefono non � corretto").toJson()).build();
		if (!isValidMail(user.geteMail()))
			return Response.status(323, new ErrorFounded(406,"L'email non � corretta").toJson()).build();
		// forse controllo identity Id
		
		String encoded = "";
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(
			  user.getPassword().getBytes(StandardCharsets.UTF_8));
			encoded = new String(Hex.encode(hash));
			
		}catch(Exception e) {
			return Response.status(323, new ErrorFounded(406,e.toString()).toJson()).build();
		}
		
		int generatedId = saveGenerics(user.getLivingPlace(), user.geteMail(), user.getPhoneNumber(),
				user.getBirthPlace());
		if (generatedId == -1) {
			return Response.status(406, new ErrorFounded(406,"User still present in our Database ").toJson()).build();
		}
		// DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		// String strDate = dateFormat.format(dateOfBirth)
		saveUser(new String[] { user.getName(), user.getSurname(), user.getCf(), encoded,
				Integer.toString(generatedId), user.getGender().toString() }, user.getDateOfBirth());
		// System.out.println("Registered User " + generatedId + " successfully");
		return Response.ok("User registered successfully").build();
	}

	
	@Path("/edit_user")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response editUserGenerics(User user) {

		String myQuery = " UPDATE generics SET living_place=?, eMail = ?,phone_number=? WHERE id = ?";
		try {
			Connection myConnection = DBConnection.connect();
			PreparedStatement preparedStatement = myConnection.prepareStatement(myQuery);
			preparedStatement.setString(1, user.getLivingPlace());
			preparedStatement.setString(2, user.geteMail());
			preparedStatement.setString(3, user.getPhoneNumber());
			preparedStatement.setInt(4, user.getId());

			preparedStatement.executeUpdate();

		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("VendorError: " + ex.getErrorCode());
			return Response.status(404, new ErrorFounded(404, "The modify is not register!").toJson()).build();
		}
		return Response.status(200, new ErrorFounded(200, "The modify is valid.").toJson()).build();

	}


	public static void logout() {

	}

	public static boolean isValidFiscalCode(String cf) {
		String regex = "^[A-Za-z]{6}[0-9]{2}[A-Za-z]{1}[0-9]{2}[A-Za-z]{1}[0-9]{3}[A-Za-z]{1}$";
		return cf.matches(regex);
	}

	private static boolean isValidMail(String email) {
		String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
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

	@Path("/delete_user")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public static Response deleteUserById(@QueryParam("id") int id) {
		// myQuery non funziona
		String myQuery = "DELETE FROM generics WHERE id = (SELECT contact FROM user WHERE id = ?);";
		String myQuery2 = "DELETE FROM user WHERE id = ?";
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement(myQuery);
				PreparedStatement preparedStatement2 = myConnection.prepareStatement(myQuery2);) {
			preparedStatement.setInt(1,id);
			preparedStatement2.setInt(1,id);

			ResultSet rs = preparedStatement.executeQuery();
			ResultSet rs2 = preparedStatement2.executeQuery();
			//System.out.println("Deleted User " + id + " successfully");
			return Response.ok("User deleted").build();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("VendorError: " + e.getErrorCode());
			e.printStackTrace();
			return Response.status(406,new ErrorFounded(406,"User deleted").toJson()).build();
		}

	}
	
	
	
	@Path("/find_users")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public static Response findUsers() throws ParseException {
		// myQuery non funziona
		String myQuery = "SELECT * FROM user,generics WHERE user.contact = generics.id";
		List<User> allUsers = new ArrayList<>();
		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement(myQuery)) {
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {

		// String name, String surname, Date dateOfBirth, String token, String cf
				allUsers.add(new User(rs.getString("NAME"), rs.getString("SURNAME"), rs.getString("BIRTH_DATE"),
						Gender.valueOf(rs.getString("GENDER")),rs.getString("BIRTH_PLACE"),rs.getString("LIVING_PLACE"),
						rs.getString("FISCAL_CODE"),rs.getString("PHONE_NUMBER"),
						rs.getString("EMAIL")));
			}
			
		}catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("VendorError: " + e.getErrorCode());
			e.printStackTrace();
			return Response.status(406,new ErrorFounded(406,"User deleted").toJson()).build();
		}
		String all = "";
		
		for(User u: allUsers) {
			all += u.toJson() + "\n";
		}
		
		return Response.ok(all).build();

	}

	
	
	

	private static void saveUser(String[] data, Date dateOfBirth) {
		if (Integer.parseInt(data[4]) == -1)
			return;
		String myQuery = "INSERT INTO user(name, surname, fiscal_code, password, birth_date, contact, create_at, update_at,gender)"
				+ " VALUES (?,?,?,?,?,?,?,?,?)";

		try (Connection myConnection = DBConnection.connect();
				PreparedStatement preparedStatement = myConnection.prepareStatement(myQuery);) {
			preparedStatement.setString(1, data[0]);
			preparedStatement.setString(2, data[1]);
			preparedStatement.setString(3, data[2]);
			preparedStatement.setString(4, data[3]);	
			// Date date=Date.valueOf(data[4]);
			preparedStatement.setDate(5, dateOfBirth);
			preparedStatement.setInt(6, Integer.parseInt(data[4]));
			preparedStatement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
			preparedStatement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
			
			 preparedStatement.setString(9, data[5]);
			 
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
