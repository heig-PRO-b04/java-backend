# General Usage

## Definitions

+ A **User** is a poll participant, who may answer polls.
+ A **Moderator** is a poll manager, who may create, delete and edit polls.

## Usage

There are two parts to this API:

1. __[Users](APIv1.Users)__: Will specify how to connect with the server from the point of view of a user
2. __[Moderators](APIv1.Moderators)__: Will specify how to connect with the server from the point of view of a poll moderator

## Errors

As a general rule, and if not specified otherwise, all errors will be sent with a corresponding message. The correct HTTP error code will be sent as well.

```json
{
    "message"       : "Error 2: This is not normal"
}
```