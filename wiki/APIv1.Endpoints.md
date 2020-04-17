# Table of Contents

- [Table of Contents](#table-of-contents)
- [List of endpoints](#list-of-endpoints)
    - [Register](#register)
        - [`POST /register`](#post-register)
    - [Token](#token)
        - [`POST /auth`](#post-auth)
    - [User connection](#user-connection)
        - [`POST /connect`](#post-connect)
    - [Poll](#poll)
        - [`GET /mod/{idModerator}/poll`](#get-modidmoderatorpoll)
        - [`GET /mod/{idModerator}/poll/{idPoll}`](#get-modidmoderatorpollidpoll)
        - [`POST /mod/{idModerator}/poll`](#post-modidmoderatorpoll)
        - [`PUT /mod/{idModerator}/poll/{idPoll}`](#put-modidmoderatorpollidpoll)
        - [`DELETE  /mod/{idModerator}/poll/{idPoll}`](#delete--modidmoderatorpollidpoll)
    - [Session](#session)
        - [`GET /session`](#get-session)
        - [`GET /mod/{idModerator}/poll/{ídPoll}/session`](#get-modidmoderatorpollídpollsession)
        - [`PUT /mod/{idModerator}/poll/{ídPoll}/session`](#put-modidmoderatorpollídpollsession)
    - [Question](#question)
        - [`GET /mod/{idModerator}/poll/{idPoll}/question`](#get-modidmoderatorpollidpollquestion)
        - [`GET /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}`](#get-modidmoderatorpollidpollquestionidquestion)
        - [`POST /mod/{idModerator}/poll/{idPoll}/question`](#post-modidmoderatorpollidpollquestion)
        - [`PUT /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}`](#put-modidmoderatorpollidpollquestionidquestion)
        - [`DELETE /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}`](#delete-modidmoderatorpollidpollquestionidquestion)
    - [Answer](#answer)
        - [`GET /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer`](#get-modidmoderatorpollidpollquestionidquestionanswer)
        - [`GET /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer/{idAnswer}`](#get-modidmoderatorpollidpollquestionidquestionansweridanswer)
        - [`POST /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer`](#post-modidmoderatorpollidpollquestionidquestionanswer)
        - [`PUT /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer/{idAnswer}`](#put-modidmoderatorpollidpollquestionidquestionansweridanswer)
        - [`DELETE /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer/{idAnswer}`](#delete-modidmoderatorpollidpollquestionidquestionansweridanswer)
        - [`PUT /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer/{idAnswer}/vote`](#put-modidmoderatorpollidpollquestionidquestionansweridanswervote)
- [Errors](#errors)

# List of endpoints

## Register
### `POST /register`
Registers a moderator.

The json representation of the moderator needs to be sent in the request's body.

On success, the server will send the moderator back.

## Token
### `POST /auth`
Requests a token.

To be connected as a moderator, send the json representation of a moderator in the request's body.

On success, the server will send the moderator back.

## User connection
### `POST /connect`
Requests a token for a user connecting to a specified poll.

```json
{
    "code" : "0x086F"
}
```

On success, the server will send a token associated to the user making the request.

The `code` consists of a 4-letter-long sequence of hexadecimal digits, prefixed by the "0x"
sequence. Codes must use only capital letters.

The emoji mapping table is as follows :

| Emoji | Hex value |
|-------|-----------|
| ✅ | 0 |
| 🍺 | 1 |
| 🍔 | 2 |
| 😻 | 3 |
| 👻 | 4 |
| 🦄 | 5 |
| 🍀 | 6 |
| ⛄️ | 7 |
| 🔥 | 8 |
| 🥳 | 9 |
| 🥑 | A |
| 🥶 | B |
| 🎋 | C |
| 🌈 | D |
| ☂️ | E |
| 🎹 | F |

## Poll
### `GET /mod/{idModerator}/poll`
Get all available polls.

A token is needed.

On success, a list of polls will be returned.

### `GET /mod/{idModerator}/poll/{idPoll}`
Get poll with id `idPoll`.

A token is needed.

On success, the poll will be returned.

### `POST /mod/{idModerator}/poll`
Creates a new poll.

A token is needed. Send the json representation of the poll in the request's
body.

On success, the poll is returned by the server.
### `PUT /mod/{idModerator}/poll/{idPoll}`
Updates a poll.

A token is needed. Send the json representation of the poll in the request's
body.

On success, the poll is returned by the server.
### `DELETE  /mod/{idModerator}/poll/{idPoll}`
Deletes a poll.

A token is needed.

On success, this message is returned by the server:
```json
{
  "message" : "Poll deleted"
}
```

## Session
### `GET /session`
Get the correct session corresponding with the given token.

A token is needed.

On success, the server will send a session object.

### `GET /mod/{idModerator}/poll/{ídPoll}/session`
Get the last active session for a given poll.

A token is needed.

On success, a Session object is sent by the server.

### `PUT /mod/{idModerator}/poll/{ídPoll}/session`
Creates or updates the state of a session. A Session object should be sent in the request's body.

A token is needed

## Question
### `GET /mod/{idModerator}/poll/{idPoll}/question`
Get all questions forming a poll.

On success, a list of questions will be returned.

### `GET /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}`
Get question with id `idQuestion`

On success, the question will be returned.

### `POST /mod/{idModerator}/poll/{idPoll}/question`
Creates a new question.

A token is needed. Send the json representation of the question in the request's body.

### `PUT /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}`
Updates a question.

A token is needed. Send the json representation of the question in the request's body.
### `DELETE /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}`
Deletes a question.

A token is needed.

On success, this message is returned by the server:
```json
{
  "message" : "Question deleted"
}
```

## Answer
### `GET /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer`
Get answers forming a question.

On success, a list of answers will be returned.

### `GET /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer/{idAnswer}`
Get the answer with id `idAnswer`

On success, the answer will be returned.

### `POST /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer`
Creates a new answer.

A token is needed. Send the json representation of the answer in the request's body.

### `PUT /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer/{idAnswer}`
Updates an answer.

A token is needed. Send the json representation of the answer in the request's body.
### `DELETE /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer/{idAnswer}`
Deletes an answer.

A token is needed.

On success, this message is returned by the server:
```json
{
  "message" : "Answer deleted"
}
```

### `PUT /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer/{idAnswer}/vote`
Votes for an answer.

Send the json representation of the vote in the request's body.

# Errors
On error, a message indicating why the error occured must be sent:
```json
{
  "error" : "Something terrible happened"
}
```
