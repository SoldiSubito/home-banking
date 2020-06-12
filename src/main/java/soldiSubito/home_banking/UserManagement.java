package soldiSubito.home_banking;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

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
			if (age < 18) throw new IllegalArgumentException("Devi avere almeno 18 anni per creare un account");
			
		}

		
		
		
		
	}
	public void modifyPhoneNumber() {
		
	}
	public void identityId() {
		
	}
	
	public static void logout() {
		
	}
}
