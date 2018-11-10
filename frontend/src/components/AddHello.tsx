import * as React from 'react';

export interface IAppProps {
}

export interface IAppState {
  value: string
}

export default class IApp extends React.Component<IAppProps, IAppState> {
  constructor(props: IAppProps) {
    super(props);

    this.state = {
      value: ""
    }
  }

  public render() {
    return (
      <React.Fragment>
        <input type="text" value={this.state.value} onChange={this.onChange} /> <button onClick={this.submit} />
      </React.Fragment>
    );
  }

  private submit = () => {
    console.log("submitted")
  }

  private onChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    event.preventDefault()
    this.setState({ value: event.target.value })
  }
}
