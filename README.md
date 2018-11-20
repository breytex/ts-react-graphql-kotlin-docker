# react-yoga-mongo-nginx-docker 

![](https://img.shields.io/badge/Approved-Value--Deal-brightgreen.svg)

## Hello

This is a sample dev stack for getting into React + GraphQL with a Kotlin backend. It demos:

* nginx
* docker (docker-compose)
* [create-react-app](https://github.com/facebook/create-react-app) with typescript
* vertx.io with Kotlin & Reactive Extensions
* graphql
  * queries
  * mutations
  * data-fetching (batch-)loading
  * custom directives (e.g. input validation or authentication)
* mongoDB

You can start building your first react-graphql-app in <5min.

## Install

Run in your cli:

```bash
$ cd <dir>

# downloads docker images and installs npm packages inside all containers
$ docker-compose build

# install packages locally to enable lint and package autocompletion
$ cd frontend
$ npm install

# ... same for backend
$ cd ../backend
$ ./gradlew clean assemble

# If you want some test data
$ docker-compose run --rm backend ./gradlew runSeeder

# Run it ᕕ( ᐛ )ᕗ
$ cd ../
$ docker-compose up
```

Open [localhost](http://localhost) in your browser :)

## Development
### Frontend
**Add new npm packages**
```bash
# install new package in docker container
docker-compose run --rm frontend npm install package-name --save
# also install the same package locally for autocompletion (packages to install are automatically detected in shared package.json)
cd frontend && npm install
```
**Update packages in docker container**

When the frontend dependencies change, you have to update the node_modules on your locale filesystem as well as in the docker container.
```bash
# install new packages in the docker container
docker-compose run --rm frontend npm install
# install new packages locally for autocompletion
cd frontend && npm install
```
**Rebuild (frontend) docker container**

If you change to another branch and with that change a lot of frontend dependencies, its sometimes necessary to rebuild the entire frontend container to avoid package sideeffects.
```bash
docker-compose build --no-cache frontend
```
## Random tips

* use the mongod cli or a gui (like `brew cask install robo-3t`) to dive into the database
* username: graphql
* password: yoga123
* password: graphqldb
* localhost:27017
* can be changed in docker-compose.yml
* work on the graphQL/mongodb schema at `backend/src/main/java/stack/saas/backend/webserver/graphql/components/hello/schema`

## Todos

* Deployment scripts: gonna deliver when I finish my first own project based on this stack

## Troubleshooting

### Failed to start mongodb

#### Error

```text
Creating react-yoga-mongo-nginx-docker_mongo_1 ... error

ERROR: for react-yoga-mongo-nginx-docker_mongo_1  Cannot start service mongo: driver failed programming external connectivity on endpoint react-yoga-mongo-nginx-docker_mongo_1 (7c2c2943d6b6394f85403767dd3fb7d04d05a07d1eff7ad7ef145bf327e80d5f): Error starting userland proxy: listen tcp 0.0.0.0:27017: bind: address already in use

ERROR: for mongo  Cannot start service mongo: driver failed programming external connectivity on endpoint react-yoga-mongo-nginx-docker_mongo_1 (7c2c2943d6b6394f85403767dd3fb7d04d05a07d1eff7ad7ef145bf327e80d5f): Error starting userland proxy: listen tcp 0.0.0.0:27017: bind: address already in use
ERROR: Encountered errors while bringing up the project.
```

#### Solution

Stop your existing mongodb server.
