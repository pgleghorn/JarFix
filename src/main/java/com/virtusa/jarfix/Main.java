package com.virtusa.jarfix;

import io.inbot.utils.ReplacingInputStream;
import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pgleghorn
 */
public class Main {

    static Properties props = new Properties();

    Main() {
        props = new Properties();
        try {
            InputStream propfile = new FileInputStream("jarfix.properties");
            props.load(propfile);
        } catch (Exception e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void main(String args[]) throws IOException {
        Main m = new Main();
        m.modifyJar(props.getProperty("jarfile"));
    }

    void modifyJar(String jarPath) throws IOException {
        Path jarFilePath = Paths.get(jarPath);
        try (FileSystem fs = FileSystems.newFileSystem(jarFilePath, null)) {

            String classname = (String) props.get("classname");
            String classnameTemp = classname + new Date().getTime();

            Path source = fs.getPath(classname);
            if (Files.exists(source)) {
                System.out.println("class " + classname + " found");
            }

            Path temp = fs.getPath(classnameTemp);
            Files.move(source, temp);
            copyAndReplace(temp, source);
            Files.delete(temp);
        }
    }

    void copyAndReplace(Path src, Path dst) throws IOException {
        try {
            byte[] data = Files.readAllBytes(src);

            System.out.println("class is " + data.length + " bytes");

            ByteArrayInputStream targetStream = new ByteArrayInputStream(data);
            ByteArrayOutputStream destStream = new ByteArrayOutputStream();

            String search = ((String) props.get("search"));
            String replace = ((String) props.get("replace"));
            System.out.println("replacing " + search + " with " + replace);

            ReplacingInputStream ris = new ReplacingInputStream(targetStream, search.getBytes("US-ASCII"), replace.getBytes("US-ASCII"));
            int b;
            while ((b = ris.read()) > -1) {
                destStream.write(b);
            }

            Files.write(dst, destStream.toByteArray());
            byte[] dataout = Files.readAllBytes(dst);
            System.out.println("changed: " + !Arrays.equals(data, dataout));
        } catch (Exception e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
