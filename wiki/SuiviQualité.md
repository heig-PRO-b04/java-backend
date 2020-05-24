# Suivi Qualité
## Principe de supervision
TODO

## Liste des contrôles effectués
Comme expliqué dans la section précédente, chaque modification de code sur la branche master est effectuée par une avec code review par au moins une personne comprenant le code et travaillant dans le même pôle. En plus de cela, un certain nombre de tests sont effectués automatiquement et dont les résultats sont disponibles sur la page GitHub de la Pull Request en question. Au cours du projet, de plus en plus de tests permettant de garantir la qualité du produit ont été ajoutés.\
Les tests effectués sont les suivants :

- Pour le backend
    - tests unitaires testant le chacun des endpoints de la manière suivante:
        - Simuler la population du contenu de la base de donnée avec des données factices
        - Vérification que un scénario d'utilisation standard de l'endpoint produise le résultat attendu
        - Vérification que un scénario de mauvaise utilisation de l'endpoint lève les exceptions attendues
    - tests de formattage suivant Google Checkstyle
        - Documentation obligatoire pour tout méthode publique
        - Formattage du code selon le "Google Java Style Guide"
    - tests d'intégration vérifiant le fonctionnement des différents endpoints et leur conformité à l'api.\
    Ces tests sont effectués en déployant le backend en local grâce à Docker-Compose et effectuant des requêtes grâce à des scripts python\
    Les tests effectués sont les suivants :
        - En tant que modérateur    
            - Enregistrement d'un utilisateur
            - Connexion d'un utilisateur
            - Création d'un sondage
            - Création de questions
            - Création de réponses
            - Ouverture du sondage aux participants
        - En tant que participant
            - Connexion à un sondage
            - Récupération des questions
            - Récupération des réponses

- Pour le frontend android :
    - Test de compilation
    - TODO Tests graphiques avec Espresso
    - A noter que le formatage "Google Java Style Guide" n'a pas été appliqué de manière stricte, le Framework Android le rendant non pertinent. Tout les collaborateurs ayant contribué au frontend android utilisent donc le même éditeur, qui formate le code de la même manière localement.

- Pour le frontend web :
    - tests de formatage du code Elm permettant une homogénéité du code entre les différents auteurs.
    - tests analysant de code de manière statique permettant de vérifier l'utilisation de bonnes pratiques de 
    -  tests d'intégration continue end-to-end scriptant une utilisation graphique de l'application web.\
    Ces tests sont effectués déployant le backend et le frontend en local, et utilisant Selenium pour scripter une utilisation graphique de l'application et assurer que plusieurs scénarios d'utilisations sont toujours possibles et qu'aucune régression fonctionnelle n'est engendrée par les modifications.\
    Les Scénarios d'utilisation testés graphiquement avec Selenium sont les suivants :
        - Enregistrement d'un utilisateur
        - Connexion d'un utilisateur
        - Création d'un sondage
        - Ouverture du sondage aux participants
        - Récupération du code Emoji
        - Création de question dans les différents modes de visibilité disponibles
        - TODO Modification de la description de la question
        - Création d'une réponse
        - Modification de nombre de réponses minimum / maximum pour une question
        - Suppression d'une réponse
        - Suppression d'une question
        - Suppression d'un sondage
        - TODO Modification du nom de compte utilisateur
        - TODO Modification du mot de passe utilisateur
        - TODO Suppression du compte utilisateurprogrammation en Elm

        

## Instructions pour vérifier les protocoles
L'entièreté des tests unitaires, d'intégration continue, de formatage, et autres ayant été effectués sur GitHub grâce à Travis CI et à GitHub Actions, un historique pour chacun de ces tests pour chaque Pull Request ou commit de merge sur le master y est disponible.\
Chaque Pull Request avec les tests effectués, la personne ayant revu le code (et potentiellement commenté / demandé des modifications), ainsi que les changements effectués sont disponibles aux adresses suivantes:

- Pour le backend: https://github.com/heig-PRO-b04/java-backend/pulls?q=is:pr+is:closed
- Pour le frontend android: https://github.com/heig-PRO-b04/android-frontend/pulls?q=is:pr+is:closed
- Pour le frontend web: https://github.com/heig-PRO-b04/elm-frontend/pulls?q=is:pr+is:closed