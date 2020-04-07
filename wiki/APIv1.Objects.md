# Table of Contents

- [Table of Contents](#table-of-contents)
    - [Token](#token)
        - [Client](#client)
        - [Server](#server)
    - [Poll](#poll)
        - [Client](#client-1)
        - [Server](#server-1)
    - [Question](#question)
        - [Client](#client-2)
        - [Server](#server-2)
        - [General](#general)
    - [Answers](#answers)
        - [Client](#client-3)
        - [Server](#server-3)
    - [Votes](#votes)
        - [Client](#client-4)

## Token
### Client
When a token needs to be sent by a client, it must be via the URL:

`/ex/ample?token=abcdefghi`

### Server
When the server needs to send a token, it should be represented as so:

```json
{
  "token" : "abcdefghi"
}
```

## Poll
### Client
When a client needs to send a poll, it should be represented as so:
```json
{
  "title" : "My title"
}
```

### Server
When the server needs to send a poll, it should be represented as so:
```json
{
  "idModerator" : {idModerator},
  "idPoll"      : {idPoll},
  "title"       : "My title"
}
```

## Question
### Client
When a client needs to send a question, it should be represented as so:

```json
{
  "title"       : "This is a Question title",
  "details"     : "Comment",
  "visibility"  : "hidden",
  "answersMin"  : 1,
  "answersMax"  : 42
}
```

### Server
When the server needs to send a question, it should be represented as so:

```json
{
  "idModerator" : {idModerator},
  "idPoll"      : {idPoll},
  "idQuestion"  : {idQuestion},
  "title"       : "This is a Question title",
  "details"     : "Comment",
  "visibility"  : "hidden",
  "answersMin"  : 1,
  "answersMax"  : 42
}
```
### General

`visibility` can have one of three values: `visible`, `hidden` or `archived`.

Attributes `answersMin` and `answersMax` are __optionnal__.

## Answers
### Client
When a client needs to send an answer, it should be represented as so:
```json
{
  "title"       : "First answer",
  "description" : "This is a description"
}
```
### Server
When the server needs to send an answer, it should be represented as so:

```json
{
  "idModerator" : {idModerator},
  "idPoll"      : {idPoll},
  "idQuestion"  : {idQuestion},
  "idAnswer"    : {idAnswer},
  "title"       : "First answer",
  "description" : "This is a description"
}
```

## Votes
### Client
When a client needs to send a vote for an answer, it should be represented as so:

```json
{
  "checked" : true
}
```

The value of `checked` is a boolean and should represent if the answer is selected in the user's UI.
