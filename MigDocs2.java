import java.util.*;
import java.io.File;

public class MigDocs2 {
    public static void main(String[] args) {
        System.out.println("Hello, World");
        
        String docsPath = "/path/to/origin";
        String docs2Path = "/path/to/destination"; //"/Users/simoncampano/dev/INTERNAL_APPS/training-content/content"
        // TODO : algo de migration

        try{
            new CheckDocs2(docs2Path);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        } 
    }
}
