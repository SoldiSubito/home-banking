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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

import soldiSubito.home_banking.api.EditUserApi;
import soldiSubito.home_banking.api.FindUsersResponse;
import soldiSubito.home_banking.api.LoginForm;
import soldiSubito.home_banking.api.UserApi;
import soldiSubito.home_banking.database.UserDAO;
import soldiSubito.home_banking.entity.ErrorFounded;
import soldiSubito.home_banking.entity.User;

@Path("/user")
public class UserController {

	@Path("/login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(LoginForm login) {

		boolean logged = UserDAO.login(login.getCf(), login.getPwd());

		if (logged) {
			// richiama da userDAO.getByusername -->mappandola su loginresponse
			return Response.ok("Utente valido").build();

		} else
			return Response.status(403, "Username o password non validi").build();

	}

	@Path("/register")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response register(UserApi userApi) {

		long age = ChronoUnit.YEARS.between(userApi.getDateOfBirth().toLocalDate(), LocalDate.now());
		if (age < 18)
			return Response
					.status(323, new ErrorFounded(406, "Devi avere almeno 18 anniper creare un account.").toJson())
					.build();

		if (userApi.getName().isBlank())
			return Response.status(323, new ErrorFounded(406, "Il nome non pu� essere vuoto.").toJson()).build();
		if (userApi.getSurname().isBlank())
			return Response.status(323, new ErrorFounded(406, "Il cognome non pu� essere vuoto.").toJson()).build();
		if (userApi.getBirthPlace().isBlank())
			return Response.status(323, new ErrorFounded(406, "Il luogo di nascita non pu� essere vuoto.").toJson())
					.build();
		if (userApi.getLivingPlace().isBlank())
			return Response.status(323, new ErrorFounded(406, "La residenza non pu� essere vuota.").toJson()).build();

		if (!isValidFiscalCode(userApi.getCf()))
			return Response.status(323, new ErrorFounded(406, "Il codice fiscale non � corretto").toJson()).build();
		if (!isValidNumeroFisso(userApi.getPhoneNumber().trim())
				&& !isValidNumeroMobile(userApi.getPhoneNumber().trim()))
			return Response.status(323, new ErrorFounded(406, "Il numero di telefono non � corretto").toJson()).build();
		if (!isValidMail(userApi.geteMail()))
			return Response.status(323, new ErrorFounded(406, "L'email non � corretta").toJson()).build();

		User user = User.from(userApi);

		UserDAO.saveUser(user);

		return Response.ok("User registered successfully").build();
	}

	@Path("/edit_user")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response editUserGenerics(EditUserApi userApi) {

		// User user = User.from(userApi);

		boolean edited = UserDAO.editUser(userApi);
		if (edited) {
			return Response.ok("The modify is valid.").build();

		} else
			return Response.status(200, new ErrorFounded(200, "The modify is valid.").toJson()).build();

	}

	@Path("/delete_user")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public static Response deleteUserById(@QueryParam("id") int id) {
		if (UserDAO.deleteUser(id)) {
			return Response.ok("User deleted").build();

		} else
			return Response.status(406, new ErrorFounded(406, "User deleted").toJson()).build();

	}

	
	
	@Path("/find_users")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public static Response findUsers() throws ParseException {
		List<User> listUser = UserDAO.findUsers();

		List<UserApi> uApi = new ArrayList<>();

		for (User u : listUser) {
			uApi.add(UserApi.from(u));
		}

		FindUsersResponse fUser = new FindUsersResponse(uApi);

		return Response.ok(fUser.toJson()).build();

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
