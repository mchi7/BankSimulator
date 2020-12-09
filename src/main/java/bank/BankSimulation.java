package bank;

import java.util.Calendar;

import bank.BankDatabase.AccountNotFound;
import bank.BankDatabase.CustomerNotFound;
import banking.Failure;
import banking.InvalidPIN;
import banking.Message;
import banking.Money;
import banking.Status;
import banking.Success;

public class BankSimulation {
	private static BankSimulation instance = null;
	BankDatabase database = BankDatabase.getInstance();
	FeesCalculator fCalc;

	private BankSimulation() {
		fCalc = new FeesCalculator();
	}

	public static BankSimulation getInstance() {
		if (instance == null)
			connect();

		return instance;
	}

	public static void connect() {
		instance = new BankSimulation();
	}

	public static void disconnect() {
		instance = null;
	}

	/**
	 * Simulate the handling of a message
	 *
	 * @param message
	 *            the message to send
	 * @return status code returned by bank
	 */
	public Status handleMessage(Message message) {
		int cardNumber = message.getCard().getNumber();
		int pin = message.getPIN();
		CustomerEntry customer;
		try {
			customer = database.getCustomerInfo(cardNumber);
		} catch (CustomerNotFound e) {
			return new Failure("Invalid card");
		}
		
		if (customer.getPin() != pin) {
			return new InvalidPIN();
		}
		
		switch (message.getMessageCode()) {
		case Message.WITHDRAWAL:

			return withdrawal(message, customer.getID(), customer.isStudent());

		case Message.INITIATE_DEPOSIT:

			return initiateDeposit(message, customer.getID());

		case Message.COMPLETE_DEPOSIT:

			return completeDeposit(message, customer.getID(), customer.isStudent());

		case Message.TRANSFER:

			return transfer(message, customer.getID(), customer.isStudent());

		case Message.INQUIRY:

			return inquiry(message, customer.getID());
		}

		// Need to keep compiler happy

		return null;
	}

	/**
	 * Simulate processing of a withdrawal
	 *
	 * @param message
	 *            the message describing the withdrawal requested
	 * @param ownerID TODO
	 * @param student TODO
	 * @return status code derived from current values
	 */
	private Status withdrawal(Message message, int ownerID, boolean student) {
		AccountEntry account;
		try {
			account = database.getAccountInfo(ownerID, message.getFromAccount());
		} catch (AccountNotFound e) {
			return new Failure("Invalid account type");
		}
		
		Money amount = message.getAmount();

		Money limitRemaining = centsToMoney(account.getDailyLimit());
		limitRemaining.subtract(centsToMoney(account.getWithdrawals()));
		
		System.out.println("Withdrawals: " + centsToMoney(account.getWithdrawals()));
		if (!amount.lessEqual(limitRemaining))
			return new Failure("Daily withdrawal limit exceeded");

		if (!amount.lessEqual(centsToMoney(account.getAvailableBalance())))
			return new Failure("Insufficient available balance");

		// Update withdrawals today and account balances once we know everything
		// is OK.
		
		int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		int fee = fCalc.calculateWithdrawalFee(amount.getAmount(), account.getBalance(), student, dayOfWeek);
		
		// Renew balance
		int balance = account.getBalance() - amount.getAmount() - fee;
		account.setBalance(balance);
		
		// Renew available balance
		int availableBalance = account.getAvailableBalance() - amount.getAmount() - fee;
		account.setAvailableBalance(availableBalance);

		// Attach updated balances
		Status status = new Success(account);
		
		// Update database
		try {
			database.updateAccountInfo(message.getFromAccount(), account);
			database.addWithdrawal(message.getFromAccount(), ownerID, amount.getAmount());
		} catch (AccountNotFound e) {
			return new Failure("Invalid account type");
		}

		return status;
	}
	
	

	/**
	 * Simulate initiation of a deposit. At this point, the bank only approves
	 * the validity of the deposit - no changes to the records are made until
	 * the envelope is actually inserted
	 *
	 * @param message
	 *            the message describing the deposit requested
	 * @param ownerID TODO
	 * @return status code derived from current values
	 */
	private Status initiateDeposit(Message message, int ownerID) {
		AccountEntry account;
		try {
			account = database.getAccountInfo(ownerID, message.getFromAccount());
		} catch (AccountNotFound e) {
			return new Failure("Invalid account type");
		}

		// Don't update anything yet

		return new Success(account);
	}

