package atm.transaction;

import banking.Money;

public class BankNotesValidator {
	public static boolean checkWithdrawalNotes(Money amount) {
		int cents = amount.getAmount();
		if (cents%100 == 0) {
			int notes = cents / 100;
			int remainder = notes % 50;
			if (remainder == 0) {
				return true;
			} else {
				remainder %= 20;
				if (remainder == 0) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}
	
	public static boolean checkDepositNotes(Money amount) {
		int cents = amount.getAmount();
		if (cents%100 == 0) {
			int notes = cents / 100;
			int remainder = notes % 50;
			if (remainder == 0) {
				return true;
			} else {
				remainder %= 20;
				if (remainder == 0) {
					return true;
				} else {
					remainder %= 10;
					if (remainder == 0) {
						return true;
					} else {
						return false;
					}
				}
			}
		} else {
			return false;
		}
	}

}
