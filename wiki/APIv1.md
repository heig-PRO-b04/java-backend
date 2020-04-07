# General Usage

## Definitions

+ A **User** is a poll participant, who may answer polls.
+ A **Moderator** is a poll manager, who may create, delete and edit polls.

## Usage

There are two parts to this API:

1. __[Endpoints](APIv1.Users)__: Will specify what endpoints are available and how to use them
2. __[Objects](APIv1.Moderators)__: Will specify how to represent different objects in json

## Status

When the endpoint is running, you may hit the root endpoint. You will always get a 200 response
code, indicating that everything is fine.

```json
{
    "message"       : "Everything is fine."
}
```
