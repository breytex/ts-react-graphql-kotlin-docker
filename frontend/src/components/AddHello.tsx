import gql from "graphql-tag"
import * as React from 'react'
import { Mutation } from "react-apollo"

export interface IAppProps {
}

export interface IAppState {
  data: { value: string }
}

const ADD_HELLO = gql`
  mutation AddHello($value: String!){
    saveHello(value:$value){
      name
    }
  }
`

export default class IApp extends React.Component<IAppProps, IAppState> {
  constructor(props: IAppProps) {
    super(props)

    this.state = {
      data: { value: "" }
    }
  }



  public render() {
    return (
      <Mutation mutation={ADD_HELLO}>
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
}
