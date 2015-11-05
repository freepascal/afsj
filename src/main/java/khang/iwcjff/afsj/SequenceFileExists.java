/**
 * Ajik File splitter n joiner
 * Draft version 0.1
 * Copyright (c) Khang Tran
 */

package khang.iwcjff.afsj;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class SequenceFileExists {
    
    private File first;   
    private IExtensionGenerator generator;
    private String pathWithoutExt;
    
    public SequenceFileExists(File fst) {
		if (fst.exists()) {
			first = fst;
			String fstString = fst.getAbsolutePath();
			String extString = fstString.substring(fstString.lastIndexOf("."), fstString.length());
			pathWithoutExt = fstString.substring(0, fstString.lastIndexOf("."));
			if (extString.contains("001")) { 	   
				generator = new StandardExtensionGenerator(1, (int)(Math.pow(10, extString.length()-1)-1));                
			} else if (extString.contains("__a")) {
				generator = new LetterExtensionGenerator('a', 'z');
			} else if (extString.endsWith("1")) {
				generator = new DigitsExtensionGenerator(1, 9999);
				String prefix = extString.substring(0, extString.length() - 1);
				generator.setPrefix(prefix);				
			} else {
				throw new IllegalArgumentException();
			}
			return;
		}	
		// prefer throwing IllegalArgumentException than FileNotFoundException
		throw new IllegalArgumentException();
    }
    
    public List<File> getSequenceFileExists() {
        List<File> result = new ArrayList<>();
		while (generator.hasNext()) {
			File nextFile = new File(pathWithoutExt + generator.next());
			if (nextFile.exists()) {
				result.add(nextFile);
			} else {
				return result;
			}
		}        
        return result;
    }
}
