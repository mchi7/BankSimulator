import java.net.InetAddress;

import atm.ATM;
import banking.Money;

public class ATMMain {

	public static void main(String[] args) {
		try {
			// Bank information. Important only for the receipt.
			int bankID = 0;
			String location = "London";
			String bankName = "CIBC Branch";
			InetAddress bankAddress = null;
			
			// Parameters used when testing the app.
			boolean testing = true;
			int cardID = 6789;
			String pin = "678";
			// Use to choose type of transaction.
			// (0 = withdrawal, 1 = deposit, 2 = transfer, 3 = inquiry)
			int transactionChoice = 0;
			// Choose accounts the money are coming from or going to.
			// (0 = checking, 1 = savings, 2 = money market)
			int fromAccount = 0;
			int toAccount = 1;
			// The amount of that particular transaction.
			Money amount = new Money(20);
			ATM theATM = new ATM(
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

			new Thread(theATM).start();

			Thread.sleep(5000);

			System.out.println("Switch ATM ON.");
			theATM.switchOn();

			Thread.sleep(5000);

			System.out.println("Set initial cash available to the machine.");
			theATM.getCashDispenser().setInitialCash(new Money(2000));
			System.out.println("Connect to bank.");
			theATM.getNetworkToBank().openConnection();

			theATM.cardInserted();

			Thread.sleep(5000);

			System.out.println("Switch ATM OFF.");
			theATM.switchOff();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
