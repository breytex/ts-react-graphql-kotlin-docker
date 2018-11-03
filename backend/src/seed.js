import seeder from './db/seeder'

const credentials = { 
    user: process.env.MONGO_USERNAME, 
    pwd: process.env.MONGO_PASSWORD, 
    db: process.env.MONGO_DATABASE, 
    url: 'mongo:27017' 
}

seeder(credentials)

