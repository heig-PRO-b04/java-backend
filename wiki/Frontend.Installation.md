# Installation du frontend Elm

Le frontend de notre application est distribué sous la forme d'un projet
NodeJS, et utilise le build system Yarn pour la gestion des paquets et des
dépendances. Il est actuellement déployé sur [GitHub Pages](https://rockin.app)
et peut aussi être construit en local. Nous recommandons néanmoins d'utiliser
la version de production, qui est déployée en environnement stable.

## Installation avec Docker

Un fichier `Dockerfile` est fourni avec le projet, afin de le construire dans
un environnement Docker. Pour ce faire, il suffit de construire l'image
associée en utilisant la commande `docker build -t mon-image .`, puis de la
faire tourner en local avec du __port mapping__ en utilisant une
commande comme `docker run -p 1234:80 mon-image`. Cette action servira le site
web de manière statique sur `http://localhost:1234`, et accèdera à l'API de
production.

## Installion avec Yarn

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

## Tests d'intégration

Une suite de tests d'intégration est lancée à chaque commit sur GitHub. Les
résultats d'exécution sont disponibles dans la section
[Actions](https://github.com/heig-PRO-b04/elm-frontend/actions?query=workflow%3A%22Integration+Tests%22)
du repository. Les scripts de lancement des tests ont été écrrits pour la
plateforme GitHub Actions / Ubuntu LTS et sont disponible dans le
sous-dossier `.github/workflows`.
