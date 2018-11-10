import gql from "graphql-tag"
import { query } from "micro-graphql-react"
import * as React from 'react'

export interface IAppProps {

}

export interface IAppState {
}

const LOAD_HELLOS = gql`query{
  allHellos{
    name
  }
}`

@query(LOAD_HELLOS)
export default class IApp extends React.Component<IAppProps, IAppState> {
  constructor(props: IAppProps) {
    super(props)

    this.state = {
    }
  }

  public render() {
    console.log(this.props)

    return (
      <div />
    )
  }
}
