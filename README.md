# TP_ACOL_2026_IF
Simulation boursière basique avec interface shell

Pour zip le projet : tar -czvf projet.tar.gz src/ Makefile README.md lib/ market.txt players.txt portfolios/ doc/ 

Pour lancer la simulation : make

Le répertoire doc/ contient la documentation du projet.

Le répertoire bin/ contient les fichiers .class des classes implémentées.

Le répertoire lib/ contient le fichier gson.jar, permettant de d’enregistrer au format JSON les données de jeux.

Le répertoire portfolio/ contient les données des comptes joueurs, permettant la persistance de la simulation.

Le répertoire src/ contient les fichiers source du projet. Le fichier simulation.java étant le programme principal.

Le projet est doté d’un Makefile permettant une compilation simple du projet.

Le fichier market.txt contient les informations du marché financier, tandis que le fichier players.txt contient les adresses mail et mot de passe des comptes joueurs, permettant au master de contrôler les comptes.


