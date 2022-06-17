import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import { useAuthContext } from './context/AuthContext';

export default function PrivateRoute({component: Component, ...rest}) {
    const authContext = useAuthContext();
    const isAuthenticated = authContext.isAuthenticated;
    console.log(`isAuthenticated: ${isAuthenticated}`);
    return (
        <Route
            {...rest}
            render={props =>
                isAuthenticated ? (
                    <Component {...props} />
                ) : (
                    <Redirect to={{ pathname: "/login", state: { referer: props.location } }} />
                )
            }
        />
    );
};
