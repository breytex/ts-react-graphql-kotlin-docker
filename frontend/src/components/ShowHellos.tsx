import gql from "graphql-tag"
import * as React from 'react'
import { Query } from "react-apollo"

export interface IShowHellosProps {
}

export interface IShowHellosState {
}

const QUERY_HELLOS = gql`
query{
    allHellos{
        name
    }
}
`

export default class ShowHellos extends React.Component<IShowHellosProps, IShowHellosState> {
  constructor(props: IShowHellosProps) {
    super(props)

    this.state = {
    }
  }

  public render() {
    return (
      <Query
        query={QUERY_HELLOS}
      >
        {({ loading, error, data }) => {
          if (loading) { return <p>Loading...</p> }
          if (error) { return <p>Error :(</p> }

          return data.allHellos.map(({ name }: { name: string }, index: number) => (
            <div key={index}>
              <b>{index + 1}:</b> {name}
            </div>
          ))
        }}
      </Query>
    )
  }
}
