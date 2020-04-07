# Table of Contents

- [Table of Contents](#table-of-contents)
    - [Token](#token)
    - [Poll](#poll)
        - [Client](#client)
        - [Server](#server)
    - [Question](#question)
        - [Client](#client-1)
        - [Server](#server-1)
        - [General](#general)
    - [Answers](#answers)
        - [Client](#client-2)
        - [Server](#server-2)

## Token
When a token is needed in a request, it should be passed in the URL as so:

`/ex/ample?token=abcdef`

The format is not yet defined.

## Poll
### Client
When a client needs to send a poll, it should be represented as so:
```json
{
  "title" : "My new poll"
}
```
### Server
When the server needs to send a poll, it should be represented as so:
```json
{
  "idModerator" : {idModerator},
  "idPoll"      : {idPoll},
  "title"       : "My new poll"
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
```
### Server
