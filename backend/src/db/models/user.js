import mongoose from 'mongoose'

const ModelName = "User"

const UserSchema = new mongoose.Schema({
  name: {
    type: String,
    required: true,
    unique: true,
  },
  email: {
    type: String,
    required: true,
  },
  createdOn: {
    type: Date,
    default: Date.now
  },
})


export const UserSeed = {
  'model': ModelName,
  'documents': [{
      'name': 'Test User',
      'email': 'test@test.com'
    },
    {
      'name': 'Foo Bar',
      'email': 'foo.bar@unnecessarilylongemailaddress.com'
    }
  ]
}


export default mongoose.model(ModelName, UserSchema)