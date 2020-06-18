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
		Date dateOfBirth = Date.valueOf(LocalDate.of(1925,05,06));
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
		//UserManagement.register("Irene","Carfì",dateOfBirth, Gender.FEMALE,
		//		"Boh, penso al sud","Stanford","CRFIRN95E40E123a".toUpperCase(),"342267893","something@something.com".toUpperCase(), "Irenz", "ASmartPassword");
		//UserManagement.login("NFFSAD95R24B354O", "token");
		//Conto.createConto("2", 720.0, "IT123546", "DISPONIBILE", 0, Date.valueOf(LocalDate.now()));
//		Conto.findByIban("IT666666");
//		Conto.findByIban("IT123546");
//		Conto.bonifico("IT123546", "IT666666", 100);
//		Conto.findByIban("IT666666");
//		Conto.findByIban("IT123546");
//		Conto.findById(1);
		System.out.println(ContoDAO.findByOwner("9").toString());
		//UserManagement.deleteUserById(7);
	}
}
