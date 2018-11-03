import mongoose from 'mongoose'

const ModelName = "Post"

const PostSchema = new mongoose.Schema({
  title: {
    type: String,
    required: true,
    unique: true,
  },
  desc: {
    type: String,
    required: true,
  },
  author: {
    type: String,
    required: true,
  },
  createdOn: {
    type: Date,
    default: Date.now
  },
})

export const PostSeed = {
  'model': ModelName,
  'documents': [{
      title: "foo bar",
      desc: "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et",
      author: "Foo Bar"
    },
    {
      title: "bar bar",
      desc: "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et",
      author: "Angela Merkel"
    }
  ]
}


export default mongoose.model(ModelName, PostSchema)