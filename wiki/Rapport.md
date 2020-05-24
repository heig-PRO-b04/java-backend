# Rapport : rendu final
Ce document contient toutes les informations demandées pour le rendu final de ce projet.
Ce dernier étant composé de trois axes, nous avons séparé les détails de ces trois axes.

- [Test/installation](##Test/installation)
  + [Web](###Web)
  + [Android](###Android)
  + [Back-end](###Back-end)
- [Structure](##Structure)
  + [Web](###Web)
  + [Android](###Android)
  + [Back-end](###Back-end)
- [Qualité](##Qualité)
  + [Générale](###Générale)
  + [Web](###Web)
  + [Android](###Android)
  + [Back-end](###Back-end)

## Test/installation

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

## Structure

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

## Qualité
### Générale
Afin de nous assurer de la qualité de notre projet sur la durée, nous avons mis en place un certain nombre d'outils nous permettant de tester le bon fonctionnement de chaque axe du projet de manière automatisée.
Notre objectif était de nous assurer d'avoir un endroit ou le projet reste dans un état cohérent, fonctionnel et accessible pour tous les membres du groupes. Nous avons donc décidé d'utiliser [GitHub](github.com) pour stocker une version que nous assurons toujours fonctionnelle de notre projet. Avec ceci, nous utilisons [Travis](https://travis-ci.org/) qui nous permet de faire tourner des tests à chaque Pull Request (PR) sur la branche maitresse de notre projet. Avec tous ces outils, nous avons pu mettre en place un certain nombre de règles nous empêchant de mettre sur la version centrale du projet, tout code ne passant pas les tests. De plus, nous avons décidé d'empêcher l'ajout de code tant que ce dernier n'a pas été revu par un autre membre du groupe.

### Web

### Android

### Back-end
