package khang.iwcjff.afsj;

import org.junit.Assert;
import org.junit.Test;
import java.util.NoSuchElementException;

public class LetterExtensionGeneratorTest {
	
	@Test(expected=IllegalArgumentException.class)
	public void constructShouldThrowException0() {
		IExtensionGenerator eg = new LetterExtensionGenerator(
			'a' - 1,
			'z'
		);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructShouldThrowException1() {		
		IExtensionGenerator eg = new LetterExtensionGenerator(
			'a',
			'z' + 1
		);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructShouldThrowException2() {
		IExtensionGenerator eg = new LetterExtensionGenerator(
			'z',
			'a'
		);
	}
	
	@Test public void hasNextAlwaysTrue() {
		IExtensionGenerator eg = new LetterExtensionGenerator(
			'a',
			'a'
		);
		Assert.assertTrue(eg.hasNext());
		Assert.assertTrue(eg.next().equals(
				".__a"
			)
		);
	}
	
	@Test(expected=NoSuchElementException.class)
	public void hasNextShouldThrowException() {
		IExtensionGenerator eg = new LetterExtensionGenerator(
			'a',
			'a'
		);
		Assert.assertTrue(
			eg.hasNext() && eg.next().equals(".__a")
		);
		eg.next();
	}
	
	@Test public void hasNextShouldFalse() {
		IExtensionGenerator eg = new LetterExtensionGenerator(
			'a',
			'a'
		);
		Assert.assertTrue(
			eg.hasNext() && eg.next().equals(".__a")
		);
		Assert.assertFalse(eg.hasNext());
	}
	
	@Test(expected=IllegalArgumentException.class) 
	public void setPrefixShouldThrowException() {
		IExtensionGenerator eg = new LetterExtensionGenerator(
			'a',
			'a'
		);
		eg.setPrefix(" ");
		Assert.assertTrue(eg.getPrefix().equals(".__"));
	}
	
	@Test public void assertSetPrefixWithNonEmptyString() {
		IExtensionGenerator eg = new LetterExtensionGenerator(
			'a',
			'a'
		);
		eg.setPrefix(" iwcjff ");
		Assert.assertTrue(eg.getPrefix().equals(
				".iwcjff"
			)
		);
	}
	
	@Test public void assertSetPrefixWithParamBeginsWithADot() {
		IExtensionGenerator eg = new LetterExtensionGenerator(
			'a',
			'a'
		);
		eg.setPrefix("	.iwcjff		");
		Assert.assertTrue(eg.getPrefix().equals(
				".iwcjff"
			)
		);
	}
}
