/**
 * Ajik File splitter n joiner
 * Draft version 0.1
 * Copyright (c) Khang Tran
 */

package khang.iwcjff.afsj;

// Generating extensions like .a99, a.100, a.101,... a999, a1000,...
public class DigitsExtensionGenerator extends AbstExtensionGenerator {
    
    private String prefix = "";

    public DigitsExtensionGenerator(int nmin, int nmax) {
        if (nmin < 1 || nmin > nmax) {
			throw new IllegalArgumentException();
        }
        
        // i don't like this.min = min 
        // set the current number         
        max = nmax;
        current = nmin - 1;       
    }
    
    @Override public String next() {
        return (prefix + String.valueOf(++current));
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
