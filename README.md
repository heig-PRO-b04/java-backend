# heig-PRO-b04/java-backend

[![Build Status](https://travis-ci.com/heig-PRO-b04/java-backend.svg?branch=master)](https://travis-ci.com/heig-PRO-b04/java-backend)
[![Heroku App Status](https://heroku-shields.herokuapp.com/heig-PRO-b04)](https://heig-PRO-b04.herokuapp.com)

An application to create, edit and delete polls that can be answered in real
time. In this repository, you'll find the codebase of the web server of the
system.

This software is developed as semester project (PRO) at HEIG-VD, academic year
2019/20.

## Development team:

| Name                                   |                                  |
|----------------------------------------|----------------------------------|
| Matthieu Burguburu (ass. project lead) | matthieu.burguburu@heig-vd.ch    |
| David Dupraz                           | david.dupraz@heig-vd.ch          |
| Clarisse Fleurimont                    | clarisse.fleurimont@heig-vd.ch   |
| Alexandre Piveteau (project lead)      | alexandre.piveteau@heig-vd.ch    |
| Guy-Laurent Subri                      | guy-laurent.subri@heig-vd.ch     |

## Dependencies

This project uses Maven and is automatically deployed to Heroku. It also relies
on the Spring framework.

## Building and installing

To launch the app locally, you need to :

0. Install a local PostgreSQL instance, configured to work with the backend. You need to have the
   following environment variables set :
    + `SPRING_DATASOURCE_URL` : this is the URL at which the database is exposed.
    + `SPRING_DATASOURCE_USERNAME` : this is a valid username to access the database.
    + `SPRING_DATASOURCE_PASSWORD` : this is a valid password to access the database.
    + The schema will **automatically** be created by the backend environment !
    + More details information on how to set the environment variables can be found in the
      [Wiki](https://github.com/heig-PRO-b04/java-backend/wiki/Database.Setup).
1. Clone the repository
2. Run `mvn clean install` at the root of the repo. This will resolve all the
   required dependencies.
3. Run `mvn spring-boot:run` and go to the port indicated in the command line.