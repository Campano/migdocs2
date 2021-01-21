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
        File test = new File ("/Users/antoinecruveilher/dev/docs/documentation/architecture-fullprofilemode.png");
        System.out.println(getExtension(test));
    }

}