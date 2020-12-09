package atm;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bank.AccountEntry;
import bank.BankDatabase;
import bank.BankDatabase.AccountNotFound;
import bank.BankDatabase.CustomerNotFound;
import bank.CustomerEntry;
import banking.Balances;
import banking.Money;
import banking.exceptions.InvalidPINException;
import banking.exceptions.InvalidAmountException;

public class ATMSessionPinTest {

	boolean testing = true;

	// Customer Information.
	String firstName = "John";
	String lastName = "Doe";
	boolean isStudent = false;
	int cardID = 6789;
	int intPin;
	String pin;
	
	//Account balance and daily limit
	String accountTypeName = "Checking";
	int balance = 500000;
	int dailyLimit = 100000;
	
	// Bank information. Important only for the receipt.
	int bankID = 0;
	String location = "London";
	String bankName = "CIBC Branch";
	InetAddress bankAddress = null;
	
	// Use to choose type of transaction.
	// (0 = withdrawal, 1 = deposit, 2 = transfer, 3 = inquiry)
	int transactionChoice = 0;
	
	// Choose accounts the money are coming from or going to.
	// (0 = checking, 1 = savings, 2 = money market)
	int fromAccount = 0;
	int toAccount = 1;
	
	// The amount of that particular transaction.
	Money amount;
	
	public ATM atm;
	public BankDatabase database;
	public Balances balances;
	public Session session;
	
	Connection conn;

	
	@Before
	public void before2() throws AccountNotFound, CustomerNotFound {
		database = BankDatabase.getInstance();
		
		CustomerEntry retrievedCustomer = database.getCustomerInfo(cardID);
		int customerID = retrievedCustomer.getID();

		AccountEntry checkingAccount = new AccountEntry(customerID, "Checking", 500000, 500000, 100000, 0); //database.getAccountInfo(customerID, 0);
		AccountEntry savingsAccount = new AccountEntry(customerID, "Checking", 500000, 500000, 100000, 0);//database.getAccountInfo(customerID, 1);
		checkingAccount.setBalance(500000);
		checkingAccount.setAvailableBalance(500000);
		checkingAccount.setDailyLimit(100000);
		database.addAccount(0, checkingAccount);
		savingsAccount.setBalance(500000);
		savingsAccount.setAvailableBalance(500000);
		savingsAccount.setDailyLimit(100000);
		database.addAccount(1, savingsAccount);
		System.out.println(3);
	}
	
	
	@Before
	public void before1() throws AccountNotFound, CustomerNotFound, IOException {
		//Boolean value = Files.deleteIfExists(Paths.get("bank.db")); 
		database = BankDatabase.getInstance();
		intPin = 1234;
		CustomerEntry customer = new CustomerEntry(
				0,
				firstName,
				lastName,
				isStudent,
				cardID,
				intPin
		);
		database.addCustomer(customer);
		System.out.println(2);
	}
	
	

	
//	@After
//	public void after() throws SQLException {
//		String url = "jdbc:sqlite:bank.db";
//		Connection conn = DriverManager.getConnection(url);
//		
//		// Clear Customers
//		String sql_delete = "DELETE FROM customers WHERE cardID = ?";
//		PreparedStatement pstmt = conn.prepareStatement(sql_delete);
//		pstmt.setInt(1, cardID);
//		pstmt.execute();
//		pstmt.close();
//		conn.close();
//	}
	
	//Test names are withdraw value _ pin value
	
	@Test(expected = InvalidPINException.class)
	public void minNeg_Min() throws Exception{
		amount = new Money(-1);
		pin = "123";
		
		atm = new ATM(
				bankID, 
				location, 
				bankName, 
				bankAddress, 
				testing, 
				cardID, 
				pin, 
				transactionChoice, 
				fromAccount, 
				toAccount, 
				amount
				);
		Session testSession = new Session(atm, cardID, pin, transactionChoice, fromAccount, toAccount, amount);
		try{
			testSession.performSession();
		}
		catch(Exception e){
			System.out.println(e);
			throw e;
		}
	}

	
	@Test(expected = InvalidPINException.class)
	public void min_Min() throws Exception{
		amount = new Money(0);
		pin = "123";
		
		atm = new ATM(
				bankID, 
				location, 
				bankName, 
				bankAddress, 
				testing, 
				cardID, 
				pin, 
				transactionChoice, 
				fromAccount, 
				toAccount, 
				amount
				);
		Session testSession = new Session(atm, cardID, pin, transactionChoice, fromAccount, toAccount, amount);
		try{
			testSession.performSession();
		}
		catch(Exception e){
			System.out.println(e);
			throw e;
		}
	}
	
