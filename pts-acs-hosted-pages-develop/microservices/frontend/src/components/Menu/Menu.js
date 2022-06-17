import React from 'react';
import { Link } from 'react-router-dom';
import { useAuthContext } from '../../context/AuthContext';
import { useCookies } from 'react-cookie';
import { ACCESS_TOKEN_NAME } from '../Utils';

export default function Menu(props) {
    const authContext = useAuthContext();
    const isAuthenticated = authContext.isAuthenticated;
    const { userHasAuthenticated } = useAuthContext();
    const [cookies, setCookies, removeCookie] = useCookies([ACCESS_TOKEN_NAME]);
    const handleLogout = () => {
        userHasAuthenticated(false);
        removeCookie(ACCESS_TOKEN_NAME);
    }
    return (
        <ul>
            <Link to="/"><li>Home</li></Link>
            <Link to="/credentials"><li>Credentials</li></Link>
            <Link to="/companyId"><li>Customer Id</li></Link>
            <Link to="/forms"><li>Forms</li></Link>
            {/*<Link to="/demo"><li>Demo</li></Link>*/}
            <Link to="api-docs"><li>API Docs</li></Link>
            { isAuthenticated
                ? <Link to="/" onClick={handleLogout}><li>Logout</li></Link>
                : <Link to="/login"><li>Login</li></Link>
            }
        </ul>
    );
};
