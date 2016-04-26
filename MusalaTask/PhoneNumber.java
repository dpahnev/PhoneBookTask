package MusalaTask;

public class PhoneNumber implements Comparable<PhoneNumber> {

	private String phoneNumber;
	private int numberOfCalls;

	public PhoneNumber() {
		numberOfCalls = 0;
	}

	public PhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		numberOfCalls = 0;
	}

	public PhoneNumber(String phoneNumber, int numberOfCalls) {
		this.phoneNumber = phoneNumber;
		this.numberOfCalls = numberOfCalls;
	}

	public void setNumberOfCalls(int numberOfCalls) {
		this.numberOfCalls = numberOfCalls;
	}

	public Integer getNumberOfCalls() {
		return numberOfCalls;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return phoneNumber + " called " + numberOfCalls + " times";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PhoneNumber other = (PhoneNumber) obj;
		if (phoneNumber == null) {
			if (other.phoneNumber != null) {
				return false;
			}
		} else if (!phoneNumber.equals(other.phoneNumber)) {
			return false;
		}
		return true;
	}

	// if numbersOfCalls is 0 they will be sorted by order of input
	@Override
	public int compareTo(PhoneNumber otherPhoneNumber) {
		if (this.getPhoneNumber().equals(otherPhoneNumber.getPhoneNumber())) {
			return 0;
		} else {
			int result = this.numberOfCalls - otherPhoneNumber.getNumberOfCalls();
			if (result == 0) {
				return 1;
			} else
				return result;
		}
	}

}
