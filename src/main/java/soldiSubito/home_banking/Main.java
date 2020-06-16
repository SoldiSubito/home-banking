package soldiSubito.home_banking;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;
public class Main {

	public static void main(String[] args) throws SQLException {
		/*Scanner scanner = new Scanner(System.in);
		String inputString = scanner.nextLine();
		*/
		Date dateOfBirth = Date.valueOf(LocalDate.of(1850,10,17));
		/*String name,
		String surname,
		LocalDate dateOfBirth,
		Gender gender,
		String birthPlace,
		String livingPlace,
		String cf,
		String phoneNumber,
		String eMail,
		String identityId*/
		UserManagement.register("Joseph","Stalin",dateOfBirth, Gender.MALE,
				"Moscow","Moscow","ADFSAD95R24B354O","345567894","josephino@gmail.communism".toUpperCase(),"CiCCio", "Password");
		//UserManagement.login("NFFSAD95R24B354O", "token");
		//Conto.createConto("2", 720.0, "IT123546", "DISPONIBILE", 0, Date.valueOf(LocalDate.now()));
	}
}
