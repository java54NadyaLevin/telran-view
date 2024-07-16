package telran.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Predicate;

public interface InputOutput {
	String readString(String prompt);

	void writeString(String str);

	default void writeLine(Object obj) {
		writeString(obj.toString() + "\n");
	}

	default <T> T readObject(String prompt, String errorPrompt, Function<String, T> mapper) {
		T res = null;
		boolean running = false;
		do {
			String str = readString(prompt);
			running = false;
			try {
				res = mapper.apply(str);
			} catch (RuntimeException e) {
				writeLine(errorPrompt + " " + e.getMessage());
				running = true;
			}

		} while (running);
		return res;
	}

	/**
	 * 
	 * @param prompt
	 * @param errorPrompt
	 * @return Integer number
	 */
	default Integer readInt(String prompt, String errorPrompt, Predicate<String> predicate) {
		return (Integer) readObject(prompt, errorPrompt, str -> {
			if(!predicate.test(str)) {
				throw new RuntimeException("");
			}
			return Integer.parseInt(str);
		});
	}

	default Long readLong(String prompt, String errorPrompt) {

		return (Long) readObject(prompt, errorPrompt, str -> {
			Long longNumber = Long.parseLong(str);
			return longNumber;
		});

	}

	default Double readDouble(String prompt, String errorPrompt) {
		return (Double) readObject(prompt, errorPrompt, str -> {
			Double doubleNumber = Double.parseDouble(str);
			return doubleNumber;
		});

	}

	default Double readNumberRange(String prompt, String errorPrompt, double min, double max) {
		return (Double) readObject(prompt, errorPrompt, str -> {
				Double doubleNumber = Double.parseDouble(str);
				if(doubleNumber == null
						|| doubleNumber.compareTo(min) < 0 
						|| doubleNumber.compareTo(max) >= 0) {
					throw new RuntimeException("");
				}
				return doubleNumber;
		});
	}

	default String readStringPredicate(String prompt, String errorPrompt, Predicate<String> predicate) {
		
		return (String) readObject(prompt, errorPrompt, str -> {
			if(!predicate.test(str)) {
				throw new RuntimeException("");
			}
			return str;
			});
	}

	default String readStringOptions(String prompt, String errorPrompt, HashSet<String> options) {
		return (String) readObject(prompt, errorPrompt, str -> {
			if(!options.contains(str)) {
				throw new RuntimeException("");
			}
			return str;
		});
	}

	default LocalDate readIsoDate(String prompt, String errorPrompt) {
		return (LocalDate) readObject(prompt, errorPrompt, str -> {
			LocalDate date = dateFormatCheck(str);
			return date;
		});
	}

	private LocalDate dateFormatCheck(String str) {
		LocalDate date;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		try {
			date = LocalDate.parse(str, formatter);
		} catch (DateTimeParseException e) {
			throw new RuntimeException("Incorrect date format");
		}
		
		return date;
	}

	default LocalDate readIsoDateRange(String prompt, String errorPrompt, LocalDate from, LocalDate to) {
		return (LocalDate) readObject(prompt, errorPrompt, str -> {
			LocalDate date = dateFormatCheck(str);
			if(date == null) {
				throw new RuntimeException("Incorrect date format");
			}
			if(date.compareTo(from) < 0 || date.compareTo(to) > 0) {
				throw new RuntimeException("Date is greater than current date");
			}
			return date;
		});
	}

}
