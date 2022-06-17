import React from 'react';
import logo from '../logo.svg';
import '../App.css';
import ServerTime from './ServerTime';

function Demo() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <ServerTime />
        <p>
          Edit <code>src/demo/Demo.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
}

export default Demo;