	@Test(expected = InvalidPINException.class)
	public void minPos_min() throws Exception{
		amount = new Money(1);
		pin = "123";
		
		atm = new ATM(
				bankID, 
				location, 
				bankName, 
				bankAddress, 
				testing, 
				cardID, 
				pin, 
				transactionChoice, 
				fromAccount, 
				toAccount, 
				amount
				);
		Session testSession = new Session(atm, cardID, pin, transactionChoice, fromAccount, toAccount, amount);
		try{
			testSession.performSession();
		}
		catch(Exception e){
			System.out.println(e);
			throw e;
		}
	}
	
	@Test(expected = InvalidPINException.class)
	public void Nominal_min() throws Exception{
		amount = new Money(500);
		pin = "123";
		
		atm = new ATM(
				bankID, 
				location, 
				bankName, 
				bankAddress, 
				testing, 
				cardID, 
				pin, 
				transactionChoice, 
				fromAccount, 
				toAccount, 
				amount
				);
		Session testSession = new Session(atm, cardID, pin, transactionChoice, fromAccount, toAccount, amount);
		try{
			testSession.performSession();
		}
		catch(Exception e){
			System.out.println(e);
			throw e;
		}
	}
	
	@Test(expected = InvalidPINException.class)
	public void maxNeg_min() throws Exception{
		amount = new Money(999);
		pin = "123";
		
		atm = new ATM(
				bankID, 
				location, 
				bankName, 
				bankAddress, 
				testing, 
				cardID, 
				pin, 
				transactionChoice, 
				fromAccount, 
				toAccount, 
				amount
				);
		Session testSession = new Session(atm, cardID, pin, transactionChoice, fromAccount, toAccount, amount);
		try{
			testSession.performSession();
		}
		catch(Exception e){
			System.out.println(e);
			throw e;
		}
	}
	
	@Test(expected = InvalidPINException.class)
	public void max_min() throws Exception{
		amount = new Money(1000);
		pin = "123";
		
		atm = new ATM(
				bankID, 
				location, 
				bankName, 
				bankAddress, 
				testing, 
				cardID, 
				pin, 
				transactionChoice, 
				fromAccount, 
				toAccount, 
				amount
				);
		Session testSession = new Session(atm, cardID, pin, transactionChoice, fromAccount, toAccount, amount);
		try{
			testSession.performSession();
		}
		catch(Exception e){
			System.out.println(e);
			throw e;
		}
	}
	
	@Test(expected = InvalidPINException.class)
	public void maxPos_min() throws Exception{
		amount = new Money(1001);
		pin = "123";
		
		atm = new ATM(
				bankID, 
				location, 
				bankName, 
				bankAddress, 
				testing, 
				cardID, 
				pin, 
				transactionChoice, 
				fromAccount, 
				toAccount, 
				amount
				);
		Session testSession = new Session(atm, cardID, pin, transactionChoice, fromAccount, toAccount, amount);
		try{
			testSession.performSession();
		}
		catch(Exception e){
			System.out.println(e);
			throw e;
		}
	}	
	
	
	@Test(expected = InvalidAmountException.class)
	public void minNeg_Nominal() throws Exception{
		amount = new Money(-1);
		pin = "1234";
		
		atm = new ATM(
				bankID, 
				location, 
				bankName, 
				bankAddress, 
				testing, 
				cardID, 
				pin, 
				transactionChoice, 
				fromAccount, 
				toAccount, 
				amount
				);
		Session testSession = new Session(atm, cardID, pin, transactionChoice, fromAccount, toAccount, amount);
		try{
			testSession.performSession();
		}
		catch(Exception e){
			System.out.println(e);
			throw e;
		}
	}
	
	
	
