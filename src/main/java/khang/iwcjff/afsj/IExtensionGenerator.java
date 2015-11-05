/**
 * Ajik File splitter n joiner
 * Version 0.2
 * Copyright (c) 2014-2015, Khang Tran
 */

package khang.iwcjff.afsj;

public interface IExtensionGenerator {    
    boolean hasNext();
    String next();    
    void setPrefix(String prefx);
    String getPrefix();
}
