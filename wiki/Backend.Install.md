# Backend -  guide d'installation

## Outils de base
IntelliJ et PostgreSQL sont les deux seuls outils fondamentalement nécessaires. Ils doivent donc tout 
d'abord être installés et fonctionnels. A noter que l'interface par défaut de PostgreSQL n'est pas
obligatoire, mais reste un plus. Certains préféreront toutefois le piloter depuis la ligne de commande.

## Spring
Dans IntelliJ, il est nécessaire de créer un nouveau projet de type Maven. En effet, Spring s'active
via le fichier `pom.xml`. Ce ne sera cependant de loin pas la seule dépendance à s'ajouter ! Reprendre
le fichier fourni tel quel est une option, mais les dépendances peuvent aussi être ajoutées par IntelliJ
ou manuellement.

## Configuration
Une fois Spring activé et le code importé, il faut se pencher sur le fichier `application.properties`.
Il regroupe différentes configurations de Spring et SQL, comme l'URL du serveur SQL, le mot de passe, etc.
 Notre fichier utilise des variables d'environnement telles que `SPRING_DATASOURCE_USERNAME` qui doivent
être configurées dans IntelliJ.

Une des lignes possibles est `spring.jpa.hibernate.ddl-auto=create`. Ce paramètre indique à Spring d'effacer
la base de données et de la recréer à chaque démarrage. Cette action est utile pendant le développement, 
la structure du projet changeant fréquemment. Toutefois, au stade de la production, il doit être réglé sur
`none` ou retiré du fichier.

## Exécution
Une fois les outils correctement configurés, le projet peut être compilé. L'outil Postman est alors
utile pour envoyer des requêtes au serveur et vérifier son comportement.
