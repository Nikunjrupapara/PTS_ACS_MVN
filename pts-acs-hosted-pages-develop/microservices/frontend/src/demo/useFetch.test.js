import { renderHook } from "@testing-library/react-hooks";
import useFetch from "./useFetch";
import nock from 'nock';

test('renders current date and time', async () => {
    const scope = nock('http://localhost')
    //.log(console.log)
    .get('/api/hello')
    .reply(200, "Hello, the time at the server is now 2020-03-04T09:50:32.650Z\n");

    const { result, waitForNextUpdate } = renderHook(() => 
        useFetch('/api/hello')
    );

    await waitForNextUpdate();
    
    expect(result.current).toEqual("Hello, the time at the server is now 2020-03-04T09:50:32.650Z\n");
    scope.done();
});