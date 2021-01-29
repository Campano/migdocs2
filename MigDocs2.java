import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.nio.charset.Charset;

public class MigDocs2 {
    private final static String DOCS_PATH = "/Users/antoinecruveilher/dev/docs/documentation";
    private final static String DOCS2_PATH = "/Users/antoinecruveilher/Desktop/DocsForSimplicity/docs2";
    // private final static String DOCS_PATH = "/Users/simoncampano/dev/simplicite.io/docs.simplicite.io/documentation";
    // private final static String DOCS2_PATH = "/Users/simoncampano/dev/tmp";
    private final static Pattern MD_FILE_PATTERN = Pattern.compile(".*?([a-zA-Z0-9_-]+\\.md).*");
    private final static Pattern DIR_NAME_PATTERN = Pattern.compile("[a-z-]+");
    private final static Pattern PNG_FILE_PATTERN = Pattern.compile(".*?([a-zA-Z0-9_-]+\\.png).*");
    private static ArrayList < String > lessonOrder = new ArrayList < > ();
    private static ArrayList < String > fichierTraites = new ArrayList < > ();
    private final static int sizePrefixForJSON = 3;
    private final static String characterSet = "UTF-8";

    public static void main(String[] args) {
        System.out.println("Welcome to MigDocs2 tool");
        try {
            migrate();
        } catch (MigDocs2Exception e) {
            System.out.println(e.getMessage());
        }
        try{ 
            new CheckDocs2(DOCS2_PATH); 
        } 
        catch(Exception e){
            System.out.println(e.getMessage()); 
        }
        System.out.println("Success !!!!!");
    }

