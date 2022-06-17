import React from 'react';
import useFetch from "./useFetch";

export default function ServerTime() {
    const data = useFetch('/api/hello');
    return (
        <h3 className="App-title">{data}</h3>
    );
  }