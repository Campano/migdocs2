Algorithme informel de migdocs2
====================

Fonction principale
---------------------------

Déclarer la variable "répertoire origine"
Déclarer la variable "répertoire cible"
Déclarer la variable "fichiers traîtés"

Si pas de fichier "index.md" dans le répertoire origine
    Renvoyer erreur

Déclarer la liste "ordre des leçons"
Pour chaque ligne du fichier "index.md"
    Si on détecte un nom de fichier md (via une regex "alphanumériques, tirets, underscores + '.md'")
        Ajouter le nom de fichier md à "ordre des leçons"

Créer la catégorie "CTG_50_docs" et son fichier json dans le répertoire cible

Fonction "Traiter contenu dossier" ("répertoire origine", "CTG_50_docs")

Pour chacun des fichiers dans le "répertoire origine" et des sous-répertoires
    Si le fichier n'apparaît pas dans la liste des "fichiers traîtés"
        Renvoyer erreur


Fonction "Traiter contenu dossier" (dossier origine, dossier destination)
---------------------------
Initialiser compteur ordre à 0
Déclarer la liste ordonnée "leçons de ce dossier"

Pour chaque item de la liste "ordre des leçons"
    Si la leçon existe dans ce dossier, ajouter à la liste "leçons de ce dossier"

Pour chaque fichier de ce dossier
    Si c'est un dossier
        ordre += 10 
        Lire le nom du dossier (du type "01-core")
        Détecter le nom de la catégorie ("core" via une regex: deux chiffres + un tiret + alphanum)
            Si ne correspond pas, renvoyer erreur
        Créer le dossier catégorie (type "CTG_10_core") avec l'ordre et son fichier json dans "CTG_50_docs"
        Appeler fonction "Traiter contenu dossier" (ce dossier, dossier catégorie créé)

    Si c'est un fichier ".md", et qu'il n'existe pas dans "leçons de ce dossier"
        ajouter à la liste "leçons de ce dossier"

Pour chaque fichier md dans les "leçons de ce dossier"
    ordre += 10
    Ajouter le nom à la liste des fichiers traîtés
    Lire le nom du fichier ".md" (du type basic-code-examples.md)
    Détecter le nom de la leçon (Basic code example -> Remplacer les tirets par des espaces et mettre la première lettre en majuscule)
        Si le nom n'est pas détectable, renvoyer erreur
    Créez la leçon (type "LSN_01_basics-code-examples) avec l'ordre, son fichier "basic-code-examples.md" et son fichier json
        Si il y a une problème lors de la création, renvoyez erreur
    Pour chaque ligne du fichier md
        Si on détecte un nom de fichier png
            Si le fichier existe dans le dossier présent
                Copier le fichier dans la leçon
                Ajouter le nom à la liste des fichiers traîtés
            Sinon
                Renvoyer erreur