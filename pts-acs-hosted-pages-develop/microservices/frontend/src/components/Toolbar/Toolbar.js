import React from 'react';
import DrawerToggleButton from '../SideDrawer/DrawerToggleButton';
import Menu from '../Menu/Menu';
import './Toolbar.css';


const toolbar = props => (
    <header className="toolbar">
        <nav className="toolbar_navigation">
            <div className="toolbar-toggle-button">
                <DrawerToggleButton click={props.drawerToggleClickHandler} />
            </div>
            <div className="toolbar_logo">
                <a href="/">
                    <img
                        src="https://www.infogroup.com/wp-content/themes/infogroup/dist/assets/img/infogroup-soon-to-be-data-axle.png"
                        alt="Infogroup, soon to be Data Axle"
                    />
                </a>
            </div>
            <div className="spacer" />
            <div className="toolbar_navigation_items">
                <Menu />
            </div>
        </nav>
    </header>
);

export default toolbar;
