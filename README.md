# heig-PRO-b04/java-backend

[![Build Status](https://travis-ci.com/heig-PRO-b04/java-backend.svg?branch=master)](https://travis-ci.com/heig-PRO-b04/java-backend)

An application to create, edit and delete polls that can be answered in real
time. In this repository, you'll find the codebase of the web server of the
system.

This software is developed as semester project (PRO) at HEIG-VD, academic year
2019/20.

## Development team:

| Name                                   |
|----------------------------------------|
| Matthieu Burguburu (ass. project lead) |
| David Dupraz                           |
| Clarisse Fleurimont                    |
| Alexandre Piveteau (project lead)      |
| Guy-Laurent Subri                      |

## Dependencies

This project uses Maven and is automatically deployed to Heroku. It also relies
on the Spring framework.

## Building and installing

To launch the app locally, you need to :

1. Clone the repository
2. Run `mvn clean install` at the root of the repo. This will resolve all the
   required dependencies.
3. Run `mvn spring-boot:run` and go to the port indicated in the command line.