import seeder from 'mongoose-seed'
import { UserSeed } from './models/user'
import { PostSeed } from './models/post'

var data = [UserSeed, PostSeed]

// Connect to MongoDB via Mongoose
export default ({ user, pwd, url, db }) => seeder.connect(`mongodb://${user}:${pwd}@${url}/${db}`, () => {

  // Load Mongoose models
  seeder.loadModels(
    [
      './src/db/models/post.js',
      './src/db/models/user.js'
    ]
  )

  // Clear specified collections
  seeder.clearModels(['User', 'Post'], () => {
    seeder.populateModels(data, () => {
      seeder.disconnect()
      console.log("Successfuly seeded database!")
    })
  })
})