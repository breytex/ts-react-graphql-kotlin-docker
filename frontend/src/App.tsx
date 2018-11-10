import * as React from 'react'
import './App.css'
import AddHello from './components/AddHello'
import ShowHellos from './components/ShowHellos'
import logo from './logo.svg'

class App extends React.Component {
  public render() {
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <h1 className="App-title">Welcome to React</h1>
        </header>
        <p className="App-intro">
          <h1>Add hellos</h1>
          <AddHello />
          <h1>List of all hellos</h1>
          <ShowHellos />
        </p>
      </div>
    )
  }
}

export default App
