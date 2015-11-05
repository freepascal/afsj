package khang.iwcjff.afsj;

import org.junit.Test;
import org.junit.Assert;

public class DigitsExtensionGeneratorTest {	
	@Test(expected=IllegalArgumentException.class)
	public void constructShouldThrowException0() {
		IExtensionGenerator eg = new DigitsExtensionGenerator(
			0,
			9999
		);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructShouldThrowException1() {
		IExtensionGenerator eg = new DigitsExtensionGenerator(
			9999,
			1
		);
	}
	
	@Test public void hasNextAlwaysTrue() {
		IExtensionGenerator eg = new DigitsExtensionGenerator(
			99,
			999
		);	
		Assert.assertTrue(eg.hasNext() && eg.next().equals("99"));
		Assert.assertTrue(eg.hasNext() && eg.next().equals("100"));
	}
	
	@Test public void hasNextShouldFalse() {
		IExtensionGenerator eg = new DigitsExtensionGenerator(
			1,
			1
		);
		Assert.assertTrue(eg.hasNext() && eg.next().equals("1"));
		Assert.assertFalse(eg.hasNext());
	}
	
	@Test(expected=IllegalArgumentException.class) 
	public void setPrefixShouldThrowException() {
		IExtensionGenerator eg = new DigitsExtensionGenerator(
			1,
			1
		);
		eg.setPrefix(" ");
		Assert.assertTrue(eg.getPrefix().equals(""));
	}	
	
	@Test public void assertSetPrefixWithNonEmptyString() {
		IExtensionGenerator eg = new DigitsExtensionGenerator(
			1,
			1
		);
		eg.setPrefix(" iwcjff ");
		Assert.assertTrue(eg.getPrefix().equals(
				".iwcjff"
			)
		);		
	}
	
	@Test public void assertSetPrefixWithParamBeginsWithADot() {
		IExtensionGenerator eg = new DigitsExtensionGenerator(
			1, 
			1			
		);
		eg.setPrefix("	.iwcjff		");
		Assert.assertTrue(eg.getPrefix().equals(".iwcjff"));
	}
}
