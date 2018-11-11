import ApolloClient from "apollo-boost"
import * as React from 'react'
import { ApolloProvider } from "react-apollo"
import * as ReactDOM from 'react-dom'
import App from './App'
import './index.css'
import registerServiceWorker from './registerServiceWorker'

const client = new ApolloClient({
  uri: "/api/graphql"
})

ReactDOM.render(
  <ApolloProvider client={client}><App /></ApolloProvider>,
  document.getElementById('root') as HTMLElement
)
registerServiceWorker()
