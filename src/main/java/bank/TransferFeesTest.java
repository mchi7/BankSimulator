package bank;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TransferFeesTest {
	
	FeesCalculator feesObj = new FeesCalculator();
	
	@Parameter(0)
	public boolean isStudent;
	@Parameter(1)
	public int withdrawnAmount;
	@Parameter(2)
	public int fromBalance;
	@Parameter(3)
	public int toBalance;
	@Parameter(4)
	public double feeCharged;
	
	@Parameters
	public static Collection<Object[]> data() { 
		Object[][] data = new Object[][] {
			{true, 9900, 99900, 99900, 10}, {true, 9900, 99900, 100000, 5},
			{true, 9900, 100000, 99900, 50}, {true, 9900, 100000, 100000, 25},
			{true, 10000, 99900, 99900, 5}, {true, 10000, 99900, 100000, 2.5},
			{true, 10000, 100000, 99900, 25}, {true, 10000, 100000, 100000, 12.5},
			{false, 9900, 99900, 99900, 20}, {false, 9900, 99900, 100000, 1}, 
			{false, 10000, 100000, 99900, 100}, {false, 9900, 100000, 100000, 50},
			{false, 10000, 99900, 99900, 10}, {false, 10000, 99900, 100000, 5},
			{false, 10000, 100000, 99900, 50}, {false, 10000, 100000, 100000, 25}
		};
		
		return Arrays.asList(data);

	}
	
	@Test
	public void testMultipleWithdrawalFees() {
		double fee = feesObj.calculateTransferFee(withdrawnAmount, fromBalance, toBalance, isStudent);
		System.out.println(fee);
		assertEquals(fee, feeCharged,0.0);	
	}

}
