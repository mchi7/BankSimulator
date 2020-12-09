package banking;

public class InvalidPIN extends Failure {
	/**
	 * Constructor
	 *
	 * @param message
	 *            description of the failure
	 */
	public InvalidPIN() {
		super("Invalid PIN");
	}

	public boolean isInvalidPIN() {
		return true;
	}
}
