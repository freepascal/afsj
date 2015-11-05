/**
 * Ajik File splitter n joiner
 * Draft version 0.1
 * Copyright (c) Khang Tran
 */

package khang.iwcjff.afsj;

import java.util.NoSuchElementException;

public class LetterExtensionGenerator extends AbstExtensionGenerator {
    
    private int totalDigits;
    private int prefixLength;
    private String prefix = ".__";
    
    public LetterExtensionGenerator(int cmin, int cmax) {        
        if (cmin >= (int)'a' && 
			cmax <= (int)'z' && 
			cmin <= cmax) 
		{
            max = cmax;
            // set the current number = min value - 1;    
            current = cmin - 1;
            return;
        }
		throw new IllegalArgumentException();
    }
           
    @Override public String next() {    
		if (hasNext()) {    
			return (prefix + (char)(++current));
		}
		throw new NoSuchElementException();
    }
    
    @Override public void setPrefix(String prefx) {
        prefx = prefx.trim();
        if (prefx.equals("")) {
			throw new IllegalArgumentException();
        }  
        if (!prefx.startsWith(".")) prefx = "." + prefx;        
        // set the prefix
        prefix = prefx;
    }
    
    @Override public String getPrefix() { return prefix; }           
}
