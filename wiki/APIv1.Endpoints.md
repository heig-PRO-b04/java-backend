# Table of Contents

- [Table of Contents](#table-of-contents)
- [List of endpoints](#list-of-endpoints)
    - [Token](#token)
        - [`POST /auth`](#post-auth)
    - [User connection](#user-connection)
        - [`POST /connect`](#post-connect)
    - [Poll](#poll)
        - [`POST /mod/{idModerator}/poll`](#post-modidmoderatorpoll)
        - [`PUT /mod/{idModerator}/poll/{idPoll}`](#put-modidmoderatorpollidpoll)
        - [`DELETE  /mod/{idModerator}/poll/{idPoll}`](#delete--modidmoderatorpollidpoll)
    - [Question](#question)
        - [`GET /mod/{idModerator}/poll/{idPoll}/question`](#get-modidmoderatorpollidpollquestion)
        - [`POST /mod/{idModerator}/poll/{idPoll}/question`](#post-modidmoderatorpollidpollquestion)
        - [`PUT /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}`](#put-modidmoderatorpollidpollquestionidquestion)
        - [`DELETE /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}`](#delete-modidmoderatorpollidpollquestionidquestion)
    - [Answer](#answer)
        - [`GET /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer`](#get-modidmoderatorpollidpollquestionidquestionanswer)
        - [`POST /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer`](#post-modidmoderatorpollidpollquestionidquestionanswer)
        - [`PUT /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer/{idAnswer}`](#put-modidmoderatorpollidpollquestionidquestionansweridanswer)
        - [`DELETE /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer/{idAnswer}`](#delete-modidmoderatorpollidpollquestionidquestionansweridanswer)
        - [`PUT /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer/{idAnswer}/vote`](#put-modidmoderatorpollidpollquestionidquestionansweridanswervote)
- [Errors](#errors)

# List of endpoints

## Token
### `POST /auth`
Requests a token.

If wanting to be connected as a moderator, send json representation of a moderator
in the request's body.

## User connection
### `POST /connect`
Requests a token for a user connecting to a specified poll.

```json
{
    "code" : "0x086F"
}
```

The `code` consists of a 4-letter-long sequence of hexadecimal digits, prefixed by the "0x" sequence. Codes must use only capital letters.

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

## Question
### `GET /mod/{idModerator}/poll/{idPoll}/question`
Get questions forming a poll.

On success, a list of questions will be returned.

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
