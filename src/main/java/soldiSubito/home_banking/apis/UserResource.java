package soldiSubito.home_banking.apis;

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

import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.config.PropertyVisibilityStrategy;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import soldiSubito.home_banking.DBConnection;
import soldiSubito.home_banking.User;

@Path("/user")
public class UserResource {
	
	
	@GET
	public Response login() {
		String cf ="NFFYMR95R26B354O"; 
		String password = "token";
		User user = null;
		String myQuery = "SELECT * FROM user WHERE fiscal_code = ? AND password = ?";
		String response = "";
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
				//String name, String surname, Date dateOfBirth, String token, String cf
				user = new User(rs.getString("NAME"), rs.getString("SURNAME"), rs.getDate("BIRTH_DATE"),rs.getString("PASSWORD"),rs.getString("FISCAL_CODE"));
			}else {
				System.out.println("Nome utente o password non corrispondono.");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Response.ok(user.toJson()).build();
		
	}
	
	

	

}
