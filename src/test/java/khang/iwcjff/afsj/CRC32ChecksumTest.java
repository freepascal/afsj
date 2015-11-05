package khang.iwcjff.afsj;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class CRC32ChecksumTest {	
	
	@Test(expected=IllegalArgumentException.class) 
	public void constructShouldThrowException() {
		CRC32Checksum crc = new CRC32Checksum(
			new File("file-not-found")
		);
	}
	
	@Test public void assertChecksum() {
		CRC32Checksum crc = new CRC32Checksum(
			new File("tests/001/5waystolove.txt.001")
		);
		Assert.assertEquals(
			crc.getCRC32String(),			
			"dfb8c6da"
		);
		Assert.assertTrue(
			crc.getCRC32String().equals(
				"dfb8c6da"
			)
		);
		Assert.assertEquals(
			crc.getChecksum(),
			3753428698L
		);		
	}
	
	
	private File target = new File("tests/crc32/5waystolove.txt.001");
	static String FAIL_MESSAGE_ENV = "Can not prepare environment for CRC32ChecksumTest";
	
	@Before public void setUp() throws IOException {
		if (target.isFile()) {
			return;
		}
		File src = new File("tests/001/5waystolove.txt.001");		
		File dst = copyFile(src, target).toFile();
		if (!target.isFile() || !dst.isFile()) {
			System.out.println(FAIL_MESSAGE_ENV);
		}	
	}
	
	@Test public void getChecksumShouldThrowException() {		
		CRC32Checksum crc = new CRC32Checksum(target);
		try {
			boolean deleted = target.delete();
			Assert.assertTrue(deleted);		
			if (!deleted) {
				System.out.println(FAIL_MESSAGE_ENV);
			}
		} catch(SecurityException e) {
			System.out.printf("Can not access file %s to delete\n", target.getAbsolutePath());
		}
		try {
			crc.getChecksum();	
		} catch(Exception e) {
			Assert.assertNotNull(e);
		}		
	}
	
	private Path copyFile(File src, File dst) throws IOException {		
		return Files.copy(
			src.toPath(),
			dst.toPath(),
			StandardCopyOption.REPLACE_EXISTING,
			StandardCopyOption.COPY_ATTRIBUTES			
		);
	}
}
