package telran.view;

import java.time.LocalDate;
import java.util.HashSet;


import org.junit.jupiter.api.Test;

record User(String username, String password, LocalDate dateLastLogin, String phoneNumber, int numberOfLogins) {
}

class InputOutputTest {
	InputOutput io = new SystemInputOutput();

//	@Test
//	void readObjectTest() {
//		User user = io.readObject("Enter user in format <username>#<password>#<dateLastLogin>"
//				+ "#<phone number>#<number of logins>", "Wrong user input format", str -> {
//					String[] tokens = str.split("#");
//					return new User(tokens[0], tokens[1],
//							LocalDate.parse(tokens[2]), tokens[3], Integer.parseInt(tokens[4]));
//				});
//		io.writeLine(user);
//	}
	@Test
	void readUserByFields() {

		String username = io.readStringPredicate(
				"Enter username (at least 6 letters, first capital, others in lower case)", "Wrong name format",
				s -> s.matches("^[A-Z][a-z]{5,}$"));
		io.writeLine(username);
		String password = io.readStringPredicate(
				"Enter pass (at least 8 letters, one capital, one digit, one special symbol)", "Wrong password format",
				s -> s.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[#\\$*&%]).{8,}$"));
		io.writeLine(password);

		String mobile = io.readStringPredicate(
				"Enter mobile phone number (Israel)", "Wrong number format",
				s -> s.matches("(\\+972-?|0)5\\d-?(\\d{3}-\\d{2}-|\\d{2}-?\\d{3}-?)\\d{2}"));
		io.writeLine(mobile);
		
		Integer logins = io.readInt("Enter number of logins", "Number should be greater then 0", s -> s.matches("^[1-9]\\d*$"));
		io.writeLine(logins);
		
		LocalDate dateLastLogin = io.readIsoDateRange("Enter last login date", 
				"Date provided is greater than a current one", 
				LocalDate.of(0, 1, 1), LocalDate.now());
		io.writeLine(dateLastLogin);

		User user = new User(username, password, dateLastLogin, mobile, logins);
		io.writeLine(user);
	}
	
	@Test
	void getLongTest() {
		Long longNumber = io.readLong("Enter a number (long)", "Enter a number");
		io.writeLine(longNumber);
	}

	@Test
	void getDoubleTest() {
		Double doubleNumber = io.readDouble("Enter a number (double)", "Enter a number");
		io.writeLine(doubleNumber);
	}

	@Test
	void readStringOptionsTest() {
		HashSet<String> languagesSet = new HashSet<String>();
		languagesSet.add("russian");
		languagesSet.add("belarussian");
		languagesSet.add("english");
		languagesSet.add("hebrew");
		String languages = io.readStringOptions(
				"Enter main language of account interface", 
				"Please choose one of the following: russian, belarussian, english, hebrew",
				languagesSet);
		io.writeLine(languages);
		
	}

	@Test
	void readNumberRangeTest() {
		Double doubleNumber = io.readNumberRange("Enter a number (range -1 - 100)", "Enter a -1 <= number < 100 ", -1, 100);
		io.writeLine(doubleNumber);
	}

}
