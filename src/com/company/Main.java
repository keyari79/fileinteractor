package com.company;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
 //       doTryCatchFinally();
  //      doTryWithResources();
//        doTryWithResourcesMulti();
   //       doCloseThing();

        String[] data =  {
                "Line 1 ",
                "Line  2 ",
                "Line 3 3 3",
                "Line  4 4 4",
                "Line 5 5 5 5 5"
        };

        try(FileSystem zipFs = openZip(Paths.get("myData.zip"))) {
                copyToZip(zipFs);
                writeToFileInZip1(zipFs,data);
                writeToFileInZip2(zipFs,data);

        } catch (Exception e) { System.out.println(e.getClass().getSimpleName() + "----" + e.getMessage()); }


    }
    // with this method we create a zip file in the current path - return type is a FileSystem
    private static FileSystem openZip(Path zipPath) throws IOException,URISyntaxException {
            Map<String, String> providerProps = new HashMap<>(); providerProps.put("create","true"); // creating the options for creating the zip file
            URI zipUri = new URI("jar:file", zipPath.toUri().getPath(),null); // creating the uri by reading from the current path
            FileSystem zipFs = FileSystems.newFileSystem(zipUri,providerProps); // creating the filesystem itself
            return zipFs;
    }

// here we create a method to copy into the zip file
    private static void copyToZip(FileSystem zipFs) throws IOException{
        Path sourceFile = Paths.get("file1.txt");
        Path destFile = zipFs.getPath("file1Copied.txt");
        Files.copy(sourceFile,destFile,StandardCopyOption.REPLACE_EXISTING);
    }


    private static void writeToFileInZip1(FileSystem zipFs, String[] data) throws IOException {
            try(BufferedWriter writer = Files.newBufferedWriter(zipFs.getPath("/newFile1.txt"))) {
                for (String d:data) {
                    writer.write(d);
                    writer.newLine();
                }
            }
    }

    private static void writeToFileInZip2(FileSystem zipFs, String[] data) throws IOException {
            Files.write(zipFs.getPath("/newFile2.txt"), Arrays.asList(data), Charset.defaultCharset(),StandardOpenOption.CREATE);
    }

    public static void doTryCatchFinally() {
            char[] buff = new char[8];
            int length;
            Reader reader = null;
            try {
                reader = Helper.openReader("file1.txt");
                while ((length = reader.read(buff)) >= 0) {
                    System.out.println("\nlength: " + length + "reading the buffer " + buff.toString());
//                    for (int i = 0; i < length; i++) { //                       System.out.println(buff[i]); //                   }
                }
            } catch ( IOException e ) {
                System.out.println(e.getClass().getSimpleName() + " - " + e.getMessage());
            } finally {
                try {
                    if (reader != null) reader.close();
                } catch (IOException e2) {
                    System.out.println(e2.getClass().getSimpleName() + " --" + e2.getMessage());
                }
            }
        }
        public static void doTryWithResources() {
            char[] buff = new char[8];
            int length;
            try (Reader reader = Helper.openReader("file1.txt")) {
                while ((length = reader.read(buff)) >= 0) {
                    System.out.println("\nlength: " + length);
                    for (int i = 0; i < length; i++) {
                        System.out.println(buff[i]);
                    }
                }
            } catch ( IOException e ) {
                System.out.println(e.getClass().getSimpleName() + " - " + e.getMessage());
            }
        }

        public static void doTryWithResourcesMulti() {
            char[] buff = new char[8]; // defining the size of the stream buffer
            int length;
            try (Reader reader = Helper.openReader("file1.txt");//opening the reading buffer
                 Writer writer = Helper.openWriter("file2.txt") // opening the writing buffer
                 ) {
                while ((length = reader.read(buff)) >= 0) {
                    System.out.println("\nlength: " + length);
                    writer.write(buff, 0, length); // for buffer-of size length - read from file1 and write into file2
                }
            } catch ( IOException e ) {
                System.out.println(e.getClass().getSimpleName() + " - " + e.getMessage());
            }
        }

        public static void doCloseThing() {
            try(MyAutoCloseable ac = new MyAutoCloseable()) {
                ac.saySomething();
            } catch (IOException e) {
                System.out.println(e.getClass().getSimpleName() + " -- " + e.getMessage());
                for(Throwable t:e.getSuppressed())
                    System.out.println("Supressed : " + t.getMessage());
            }
        }




}
