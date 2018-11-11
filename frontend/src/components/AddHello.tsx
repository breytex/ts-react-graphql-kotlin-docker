import { MutationUpdaterFn } from 'apollo-boost'
import gql from "graphql-tag"
import * as React from 'react'
import { Mutation } from "react-apollo"

export interface IAddHelloProps {
}

export interface IAddHelloState {
  data: { value: string }
}

const ADD_HELLO = gql`
  mutation AddHello($value: String!){
    saveHello(value:$value){
      name
    }
  }
`

const QUERY_HELLOS = gql`
query{
    allHellos{
        name
    }
}
`

export default class AddHello extends React.Component<IAddHelloProps, IAddHelloState> {
  constructor(props: IAddHelloProps) {
    super(props)

    this.state = {
      data: { value: "" }
    }
  }

  public render() {
    return (
      <Mutation
        mutation={ADD_HELLO}
        update={this.updateCache()}>
        {(addHello, { data }) => (
          <form
            onSubmit={this.onSubmit(addHello)}>
            <input type="text" value={this.state.data.value} onChange={this.onChange} /> <button type="submit" >Add hello</button>
          </form>
        )}
      </Mutation>
    )
  }

  private onSubmit = (commit: (data: object) => void) => {
    return (event: React.FormEvent<HTMLFormElement>) => {
      event.preventDefault()
      commit({ variables: this.state.data })
    }
  }

  private onChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    event.preventDefault()
    this.setState({ data: { value: event.target.value } })
  }

  private updateCache(): MutationUpdaterFn<any> | undefined {
    return (cache, { data: { saveHello } }: { data: { saveHello: any } }) => {
      const query: { allHellos: any[] | null } | null = cache.readQuery({ query: QUERY_HELLOS })

      if (query && query.allHellos) {
        const newQuery = {
          data: { allHellos: query.allHellos.concat([saveHello]) },
          query: QUERY_HELLOS
        }
        cache.writeQuery(newQuery)
      }
    }
  }
}