	/**
	 * Simulate completion of a deposit
	 *
	 * @param message
	 *            the message describing the deposit requested
	 * @param ownerID TODO
	 * @param student TODO
	 * @return status code - must always be success in this case
	 */
	private Status completeDeposit(Message message, int ownerID, boolean student) {
		AccountEntry account;
		try {
			account = database.getAccountInfo(ownerID, message.getToAccount());
		} catch (AccountNotFound e) {
			return new Failure("Invalid account type");
		}

		Money amount = message.getAmount();
		
		// Calculate interest
		int interest = fCalc.calculateDepositInterest(amount.getAmount(), account.getBalance(), student);
		
		// Renew balance
		int balance = account.getBalance() + amount.getAmount() + interest;
		account.setBalance(balance);
		
		// Attach updated balances
		Status status = new Success(account);
			
		// Update database
		try {
			database.updateAccountInfo(message.getToAccount(), account);
		} catch (AccountNotFound e) {
			return new Failure("Invalid account type");
		}
		return status;
	}
	
	

	/**
	 * Simulate processing of a transfer
	 *
	 * @param message
	 *            the message describing the transfer requested
	 * @param ownerID TODO
	 * @param student TODO
	 * @return status code derived from current values
	 */
	private Status transfer(Message message, int ownerID, boolean student) {
		AccountEntry fromAccount;
		try {
			fromAccount = database.getAccountInfo(ownerID, message.getFromAccount());
		} catch (AccountNotFound e) {
			return new Failure("Invalid 'from' account type");
		}

		AccountEntry toAccount;
		try {
			toAccount = database.getAccountInfo(ownerID, message.getToAccount());
		} catch (AccountNotFound e) {
			return new Failure("Invalid 'to' account type");
		}
		
		if (fromAccount.getAccountType().equals(toAccount.getAccountType()))
			return new Failure("Can't transfer money from an account to itself.");

		Money amount = message.getAmount();

		if (!amount.lessEqual(centsToMoney(fromAccount.getAvailableBalance())))
			return new Failure("Insufficient available balance");

		// Update account balances once we know everything is OK
		int balance, availableBalance;
		
		// Renew from account balance 
		balance = fromAccount.getBalance() - amount.getAmount();
		fromAccount.setBalance(balance);
				
		// Renew from account available balance
		availableBalance = toAccount.getAvailableBalance() - amount.getAmount();
		toAccount.setAvailableBalance(availableBalance);
		
		//Calculate fee
		int fee = fCalc.calculateTransferFee(amount.getAmount(), fromAccount.getBalance(), toAccount.getBalance(), student);
		
		// Renew to account balance 
		balance = toAccount.getBalance() - amount.getAmount() - fee;
		toAccount.setBalance(balance);
						
		// Renew to account available balance
		availableBalance = toAccount.getAvailableBalance() - amount.getAmount() - fee;
		fromAccount.setAvailableBalance(availableBalance);

		// Attach updated balances
		Status status = new Success(toAccount);
					
		// Update database
		try {
			database.updateAccountInfo(message.getFromAccount(), fromAccount);
			database.updateAccountInfo(message.getToAccount(), toAccount);
		} catch (AccountNotFound e) {
			return new Failure("Invalid account type");
		}
		return status;
	}
	
	

	/**
	 * Simulate processing of an inquiry
	 *
	 * @param message
	 *            the message describing the inquiry requested
	 * @param ownerID TODO
	 * @return status code derived from current values
	 */
	private Status inquiry(Message message, int ownerID) {
		AccountEntry account;
		try {
			account = database.getAccountInfo(ownerID, message.getFromAccount());
		} catch (AccountNotFound e) {
			return new Failure("Invalid account type");
		}

		// Return requested balances
		return new Success(account);
	}
	
	private Money centsToMoney(int amount) {
		int dollars = amount / 100;
		int cents = amount % 100;
		
		return new Money(dollars, cents);
	}

}
