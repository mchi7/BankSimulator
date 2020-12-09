package banking;

import bank.AccountEntry;

public class Success extends Status {
	AccountEntry account;
	
	public Success(AccountEntry account) {
		super();
		this.account = account;
	}
	
	public AccountEntry getAccountInfo() {
		return account;
	}

	public boolean isSuccess() {
		return true;
	}

	public boolean isInvalidPIN() {
		return false;
	}

	public String getMessage() {
		return null;
	}
}
