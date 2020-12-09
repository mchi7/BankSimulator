package bank;

public class AccountEntry {
	private int ownerID;
	private String accountType;
	private int balance;
	private int availableBalance;
	private int dailyLimit;
	private int withdrawals;
	
	/** Construstor for an account entry
	 * @param balance			Account's balance
	 * @param availableBalance	Balance available at the moment
	 * @param dailyLimit		Daily limit for that account
	 * @param withdrawals		Withdrawals performed today
	 */
	public AccountEntry(int ownerID, String accountType, int balance, int availableBalance, int dailyLimit, int withdrawals) {
		super();
		this.ownerID = ownerID;
		this.accountType = accountType;
		this.balance = balance;
		this.availableBalance = availableBalance;
		this.dailyLimit = dailyLimit;
		this.withdrawals = withdrawals;
	}

	public int getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(int availableBalance) {
		this.availableBalance = availableBalance;
	}

	public int getDailyLimit() {
		return dailyLimit;
	}

	public void setDailyLimit(int dailyLimit) {
		this.dailyLimit = dailyLimit;
	}

	public int getWithdrawals() {
		return withdrawals;
	}

	public void setWithdrawals(int withdrawals) {
		this.withdrawals = withdrawals;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountEntry other = (AccountEntry) obj;
		if (accountType == null) {
			if (other.accountType != null)
				return false;
		} else if (!accountType.equals(other.accountType))
			return false;
		if (availableBalance != other.availableBalance)
			return false;
		if (balance != other.balance)
			return false;
		if (dailyLimit != other.dailyLimit)
			return false;
		if (ownerID != other.ownerID)
			return false;
		if (withdrawals != other.withdrawals)
			return false;
		return true;
	}
	
	
}