	@Test
	public void min_Nominal() throws Exception{
		amount = new Money(0);
		pin = "1234";
		
		atm = new ATM(
				bankID, 
				location, 
				bankName, 
				bankAddress, 
				testing, 
				cardID, 
				pin, 
				transactionChoice, 
				fromAccount, 
				toAccount, 
				amount
				);
		Session testSession = new Session(atm, cardID, pin, transactionChoice, fromAccount, toAccount, amount);
		try{
			testSession.performSession();
		}
		catch(Exception e){
			System.out.println(e);
			throw e;
		}
	}
	
	
	
	@Test(expected = InvalidAmountException.class)
	public void minPos_Nominal() throws Exception{
		amount = new Money(1);
		pin = "1234";
		
		atm = new ATM(
				bankID, 
				location, 
				bankName, 
				bankAddress, 
				testing, 
				cardID, 
				pin, 
				transactionChoice, 
				fromAccount, 
				toAccount, 
				amount
				);
		Session testSession = new Session(atm, cardID, pin, transactionChoice, fromAccount, toAccount, amount);
		try{
			testSession.performSession();
		}
		catch(Exception e){
			System.out.println(e);
			throw e;
		}
	}
	
	
	
	@Test()
	public void Nominal_Nominal() throws Exception{
		amount = new Money(500);
		pin = "1234";
		
		atm = new ATM(
				bankID, 
				location, 
				bankName, 
				bankAddress, 
				testing, 
				cardID, 
				pin, 
				transactionChoice, 
				fromAccount, 
				toAccount, 
				amount
				);
		Session testSession = new Session(atm, cardID, pin, transactionChoice, fromAccount, toAccount, amount);
		try{
			testSession.performSession();
		}
		catch(Exception e){
			System.out.println(e);
			throw e;
		}
	}
	

	
	@Test(expected = InvalidAmountException.class)
	public void maxNeg_Nominal() throws Exception{
		amount = new Money(999);
		pin = "1234";
		
		atm = new ATM(
				bankID, 
				location, 
				bankName, 
				bankAddress, 
				testing, 
				cardID, 
				pin, 
				transactionChoice, 
				fromAccount, 
				toAccount, 
				amount
				);
		Session testSession = new Session(atm, cardID, pin, transactionChoice, fromAccount, toAccount, amount);
		try{
			testSession.performSession();
		}
		catch(Exception e){
			System.out.println(e);
			throw e;
		}
	}

	
	
	@Test()
	public void max_Nominal() throws Exception{
		amount = new Money(1000);
		pin = "1234";
		
		atm = new ATM(
				bankID, 
				location, 
				bankName, 
				bankAddress, 
				testing, 
				cardID, 
				pin, 
				transactionChoice, 
				fromAccount, 
				toAccount, 
				amount
				);
		Session testSession = new Session(atm, cardID, pin, transactionChoice, fromAccount, toAccount, amount);
		try{
			testSession.performSession();
		}
		catch(Exception e){
			System.out.println(e);
			throw e;
		}
	}
	
	
	
	@Test(expected = InvalidAmountException.class)
	public void maxPos_Nominal() throws Exception{
		amount = new Money(1001);
		pin = "1234";
		
		atm = new ATM(
				bankID, 
				location, 
				bankName, 
				bankAddress, 
				testing, 
				cardID, 
				pin, 
				transactionChoice, 
				fromAccount, 
				toAccount, 
				amount
				);
		Session testSession = new Session(atm, cardID, pin, transactionChoice, fromAccount, toAccount, amount);
		try{
			testSession.performSession();
		}
		catch(Exception e){
			System.out.println(e);
			throw e;
		}
	}
	
	
	
