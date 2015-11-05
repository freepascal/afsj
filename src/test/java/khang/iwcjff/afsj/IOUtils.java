package khang.iwcjff.afsj;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class IOUtils {
	
	// parameter must be a file
	// for instance: getOriginalFileName(new File("file.ext.001")) return "file.ext"		
	public static String getOriginalFileName(File fp) {
		if (!fp.isFile()) {
			throw new IllegalArgumentException();
		}
		return fp.getAbsolutePath().substring(0, 
			fp.getAbsolutePath().lastIndexOf(".")
		);		
	}
	
	public static Path copyFile(File src, File dst) throws IOException {
		// return the path to the target file		
		return Files.copy(
			src.toPath(),
			dst.toPath(),
			StandardCopyOption.REPLACE_EXISTING,
			StandardCopyOption.COPY_ATTRIBUTES
		);
	}
}
