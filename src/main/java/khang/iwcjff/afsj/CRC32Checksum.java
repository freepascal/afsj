/**
 * Ajik File splitter n joiner
 * Draft version 0.1
 * Copyright (c) Khang Tran
 */

package khang.iwcjff.afsj;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.CRC32;

public class CRC32Checksum {
    
    // file to calculate checksum
    private File file;
    
    // file crc-32 checksum value
    // if checksum value = 1, there is an error
    private long checksum = -1;
    
    public CRC32Checksum(File fi) {
        if (!fi.exists()) {
			String fmt = String.format("File %s not found", fi.getAbsolutePath());
			throw new IllegalArgumentException(fmt);
        }
        file = fi;     
    }
    
    // if value = -1, raise error on calculating checksum
    public long getChecksum() {
        if (checksum != -1) return checksum;
        CRC32 crc = new CRC32();        
        try(FileInputStream fis = new FileInputStream(file)) {
            byte[] inBytes = new byte[1024*1024];
            int bytesRead;
            while((bytesRead = fis.read(inBytes)) != -1) {
                crc.update(inBytes, 0, bytesRead);
            }
            fis.close();
            checksum = crc.getValue();   
		} catch(FileNotFoundException e) {
			System.out.printf("File %s not found\n", file.getAbsolutePath());
		} catch(IOException e) {
			System.out.printf("An error occurs while calculating checksum of file %s\n", file.getAbsolutePath());
		}
        return checksum;
    }
    
    public String getCRC32String() {
        if (checksum != -1) return Long.toHexString(checksum);
        return Long.toHexString(getChecksum());
    }
}
