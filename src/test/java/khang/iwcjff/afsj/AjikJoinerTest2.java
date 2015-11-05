package khang.iwcjff.afsj;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class AjikJoinerTest2 {
	
	@Before public void setUp() throws IOException {
		// preparing environment for joinerUseAppendMethod1()
		File src = new File("tests/joiner/setup/5waystolove.txt.001");
		File dst = new File("tests/joiner/11/5waystolove.txt.001");
		if (!IOUtils.copyFile(src, dst).toFile().exists()) {
			throw new RuntimeException();
		}		
	}
	
	@Test public void joinerUseAppendMethod1() {
		File fileStart = new File("tests/joiner/11/5waystolove.txt.001");
		AjikJoiner joiner = new AjikJoiner(
			Arrays.asList(fileStart),
			new File(IOUtils.getOriginalFileName(fileStart))
		);
		joiner.setDeletingSourceAfterJoining(true);
		joiner.append(fileStart);
		joiner.close();
	}
	
	@Test public void joinerUseAppendMethod2() {
		File fileStart = new File("tests/joiner/12/5waystolove.txt.005");
		AjikJoiner joiner = new AjikJoiner(
			Arrays.asList(fileStart),
			new File(IOUtils.getOriginalFileName(fileStart))
		);
		joiner.append(fileStart);
		joiner.close();
	}
}
