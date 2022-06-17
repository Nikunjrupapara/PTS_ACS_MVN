import React, { useState } from 'react';
import { AuthContext } from './context/AuthContext';
import Backdrop from './components/Backdrop/Backdrop';
import Routes from './Routes';
import Toolbar from './components/Toolbar/Toolbar';
import SideDrawer from './components/SideDrawer/SideDrawer';
import Container from '@material-ui/core/Container';
import Paper from '@material-ui/core/Paper';

function App() {
    const [sideDrawerOpen, setSideDrawerOpen] = useState(false);
    const [isAuthenticated, userHasAuthenticated] = useState(false);

    const drawerToggleClickHandler = () => {
        setSideDrawerOpen(!sideDrawerOpen);
    };

    const backdropClickHandler = () => {
        setSideDrawerOpen(false);
    };

    let backdrop;
    if (sideDrawerOpen) {
        backdrop = <Backdrop click={backdropClickHandler} />;
    }
    return (
        <AuthContext.Provider value={{ isAuthenticated, userHasAuthenticated }}>
            <div style={{height: '100%'}}>
                <Toolbar drawerToggleClickHandler={drawerToggleClickHandler} />
                <SideDrawer show={sideDrawerOpen} />
                {backdrop}
                <Container component="main" maxWidth="xl">
                    <Paper>
                        <Routes />
                    </Paper>
                </Container>
            </div>
        </AuthContext.Provider>
    );
}

export default App;