    private static void migrate() throws MigDocs2Exception {
        File origin = new File(DOCS_PATH);
        File target = new File(DOCS2_PATH);
        ArrayList < String > fichierTraites = new ArrayList < > ();
        if (!origin.exists() || !target.exists())
            throw new MigDocs2Exception("MIG_ERR_BASE_DIR_NOT_FOUND");
        File indexMd = new File(origin, "index.md");
        if (!indexMd.exists())
            throw new MigDocs2Exception("MIG_ERR_INDEX_MD_NOT_FOUND", indexMd);
        try {
            for (String line: Files.readAllLines(Paths.get(indexMd.getPath()), Charset.forName(characterSet))) {
                Matcher m = MD_FILE_PATTERN.matcher(line);
                if (m.matches()) {
                    lessonOrder.add(m.group(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new MigDocs2Exception("MIG_ERR_READING_INDEX");
        }
        File theDir = new File(DOCS2_PATH + "/CTG_50_docs/");
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
        createJSON(theDir,"docs");
        dealFolderContent(origin, theDir);
    }

    public static String getExtension(File file) {
        String extension = "";
        int i = file.getName().lastIndexOf('.');
        if (i > 0) {
            extension = file.getName().substring(i + 1);
        }
        return extension;
    }

    public static void dealFolderContent(File origin, File target) throws MigDocs2Exception {
        //utile pour le nom de la lesson
        String tmp = "rien";
        Integer order = 0;
        ArrayList < File > LessonsOfThisFile = new ArrayList < > ();
        for (String lesson: lessonOrder) {
            File currentLesson = new File(origin, lesson);
            if (currentLesson.exists()) {
                LessonsOfThisFile.add(currentLesson);
            }
        }
        File[] files = origin.listFiles();
        for (File currentFile: files) {
            if (currentFile.isDirectory()) {
                order += 10;
                Matcher m = DIR_NAME_PATTERN.matcher(currentFile.getName());
                if (m.find()) {
                    tmp = m.group(0);
                }
                tmp=tmp.substring(1,tmp.length());
                File newCategory = new File(target.toString());
                if (!newCategory.exists()) {
                    newCategory.mkdirs();
                }
                File nextCategory = new File (target.toPath()+ "/CTG_" + order + "_" + tmp);
                if (!nextCategory.exists()) {
                    nextCategory.mkdirs();
                }
                createJSON(nextCategory,tmp.substring(0, 1).toUpperCase() + tmp.substring(1));
                dealFolderContent(currentFile, nextCategory); 
            }
            else if (getExtension(currentFile).equals("md") && !LessonsOfThisFile.contains(currentFile)) {
                LessonsOfThisFile.add(currentFile);
            }
        }
        
        for (File currentFile: LessonsOfThisFile) {
            order += 10;
            fichierTraites.add(currentFile.getName());
            File newLessonDirectory = new File(target.toPath()+"/LSN_"+order+"_"+currentFile.getName().substring(0, currentFile.getName().length() - 3));
            if (!newLessonDirectory.exists()) {
                newLessonDirectory.mkdirs();
            }
            createJSON(newLessonDirectory,getTitleOfLesson(currentFile.getName()));
            File newMDLesson = new File(newLessonDirectory.toPath()+"/"+currentFile.getName());
            try{
                copyFile(currentFile,newMDLesson);
            }
            catch(Exception e) {
                e.printStackTrace();
                throw new MigDocs2Exception("MIG_ERR_COPY_MD");
            }
            try {   
                for (String line: Files.readAllLines(Paths.get(currentFile.getPath()), Charset.forName(characterSet))) {
                    String PNGimport = "";
                    Matcher m = PNG_FILE_PATTERN.matcher(line);
                    if (m.matches()) {
                        PNGimport = m.group(1);
                    }
                    if(!PNGimport.equals("")){
                        File FilePNGImport = new File (DOCS_PATH+"/"+PNGimport);
                        if (FilePNGImport.exists()){
                            copyFile(origin,newLessonDirectory);
                        }
                    }
                    fichierTraites.add(PNGimport);                
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MigDocs2Exception("MIG_ERR_PNG");
            }
        }
    }

    public static String getTitleOfLesson(String toTitle){
        toTitle = toTitle.replace('-', ' ');
        toTitle = toTitle.substring(0, toTitle.length() - 3);
        toTitle = toTitle.substring(0, 1).toUpperCase() + toTitle.substring(1);
        return toTitle;
    }
    
    public static void copyFile( File from, File to ) throws IOException {
        Files.copy( from.toPath(), to.toPath() );
    }

    public static void createJSON (File toJSON,String title) throws MigDocs2Exception {       
        try{
            if (toJSON.getName().substring(0,sizePrefixForJSON).equals("LSN")){
                File newFileJSON = new File (toJSON.toPath()+"/lesson.json");
                writeJSON(newFileJSON, title);
            }
            else if (toJSON.getName().substring(0,sizePrefixForJSON).equals("CTG")){
                File newFileJSON = new File (toJSON.toPath()+"/category.json");
                writeJSON(newFileJSON, title);
            }
        }
        catch(Exception e){
            e.printStackTrace();
            throw new MigDocs2Exception("MIG_ERR_JSON_CREATION");
        }        
    }

    public static void writeJSON (File jsonFile,String title)throws MigDocs2Exception{
        try{
                PrintWriter newJSON = new PrintWriter(jsonFile, characterSet);
                newJSON.println("{");
                newJSON.println("    \"ANY\": {");
                newJSON.println("        \"title\": \""+title+"\",");
                newJSON.println("        \"description\": \"\"");
                newJSON.println("    },");
                newJSON.println("    \"ENU\": {");
                newJSON.println("        \"title\": \""+title+"\",");
                newJSON.println("        \"description\": \"\"");
                newJSON.println("    }");
                newJSON.println("  }");
                newJSON.close();
        }
        catch(Exception e){
            e.printStackTrace();
            throw new MigDocs2Exception("MIG_ERR_JSON_CREATION");
        }
    }
    

    public static class MigDocs2Exception extends Exception {
        private String additional;

        public MigDocs2Exception(String msg) {
            super(msg);
            additional = "";
        }

        public MigDocs2Exception(String msg, String additional) {
            super(msg);
            this.additional = additional;
        }

        public MigDocs2Exception(String msg, File f) {
            super(msg);
            this.additional = f.getPath();
        }
        @Override
        public String getMessage() {
            return super.getMessage() + " : " + additional;
        }
    }
}
