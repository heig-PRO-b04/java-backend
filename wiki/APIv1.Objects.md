# Table of Contents

- [Table of Contents](#table-of-contents)
    - [General](#general)
    - [Moderator](#moderator)
        - [Client](#client)
        - [Server](#server)
    - [Token](#token)
        - [Client](#client-1)
        - [Server](#server-1)
    - [Poll](#poll)
        - [Client](#client-2)
        - [Server](#server-2)
    - [Session](#session)
        - [Client](#client-3)
        - [Server](#server-3)
    - [Question](#question)
        - [Client](#client-4)
        - [Server](#server-4)
        - [General](#general-1)
    - [Answers](#answers)
        - [Client](#client-5)
        - [Server](#server-5)
    - [Votes](#votes)
        - [Client](#client-6)

## General
When a list of objects needs to be sent, it will be done so using JSON arrays.

## Moderator
### Client
When a moderator needs to be sent by a client, it should be represented as so:

```json
{
  "username" : "guy-laurent",
  "password" : "1234"
}
```

### Server
When a moderator needs to be sent by the server, it should be represented as so:

```json
{
  "idModerator" : "123",
  "token"       : "abcdefghi"
}
```

## Token
### Client
When a token needs to be sent by a client, it should be via the URL:

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

## Session
**Note**: The `status` can be either:

* `open` meaning the session is active and people can connect to it
* `quarantined` meaning the session is active but people cannot connect to it anymore
* `closed` meaning the session is not active anymore

### Client
When the client needs to send a session, it should be represented as so:

```json
{
  "status"      : `open`
}
```
### Server
When the server needs to send a session, it should be represented as so:

```json
{
  "idModerator" : {idModerator},
  "idPoll"      : {idPoll},
  "idSession"   : {idSession},
  "code"        : "0xABCD",
  "status"      : `open`
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
  "indexInPoll" : 1,
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
  "indexInPoll" : 1,
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
