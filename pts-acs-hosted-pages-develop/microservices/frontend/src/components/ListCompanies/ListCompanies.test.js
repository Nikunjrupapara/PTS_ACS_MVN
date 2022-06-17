import React from 'react';
import { render, unmountComponentAtNode } from 'react-dom';
import { act } from 'react-dom/test-utils';
import Axios from 'axios';
import MockAdapter from 'axios'

import ListCompanies from './ListCompanies';

jest.mock('axios');
let container;

beforeEach(() => {
    container = document.createElement("div");
    document.body.appendChild(container);
});

afterEach(() => {
    unmountComponentAtNode(container);
    document.body.removeChild(container);
    container = null;
});

describe("List Companies", () => {
    test("displays a message when unauthenticated", async () => {
        const err = {
            response: {
                status: 401,
                statusText: "Unauthorized",
                data: {
                    "status": 401,
                    "message": "Unauthorized",
                    "moreInfo": "Access is denied"
                },
                config: {},
                headers: {},
                request: {}
            }
        };
        Axios.get.mockImplementationOnce(() => Promise.reject(err));

        await act(async () => {
            render(<ListCompanies />, container);
        });
        expect(container.textContent).toEqual("You must log in as a user with the Admin role to use this page.");
    });
    test("displays a message when there are no companies", async () => {
        const data = {data:[]};
        Axios.get.mockImplementationOnce(() => Promise.resolve(data));

        await act(async () => {
            render(<ListCompanies />, container);
        });
        expect(container.textContent).toEqual("No companies were found!");
    });
    test("renders without crashing", async () => {
        const data = {data:[{company: "jefftronics3"},{company:"ymnewsolutions"},{company:"musicnotes"}]};
        Axios.get.mockImplementationOnce(() => Promise.resolve(data));

        await act(async () => {
            render(<ListCompanies />, container);
        });
        const text = container.textContent;
        console.log(`${text}`);
        expect(text).toEqual("Select a company");
    });
});