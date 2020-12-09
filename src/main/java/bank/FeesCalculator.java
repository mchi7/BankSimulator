package bank;

import java.util.Calendar;

public class FeesCalculator {
	public int calculateWithdrawalFee(int amount, int accountBalance, boolean student, int dayOfWeek) {
		// Calculate fee percentage
		float feePercentage;
		// Check if student
		if (student) {
			// Check if it's weekend
			if ((dayOfWeek == Calendar.SATURDAY) || (dayOfWeek == Calendar.SUNDAY)) {
				feePercentage = 0.0f;
			} else {
				feePercentage = 0.001f;
			}
		} else {
			if (accountBalance < 100000) {
				feePercentage = 0.002f;
			} else if (accountBalance < 1000000) {
				feePercentage = 0.001f;
			} else {
				feePercentage = 0.0f;
			}
		}
		
		// Calculate fee
		int fee = Math.round(amount * feePercentage);
		
		return fee;
	}
	
	public int calculateDepositInterest(int amount, int accountBalance, boolean student) {
		// Calculate interest percentage
		float interestPercentage;
		// Check if student
		if (student) {
			// Check amount deposited
			if (amount > 10000) {
				// Check balance
				if (accountBalance > 100000) {
					interestPercentage = 0.01f;
				} else {
					interestPercentage = 0.005f;
				}
			} else {
				if (accountBalance > 500000) {
					interestPercentage = 0.005f;
				} else {
					interestPercentage = 0.002f;
				}
			}
		} else {
			if (amount > 50000) {
				if (accountBalance > 500000) {
					interestPercentage = 0.01f;
				} else {
					interestPercentage = 0.005f;
				}
			} else {
				if (accountBalance > 1000000) {
					interestPercentage = 0.005f;
				} else {
					interestPercentage = 0.0f;
				}
			}
		}
		
		// Calculate interest
		int interest = Math.round(amount * interestPercentage);
		
		return interest;
	}
	
	public int calculateTransferFee(int amount, int fromAccountBalance, int toAccountBalance, boolean student) {
		// Calculate fee percentage
		float feePercentage;
		// Check if student
		if (student) {
			if (amount < 10000) {
				if (fromAccountBalance < 100000) {
					if (toAccountBalance < 100000) {
						feePercentage = 0.001f;
					} else {
						feePercentage = 0.0005f;
					}
				} else {
					if (toAccountBalance < 100000) {
						feePercentage = 0.005f;
					} else {
						feePercentage = 0.0025f;
					}
				}
			} else {
				if (fromAccountBalance < 100000) {
					if (toAccountBalance < 100000) {
						feePercentage = 0.0005f;
					} else {
						feePercentage = 0.00025f;
					}
				} else {
					if (toAccountBalance < 100000) {
						feePercentage = 0.0025f;
					} else {
						feePercentage = 0.00125f;
					}
				}
			}
		} else {
			if (amount < 10000) {
				if (fromAccountBalance < 100000) {
					if (toAccountBalance < 100000) {
						feePercentage = 0.002f;
					} else {
						feePercentage = 0.001f;
					}
				} else {
					if (toAccountBalance < 100000) {
						feePercentage = 0.01f;
					} else {
						feePercentage = 0.005f;
					}
				}
			} else {
				if (fromAccountBalance < 100000) {
					if (toAccountBalance < 100000) {
						feePercentage = 0.001f;
					} else {
						feePercentage = 0.0005f;
					}
				} else {
					if (toAccountBalance < 100000) {
						feePercentage = 0.005f;
					} else {
						feePercentage = 0.0055f;
					}
				}
			}
		}
		
		// Calculate fee
		int fee = Math.round(amount * feePercentage);
		
		return fee;
	}
}
