import { Client, setDefaultClient } from "micro-graphql-react"
import * as React from 'react'
import * as ReactDOM from 'react-dom'
import App from './App'
import './index.css'
import registerServiceWorker from './registerServiceWorker'

const client = new Client({
  endpoint: "/api/graphql",
  fetchOptions: { credentials: "include" }
})

setDefaultClient(client)

ReactDOM.render(
  <App />,
  document.getElementById('root') as HTMLElement
)
registerServiceWorker()
