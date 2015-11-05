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
import java.nio.channels.FileChannel;

public class AjikSplitter {
    
    private File fileInput;
    private File dirOutput;
    private String fileInputPath;
    private FileChannel fileSourceChannel;
    private String targetExtension = "";
    private long totalSize;
    private long totalParts;
    private long partSize;
    private boolean byParts;
    private IExtensionGenerator extGen;
    
    // the pointers 
    private long partPointer;
    private long filePointer;
    
    private boolean deleteAfterSplitting = false;
    
    public AjikSplitter(File finput, long psize, File diroutput) {
        if (!finput.exists()) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "File source " + finput.getName() + " not found",
                    "Warning",
                    javax.swing.JOptionPane.WARNING_MESSAGE
                );
            return;
        }
        byParts = false;
        dirOutput = diroutput;
        // i don't like this.fileInput = fileInput styles
        fileInput = finput;
     //   fileInputPath = fileInput.getAbsolutePath();
        
        // get the total size of source
        totalSize = fileInput.length();
        partSize = psize;
        totalParts = (long)Math.ceil((double)totalSize / partSize);
        partPointer = 0;
        filePointer = 0;
        
        extGen = (totalParts < 1000)? new StandardExtensionGenerator(1, 999): new StandardExtensionGenerator(1, (int)totalParts);
        
        // initialize the object of file input stream
        try {
            fileSourceChannel = new FileInputStream(fileInput).getChannel();
        } catch(FileNotFoundException e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "File " + fileInput.getName() + " not found",
                    "Warning",
                    javax.swing.JOptionPane.WARNING_MESSAGE
                );
        }
    }
       
    public AjikSplitter(long nparts, File finput, File diroutput) {        
        if (!finput.exists()) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "File source " + finput.getName() + " not found",
                    "Warning",
                    javax.swing.JOptionPane.WARNING_MESSAGE                    
                );
            return;
        }
        
        // i don't like this.fileInput = fileInput styles
        byParts = true;
        dirOutput = diroutput;
        fileInput = finput;
        fileInputPath = fileInput.getAbsolutePath();
        
        // get the total size of source
        totalSize = fileInput.length();
        totalParts = nparts;
        partSize = (long)Math.ceil((double)totalSize / totalParts);       
       
        partPointer = 0;
        filePointer = 0;        
        
        extGen = (totalParts < 1000)? new StandardExtensionGenerator(1, 999): new StandardExtensionGenerator(1, (int)totalParts);
        
        // initialize the object of file input stream
        try {
            fileSourceChannel = new FileInputStream(fileInput).getChannel();
        } catch(FileNotFoundException e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "File " + fileInput.getName() + " not found",
                    "Warning",
                    javax.swing.JOptionPane.WARNING_MESSAGE
                );
        }       
    }
    
    public boolean hasNext() {   
        // see here
        return ((partPointer < totalParts) && (partPointer >= 0)); // && extGen.hasNext());
    }
    
    public void next() throws IOException {
        try (FileChannel newPart = new FileOutputStream(new File(dirOutput, fileInput.getName() + extGen.next())).getChannel()) {
            if (filePointer + partSize <= totalSize) {
                fileSourceChannel.transferTo(filePointer, partSize, newPart);
                filePointer += partSize;
                ++partPointer;
            } else if (totalSize < filePointer + partSize) {
                fileSourceChannel.transferTo(filePointer, totalSize - filePointer, newPart);
                filePointer = totalSize;
                ++partPointer;
            }
        }
    }
    
    public void close() {        
        // try to close resource
        try {
            if (fileSourceChannel != null) fileSourceChannel.close();
            if (deleteAfterSplitting) {
                fileInput.deleteOnExit();
            }
        } catch(IOException e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Error on closing source file " + fileInput.getName(),
                    "Warning",
                    javax.swing.JOptionPane.WARNING_MESSAGE
                );
        }            
    }
    
    public void setDeletingSourceAfterSplitting(boolean flag) {
        deleteAfterSplitting = flag;
    }
}
