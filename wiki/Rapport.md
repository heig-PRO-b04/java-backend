# Rapport : rendu final
Ce document contient toutes les informations demandées pour le rendu final de ce projet.
Ce dernier étant composé de trois axes, nous avons séparé les détails de ces trois axes.

- [Package de test/installation](https://github.com/heig-PRO-b04/java-backend/wiki/Rapport#Package-de-testinstallation)
  + [Web](https://github.com/heig-PRO-b04/java-backend/wiki/Rapport#Web)
  + [Android](https://github.com/heig-PRO-b04/java-backend/wiki/Rapport#Android)
  + [Back-end](https://github.com/heig-PRO-b04/java-backend/wiki/Rapport#Back-end)
- [Organisation du code](https://github.com/heig-PRO-b04/java-backend/wiki/Rapport#Organisation-du-code)
  + [Web](https://github.com/heig-PRO-b04/java-backend/wiki/Rapport#Web-1)
  + [Android](https://github.com/heig-PRO-b04/java-backend/wiki/Rapport#Android-1)
  + [Back-end](https://github.com/heig-PRO-b04/java-backend/wiki/Rapport#Back-end-1)
- [Suivi Qualité](https://github.com/heig-PRO-b04/java-backend/wiki/Rapport#Suivi-Qualité)
  + [Principe général](https://github.com/heig-PRO-b04/java-backend/wiki/Rapport#Principe-général)
  + [Liste des contrôles effectués](https://github.com/heig-PRO-b04/java-backend/wiki/Rapport#Liste-des-contrôles-effectués)
  + [Instructions pour vérifier les protocoles](https://github.com/heig-PRO-b04/java-backend/wiki/Rapport#Instructions-pour-vérifier-les-protocoles)

## Package de test/installation

### Web
Le frontend de notre application est distribué sous la forme d'un projet
NodeJS, et utilise le build system Yarn pour la gestion des paquets et des
dépendances. Il est actuellement déployé sur [GitHub Pages](https://rockin.app)
et peut aussi être construit en local. Nous recommandons néanmoins d'utiliser
la version de production, qui est déployée en environnement stable.

#### Installation avec Docker

Un fichier `Dockerfile` est fourni avec le projet, afin de le construire dans
un environnement Docker. Pour ce faire, il suffit de construire l'image
associée en utilisant la commande `docker build -t mon-image .`, puis de la
faire tourner en local avec du __port mapping__ en utilisant une
commande comme `docker run -p 1234:80 mon-image`. Cette action servira le site
web de manière statique sur `http://localhost:1234`, et accèdera à l'API de
production.

#### Installation avec Yarn

Pour installer l'application directement avec `yarn`, il faut exécuter les
commandes suivantes :

- `yarn install`
- `yarn dev`

Un serveur de développement sera alors créé sur le port `1234`. Nous
recommandons d'utiliser une version de `yarn 1.22.4` ou plus.

La configuration `yarn` permet aussi d'utiliser les commandes suivantes :

- `yarn test`, qui lance les tests unitaires du projet.
- `yarn analyse`, qui lance un outil d'analyse statique du code Elm.
- `yarn format`, qui s'assure que le code Elm est formatté suivant une variante
  stricte des standards du language.
- `yarn build`, qui construit la version distribuable du site.

#### Tests d'intégration

Une suite de tests d'intégration est lancée à chaque commit sur GitHub. Les
résultats d'exécution sont disponibles dans la section
[Actions](https://github.com/heig-PRO-b04/elm-frontend/actions?query=workflow%3A%22Integration+Tests%22)
du repository. Les scripts de lancement des tests ont été écrrits pour la
plateforme GitHub Actions / Ubuntu LTS et sont disponible dans le
sous-dossier `.github/workflows`.


### Android
Afin d'utiliser l'application créée dans ce projet, il faut commencer par l'installer.
Il y a deux façon de ce faire.
La première option et la plus simple est de la télécharger sur GooglePlay.
La seconde est de l'installer depuis le repo git.

#### GooglePlay
Notre application peut être trouvée sur le Google Play Store sous le nom :
[Rockin App - Live polls for conferences & schools](https://play.google.com/store/apps/details?id=ch.heigvd.pro.b04.android).
Clicker sur *Installer* puis *Ouvrir* afin d'utiliser l'application.

#### Git
Afin d'installer cette application depuis le repository git, il faudra avoir [AndroidStudio](https://developer.android.com/studio/?gclid=CjwKCAjwqpP2BRBTEiwAfpiD-x7x769nVXJsrEjLQddG8JdqBmjl1YnouceOM0egLysoSMzrr1Z6DBoCB2gQAvD_BwE&gclsrc=aw.ds), ou un autre IDE compatible avec le developpement Android, installé sur votre machine.
Il est aussi possible d'installer l'application en suivant les instructions suivantes :

1. Télécharger ou cloner ce repository git sur votre machine locale.
En cas de téléchargement de l'archive, l'extraire et passer à l'instruction suivante.
2. Ouvrir le projet en tant que projet AndroidStudio.
3. S'assurer d'être en mode ProductionAPIRelease.
4. Conncter son appareil mobile à la machine.
5. Faire tourner le projet sous AndroidStudio.
6. Une fois que AndroidStudio a fini son travail, il suffit de trouver et d'ouvrir l'application RockinApp sur le mobile.

### Back-end

## Organisation du code

### Web
#### Structure générale

Le frontend web de notre application est basé sur `npm` et utilise `yarn`. Le
code source est structuré de la manière suivante.

- Les fichiers de configuration sont à la racine du repository.
- Les fichiers source (en Elm) sont dans le dossier `src`.
- Le contenu statique est dans le dossier `static`.
- Les tests d'intégration sont dans le dossier `integration-tests`.

#### Elm

[rockin.app](https://rockin.app) est une **Single Page Application** écrite en
Elm, un language compilé en Javascript. Ainsi, le frontend est complètement
indépendant du backend, qui ne se charge pas de générer le contenu de la page
mais sert seulement du contenu statique.

Elm est un language fonctionnel pur, strictement typé. Le système de type
garantit notamment l'absence d'exceptions; un programme qui ne respecte pas le
système de types ne compilera simplement pas.

L'utilisation d'un système de types fort permet de garantir des propriétés
par le système de types plutôt qu'avec des tests unitaires. Dans ce frontend,
nous avons par exemple besoin d'utiliser une structure de données qui
représente un élément sélectionné dans une liste : plutôt que de modéliser
cette structure naïvement avec une liste et un `int` qui contiendrait l'indice
de la position sélectionnée, nous utilisons un triplet
(liste1, sélection, liste2), où `liste1` correspond aux éléments précédant
la `sélection`, et `liste2` les éléments à sa suite. Par construction, il
n'est pas possible d'avoir un élément sélectionné qui ne fait pas partie de la
liste - en revanche, avec la représentation utilisant un `int` qui stocke une
position sélectionnée, on peut se retrouver avec un indice qui pointe sur une
position hors de la liste. L'utilisation d'un système de type fort permet
d'éviter ces états incohérents.

##### Structure du code

Le code est réparti en plusieurs modules :

- Le module `Main` gère la navigation entre les pages.
- Les différentes pages sont dans le paquet `Page`.
- L'API est déclarée dans le paquet `Api` et le module éponyme.
- Les modules utilitaires sont majoritairement dans des modules nommés
  `Extra` (suivant les conventions Elm).

##### Points notables
- Pour nous protéger contre des erreurs de programmation, le module Api
  restreint drastiquement l'accès aux informations de connexion. Ainsi, nous
  avons construit un ensemble de types qui garantit que :
  + Les credentials ne peuvent pas être loggés par erreur.
  + Les requêtes ne peuvent pas être faites sans informations
    d'authentification.
  + Il n'est pas possible de faire une requête authentifiée sur un endpoint
    dont la racine n'est pas `https://api.rockin.app/`.
  + La persistence du token d'authentification dans le `localStorage` est fait
    de manière transparente.
- Elm ne permet pas d'injecter des balises HTML, car le contenu est
  automatiquement échappé lors du rendu. Cela réduit fortement le risque
  d'attaque par XSS.
- Les pages de la SPA sont isolées les unes des autres : c'est le module `Main`
  qui se charge de router les messages entre les sous-pages, sans que le type
  des messages ne lui soit connu.
- Quelques éléments d'interface graphique ont été factorisés dans le paquet
  `Picasso`.

### Android
#### Structure générale
Le code est divisé dans cinq paquets séparés, afin présenter cette Organisation, nous divisons ce contenu en autant de chapitres.

#### Authentification
Contient toutes les classes permettant de gérer l'authentification des utilisateurs.
Afin de conserver le token d'authentification d'un utilisateur, nous avons décidé d'utiliser des LiveDatas que nous pouvons ensuite observer. Ceci nous permet d'être notifié en cas de déconnection.
Les deux autres classes dans ce paquets permettent de récupérer une session et un poll depuis le backend grace au token dans la LiveData mentionnée plus tôt.

#### Datamodel
Ce paquet contient toutes les classes que l'application récupère depuis le backend. Ces classes sont les suivantes

1. **Answer** : une réponse possible pour une question dans un poll, avec l'état dans lequel elle est (séléctionnée ou non) pour l'utilisateur courant.
2. **Poll** : le poll lié à la session à laquelle le client est connecté.
3. **Question** : une question fasant partie d'une liste de question d'un poll.
4. **Session** : la session à laquelle le client s'est connecté avec le token obtenu
5. **SessionCode** : le code de la session à laquelle l'utilisateur est connecté
6. **Token** : le token obtenu si le SessionCode est correct. Permet de se connecter à un poll.

#### Home
Ce paquet contient toutes les classes permettant de modéliser l'activité "Home". Cette activité permettra de demander à l'utilisateur d'entrer le code d'emoji ou d'utiliser le code QR pour se connecter à un poll.

#### Network
Dans ce paquet, nous avons mis toutes les classes nous permettant de gérer la connexion avec le backend à l'aide de Retrofit. C'est dans ce paquet que nous avons définis toutes les différentes requêtes que nous utilisons pour récupérer les informations nécessaires.

#### Poll
Nous avons mis dans ce paquet, toutes les classes nous permettant de gérer l'activité affichant un poll.
Cette activité est atteinte par l'utilisateur lorsqu'il a entré un code de session valide. Il se trouve alors sur une page affichant le titre du poll auquel il est connecté ainsi que la liste des questions auxquelles il peut/doit répondre.

#### Question
Ce paquet contient les classes nous permettant de gérer l'affichage des questions sur activité. Cette activité affiche le titre d'une question avec les réponses possibles sous la forme d'une liste de boutons. L'utilisateur peut alors sélectionner les réponse ce qui va les envoyer au backend.

#### Utils
Ce paquet contient différentes classes utilisées dans tout le projet.

- **LocalDebug** : qui nous permet d'afficher, en console, des messages d'erreurs adaptés aux situations.
- **PollingLiveData** : qui est une live data que nous avons modélisée pour les poll, elle vérifie que le poll est valide régulièrement et nous permet de déconnecter le client de la sessions si celle-ci est fermée.
- **SquareCardView** : qui modélise une CardView carrée que nous pouvons utiliser pour l'affichage de différentes composantes de l'application.

### Back-end

## Suivi Qualité
### Principe général
Afin de nous assurer de la qualité de notre projet sur la durée, nous avons mis en place un certain nombre d'outils nous permettant de tester le bon fonctionnement de chaque axe du projet de manière automatisée.
Notre objectif était de nous assurer d'avoir un endroit ou le projet reste dans un état cohérent, fonctionnel et accessible pour tous les membres du groupes. Nous avons donc décidé d'utiliser [GitHub](github.com) pour stocker une version que nous assurons toujours fonctionnelle de notre projet. Avec ceci, nous utilisons [Travis](https://travis-ci.org/) qui nous permet de faire tourner des tests à chaque Pull Request (PR) sur la branche maitresse de notre projet. Avec tous ces outils, nous avons pu mettre en place un certain nombre de règles nous empêchant de mettre sur la version centrale du projet, tout code ne passant pas les tests. De plus, nous avons décidé d'empêcher l'ajout de code tant que ce dernier n'a pas été revu par un autre membre du groupe.

### Liste des contrôles effectués
Comme expliqué dans la section précédente, chaque modification de code sur la branche master est effectuée par une avec code review par au moins une personne comprenant le code et travaillant dans le même pôle. En plus de cela, un certain nombre de tests sont effectués automatiquement et dont les résultats sont disponibles sur la page GitHub de la Pull Request en question. Au cours du projet, de plus en plus de tests permettant de garantir la qualité du produit ont été ajoutés.\
Les tests effectués sont les suivants :

- Pour le backend
    + tests unitaires testant le chacun des endpoints de la manière suivante:
        + Simuler la population du contenu de la base de donnée avec des données factices
        + Vérification que un scénario d'utilisation standard de l'endpoint produise le résultat attendu
        + Vérification que un scénario de mauvaise utilisation de l'endpoint lève les exceptions attendues
    + tests de formattage suivant Google Checkstyle
        + Documentation obligatoire pour tout méthode publique
        + Formattage du code selon le "Google Java Style Guide"
    + tests d'intégration vérifiant le fonctionnement des différents endpoints et leur conformité à l'api.\
    Ces tests sont effectués en déployant le backend en local grâce à Docker-Compose et effectuant des requêtes grâce à des scripts python\
    Les tests effectués sont les suivants :
        + En tant que modérateur    
            + Enregistrement d'un utilisateur
            + Connexion d'un utilisateur
            + Création d'un sondage
            + Création de questions
            + Création de réponses
            + Ouverture du sondage aux participants
        + En tant que participant
            + Connexion à un sondage
            + Récupération des questions
            + Récupération des réponses

- Pour le frontend android :
    + Test de compilation
    + TODO Tests graphiques avec Espresso
    + A noter que le formatage "Google Java Style Guide" n'a pas été appliqué de manière stricte, le Framework Android le rendant non pertinent. Tout les collaborateurs ayant contribué au frontend android utilisent donc le même éditeur, qui formate le code de la même manière localement.

- Pour le frontend web :
    + tests de formatage du code Elm permettant une homogénéité du code entre les différents auteurs.
    + tests analysant de code de manière statique permettant de vérifier l'utilisation de bonnes pratiques de
    +  tests d'intégration continue end-to-end scriptant une utilisation graphique de l'application web.\
    Ces tests sont effectués déployant le backend et le frontend en local, et utilisant Selenium pour scripter une utilisation graphique de l'application et assurer que plusieurs scénarios d'utilisations sont toujours possibles et qu'aucune régression fonctionnelle n'est engendrée par les modifications.\
    Les Scénarios d'utilisation testés graphiquement avec Selenium sont les suivants :
        + Enregistrement d'un utilisateur
        + Connexion d'un utilisateur
        + Création d'un sondage
        + Ouverture du sondage aux participants
        + Récupération du code Emoji
        + Création de question dans les différents modes de visibilité disponibles
        + TODO Modification de la description de la question
        + Création d'une réponse
        + Modification de nombre de réponses minimum / maximum pour une question
        + Suppression d'une réponse
        + Suppression d'une question
        + Suppression d'un sondage
        + TODO Modification du nom de compte utilisateur
        + TODO Modification du mot de passe utilisateur
        + TODO Suppression du compte utilisateurprogrammation en Elm

### Instructions pour vérifier les protocoles
L'entièreté des tests unitaires, d'intégration continue, de formatage, et autres ayant été effectués sur GitHub grâce à Travis CI et à GitHub Actions, un historique pour chacun de ces tests pour chaque Pull Request ou commit de merge sur le master y est disponible.\
Chaque Pull Request avec les tests effectués, la personne ayant revu le code (et potentiellement commenté / demandé des modifications), ainsi que les changements effectués sont disponibles aux adresses suivantes:

- Pour le backend: https://github.com/heig-PRO-b04/java-backend/pulls?q=is:pr+is:closed
- Pour le frontend android: https://github.com/heig-PRO-b04/android-frontend/pulls?q=is:pr+is:closed
- Pour le frontend web: https://github.com/heig-PRO-b04/elm-frontend/pulls?q=is:pr+is:closed
