package banking;

public class Failure extends Status {
	/**
	 * Constructor
	 *
	 * @param message
	 *            description of the failure
	 */
	public Failure(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return false;
	}

	public boolean isInvalidPIN() {
		return false;
	}

	public String getMessage() {
		return message;
	}

	private String message;

}
