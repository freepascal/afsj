/**
 * Ajik File splitter n joiner
 * Draft version 0.1
 * Copyright (c) Khang Tran
 */

package khang.iwcjff.afsj;

public class StandardExtensionGenerator extends AbstExtensionGenerator {
    
    private int totalDigits;
    private int prefixLength;

    public StandardExtensionGenerator(int nmin, int nmax) {
        if (nmin < 1 || nmin > nmax) {
			throw new IllegalArgumentException();
        }
        
        // i don't like this.min = min styles
        // set the current number        
        max = nmax;
        current = nmin - 1;
        
        // set the total digits of file name extensions
        int length = String.valueOf(nmax).length();
        totalDigits = (length > 3) ? length: 3;    
        prefixLength = totalDigits - String.valueOf(current).length();
    }
        
    @Override public String next() {	
        prefixLength = totalDigits - String.valueOf(++current).length();
        String result = ".";
        for(int i = 0; i < prefixLength; ++i) result += "0";        
        return (result + String.valueOf(current));
    }    
    
    // for compatibility
    @Override public void setPrefix(String prefx) {
		throw new UnsupportedOperationException();
	}
	
    @Override public String getPrefix() { return ""; }
}
