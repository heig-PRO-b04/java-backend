# Table of Contents

- [Table of Contents](#table-of-contents)
- [List of endpoints](#list-of-endpoints)
    - [Token](#token)
        - [`POST /auth`](#post-auth)
    - [Poll](#poll)
        - [`POST /mod/{idModerator}/poll`](#post-modidmoderatorpoll)
        - [`PUT /mod/{idModerator}/poll/{idPoll}`](#put-modidmoderatorpollidpoll)
        - [`DELETE  /mod/{idModerator}/poll/{idPoll}`](#delete--modidmoderatorpollidpoll)
    - [Question](#question)
        - [`POST /mod/{idModerator}/poll/{idPoll}/question`](#post-modidmoderatorpollidpollquestion)
        - [`PUT /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}`](#put-modidmoderatorpollidpollquestionidquestion)
        - [`DELETE /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}`](#delete-modidmoderatorpollidpollquestionidquestion)
    - [Answer](#answer)
        - [`POST /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer`](#post-modidmoderatorpollidpollquestionidquestionanswer)
        - [`PUT /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer/{idAnswer}`](#put-modidmoderatorpollidpollquestionidquestionansweridanswer)
        - [`DELETE /mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer/{idAnswer}`](#delete-modidmoderatorpollidpollquestionidquestionansweridanswer)

# List of endpoints

## Token
### `POST /auth`
Requests a token.

If wanting to be connected as a moderator, send json representation of a user
in the request's body.

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
