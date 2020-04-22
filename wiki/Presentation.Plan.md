# Plan de la présentation

## Rappel des objectifs du projet

- Projet de groupe, en mettant en place une collaboration efficace et avec une qualité maximale.
- Apprendre à maîtriser des méthodes de travail adaptées à la collaboration.
  + Outils techniques pour l'implémentation du projet.
  + Outils de gestion de projet.
    - Kanban board.
    - Issue tracking.
- Production d'une application complète.
  + Développement d'une API et d'un modèle de données pour gérer des sondages en temps-réel.
  + Implémentation d'un client natif mobile Android pour répondre aux sondages.
  + Implémentation d'un client web pour gérer les sondages.

## Présentation du modèle et de la vue fait & à faire

- Document centralisant la vision du projet, et les composants principaux + leur progression dans le projet.
  + Bon indicateur des composants avec lesquels on peut déjà interagir (utile notamment quand on travaille avec le backend).
- Issues détaillées pour les tâches à faire dans les 7 prochains jours. Ces issues sont mises à jour au moins lors de tous les meetings de l'équipe.
  + Les issues sont classées dans des boards Kanban.
  + Chaque pôle du projet est responsable d'organiser sa gestion des issues à courte échéance. Permet d'avoir de la flexibilité en guarantissant une certaine visibilité sur ce qu'il se passe.

## Présentation du planning avec les ajustements, et comparaison avec le planning initial

- 1 à 2 semaines de retard. Nous pensons que cela est imputable aux facteurs suivants :
  + Interblocage entre les différents pôles du projet - nous n'avions pas suffisamment anticipé les inter-dépendances et avons perdu du temps pour nous aligner.
  + Nous avons mal anticipé la phase d'apprentissage des outils, frameworks et plateformes que nous utilisons. Nous avons remarqué que notre productivité s'est drastiquement améliorée ces dernières semaines, et pensons rattraper le retard petit à petit.
  + Nous avons mal anticipé le temps que prendrait la rédaction des spécifications et la rédaction de tests unitaires.
  + La crise du COVID-19 a engendré une pause de 10 jours, et nous a forcé à annuler plusieurs week-ends de développement et mise en place de prévus.
- Mesures mises en place pour pallier au retard
  + Introduction de meetings réguliers.
  + Collaboration plus asynchrone mais moins individuelle. Les pôles communiquent plus entre eux (bas couplage et haute cohésion).

## Présentation du mode et des outils de collaboration

- Meetings réguliers (3x par semaine, 30min) et à heure fixe.
  + Objectifs : s'assurer que tout le monde sait ce qui a été fait depuis le dernier meeting, ce sur quoi chacun compte travailler et ait pu planifier un RDV avec un collègue en cas de blocker.
  + Structure :
    1. Tour de l'équipe où chacun décrit ce qu'il / qu'elle a fait depuis le dernier meeting. Démo possible.
    2. Tour de l'équipe où chacun indique s'il est actuellement bloqué par quelque chose de particulier, et organisation d'un appel ultérieur pour résoudre le problème.
    3. Tour de l'équipe afin de connaître le travail qui sera fait d'ici le prochain meeting, et planification de certains calls techniques
  + Compléments :
    - Ces meetings ne remplacent pas les discussions techniques qui sont organisées et gérées par les membres de l'équipe en fonction des besoins.

- Mise en place de processus de quality approval stricts.
  + Chaque contribution (code, documentation) est peer-reviewée par au moins un collègue. En cas de questions sur la contribution, des commentaires sont systématiquement ajouté. Ce processus de review est asynchrone.
  + Chaque contribution est soumise à des tests automatiques :
    - Des test unitaires vérifient le bon fonctionnement du backend.
    - L'intégration continue s'assure que le code fournit compile.

- Centralisation de la documentation interne sur un Wiki commun.
  + Les guidelines concernant la review de code sont dans le Wiki.
  + La spécification technique de l'API est aussi centralisée. Notamment, nous avons été capables d'implémenter une partie de l'API client en nous basant sur la spécification, avant son implémentation concrète dans le backend

- Mise en place d'un processus de déploiement automatique.
  + Le code source reviewé du backend est déployé sur **Heroku**. La base de donnée est gérée par **Heroku**. L'application tourne sur une URL publique, la rendant accessible à toute l'équipe.
  + Le code source reviewé du frontend web est déployé sur GitHub Pages. Le site est accessible sur une URL publique, et toute l'équipe peut l'utiliser.

- Contributions suivant le GitHub workflow.
  + Personne ne peut écrire directement dans le repository central. Chaque membre de l'équipe fork le repository, et fait des pull request via GitHub pour que les contributions soient intégrées.
  + Les Pull Request GitHub sont gérées et revues de manière asynchrone. Un groupe Telegram a été mis en place pour les référencer, afin d'avoir un feedback rapide si nécessaire.

- Gestion du backlog avec des issues GitHub.
  + Les bugs sont référencés dans des issues.
    - Quand les bugs sont faciles à tester, des Pull Request contenant des tests unitaires montrant les bugs sont faites. Le développeur implémentant le correctif est responsable d'intégrer le nouveau test unitaire dans son bugfix.
  + Les points qui manquent de clarté dans l'API sont référencés par des issues.
  + Les tâches planifiées pour la prochaine semaine sont référencées par des issues.

- Centralisation des outils sur GitHub (notamment la CI, une partie du déploiement, la gestion des issues).
  + Rend l'utilisation de ces outils plus agréable et efficace.
