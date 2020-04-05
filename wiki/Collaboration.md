# Coding Guidelines
## Why ?
It’s important to keep our project clean and functional. Nobody should impose bad practices on others. This documents aims at providing common guidelines and language for contributions to the codebase, both of which will be enforced during the project. 

We all produce crappy code. If your pull request is not accepted, don’t take it personally or consider it as the end of the world. It will get better over time, and that’s exactly what these guidelines should help all of us with.

## Code formatting
### Backend and Mobile application
Since we’re going to use Java we are going to follow Google’s Java Guidelines: https://google.github.io/styleguide/javaguide.html. Read the document.

Google has an IntelliJ configuration that will automatically format your code. First, import the style guide that's provided with the repository in your IntelliJ IDEA. Secondly, make sure to select it for Java Code.

It is not mandatory to use that tool, but you must follow the guidelines and pass the CI tests. 

Here are a few points that we would like to highlight:

* No tabs are allowed, only whitespaces (2.3.1)
   * Indentation is +2 spaces (4.2)
   * Line-wrapping is +4 spaces (4.5.2)
* No wildcard imports (3.3.1)
* Braces are used where optional (4.1.1). Example:
```java
if (bool) {
    sayHello();
}
```
* Egyptian brackets
* 100 chars column limit (4.4)
* Line wrapping (4.5)
* `String[] args`. Not `String args[]` (4.8.3.2)
* The order of modifiers is as follows (4.8.7):
`public protected private abstract default static final transient volatile synchronized native strictfp`
* No prefix for variable name (5.1)
* Caught exceptions are not ignored (6.2)
* In the Javadoc, always specify if the code can throw an exception with `@throws`
* In the **backend**, make sure to run `mvn checkstyle:check` before commits (and save yourself some additional PR comments).

### Frontend
Follow the official styleguide: https://elm-lang.org/docs/style-guide.
The use of https://github.com/avh4/elm-format is mandatory.
This will be enforced via the CI, and code failing to match the formatting will not be merged into the master branch.

## Miscellaneous 
### Database names
The name of the schemas and tables follow the CamelCase style with a capital at start. So not tableName but TableName.
Any field name is redacted in a classic camelCase style.

The primary key of a table is always named according this style: id<TableName>. For example, the PK of table Poll is named idPoll. If there multiple keys, this scheme is adapted like this: id<TableName><Field>. For example, if table Moderator had 2 PKs, they would be called idModeratorUserName and idModeratorPwdHash.

A foreign key (primary or not) is always named following this scheme: idx<RestofIdReferenced>. For example, in any table the foreign key referencing idPoll is called idxPoll. Same thing applies to chains of foreign keys, so for example two keys referencing idModerator would be named idxModerator.

### Others
You are encouraged to do pair-programming.

Documentation is not optional.

Tests are not optional. All code submitted without sufficient test coverage will not be accepted.
Follow the Boy Scout Rule: _Always leave the campground cleaner than you found it_.

## Git usage
### Git commits
This document will be our reference for acceptable git commit messages: https://chris.beams.io/posts/git-commit/.

If git commit messages do not conform or are not representative of the changes, the PR will not be accepted.

Special mention to rule #7: _Use the body to explain what and why vs. how_

If you need ideas for words, please use a thesaurus.

### Git workflow
To push new features, fix bugs, add tests or review documentation, we will be following the GitHub Workflow. This means :

1. Forking the centralized `heig-PRO-b04/...` repository.
2. Creating a branch from the fork.
3. Performing the changes in the branch.
4. Pushing the branch to the fork, and make a pull request through GitHub's user interface. The pull request is made against the master branch of the upstream repository.
5. When the pull request is approved, you should make sure to fetch the changes from the upstream after the changes are merged.

Of course, you cannot accept your own changes. Ever. Even if we all know your code is the best. We recommend that the reviewer doesn't merge the branch, but let's the person who wrote it do it themselves.
