package khang.iwcjff.afsj;

import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.File;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class SequenceFileExistsTest {
		
	@Test(expected=IllegalArgumentException.class) 		
	public void constructShouldThrowException() {
		File f = new File("i-wanna-it-not-exists");
		SequenceFileExists sfe = new SequenceFileExists(f);		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructShouldThrowExceptionWithUncognizedExt() {
		File f = new File(
			makeFilePath("unrecognized")
		);
		SequenceFileExists sfe = new SequenceFileExists(f);
	}
	
	static String EXTENSIONS_RECOGNIZED[] = {
		"001",
		"__a",
		"a1"
	};
	
	@Test public void testExtensionRecognized() {
		for(String ext: EXTENSIONS_RECOGNIZED) {
			File f = new File(
				makeFilePath(ext)
			);
			withFileParameter(f);			
		}
	}	
	
	private void withFileParameter(File f) {
		Assert.assertTrue(f.isFile());		
		SequenceFileExists sfe = new SequenceFileExists(f);
		Assert.assertTrue(
			assertSameFileCollection(
				sfe.getSequenceFileExists(),
				f.getParentFile()				
			)
		);		
	}
	
	// assert List<File> source with List<File> target
	// return true if they are same
	private boolean assertSameFileCollection(List<File> fileColl, File parentDirectory) {
		List<File> filesInDirectory = Arrays.<File>asList(parentDirectory.listFiles());
		Collections.sort(filesInDirectory);		
		if (filesInDirectory == null) {
			throw new IllegalArgumentException();
		}		
		for(File f: fileColl) {
			int found = Collections.<File>binarySearch(filesInDirectory, f, fileComparator);
			if (found < 0) {				
				return false;
			}
		}
		return true;
	}
	
	private static Comparator<File> fileComparator = new Comparator<File>() {
		@Override public int compare(File f1, File f2) {
			return f1.getAbsolutePath().compareTo(f2.getAbsolutePath());
		}
	};
	
	private String makeFilePath(String ext) {
		String fileRelative = String.format(
			"tests/%s/5waystolove.txt.%s",
			ext,
			ext
		);
		return fileRelative;
	}
}
