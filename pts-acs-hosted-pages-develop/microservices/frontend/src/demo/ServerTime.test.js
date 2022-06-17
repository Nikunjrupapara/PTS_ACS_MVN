import React from 'react';
import { render } from '@testing-library/react';
import ServerTime from './ServerTime';
import useFetch from './useFetch';
jest.mock("./useFetch");

describe('contains time on server', () => {

  test('renders current date and time', async () => {
    useFetch.mockReturnValue("Hello, the time at the server is now 2020-03-04T09:50:32.650Z\n");
    const { getByText } = render(<ServerTime />);
    const dateElement = getByText(/Hello, the time at the server is now */i);
    expect(dateElement).toBeInTheDocument();
    expect(useFetch).toHaveBeenCalled();
  });
});
