/**
 * Ajik File splitter n joiner
 * Draft version 0.1
 * Copyright (c) Khang Tran
 */

package khang.iwcjff.afsj;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class AjikJoiner {
    
    private List<File> files;
    
    // file object for file output
    private File fileOutput;
    
    // stream object for file output
    private FileOutputStream fileDest;
    
    // total size of all parts
    private int totalSize;
    
    // total bytes read 
    private int bytesRead = 0; 
    
    // block size
    private int chunk = 1024*1024; // 1 MB
    
    // block size constant
    private final int CHUNK_SIZE = 1024*1024; // 1MB
    
    // delete input parts after joining, default = false    
    private boolean deleteAfterJoining = false;
    
    public AjikJoiner(Collection<File> fileCollection, File file_output) {
        this.files = new ArrayList<>();
        this.files.addAll(fileCollection);
        this.fileOutput = file_output; 
                     
        // initialize the total size
        this.totalSize = 0;
        
        // counts the total size of files
        for(int i = 0; i < this.files.size(); ++i) totalSize += this.files.get(i).length();        
    }
    
    // if overwrite = false (default true), notify if file output exists and exit
    // else overwrite = true && file output exists, overwrite the output file
    public void startJoining(boolean overwrite) throws IOException {
        
        // list of files is empty
        if (files.isEmpty()) {
            return;
        }
        
        // notify if file ouput exists
        if ((!overwrite) && fileOutput.exists()) {
            String fmt = String.format(
				"File ouput %s exists\n",
				fileOutput.getAbsolutePath()
            );
            System.out.print(fmt);
            return;
        }
        
        // determine chunk size to block read
        if (totalSize < CHUNK_SIZE) chunk = totalSize;
        
        Collection<FileInputStream> fisCollection = new ArrayList<>();
        for(int i = 0; i < files.size(); ++i) {
            try {
                fisCollection.add(new FileInputStream(files.get(i)));
            } catch(FileNotFoundException e) {                                
                System.out.printf("File %s not found\n", files.get(i).getName());
                return;
            }
        }
        
        SequenceInputStream f = new SequenceInputStream(Collections.enumeration(fisCollection));         
        // create new file ouput on disk
        fileDest = new FileOutputStream(fileOutput);
        byte[] inBytes = new byte[chunk];        
        int byteRead;
        while((byteRead = f.read(inBytes)) != -1) {
            bytesRead += byteRead;
            fileDest.write(inBytes, 0, byteRead);
        }      
        fileDest.flush();
        f.close();
    }
    
    public void close() {        
        // try to close resource
        try {
            if (fileDest != null) fileDest.close();
            if (deleteAfterJoining) {
                for(int i = 0; i < files.size(); ++i) files.get(i).delete(); // or deleteOnExists()
            }
        } catch(IOException e) {
            System.out.printf("Error on closing the destination file %s\n", fileOutput.getName());
        }       
    }
    
    // List.add() always return true
    protected boolean add(File file) {
        if (file.isFile()) {
            files.add(file);
            totalSize += file.length();
            return true;
        }
        throw new IllegalArgumentException();
    }
    
    public void append(File file) {       
        // check if file exists
        if (add(file)) {
            long fsize = file.length();
            if (fsize < CHUNK_SIZE) chunk = (int)fsize;
            byte[] inBytes = new byte[chunk];
            int byteRead;
            try {
                FileInputStream f = new FileInputStream(file);
                // if file output stream doesn't exist, create it
                if (!fileOutput.exists()) {                    
                    fileDest = new FileOutputStream(fileOutput);
                } else {
                    fileDest = new FileOutputStream(fileOutput, true); // append = true
                }
                while((byteRead = f.read(inBytes)) != -1) {
                    bytesRead += byteRead;
                    fileDest.write(inBytes, 0, byteRead);
                }
                fileDest.flush();
                f.close();
            } catch(FileNotFoundException e) {
                System.out.printf("File %s not found\n", file.getName());
            } catch(IOException e) {
                System.out.printf("Error occurs on reading or writing file %s\n",
					file.getName()
				);
            } finally {
				               
            }
        }
    }
        
    public void setDeletingSourceAfterJoining(boolean flag) {
        deleteAfterJoining = flag;
    }
}
