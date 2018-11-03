import { GraphQLServer } from 'graphql-yoga'
import { startDB, models } from './db'
import resolvers from './graphql/resolvers'

const db = startDB({ 
  user: process.env.MONGO_USERNAME, 
  pwd: process.env.MONGO_PASSWORD, 
  db: process.env.MONGO_DATABASE, 
  url: 'mongo:27017' 
})

const context = {
  models,
  db,
}

const Server = new GraphQLServer({
  typeDefs: `${__dirname}/graphql/schema.graphql`,
  resolvers,
  context,
})

const opts = {
  port: 4000,
  endpoint: '/api',
  subscriptions: '/api/subscriptions',
  playground: '/api/playground',
}

Server.express.enable('trust proxy')

Server.start(opts, () => {
  console.log(`Server is running on http://localdev.net/api (and uses port ${opts.port} internally)`)
})
