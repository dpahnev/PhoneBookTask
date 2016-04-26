package MusalaTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class PhoneBook implements IPhoneBook {

	private static final String NORMALIZED_FORM_PREFIX = "+359";
	private static final String REGEX_FOR_CHECK_IF_PHONE_WITH_0_IS_VALID = "[0]{1}[8]{1}[7-9]{1}[2-8]{1}\\d{6}";
	private static final String REGEX_FOR_CHECK_IF_PHONE_WITH_00_IS_VALID = "[0]{1}[0]{1}[8]{1}[7-9]{1}[2-8]{1}\\d{6}";
	private static final String REGEX_FOR_EVALUATING_IF_PHONE_IS_IN_NORMALIZED_FORM = "[+]{1}[3]{1}[5]{1}[9]{1}[8]{1}[7-9]{1}[2-8]{1}\\d{6}";

	private Map<String, PhoneNumber> phoneBook;
	private Map<PhoneNumber, String> phoneNumbers;

	public PhoneBook() {
		this.phoneBook = new TreeMap<>();
		this.phoneNumbers = new HashMap<>();
	}

	public PhoneBook(File textFile) throws PhoneNumberException, IOException, AlreadyExistingPhoneNumberException {
		this.phoneBook = new TreeMap<>();
		this.phoneNumbers = new HashMap<>();
		generatePhoneBookFromFile(textFile);
	}

	public static PhoneBook generatePhoneBookObjectFromFile(File textFile)
			throws PhoneNumberException, IOException, AlreadyExistingPhoneNumberException {
		return new PhoneBook(textFile);
	}

	private void generatePhoneBookFromFile(File textFile)
			throws PhoneNumberException, IOException, AlreadyExistingPhoneNumberException {
		if (textFile != null) {
			boolean doesExist = textFile.exists();
			if (doesExist) {
				try (BufferedReader br = new BufferedReader(new FileReader(textFile))) {
					String[] line;
					String name;
					String number;
					while (br.ready()) {
						line = br.readLine().split(",");
						if (line != null && line.length == 2) {
							name = line[0];
							try {
								number = checkForValidNumber(line[1]);
								this.addPhoneNumber(name, number);
							} catch (PhoneNumberException e) {
								continue;
							}
						}
					}
				} catch (FileNotFoundException e) {
					throw new PhoneNumberException("The File was not found.", e);
				}
			} else
				throw new PhoneNumberException("The File was not found or path for it was mistaken.");
		} else
			throw new PhoneNumberException("The File is not valid, or is null.");
	}

	@Override
	public void addPhoneNumber(String name, String number) throws AlreadyExistingPhoneNumberException {
		PhoneNumber currentPhoneNumberToBeAdded;
		try {
			currentPhoneNumberToBeAdded = new PhoneNumber(checkForValidNumber(number));
		} catch (PhoneNumberException e) {
			return;
		}

		if (!phoneNumbers.containsKey(currentPhoneNumberToBeAdded)) {
			phoneBook.put(name, currentPhoneNumberToBeAdded);
			phoneNumbers.put(currentPhoneNumberToBeAdded, name);
		} else {
			throw new AlreadyExistingPhoneNumberException("This phone number has already been added.");
		}
	}

	@Override
	public String findPhoneNumberByName(String name) throws PhoneNumberException {
		if (phoneBook.containsKey(name)) {
			PhoneNumber foundNumber = phoneBook.get(name);
			foundNumber.setNumberOfCalls(foundNumber.getNumberOfCalls() + 1);
			return foundNumber.getPhoneNumber();
		} else {
			throw new PhoneNumberException("Sorry there is no such name.");
		}
	}

	@Override
	public void printPhoneBook() {
		StringBuilder printPhoneBook = new StringBuilder();
		for (String currentName : phoneBook.keySet()) {
			printPhoneBook.append(currentName + " : " + phoneBook.get(currentName).getPhoneNumber())
					.append(System.lineSeparator());
		}
		System.out.println(printPhoneBook.toString());
	}

	@Override
	public void printMostCalledNumbers() {
		StringBuilder printPhoneNumbers = new StringBuilder();

		java.util.List<Entry<PhoneNumber, String>> sortedNumbers = phoneNumbers.entrySet().stream()
				.sorted((x, y) -> y.getKey().getNumberOfCalls().compareTo(x.getKey().getNumberOfCalls())).limit(5)
				.collect(Collectors.toList());

		for (Entry<PhoneNumber, String> phoneNumber : sortedNumbers) {
			printPhoneNumbers.append(phoneNumber.getValue() + " : " + phoneNumber.getKey().toString())
					.append(System.lineSeparator());
		}

		System.out.println(printPhoneNumbers.toString());
	}

	private String checkForValidNumber(String phoneNumber) throws PhoneNumberException {
		if (phoneNumber != null) {
			if (phoneNumber.matches(REGEX_FOR_EVALUATING_IF_PHONE_IS_IN_NORMALIZED_FORM)) {
				return phoneNumber;
			} else if (phoneNumber.matches(REGEX_FOR_CHECK_IF_PHONE_WITH_00_IS_VALID)) {
				return (NORMALIZED_FORM_PREFIX + phoneNumber.substring(2));
				// replace 00 with +359
			} else if (phoneNumber.matches(REGEX_FOR_CHECK_IF_PHONE_WITH_0_IS_VALID)) {
				// replace 0 with +359
				return (NORMALIZED_FORM_PREFIX + phoneNumber.substring(1));
			} else {
				throw new PhoneNumberException("This Phone Number is invalid, or not in normalized form.");
			}
		} else {
			throw new PhoneNumberException("This phoneNumber is empty.");
		}
	}

}
