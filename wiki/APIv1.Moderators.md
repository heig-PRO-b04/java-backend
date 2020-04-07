# Moderators API
All of theses requests are to be made by moderators only.

## Token request
Ask for a token. Additionally, the identifier for the moderator will be returned, as it is needed by
clients to perform ulterior requests with the provided token.

### Request
`POST: /api/auth/`

```json
{
    "username" : "guy-laurent",
    "password" : "1234"
}
```

### Success
```json
{
    "token"         : "ab5j20dk",
    "idModerator"   : 123
}
```

A token is a character sequence with arbitrary length.

## Poll
### Get all available polls
URL should be of the form `/api/mod/nbMod/poll/`

`GET : /api/mod/567/poll/`

#### Success
```json
[{
    "idModerator"   : 567,
    "idPoll"        : 123,
    "title"         : "My new exciting poll"
},
{
    "idModerator"   : 567,
    "idPoll"        : 124,
    "title"         : "My second exciting poll"
}]
```

### Create a new Poll
URL should be of the form `/api/mod/nbMod/poll/`

#### Request

`POST : /api/mod/567/poll/`

```json
{
    "token"         : "ab5j20dk",
    "title"         : "My new exciting poll"
}
```

#### Success
```json
{
    "idModerator"   : 567,
    "idPoll"        : 123,
    "title"         : "My new exciting poll"
}
```

### Update an existing Poll
URL should be of the form `/api/mod/nbMod/poll/nbPoll`

#### Request

`PUT : /api/mod/567/poll/123`

```json
{
    "token"         : "ab5j20dk",
    "title"         : "My apparently not so exciting poll"
}
```

__Note__: You only need to specify the data you would like to change.

#### Success
```json
{
    "idModerator"   : 567,
    "idPoll"        : 123,
    "title"         : "My apparently not so exciting poll"
}
```

### Delete an existing Poll
URL should be of the form `/api/mod/nbMod/poll/nbPoll`

#### Request
`DELETE : /api/mod/567/poll/123`

```json
{
    "token"         : "ab5j20dk"
}
```

#### Success
```json
{
    "message"       : "Poll deleted"
}
```

## Questions
### Create a new question in a poll
#### Request
URL should be of the form `/api/mod/nbMod/poll/nbPoll/question`

`POST : /api/mod/567/poll/123/question`
```json
{
    "token"         : "ab5j20dk",
    "title"         : "First Question Ever",
    "details"       : "Comment",
    "visibility"    : "hidden",
    "answersMin"    : 1,
    "answersMax"    : 42
}
```
Attributs `visibility`, `answersMin` and `answersMax` are __optionnal__.
**Note**: `visibility` is either `hidden`, `visible` or `archived`.

#### Success
```json
{
    "idModerator"   : 567,
    "idPoll"        : 123,
    "idQuestion"    : 789,
    "title"         : "First Question Ever",
    "details"       : "Comment",
    "visibility"    : "archived",
    "answersMin"    : 1,
    "answersMax"    : 42
}
```

**Note**: `visibility` is either `hidden`, `visible` or `archived`.

### Update an existing question in a poll
#### Request
URL should be of the form `/api/mod/nbMod/poll/nbPoll/question/nbQuestion`

`PUT : /api/mod/567/poll/123/question/789`
```json
{
    "token"         : "ab5j20dk",
    "title"         : "First Question Ever",
    "details"       : "Comment",
    "visibility"    : "visible",
    "answersMin"    : 1,
    "answersMax"    : 42
}
```
__Note__: You only need to specify the data you would like to change.

#### Success
```json
{
    "idModerator"   : 567,
    "idPoll"        : 123,
    "idQuestion"    : 789,
    "title"         : "First Question Ever",
    "details"       : "Comment",
    "visibility"    : "visible",
    "answersMin"    : 1,
    "answersMax"    : 42
}
```

### Delete an existing question
URL should be of the form `/api/mod/nbMod/poll/nbPoll/question/nbQuestion`

#### Request
`PUT : /api/mod/567/poll/123/question/789`

```json
{
    "token"         : "ab5j20dk"
}
```

#### Success
```json
{
    "message"       : "Question deleted"
}
```

## Answers
### Create an Answer
#### Request
URL should be of the form `/api/mod/nbMod/poll/nbPoll/question/nbQuestion/answer`

`POST : /api/mod/567/poll/123/question/789/answer`
```json
{
    "token"         : "ab5j20dk",
    "title"         : "First Answer",
    "description"   : "This is a description."
}
```

#### Success
```json
{
    "idModerator"   : 567,
    "idPoll"        : 123,
    "idQuestion"    : 789,
    "idAnswer"      : 1234,
    "title"         : "First Answer",
    "description"   : "This is a description."
}
```

### Udate an Answer
#### Request
URL should be of the form `/api/mod/nbMod/poll/nbPoll/question/nbQuestion/answer/nbAnswer`

`PUT : /api/mod/567/poll/123/question/789/answer/1234`
```json
{
    "token"         : "ab5j20dk",
    "title"         : "First Answer",
    "description"   : "This is a new description."
}
```
__Note__: You only need to specify the data you would like to change.

#### Success
```json
{
    "idModerator"   : 567,
    "idPoll"        : 123,
    "idQuestion"    : 789,
    "idAnswer"      : 1234,
    "title"         : "First Answer",
    "description"   : "This is a new description."
}
```
### Delete an Answer
URL should be of the form `/api/mod/nbMod/poll/nbPoll/question/nbQuestion/answer/nbAnswer`

#### Request
`DELETE : /api/mod/567/poll/123/question/789/answer/1234`

```json
{
    "message"       : "Answer deleted"
}
```
