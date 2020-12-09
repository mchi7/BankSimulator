package banking.exceptions;

public class InvalidAmountException extends Exception {
	public InvalidAmountException() {
		super("Invalid amount of money entered.");
	}

}
