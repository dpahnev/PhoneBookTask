package MusalaTask;

public interface IPhoneBook {

	void addPhoneNumber(String name, String number) throws PhoneNumberException, AlreadyExistingPhoneNumberException;

	String findPhoneNumberByName(String name) throws PhoneNumberException;

	void printPhoneBook();

	void printMostCalledNumbers();
}