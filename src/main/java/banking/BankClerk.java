package banking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import bank.AccountEntry;
import bank.BankDatabase;
import bank.BankDatabase.AccountNotFound;
import bank.BankDatabase.CustomerNotFound;
import bank.CustomerEntry;

public class BankClerk {
	private BankDatabase database;

	public BankClerk() {
		database = BankDatabase.getInstance();
	}

	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String choice = "";
			while (!choice.toLowerCase().equals("q")) {
				System.out.println("What do you want to do? (Q or q to exit)");
				System.out.println("\t1. Add customer.");
				System.out.println("\t2. Add account.");
				System.out.println("\t3. Add withdrawal.");
				System.out.print("Choice: ");
				choice = reader.readLine().trim().toLowerCase();
				switch (choice) {
				case "1":
					addCustomer(reader);
					break;
				case "2":
					addAccount(reader);
					break;
				case "3":
					addWithdrawal(reader);
					break;
				case "q":
					break;
				default:
					System.out.println("Choose one of available cases.");
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addCustomer(BufferedReader reader) {
		try {
			System.out.print("User's first name: ");
			String firstName = reader.readLine();
			System.out.print("User's last name: ");
			String lastName = reader.readLine();
			String student;
			boolean isStudent;
			do {
				System.out.print("Is user a student? (Y or N): ");
				student = reader.readLine();
				student = student.trim().toLowerCase();
			} while (!((student.equals("y")) || (student.equals("n"))));

			if (student.toLowerCase().equals("y"))
				isStudent = true;
			else
				isStudent = false;

			System.out.print("User's card ID: ");
			int cardID = Integer.parseInt(reader.readLine());
			System.out.print("User's PIN: ");
			int pin = Integer.parseInt(reader.readLine());

			CustomerEntry customer = new CustomerEntry(0, firstName, lastName, isStudent, cardID, pin);

			database.addCustomer(customer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addAccount(BufferedReader reader) {
		try {
			System.out.print("Owner's card ID: ");
			String cardID = reader.readLine();
			CustomerEntry customer;
			try {
				customer = database.getCustomerInfo(Integer.parseInt(cardID));
			} catch (NumberFormatException | CustomerNotFound e) {
				System.out.println("Something went wrong with the card ID inserted.");
				return;
			}
			int ownerID = customer.getID();

			String accountChosen = "";
			while (!((accountChosen.equals("1")) || (accountChosen.equals("2")) || (accountChosen.equals("3")))) {
				System.out.println("Account's type: ");
				System.out.println("\t1. Checking");
				System.out.println("\t2. Savings");
				System.out.println("\t3. Money Market");
				System.out.print("Choice: ");
				accountChosen = reader.readLine().trim();
			}
			
			int accountType = Integer.parseInt(accountChosen);
			String accountTypeName;
			switch (accountType) {
			case 1:
				accountTypeName = "Checking";
				break;
			case 2:
				accountTypeName = "Savings";
				break;
			case 3:
				accountTypeName = "Money Market";
				break;
			default:
				System.out.println("Something went wrong with the account type.");
				return;
			}
			
			System.out.print("Account's balance (in cents): ");
			int balance = Integer.parseInt(reader.readLine());
			System.out.print("Account's available balance (in cents): ");
			int availableBalance = Integer.parseInt(reader.readLine());
			System.out.print("Account's daily limit (in cents): ");
			int dailyLimit = Integer.parseInt(reader.readLine());

			AccountEntry account = new AccountEntry(ownerID, accountTypeName, balance, availableBalance, dailyLimit, 0);

			try {
				database.addAccount(accountType - 1, account);
			} catch (AccountNotFound e) {
				System.out.println("Trying to add wrong type of account.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addWithdrawal(BufferedReader reader) {
		try {
			System.out.print("Owner's card ID: ");
			String cardID = reader.readLine();
			CustomerEntry customer;
			try {
				customer = database.getCustomerInfo(Integer.parseInt(cardID));
			} catch (NumberFormatException | CustomerNotFound e) {
				System.out.println("Something went wrong with the card ID inserted.");
				return;
			}
			int ownerID = customer.getID();

			String accountChosen = "";
			while (!((accountChosen.equals("1")) || (accountChosen.equals("2")) || (accountChosen.equals("3")))){
				System.out.println("Account's type: ");
				System.out.println("\t1. Checking");
				System.out.println("\t2. Savings");
				System.out.println("\t3. Money Market");
				System.out.print("Choice: ");
				accountChosen = reader.readLine().trim();
			}
			
			int accountType = Integer.parseInt(accountChosen);
			
			System.out.print("Amount of withdrawal (in cents): ");
			int amount = Integer.parseInt(reader.readLine());

			try {
				database.addWithdrawal(accountType - 1, ownerID, amount);
			} catch (AccountNotFound e) {
				System.out.println("Trying to add wrong type of account.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		BankClerk clerk = new BankClerk();
		clerk.run();

	}

}
