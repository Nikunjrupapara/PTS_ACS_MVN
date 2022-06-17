import React from 'react';
import Menu from '../Menu/Menu';
import './SideDrawer.css';

const sideDrawer = props => {
    let drawerClasses = 'side-drawer';
    if (props.show) {
        //drawerClasses.push('open');
        drawerClasses += ' open';
    }
    return (
    <nav className={drawerClasses}>
        <Menu />
    </nav>
)};

export default sideDrawer;
