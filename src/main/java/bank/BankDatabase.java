package bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BankDatabase {

	private static BankDatabase instance = null;
	private Connection conn;

	private BankDatabase() {
		createDatabase();
	}

	public static BankDatabase getInstance() {
		if (instance == null)
			instance = new BankDatabase();

		return instance;
	}

	private void createDatabase() {
		String url = "jdbc:sqlite:bank.db";

		try {
			conn = DriverManager.getConnection(url);
			Statement stmt = conn.createStatement();

			if (conn != null) {
				String createTable;

				// create customers' table
				createTable = "CREATE TABLE IF NOT EXISTS customers (\n" 
											+ " id integer PRIMARY KEY,\n"
											+ " firstName text NOT NULL,\n" 
											+ " lastName text NOT NULL,\n" 
											+ " student boolean,\n"
											+ " cardID integer,\n"
											+ " pin integer\n" 
											+ ");";
				stmt.execute(createTable);

				// create checking accounts' table
				createTable = "CREATE TABLE IF NOT EXISTS checking (\n" 
											+ " ownerId integer,\n" 
											+ " balance integer,\n"
											+ " availableBalance integer,\n" 
											+ " dailyLimit integer\n" 
											+ ");";
				stmt.execute(createTable);

				// create savings accounts' table
				createTable = "CREATE TABLE IF NOT EXISTS saving (\n" 
											+ " ownerId integer,\n" 
											+ " balance integer,\n"
											+ " availableBalance integer,\n" 
											+ " dailyLimit integer\n" 
											+ ");";
				stmt.execute(createTable);

				// create money market accounts' table
				createTable = "CREATE TABLE IF NOT EXISTS moneyMarket (\n" 
											+ " ownerId integer,\n"
											+ " balance integer,\n" 
											+ " availableBalance integer,\n" 
											+ " dailyLimit integer\n" 
											+ ");";
				stmt.execute(createTable);

				// create table for withdrawals from checking
				createTable = "CREATE TABLE IF NOT EXISTS checkingWithdrawals (\n" 
											+ " ownerId integer,\n"
											+ " withdrawal integer,\n" 
											+ " date text\n" 
											+ ");";
				stmt.execute(createTable);

				// create table for withdrawals from savings
				createTable = "CREATE TABLE IF NOT EXISTS savingWithdrawals (\n" 
											+ " ownerId integer,\n"
											+ " withdrawal integer,\n" 
											+ " date text\n" + ");";
				stmt.execute(createTable);

				// create table for withdrawals from money market
				createTable = "CREATE TABLE IF NOT EXISTS moneyMarketWithdrawals (\n" 
											+ " ownerId integer,\n"
											+ " withdrawal integer,\n" 
											+ " date text\n" 
											+ ");";
				stmt.execute(createTable);

			}

		} catch (SQLException e) {
			System.out.println("Database creation: " + e.getMessage());
		}
	}

	public void addCustomer(CustomerEntry customer) {
		String sql = "INSERT INTO customers(firstName,lastName,student,cardID,pin) VALUES(?,?,?,?,?)";

		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, customer.getFirstName());
			pstmt.setString(2, customer.getLastName());
			pstmt.setBoolean(3, customer.isStudent());
			pstmt.setInt(4, customer.getCardID());
			pstmt.setInt(5, customer.getPin());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Add customer: " + e.getMessage());
		}
	}

	public CustomerEntry getCustomerInfo(int cardID) throws CustomerNotFound {
		String sql_select = "SELECT id,firstName,lastName,student,pin FROM customers WHERE cardID = ?";
		CustomerEntry customer = null;
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql_select);
			// set the values
			pstmt.setInt(1, cardID);
			// Execute query
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next() == false) {
				throw new CustomerNotFound(cardID);
			} else {
				customer = new CustomerEntry(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getBoolean("student"), cardID, rs.getInt("pin"));
			}
		} catch (SQLException e) {
			System.out.println("Get customer: " + e.getMessage());
		}
		
		return customer;
	}

	public void addAccount(int typeOfAccount, AccountEntry account) throws AccountNotFound {
		String sql;
		
		switch (typeOfAccount) {
		case 0:
			sql = "INSERT INTO checking(ownerID,balance,availableBalance,dailyLimit) VALUES(?,?,?,?)";
			break;
		case 1:
			sql = "INSERT INTO saving(ownerID,balance,availableBalance,dailyLimit) VALUES(?,?,?,?)";
			break;
		case 2:
			sql = "INSERT INTO moneyMarket(ownerID,balance,availableBalance,dailyLimit) VALUES(?,?,?,?)";
			break;
		default:
			throw new AccountNotFound();
		}
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, account.getOwnerID());
			pstmt.setInt(2, account.getBalance());
			pstmt.setInt(3, account.getAvailableBalance());
			pstmt.setInt(4, account.getDailyLimit());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Add account: " + e.getMessage());
		}
	}

	public AccountEntry getAccountInfo(int ownerID, int typeOfAccount) throws AccountNotFound {
		String sql_select_account;
		String sql_select_withdrawals;
		String accountType;
		
		switch (typeOfAccount) {
		case 0:
			sql_select_account = "SELECT balance,availableBalance,dailyLimit FROM checking WHERE ownerID = ?";
			sql_select_withdrawals = "SELECT withdrawal FROM checkingWithdrawals WHERE ownerID = ? AND date = date('now')";
			accountType = "checking";
			break;
		case 1:
			sql_select_account = "SELECT balance,availableBalance,dailyLimit FROM saving WHERE ownerID = ?";
			sql_select_withdrawals = "SELECT withdrawal FROM savingWithdrawals WHERE ownerID = ? AND date = date('now')";
			accountType = "savings";
			break;
		case 2:
			sql_select_account = "SELECT balance,availableBalance,dailyLimit FROM moneyMarket WHERE ownerID = ?";
			sql_select_withdrawals = "SELECT withdrawal FROM moneyMarketWithdrawals WHERE ownerID = ? AND date = date('now')";
			accountType = "money market";
			break;
		default:
			throw new AccountNotFound();
		}
		
		AccountEntry account = null;
		int withdrawals = 0;
		
		try {
			// Get withdrawals
			PreparedStatement pstmt = conn.prepareStatement(sql_select_withdrawals);
			pstmt.setInt(1, ownerID);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				withdrawals += rs.getInt("withdrawal");
			}
			
			// Get account's other info
			pstmt = conn.prepareStatement(sql_select_account);
			pstmt.setInt(1, ownerID);
			rs = pstmt.executeQuery();
			
			if (rs.next() == false) {
				throw new AccountNotFound(accountType);
			} else {
				account = new AccountEntry(ownerID, accountType, rs.getInt("balance"), rs.getInt("availableBalance"), rs.getInt("dailyLimit"), withdrawals);
			}
		} catch (SQLException e) {
			System.out.println("Account info: " + e.getMessage());
		}
		
		return account;
	}
	
	public void updateAccountInfo(int typeOfAccount, AccountEntry account) throws AccountNotFound {
		String sql_update;
		
		switch (typeOfAccount) {
		case 0:
			sql_update = "UPDATE checking SET balance = ?, availableBalance = ?, dailyLimit = ? WHERE ownerID = ?";
			break;
		case 1:
			sql_update = "UPDATE saving SET balance = ?, availableBalance = ?, dailyLimit = ? WHERE ownerID = ?";
			break;
		case 2:
			sql_update = "UPDATE moneyMarket SET balance = ?, availableBalance = ?, dailyLimit = ? WHERE ownerID = ?";
			break;
		default:
			throw new AccountNotFound();
		}
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql_update);
			// set the values
			pstmt.setInt(1, account.getBalance());
			pstmt.setInt(2, account.getAvailableBalance());
			pstmt.setInt(3, account.getDailyLimit());
			pstmt.setInt(4, account.getOwnerID());
			// Execute update
			pstmt.executeUpdate();
			pstmt.close();
			
		} catch (SQLException e) {
			System.out.println("Update account: " + e.getMessage());
		}
	}
	
	public void addWithdrawal(int typeOfAccount, int ownerID, int amount) throws AccountNotFound {
String sql;
		
		switch (typeOfAccount) {
		case 0:
			sql = "INSERT INTO checkingWithdrawals(ownerID,withdrawal,date) VALUES(?,?,date('now'))";
			break;
		case 1:
			sql = "INSERT INTO savingWithdrawals(ownerID,withdrawal,date) VALUES(?,?,date('now'))";
			break;
		case 2:
			sql = "INSERT INTO moneyMarketWithdrawals(ownerID,withdrawal,date) VALUES(?,?,date('now'))";
			break;
		default:
			throw new AccountNotFound();
		}
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, ownerID);
			pstmt.setInt(2, amount);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Add withdrawal: " + e.getMessage());
		}
	}

	public class CustomerNotFound extends Exception {

		public CustomerNotFound(int cardID) {
			super("Customer with cardID " + cardID + "does not exist.");
		}

	}

	public class AccountNotFound extends Exception {

		public AccountNotFound(String accountType) {
			super("Customer does not have a " + accountType + "account.");
		}
		
		public AccountNotFound() {
			super("Invalid account type.");
		}

	}

}
