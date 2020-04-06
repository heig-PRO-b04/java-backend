# Users API

## Poll

### Authentication

`POST : /api/connect`

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


#### Success

```json
{
    "token" : "ab5j20dk"
}
```

A token is a character sequence with arbitrary length.

### Get questions from a poll
URL should be of the form `/api/mod/nbMod/poll/nbPoll/question`. Will return a list of Questions.

#### Request

`GET : /api/mod/567/poll/123/question/`

#### Success
```json
[
   {
       "idModerator"   : 567,
       "idPoll"        : 123,
       "idQuestion"    : 789,
       "title"         : "First Question Ever",
       "details"       : "Comment",
       "visibility"    : "visible",
       "answersMin"    : 1,
       "answersMax"    : 42
   },
   {
       "idModerator"   : 567,
       "idPoll"        : 123,
       "idQuestion"    : 679,
       "title"         : "Scond Question Ever",
       "details"       : "No comment",
       "visibility"    : "hidden",
       "answersMin"    : 1,
       "answersMax"    : 10
   }
]
```

**Note**:
`visibility` is either `hidden`, `visible` or `archived`.

### Get answers from a question
URL should be of the form `/api/mod/nbMod/poll/nbPoll/question/nbQuestion/answer`. Will return a list of Answers.

#### Request

`GET : /api/mod/567/poll/123/question/679/answers`

```json
{
    "token"   : "ab5j20dk"
}
```

#### Success
```json
[
  {
       "idModerator"   : 567,
       "idPoll"        : 123,
       "idQuestion"    : 679,
       "idAnswer"      : 1234,
       "title"         : "First Answer",
       "description"   : "This is a description."
   },
   {
       "idModerator"   : 567,
       "idPoll"        : 123,
       "idQuestion"    : 679,
       "idAnswer"      : 1278,
       "title"         : "Another !",
       "description"   : ""
   }
]
```

### Vote & Update vote
URL should be of the form `/api/mod/nbMod/poll/nbPoll/question/nbQuestion/answer/nbAns/vote`

#### Request

`PUT : /api/mod/567/poll/123/question/679/answers/1278/vote`

```json
{
    "token"   : "ab5j20dk",
    "checked" : true
}
```
