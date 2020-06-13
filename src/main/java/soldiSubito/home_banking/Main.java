package soldiSubito.home_banking;

import java.util.Scanner;
public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String inputString = scanner.nextLine();
		
		UserManagement.register();
	}
}
