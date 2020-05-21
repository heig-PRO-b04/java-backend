# Organisation du code du frontend

## Structure générale

Le frontend web de notre application est basé sur `npm` et utilise `yarn`. Le
code source est structuré de la manière suivante.

- Les fichiers de configuration sont à la racine du repository.
- Les fichiers source (en Elm) sont dans le dossier `src`.
- Le contenu statique est dans le dossier `static`.
- Les tests d'intégration sont dans le dossier `integration-tests`.

## Elm

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

### Structure du code

Le code est réparti en plusieurs modules :

- Le module `Main` gère la navigation entre les pages.
- Les différentes pages sont dans le paquet `Page`.
- L'API est déclarée dans le paquet `Api` et le module éponyme.
- Les modules utilitaires sont majoritairement dans des modules nommés
  `Extra` (suivant les conventions Elm).

### Points notables

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
