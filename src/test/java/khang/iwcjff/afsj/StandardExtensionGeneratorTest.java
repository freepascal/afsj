package khang.iwcjff.afsj;

import org.junit.Assert;
import org.junit.Test;

public class StandardExtensionGeneratorTest {
	
	@Test(expected=IllegalArgumentException.class) 
	public void constructShouldThrowException0() {
		IExtensionGenerator eg = new StandardExtensionGenerator(
			0,
			9999
		);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructShouldThrowException1() {
		IExtensionGenerator eg = new StandardExtensionGenerator(
			9999, 
			0
		);
	}
	
	@Test public void assertNextShouldTrue() {
		IExtensionGenerator eg = new StandardExtensionGenerator(
			1,
			9999
		);
		Assert.assertTrue(eg.hasNext() && eg.next().equals(".0001"));
	}
	
	@Test public void getPrefixShouldEmptyString() {
		IExtensionGenerator eg = new StandardExtensionGenerator(
			1,
			1
		);
		Assert.assertTrue(eg.getPrefix().equals(""));
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void setPrefixShouldThrowException() {
		IExtensionGenerator eg = new StandardExtensionGenerator(
			1, 
			1
		);
		eg.setPrefix("Life is short, use Groovy/Java && Python!");
	}
}
