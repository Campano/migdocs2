import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;

public class MigDocs2 {
    // private final static String DOCS_PATH =
    // "/Users/antoinecruveilher/dev/docs/documentation";
    private final static String DOCS_PATH = "/Users/simoncampano/dev/simplicite.io/docs.simplicite.io/documentation";
    // private final static String DOCS2_PATH = "/path/to/destination";
    private final static String DOCS2_PATH = "/Users/simoncampano/dev/INTERNAL_APPS/training-content/content";
    private final static Pattern MD_FILE_PATTERN = Pattern.compile(".*?([a-zA-Z0-9_-]+\\.md).*");

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
        ArrayList<File> fichierTraités = new ArrayList<>();

        if (!origin.exists() || !target.exists())
            throw new MigDocs2Exception("MIG_ERR_BASE_DIR_NOT_FOUND");

        // Si pas de fichier "index.md" dans le répertoire origine, Renvoyer erreur
        File indexMd = new File(origin, "indx.md");
        if (!indexMd.exists())
            throw new MigDocs2Exception("MIG_ERR_INDEX_MD_NOT_FOUND", indexMd);

        // Déclarer la liste "ordre des leçons"
        ArrayList<String> lessonOrder = new ArrayList<>();
        try {
            // Pour chaque ligne du fichier "index.md"
            for (String line : Files.readAllLines(Paths.get(indexMd.getPath()), Charset.forName("UTF-8"))) {
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

        for (String lesson : lessonOrder)
            System.out.println(lesson);
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