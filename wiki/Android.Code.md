# Organisation
Le code est divisé dans cinq paquets séparés, afin présenter cette Organisation, nous divisons ce contenu en autant de chapitres.

## Authentification
Contient toutes les classes permettant de gérer l'authentification des utilisateurs.
Afin de conserver le token d'authentification d'un utilisateur, nous avons décidé d'utiliser des LiveDatas que nous pouvons ensuite observer. Ceci nous permet d'être notifié en cas de déconnection.
Les deux autres classes dans ce paquets permettent de récupérer une session et un poll depuis le backend grace au token dans la LiveData mentionnée plus tôt.

## Datamodel
Ce paquet contient toutes les classes que l'application récupère depuis le backend. Ces classes sont les suivantes

1. **Answer** : une réponse possible pour une question dans un poll, avec l'état dans lequel elle est (séléctionnée ou non) pour l'utilisateur courant.
2. **Poll** : le poll lié à la session à laquelle le client est connecté.
3. **Question** : une question fasant partie d'une liste de question d'un poll.
4. **Session** : la session à laquelle le client s'est connecté avec le token obtenu
5. **SessionCode** : le code de la session à laquelle l'utilisateur est connecté
6. **Token** : le token obtenu si le SessionCode est correct. Permet de se connecter à un poll.

## Home
Ce paquet contient toutes les classes permettant de modéliser l'activité "Home". Cette activité permettra de demander à l'utilisateur d'entrer le code d'emoji ou d'utiliser le code QR pour se connecter à un poll.

## Network
Dans ce paquet, nous avons mis toutes les classes nous permettant de gérer la connexion avec le backend à l'aide de Retrofit. C'est dans ce paquet que nous avons définis toutes les différentes requêtes que nous utilisons pour récupérer les informations nécessaires.

## Poll
Nous avons mis dans ce paquet, toutes les classes nous permettant de gérer l'activité affichant un poll.
Cette activité est atteinte par l'utilisateur lorsqu'il a entré un code de session valide. Il se trouve alors sur une page affichant le titre du poll auquel il est connecté ainsi que la liste des questions auxquelles il peut/doit répondre.

## Question
Ce paquet contient les classes nous permettant de gérer l'affichage des questions sur activité. Cette activité affiche le titre d'une question avec les réponses possibles sous la forme d'une liste de boutons. L'utilisateur peut alors sélectionner les réponse ce qui va les envoyer au backend.

## Utils
Ce paquet contient différentes classes utilisées dans tout le projet.

- **LocalDebug** : qui nous permet d'afficher, en console, des messages d'erreurs adaptés aux situations.
- **PollingLiveData** : qui est une live data que nous avons modélisée pour les poll, elle vérifie que le poll est valide régulièrement et nous permet de déconnecter le client de la sessions si celle-ci est fermée.
- **SquareCardView** : qui modélise une CardView carrée que nous pouvons utiliser pour l'affichage de différentes composantes de l'application.
