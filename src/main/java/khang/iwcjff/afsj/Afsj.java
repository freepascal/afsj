/**
 * Ajik File splitter n joiner
 * Draft version 0.1
 * Code first, think later
 * Copyright (c) Khang Tran
 */

package khang.iwcjff.afsj;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Afsj {    
        
    public static void main(String args[]) throws IOException {
                   
        if (args.length < 2) {            
            printHelp();
            return;
        }
        File source = new File(args[1]);        
        File target = null;
        if (args.length >= 3) {
            target = new File(args[2]);
        }
        
        if (!source.exists()) {
            System.out.printf("Source %s not found\n",
                        source.getAbsolutePath()
                    );
            return;
        }
        if (!source.isFile()) {
            System.out.printf("Source %s must be a file\n",
                        source.getAbsolutePath()
                    );
        }                
        // joining mode
        if (args[0].equalsIgnoreCase("join")) {
			if (args.length < 3) { 
                printHelp();
                return;
			}
			AjikJoiner joiner = new AjikJoiner(
				new SequenceFileExists(source).getSequenceFileExists(),
                target
			);             
			boolean delete = (args.length == 4 && args[3].equalsIgnoreCase("delete"));
			joiner.setDeletingSourceAfterJoining(delete);
			joiner.startJoining(true);
			joiner.close();
			System.out.println("Has done!");
        } else if (args[0].equalsIgnoreCase("append")) {    
            // APPEND MODE
            if (args.length < 3) {
                printHelp();
                return;
            }
            AjikJoiner joiner = new AjikJoiner(
                Arrays.asList(source),
                target
            );            
            boolean delete = (args.length == 4 && args[3].equalsIgnoreCase("delete"));
            joiner.setDeletingSourceAfterJoining(delete);
            joiner.append(source);
            joiner.close();
            System.out.println("Has done!");
        } else if (args[0].equalsIgnoreCase("split")) {
            
            if (args.length <= 3) {
                printHelp();
                return;
            }
            // split mode
            boolean delete = false;
            if (args.length == 5) {
                delete = (args.length == 5 && (args[3].equalsIgnoreCase("delete") || args[4].equalsIgnoreCase("delete")));
            }
            if (args.length == 5 && (!delete)) {
                System.out.println("Parameters is illegal");
                return;
            }
            if (args.length == 4 && args[3].equalsIgnoreCase("delete")) {
                System.out.printf("Missing split parameter\n");
                return;
            }
            String splitModeString = (args[3].equalsIgnoreCase("delete"))? args[4]: args[3];
            long splitParam = 1L;
            boolean byParts = false;
            if (splitModeString.endsWith("p") || splitModeString.endsWith("P")) {
                byParts = true;
                try {
                    splitParam = Integer.parseInt(splitModeString.substring(0, splitModeString.length()-1));
                } catch(NumberFormatException e) {
                    System.out.println("NumberFormatException thrown");
                    return;
                }
            } else {
                
                if (splitModeString.charAt(splitModeString.length() - 1) >= '0' && splitModeString.charAt(splitModeString.length()-1) <= 9 
                       && (splitModeString.charAt(splitModeString.length()) == 'b' || splitModeString.charAt(splitModeString.length()) == 'B'))  {
                    try {
                        splitParam = Integer.parseInt(splitModeString.substring(0, splitModeString.length()-1));
                    } catch(NumberFormatException e) {
                        System.out.printf("Parameter is illegal\n");
                        return;
                    }                 
                } else {                                  
                    try {
                        splitParam = Integer.parseInt(splitModeString.substring(0, splitModeString.length()-2));                        
                        String splitEnd = splitModeString.substring(splitModeString.length()-2, splitModeString.length()).toLowerCase();
                        if (splitEnd.endsWith("kb")) splitParam *= 1024;
                        else if (splitEnd.endsWith("mb")) splitParam *= (1024*1024);
                        else if (splitEnd.endsWith("gb")) splitParam *= (1024*1024*1024);
                        /*
                        else {
                            System.out.println("Split parameter is illegal");
                            return;
                        }
                         */
                    } catch(NumberFormatException e) {
                        System.out.printf("Parameter is illegal\n");
                        return;
                    }
                }
            }
            if (!target.exists()) target.mkdirs();
            AjikSplitter splitter = (byParts)? new AjikSplitter(splitParam, source, target): new AjikSplitter(source, splitParam, target);
            splitter.setDeletingSourceAfterSplitting(delete);
            int num = 0;
            while(splitter.hasNext()) {
                splitter.next();
                System.out.printf("Part %d has created\n", ++num);
            }
            splitter.close();
        } else if (args[0].equalsIgnoreCase("crc32")) {
            
            if (args.length > 3) {
                System.out.printf("Command is too long\n");
                return;
            }
            if (args.length == 2) {
                System.out.printf("CRC32 checksum of %s is %s\n", 
					source.getAbsolutePath(),
					new CRC32Checksum(source).getCRC32String()
				);
                return;
            }
            if ((!target.exists()) || target.isDirectory()) {
                System.out.printf("Parameter args[2] is illegal\n");
                return;
            }
            CRC32Checksum f1 = new CRC32Checksum(source);
            CRC32Checksum f2 = new CRC32Checksum(target);
            System.out.printf("CRC32 checksum of %s is %s\n",
                        source.getAbsolutePath(),
                        f1.getCRC32String()
                    );
            System.out.printf("CRC32 checksum of %s is %s\n",
                        target.getAbsolutePath(),
                        f2.getCRC32String()
                    );
            if (f1.getChecksum() == f2.getChecksum()) 
                System.out.printf("Equals\n");     
            else 
                System.out.printf("Not equals\n");
        }
    }
    
    public static void printHelp() {
            System.out.println("Use: ");
            System.out.println("===== Splitting =====");
            System.out.println("java -jar afsj.jar split [source] [dir output] [split mode] [config]");
            System.out.println("java -jar afsj.jar split [source] [dir output] [config] [split mode]");
            System.out.println("1. To split file data.dat into 10 equals-size parts");
            System.out.println("        java -jar afsj.jar split C:/data.dat C:/ 10p");       
            System.out.println("2. To split file data.dat into every 100mb parts");
            System.out.println("        java -jar afsj.jar split C:/data.dat C:/ 100mb");
            System.out.println("3. To split file data.dat into 10 equals-size parts and delete source after splitting");
            System.out.println("        java -jar afsj.jar split C:/data.dat C:/ 10p delete");
            System.out.println("    or  java -jar afsj.jar split C:/data.dat C:/ delete 10p");
            System.out.println("===== Joining =====");
            System.out.println("java -jar afsj.jar join [first part] [file output] [config]");            
            System.out.println("1. To join parts into one file using the first part");
            System.out.println("        java -jar afsj.jar join C:/data.dat.001 D:/");
            System.out.println("2. To join parts into one file and delete the source after joining");
            System.out.println("        java -jar afsj.jar join C:/data.dat.__a D:/ delete");
            System.out.println("===== Appending =====");
            System.out.println("java -jar afsj.jar append [source] [file target] [config]");          
            System.out.println("1. To append part 12 C:/data.dat.012 to target file D:/data");
            System.out.println("        java -jar afsj.jar append C:/data.dat.012 D:/data");
            System.out.println("2. Delete source after appending");
            System.out.println("        java -jar afsj.jar append C:/data.dat.012 D:/data delete");
            System.out.println("===== CRC32 Comparison =====");
            System.out.println("java -jar afsj.jar crc32 [source1] [source2]");
            System.out.println("1. To caculate crc32-checksum of the file, use");
            System.out.println("        java -jar afsj.jar crc32 C:/file.dat");
            System.out.println("2. To compare a.dat crc32-checksum with b.dat crc32-checksum");
            System.out.println("        java -jar afsj.jar crc32 C:/a.dat C:/b.dat");        
    }
}
