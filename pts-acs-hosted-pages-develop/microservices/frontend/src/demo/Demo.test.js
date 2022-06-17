import React from 'react';
import { render } from '@testing-library/react';
import Demo from './Demo';
import ServerTime from './ServerTime';
jest.mock('./ServerTime');

describe('contains learn React link', () => {
  test('renders learn react link', () => {
    ServerTime.mockReturnValue("<h3 className=\"App-title\">Hello, the time at the server is now 2020-03-04T09:50:32.650Z</h3>");
    const { getByText } = render(<Demo />);
    const linkElement = getByText(/learn react/i);
    expect(linkElement).toBeInTheDocument();
  });
});