	@Test(expected = InvalidPINException.class)
	public void minNeg_Max() throws Exception{
		amount = new Money(-1);
		pin = "12345";
		
		atm = new ATM(
				bankID, 
				location, 
				bankName, 
				bankAddress, 
				testing, 
				cardID, 
				pin, 
				transactionChoice, 
				fromAccount, 
				toAccount, 
				amount
				);
		Session testSession = new Session(atm, cardID, pin, transactionChoice, fromAccount, toAccount, amount);
		try{
			testSession.performSession();
		}
		catch(Exception e){
			System.out.println(e);
			throw e;
		}
	}
	

	
	@Test(expected = InvalidPINException.class)
	public void min_Max() throws Exception{
		amount = new Money(0);
		pin = "12345";
		
		atm = new ATM(
				bankID, 
				location, 
				bankName, 
				bankAddress, 
				testing, 
				cardID, 
				pin, 
				transactionChoice, 
				fromAccount, 
				toAccount, 
				amount
				);
		Session testSession = new Session(atm, cardID, pin, transactionChoice, fromAccount, toAccount, amount);
		try{
			testSession.performSession();
		}
		catch(Exception e){
			System.out.println(e);
			throw e;
		}
	}
	
		
	
	@Test(expected = InvalidPINException.class)
	public void minPos_Max() throws Exception{
		amount = new Money(1);
		pin = "12345";
		
		atm = new ATM(
				bankID, 
				location, 
				bankName, 
				bankAddress, 
				testing, 
				cardID, 
				pin, 
				transactionChoice, 
				fromAccount, 
				toAccount, 
				amount
				);
		Session testSession = new Session(atm, cardID, pin, transactionChoice, fromAccount, toAccount, amount);
		try{
			testSession.performSession();
		}
		catch(Exception e){
			System.out.println(e);
			throw e;
		}
	}
	
	
	
	@Test(expected = InvalidPINException.class)
	public void Nominal_Max() throws Exception{
		amount = new Money(500);
		pin = "12345";
		
		atm = new ATM(
				bankID, 
				location, 
				bankName, 
				bankAddress, 
				testing, 
				cardID, 
				pin, 
				transactionChoice, 
				fromAccount, 
				toAccount, 
				amount
				);
		Session testSession = new Session(atm, cardID, pin, transactionChoice, fromAccount, toAccount, amount);
		try{
			testSession.performSession();
		}
		catch(Exception e){
			System.out.println(e);
			throw e;
		}
	}
	
	
	
	@Test(expected = InvalidPINException.class)
	public void maxNeg_Max() throws Exception{
		amount = new Money(999);
		pin = "12345";
		
		atm = new ATM(
				bankID, 
				location, 
				bankName, 
				bankAddress, 
				testing, 
				cardID, 
				pin, 
				transactionChoice, 
				fromAccount, 
				toAccount, 
				amount
				);
		Session testSession = new Session(atm, cardID, pin, transactionChoice, fromAccount, toAccount, amount);
		try{
			testSession.performSession();
		}
		catch(Exception e){
			System.out.println(e);
			throw e;
		}
	}
	
	
	
	@Test(expected = InvalidPINException.class)
	public void max_Max() throws Exception{
		amount = new Money(1000);
		pin = "12345";
		
		atm = new ATM(
				bankID, 
				location, 
				bankName, 
				bankAddress, 
				testing, 
				cardID, 
				pin, 
				transactionChoice, 
				fromAccount, 
				toAccount, 
				amount
				);
		Session testSession = new Session(atm, cardID, pin, transactionChoice, fromAccount, toAccount, amount);
		try{
			testSession.performSession();
		}
		catch(Exception e){
			System.out.println(e);
			throw e;
		}
	}
	

	
	@Test(expected = InvalidPINException.class)
	public void maxPos_Max() throws Exception{
		amount = new Money(1001);
		pin = "12345";
		
		atm = new ATM(
				bankID, 
				location, 
				bankName, 
				bankAddress, 
				testing, 
				cardID, 
				pin, 
				transactionChoice, 
				fromAccount, 
				toAccount, 
				amount
				);
		Session testSession = new Session(atm, cardID, pin, transactionChoice, fromAccount, toAccount, amount);
		try{
			testSession.performSession();
		}
		catch(Exception e){
			System.out.println(e);
			throw e;
		}
	}
}
