package khang.iwcjff.afsj;

import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;

import java.io.File;
import java.io.IOException;

public class AjikJoinerTest {	
	
	// directory must end with a slash
	static String DIR = "tests/joiner/";
	
	// only filename
	// to get path we use DIR + FILE_START
	static String FILE_START = "5waystolove.txt.001";
	
	@Before public void setUp() {
		File fileStart = new File(DIR + FILE_START);
		if (!new File(IOUtils.getOriginalFileName(fileStart)).exists()) {
			// copy the "destination-output" file
		}
	}
	
	@Test public void joinerWithOverwriteTrue() {
		joinerWithOverwriteConfig(true);
	}
	
	
	@Test public void joinerWithOverwriteFalse() {
		joinerWithOverwriteConfig(false);
	}
		
	// construct AjikJointer with config overwrite flag
	void joinerWithOverwriteConfig(boolean overwrite_flag) {
		File file001 = new File(DIR + FILE_START);
		AjikJoiner joiner = new AjikJoiner(
			new SequenceFileExists(file001).getSequenceFileExists(),
			new File(IOUtils.getOriginalFileName(file001))
		);
		// start joining
		try {
			joiner.startJoining(overwrite_flag); // overwrite = true
		} catch(IOException e) {
			System.out.printf("I/O error occurs while joining files\n");
		} finally {
			joiner.close();
		}		
	}
}
