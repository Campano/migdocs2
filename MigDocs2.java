import java.util.*;
import java.util.regex.*;
import java.io.*;


public class MigDocs2 {
    public static void main(String[] args) {
        System.out.println("Hello, World");

        String docsPath = "/Users/antoinecruveilher/dev/docs/documentation"; //"/path/to/origin"; ///Users/antoinecruveilher/dev/docs/documentation
        String docs2Path = "/path/to/destination"; //"/Users/simoncampano/dev/INTERNAL_APPS/training-content/content"
        // TODO : algo de migration

        
        //Déclarer la variable "répertoire origine"
        File repertoireOrigine = new File (docsPath);
        //Déclarer la variable "répertoire cible"
        File repertoireCible = new File (docs2Path);
        //Déclarer la variable "fichiers traîtés"
        ArrayList<File> fichierTraités = new ArrayList<>();

         // Si pas de fichier "index.md" dans le répertoire origine
        //   Renvoyer erreur
            boolean indexExiste = false;
            String liste[] = repertoireOrigine.list();      
    
            if (liste != null) {         
                for (int i = 0; i < liste.length; i++) {               
                    if(liste[i].equals("index.md")){
                        indexExiste = true;
                    }
                    System.out.println(liste[i]);
                }
            } else {
                System.err.println("Nom de repertoire invalide");
            }
            if(!indexExiste){
                System.err.println("Le fichier index.md n'existe pas");
            }
            /*
        try {
            //Déclarer la variable "répertoire origine"
            File repertoireOrigine = new File(docsPath);
            //Déclarer la variable "répertoire cible"
            File repertoireCible = new File(docs2Path);
            boolean indexExiste = false;
            String liste[] = repertoireOrigine.list();

            if (liste != null) {
                for (int i = 0; i < liste.length; i++) {
                    if (liste[i].equals("index.md")) {
                        indexExiste = true;
                    }
                    System.out.println(liste[i]);
                }
            }
            if (!indexExiste) {
                System.err.println("Le fichier index.md n'existe pas");
            }
        } catch (Exception e) {
            System.out.println("Nom de repertoire invalide");
        }*/

        /*Déclarer la liste "ordre des leçons"
        Pour chaque ligne du fichier "index.md"
            Si on détecte un nom de fichier md (via une regex "alphanumériques, tirets, underscores + '.md'")
            Ajouter le nom de fichier md à "ordre des leçons"*/

        
                ArrayList<String> ordreDesLeçons = new ArrayList <>();


                
                try
                {
                    // Le fichier d'entrée
                    File file = new File(docsPath+"/index.md");    
                    // Créer l'objet File Reader
                    FileReader fr = new FileReader(file);  
                    // Créer l'objet BufferedReader        
                    BufferedReader br = new BufferedReader(fr);  

                    Pattern p = Pattern.compile("\\w*\\-*\\w*\\-\\w*\\.md");
                        
                    String line;
                    
                    while((line = br.readLine()) != null)
                    {                        
                        Matcher m = p.matcher(line);
                        ordreDesLeçons.add(m.group());
                        
                    }

                    fr.close();    
                        
                    }
                        catch(IOException e)
                    {
                        e.printStackTrace();
                    }


        /*try{
            new CheckDocs2(docs2Path);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        } */
    }
}