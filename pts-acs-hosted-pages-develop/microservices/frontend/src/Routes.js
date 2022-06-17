import React from 'react';
import { Switch, Route } from 'react-router-dom';
import Demo from './demo/Demo';
import Home from './components/Home/Home';
import CustomerIdConfig from './components/CustomerIdConfig/CustomerIdConfig';
import EditCredential from './components/Credentials/EditCredential'
import EditForm from './components/Forms/EditForm';
import ListCredentials from './components/Credentials/ListCredentials';
import ListForms from './components/Forms/ListForms';
import Login from './components/Login/Login';
import NotFound from './components/NotFound/NotFound';
import PrivateRoute from './PrivateRoute';
import Swagger from './components/Swagger/Swagger';


export default function Routes() {
    return (
        <Switch>
            <Route path="/login" exact component={Login} />
            <Route path="/" exact component={Home} />
            <PrivateRoute path="/credentials" exact component={ListCredentials} />
            <PrivateRoute path="/credentials/:company" component={EditCredential} />
            <PrivateRoute path="/companyId" exact component={CustomerIdConfig} />
            <PrivateRoute path="/forms" exact component={ListForms} />
            <PrivateRoute path="/forms/:company/:uuid" exact component={EditForm} />
            {/*<PrivateRoute path="/demo" component={Demo} />*/}
            <PrivateRoute path="/api-docs" component={Swagger} />
            <Route><NotFound /></Route>
        </Switch>
    );
}