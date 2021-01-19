import java.util.*;
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
            }
        } else {
            System.err.println("Nom de repertoire invalide");
        }
        if(!indexExiste){
            System.err.println("Le fichier index.md n'existe pas");
        }


        /*Déclarer la liste "ordre des leçons"
        Pour chaque ligne du fichier "index.md"
            Si on détecte un nom de fichier md (via une regex "alphanumériques, tirets, underscores + '.md'")
            Ajouter le nom de fichier md à "ordre des leçons"*/


        String[] ordreDesLeçons;
        try
    {
      // Le fichier d'entrée
      File file = new File("/Users/antoinecruveilher/dev/docs/documentation/index.md");    
      // Créer l'objet File Reader
      FileReader fr = new FileReader(file);  
      // Créer l'objet BufferedReader        
      BufferedReader br = new BufferedReader(fr);  
      StringBuffer sb = new StringBuffer();    
      String line;
      while((line = br.readLine()) != null)
      {
          
        // ajoute la ligne au buffer
        sb.append(line);      
        sb.append("\n");     
      }
      fr.close();    
      System.out.println("Contenu du fichier: ");
      System.out.println(sb.toString());  
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
