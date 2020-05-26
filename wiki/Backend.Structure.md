# Organisation du code du backend

## Outils utilisés
Le code est rédigé en Java, toutefois en utilisant un framework particulier nommé Spring.
Spring simplifie énormément la création d'une application utilisant une base de données.
Son grand intérêt est de permettre, via des annotations spécifiques, de créer des tables pour
les classes que l'on crée. Il n'est pas nécessaire de rédiger du code SQL et JDBC !

Le gain de temps apporté par cet outil est donc considérable. Et les fonctionnalités de Spring ne 
s'arrêtent pas là : avec des annotations de style Lombok, beaucoup de méthodes sont créées automatiquement
(notamment les getter et setter) sans venir polluer le code pour autant. Il est aussi possible de 
générer des requêtes simples sur la base de données simplement à partir de l'en-tête d'une méthode.
Etant un de ceux ayant le plus utilisé Spring de l'équipe, je puis affirmer que son emploi nous a fait
gagner un temps considérable et facilité l'implémentation du backend. Bien sûr un temps d'apprentissage
est requis, mais le jeu en vaut largement la chandelle.

## Structure du code
Le code est organisé en de nombreux packages, chacun regroupant toutes les classes relatives à un même
concept. La classe principale porte soit le même nom que le package, soit ce nom préfixé de "Client" ou
"Server". N'importe quelle classe nommée XXXIdentifier est une classe servant de clé primaire à une 
classe principale. Celles nommées XXXRepository servent à Spring pour la création de la base de données
et regroupent d'éventuelles requêtes sur cette dernière. Enfin, les classes XXXController regroupent
tous les endpoints liés à cette classe, et implémentent leurs réponses. S'il y a d'autres classes dans 
le package, elles servent à des manipulations plus précises dont l'application a parfois besoin.

### Types Client et Server
Si certains types de données (comme Vote, Answer...) sont séparés en Client et Answer, cela est pour
des raisons de traitement des requêtes. Les ClientXXX se différencient par leur absence de clé primaire.
Ils correspondent au choix de l'utilisateur sur le natel envoyé au backend. Backend qui ne renverra que
des ServerXXX, possédant un ID, car cela est obligatoire pour travailler avec une base de données. 
Il s'est révélé impossible de faire hériter ces classes d'une classe abstraite supérieure.
