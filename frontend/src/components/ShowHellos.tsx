import gql from "graphql-tag"
import * as React from 'react'
import { Query } from "react-apollo"

export interface IAppProps {
}

export interface IAppState {
}

export default class IApp extends React.Component<IAppProps, IAppState> {
  constructor(props: IAppProps) {
    super(props)

    this.state = {
    }
  }

  public render() {
    return (
      <Query
        query={gql`
                    query{
                        allHellos{
                            name
                        }
                    }
                `}
      >
        {({ loading, error, data }) => {
          if (loading) { return <p>Loading...</p> }
          if (error) { return <p>Error :(</p> }

          return data.allHellos.map(({ helloId, name }: { helloId: number, name: string }, index: number) => (
            <div key={helloId}>
              <b>{index + 1}:</b> {name}
            </div>
          ))
        }}
      </Query>
    )
  }
}
