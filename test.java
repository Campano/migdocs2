import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;



public class test {

    public static String getExtension(File file)
	{
        String extension = "";

        int i = file.getName().lastIndexOf('.');
        if (i > 0) {
            extension = file.getName().substring(i+1);
        }
        return extension;
	}

    public static void main(String[] args) {
        String toto = "basic-code-example.md";
        toto = toto.replace('-',' ');
        toto = toto.substring(0,toto.length()-3);
        toto = toto.substring(0, 1).toUpperCase() + toto.substring(1);
        System.out.println(toto);
    }

}