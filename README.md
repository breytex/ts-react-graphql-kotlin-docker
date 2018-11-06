# react-yoga-mongo-nginx-docker 

## Hello

This is a sample dev stack for getting into React + GraphQL with a Kotlin backend. It's built on:
* [create-react-app](https://github.com/facebook/create-react-app) with typescript
* mongoDB
* vertx
* nginx
* docker (docker-compose)

You can start building your first react-graphql-app in <5min.

## Install
Run in your cli:

```bash
$ cd ts-react-graphql-kotlin-docker

# downloads docker images and installs npm packages inside all containers
$ docker-compose build

# install packages locally to enable lint and package autocompletion
$ cd frontend
$ npm install

# ... same for backend
$ cd ../backend
$ ./gradlew clean assemble

# seed testdata into mongodb
# fails sometimes (couldnt connect to mongo). Just retry
$ docker-compose run --rm backend //TODO

# almost done
$ cd ../
$ docker-compose up
```

Open [localhost](localhost) in your browser :)

## Random tips

* use the mongod cli or a gui (like `brew cask install robo-3t`) to dive into the database
* username: graphql
* password: yoga123
* password: graphqldb
* localhost:27017
* can be changed in docker-compose.yml
* work on the graphQL/mongodb schema at `backend/src/main/java/eu/darken/backend/webserver/graphql/schemas/hello`

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