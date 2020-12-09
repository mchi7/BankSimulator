package bank;

public class CustomerEntry {
	private int id;
	private String firstName;
	private String lastName;
	private boolean student;
	private int cardID;
	private int pin;
	
	/** Constructor for a customer entry
	 * @param id		Customer's unique ID
	 * @param firstName	Customer's first name
	 * @param lastName	Customer's last name
	 * @param student	Indicates whether the customer is a student
	 * @param cardID	Customer's card ID
	 * @param pin		Customer's PIN
	 */
	public CustomerEntry(int id, String firstName, String lastName, boolean student, int cardID, int pin) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.student = student;
		this.cardID = cardID;
		this.pin = pin;
	}
	
	public int getID() {
		return id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public boolean isStudent() {
		return student;
	}

	public int getCardID() {
		return cardID;
	}
	
	public int getPin() {
		return pin;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerEntry other = (CustomerEntry) obj;
		if (cardID != other.cardID)
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id != other.id)
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (pin != other.pin)
			return false;
		if (student != other.student)
			return false;
		return true;
	}
	
	
}
