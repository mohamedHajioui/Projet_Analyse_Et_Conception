# Projet ANC3 2425 - Groupe a08 - Excel

## Notes de version itération 1

### Liste des bugs connus

* Affichage incorrect des arrondis (ex : 2.0 au lieu de 2 seulement dans le cas ou on ecrit une expression avec qu'une seule valeur "=2")
* 

### Diagramme de classes

* Logicielles (1ère itération) : https://drive.google.com/file/d/1WQnJeQat2qEOjuuP8V43b_qy4GiHyBti/view?usp=sharing
 
* Domaine : 
https://drive.google.com/file/d/1bcYmLdJuv665pGBFJFPEuFN_99hYNQOJ/view?usp=sharing

### Liste des fonctionnalités supplémentaires

### Divers

## Notes de version itération 2

### Diagramme

* Classes logicielles (2eme itération) : https://drive.google.com/file/d/13gg_SmWpC5F5ICeF9RvBOXNhAHAgIF-O/view?usp=sharing
* Sequence : 

  Undo : https://drive.google.com/file/d/1oQhbZjWEReZsUUjf7cJchgmMEPPdQUSa/view?usp=sharing

  Redo : https://drive.google.com/file/d/18rRv8Mv5gzXtcwsfGNFd889YsNjF8FRX/view?usp=sharing
### Liste des fonctionnalités supplémentaires
* Copier/coller
* Clear
...

## Notes de version itération 3

...


## Pour lancer le projet

### Option 1 

Dans le menu d'exécution, ne pas choisir "Current File" mais "App"

### Option 2

Dans VM options, ajouter ça : 

```
--add-exports=javafx.base/com.sun.javafx.event=org.controlsfx.controls
--add-exports=javafx.controls/com.sun.javafx.scene.control.behavior=org.controlsfx.controls
```

Source : https://github.com/controlsfx/controlsfx/wiki/Using-ControlsFX-with-JDK-9-and-above 

### Option 3

Dans la console de maven, tapper `mvn javafx:run`