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

import soldiSubito.home_banking.api.FindUsersResponse;
import soldiSubito.home_banking.api.LoginForm;
import soldiSubito.home_banking.api.UserApi;
import soldiSubito.home_banking.database.UserDAO;
import soldiSubito.home_banking.entity.ErrorFounded;
import soldiSubito.home_banking.entity.User;

@Path("/user")
public class UserManagement {

	
	
	@Path("/login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(LoginForm login) {
	
		boolean logged = UserDAO.login(login.getCf(), login.getPwd());
		
		if(logged) {
			//richiama da userDAO.getByusername  -->mappandola su loginresponse
			return Response.ok("Utente valido").build();

		}else return Response.status(403,"Username o password non validi").build();

	}
	
	

	@Path("/register")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response register(UserApi userApi) {
		
		
		// long age = ChronoUnit.YEARS.between(dateOfBirth, Date.valueOf(date));
		// if (age < 18) throw new IllegalArgumentException("Devi avere almeno 18 anni
		// per creare un account.");
		if (userApi.getName().isBlank())
			return Response.status(323, new ErrorFounded(406,"Il nome non può essere vuoto.").toJson()).build();
		//dentro entity Error(status, message)
			//throw new IllegalArgumentException("Il nome non può essere vuoto.");
		if (userApi.getSurname().isBlank())
			return Response.status(323, new ErrorFounded(406,"Il cognome non può essere vuoto.").toJson()).build();
		if (userApi.getBirthPlace().isBlank())
			return Response.status(323, new ErrorFounded(406,"Il luogo di nascita non può essere vuoto.").toJson()).build();
		if (userApi.getLivingPlace().isBlank())
			return Response.status(323, new ErrorFounded(406,"La residenza non può essere vuota.").toJson()).build();
		// if (LocalDate.now() < dateOfBirth) throw new IllegalArgumentException("La
		// data di nascita non può essere nel futuro.");
		if (!isValidFiscalCode(userApi.getCf()))
			return Response.status(323, new ErrorFounded(406,"Il codice fiscale non è corretto").toJson()).build();
		if (!isValidNumeroFisso(userApi.getPhoneNumber().trim()) && !isValidNumeroMobile(userApi.getPhoneNumber().trim()))
			return Response.status(323, new ErrorFounded(406,"Il numero di telefono non è corretto").toJson()).build();
		if (!isValidMail(userApi.geteMail()))
			return Response.status(323, new ErrorFounded(406,"L'email non è corretta").toJson()).build();
		// forse controllo identity Id
		
		
		// DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		// String strDate = dateFormat.format(dateOfBirth)
		
		User user = User.from(userApi);
		
		UserDAO.saveUser(user);
		// System.out.println("Registered User " + generatedId + " successfully");
		return Response.ok("User registered successfully").build();
	}

	
	@Path("/edit_user")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response editUserGenerics(UserApi user) {

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
		List<User> listUser = UserDAO.findUsers();
		
		List<UserApi> uApi = new ArrayList<>();
		
		for(User u: listUser) {
			uApi.add(UserApi.from(u));
		}
		
		FindUsersResponse fUser = new FindUsersResponse(uApi);
		
		return Response.ok(fUser.toJson()).build();

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
