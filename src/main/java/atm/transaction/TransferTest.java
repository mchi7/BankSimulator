package atm.transaction;


import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;

import org.junit.BeforeClass;
import org.junit.Test;

import atm.ATM;
import atm.ATMSessionPinTest;
import atm.Session;
import bank.AccountEntry;
import bank.BankDatabase;
import bank.CustomerEntry;
import bank.BankDatabase.AccountNotFound;
import bank.BankDatabase.CustomerNotFound;
import banking.Card;
import banking.Money;
import banking.exceptions.InvalidAmountException;
import banking.exceptions.InvalidPINException;
import banking.exceptions.InvalidTransactionChoiceException;


public class TransferTest {
	static int cardID = 6789;
	static BankDatabase database;
	static ATM atmSession;
	static int bankID = 0;
	static String location = "London";
	static String bankName = "CIBC Branch";
	static InetAddress bankAddress;
	
	//0 - checkings, 1 - savings, 2 - money market
	static int fromAccountOption = 0;
	static int toAccountOption = 1;
	
	
	static CustomerEntry customer;
	Card card = new Card(cardID);
	static int customerID;
	
	static AccountEntry account;
	
	
	@BeforeClass
	public static void retrieveCustomer() throws CustomerNotFound {
		database = BankDatabase.getInstance();
		customer = database.getCustomerInfo(cardID);
		customerID = customer.getID();
		
	}
	
	@Test
	public void withdraw_less_than_100 () throws AccountNotFound, InvalidPINException, InvalidAmountException, InvalidTransactionChoiceException {
		Money amount = new Money(99);
		
		atmSession = new ATM (bankID, location, bankName, bankAddress, true, cardID, "1234", 2, fromAccountOption, toAccountOption, amount);
		Session newSession = new Session(atmSession, cardID, "1234", 2, fromAccountOption, toAccountOption, amount);
		Transfer transferObj = new Transfer(atmSession, newSession, card, 1234, true);
		
		account = database.getAccountInfo(customerID, fromAccountOption);
		AccountEntry toAccount = database.getAccountInfo(customerID, toAccountOption);
		
		if (customer.isStudent()) {
			if (amount.getAmount() < 100) {
				if (account.getBalance() < 1000) {
					if (toAccount.getBalance() < 1000) {
						double fee = amount.getAmount() * 0.1;
						double total = account.getBalance() - fee;
						try {
							newSession.performSession();
							assertEquals(total, account.getBalance());
							//if (total != account.getBalance()) throw new InvalidAmountException();
							
						} catch (InvalidPINException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvalidAmountException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvalidTransactionChoiceException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else {
						double fee = amount.getAmount() * 0.05;
						double total = account.getBalance() - fee;
						newSession.performSession();
						System.out.println(total + account.getBalance());
						if (total != account.getBalance()) throw new InvalidAmountException(); 
					}
				}
			}
		}
	}
	
}
