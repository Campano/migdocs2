import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;

public class MigDocs2 {
    // private final static String DOCS_PATH =
    // "/Users/antoinecruveilher/dev/docs/documentation";
    private final static String DOCS_PATH = "/Users/antoinecruveilher/dev/docs/documentation";
    // private final static String DOCS2_PATH = "/path/to/destination";
    private final static String DOCS2_PATH = "/Users/antoinecruveilher/Desktop";
    private final static Pattern MD_FILE_PATTERN = Pattern.compile(".*?([a-zA-Z0-9_-]+\\.md).*");
    // Déclarer la liste "ordre des leçons"
    public static ArrayList < String > lessonOrder = new ArrayList < > ();

    public static void main(String[] args) {
        System.out.println("Welcome to MigDocs2 tool");
        try {
            migrate();
        } catch (MigDocs2Exception e) {
            System.out.println(e.getMessage());
        }

        /*
         * try{ new CheckDocs2(docs2Path); } catch(Exception e){
         * System.out.println(e.getMessage()); }
         */
    }

    private static void migrate() throws MigDocs2Exception {
        // Déclarer la variable "répertoire origine"
        File origin = new File(DOCS_PATH);
        // Déclarer la variable "répertoire cible"
        File target = new File(DOCS2_PATH);
        // Déclarer la variable "fichiers traîtés"
        ArrayList < String > fichierTraités = new ArrayList < > ();

        if (!origin.exists() || !target.exists())
            throw new MigDocs2Exception("MIG_ERR_BASE_DIR_NOT_FOUND");

        // Si pas de fichier "index.md" dans le répertoire origine, Renvoyer erreur
        File indexMd = new File(origin, "index.md");
        if (!indexMd.exists())
            throw new MigDocs2Exception("MIG_ERR_INDEX_MD_NOT_FOUND", indexMd);


        try {
            // Pour chaque ligne du fichier "index.md"
            for (String line: Files.readAllLines(Paths.get(indexMd.getPath()), Charset.forName("UTF-8"))) {
                // Si on détecte un nom de fichier md (via une regex "alphanumériques, tirets,
                // underscores + '.md'")
                Matcher m = MD_FILE_PATTERN.matcher(line);
                if (m.matches()) {
                    // Ajouter le nom de fichier md à "ordre des leçons"
                    lessonOrder.add(m.group(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new MigDocs2Exception("MIG_ERR_READING_INDEX");
        }

        for (String lesson: lessonOrder)
            System.out.println(lesson);

        //Créer la catégorie "CTG_50_docs" et son fichier json dans le répertoire cible
        File theDir = new File(DOCS2_PATH + "/CTG_50_docs");
        if (!theDir.exists()) {
            theDir.mkdirs();

            //Fonction "Traiter contenu dossier" ("répertoire origine", "CTG_50_docs")





        }
    }

    public static String getExtension(File file)
	{
        String extension = "";

        int i = file.getName().lastIndexOf('.');
        if (i > 0) {
            extension = file.getName().substring(i+1);
        }
        return extension;
	}

    public static void dealFolderContent(File origin, File target) {
        //utile pour le nom de la lesson
        String tmp = "";
        //Initialiser compteur ordre à 0
        Integer order = 0;

        //Déclarer la liste ordonnée "leçons de ce dossier"
        ArrayList < String > LessonsOfThisFile = new ArrayList < > ();
        //Pour chaque item de la liste "ordre des leçons"
        for (String lesson: lessonOrder) {
            //Si la leçon existe dans ce dossier, ajouter à la liste "leçons de ce dossier"
            File currentLesson = new File(origin, lesson);
            if (currentLesson.exists()) {
                LessonsOfThisFile.add(lesson);
            }
        }
        //Pour chaque fichier de ce dossier
        File[] files = origin.listFiles();

        for (File currentFile: files) {
            //Si c'est un dossier
            if (getExtension(currentFile)=="") {
                //+10 au compteur ordre 
                order += 10;
                Pattern p = Pattern.compile("[a-z]");
                Matcher m = p.matcher(currentFile.getName());
                //Lire le nom du dossier (du type "01-core")
                //Détecter le nom de la catégorie ("core" via une regex: deux chiffres + un tiret + alphanum)
                //Si ne correspond pas, renvoyer erreur
                if (m.matches()) {
                    tmp = m.group(1);
                }
                //Créer le dossier catégorie (type "CTG_10_core") avec l'ordre et son fichier json dans "CTG_50_docs"
                File newCategory = new File(DOCS2_PATH + "/CTG_50_docs/CTG_" + order + "_" + tmp);
                if (!newCategory.exists()) {
                    newCategory.mkdirs();
                }
                File nJSON = new File(DOCS2_PATH + "/CTG_50_docs/CTG_" + order + "_" + tmp + "/category.json");
                //écriture dans le json pas encore faite
                dealFolderContent(currentFile, newCategory);
            }
            //Si c'est un fichier ".md", et qu'il n'existe pas dans "leçons de ce dossier"
            else if(getExtension(currentFile)=="md" && !LessonsOfThisFile.contains(currentFile.getName())){
                //ajouter à la liste "leçons de ce dossier"
                LessonsOfThisFile.add(currentFile);
            }
        }
        for (File currentFile : LessonsOfThisFile){
            order=+10;
            //Ajouter le nom à la liste des fichiers traîtés
            fichierTraités.add(currentFile.getName());
            //Lire le nom du fichier ".md" (du type basic-code-examples.md)
            //Détecter le nom de la leçon (Basic code example -> Remplacer les tirets par des espaces et mettre la première lettre en majuscule)
            String TitleOfLesson = currentFile.getName();
            TitleOfLesson = TitleOfLesson.replace('-',' ');
            TitleOfLesson = TitleOfLesson.substring(0,TitleOfLesson.length()-3);
            TitleOfLesson = TitleOfLesson.substring(0, 1).toUpperCase() + TitleOfLesson.substring(1);
            
            //Créez la leçon (type "LSN_01_basics-code-examples) avec l'ordre, son fichier "basic-code-examples.md" et son fichier json
            //Je beug sur le  nom de chemin içi
            File nLesson = new File(DOCS2_PATH + "/CTG_50_docs/"+target);
            if (!nLesson.exists()) {
                nLesson.mkdirs();
            }

            /*Pour chaque ligne du fichier md
            Si on détecte un nom de fichier png
            Si le fichier existe dans le dossier présent
            Copier le fichier dans la leçon
            Ajouter le nom à la liste des fichiers traîtés*/
        
            // Pour chaque ligne du fichier md
            /*ArrayList <String> listePngImport = new ArrayList <>();
            for (String line: Files.readAllLines(Paths.get(currentFile.getPath()), Charset.forName("UTF-8"))) {
                
                Pattern p = Pattern.compile(".*?([a-zA-Z0-9_-]+\\.png).*");
                Matcher m = p.matcher(line);
                if (m.matches()) {
                    // Ajouter le nom de fichier md à "ordre des leçons"
                    listePngImport.add(m.group(1));
                }
                if (currentFile.exists)
            }*/

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

        public String getMessage() {
            return super.getMessage() + " : " + additional;
        }
    }
}