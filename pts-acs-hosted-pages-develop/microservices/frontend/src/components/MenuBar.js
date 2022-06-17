import React from 'react';
import clsx from 'clsx';
import { makeStyles } from '@material-ui/core/styles';
import Drawer from '@material-ui/core/Drawer';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import List from '@material-ui/core/List';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import MenuIcon from '@material-ui/icons/Menu';
import AddIcon from '@material-ui/icons/Add';
import DeleteIcon from '@material-ui/icons/Delete';
import EditIcon from '@material-ui/icons/Edit';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import DashboardIcon from '@material-ui/icons/Dashboard';
import SwapHorizontalCircleIcon from '@material-ui/icons/SwapHorizontalCircle';
import VpnKeyIcon from '@material-ui/icons/VpnKey';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import SpeakerNotesIcon from '@material-ui/icons/SpeakerNotes';
import StorageIcon from '@material-ui/icons/Storage';

import { red, blue, green, deepOrange } from '@material-ui/core/colors';
const drawerWidth = 280;

const useStyles = makeStyles(theme => ({
    toolbar: {
        paddingRight: 24, // keep right padding when drawer closed
    },
    toolbarIcon: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'flex-end',
        padding: '0 8px',
        ...theme.mixins.toolbar,
    },
    appBar: {
        background: 'grey',
        zIndex: theme.zIndex.drawer + 1,
        transition: theme.transitions.create(['width', 'margin'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen,
        }),
    },
    appBarShift: {
        marginLeft: drawerWidth,
        width: `calc(100% - ${drawerWidth}px)`,
        transition: theme.transitions.create(['width', 'margin'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    },
    title: {
        flexGrow: 1,
    },
    drawerPaper: {
        position: 'relative',
        whiteSpace: 'nowrap',
        width: drawerWidth,
        transition: theme.transitions.create('width', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    },
    drawerPaperClose: {
        overflowX: 'hidden',
        transition: theme.transitions.create('width', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen,
        }),
        width: theme.spacing(7),
        [theme.breakpoints.up('sm')]: {
            width: theme.spacing(9),
        },
    },
    paper: {
        padding: theme.spacing(2),
        display: 'flex',
        overflow: 'auto',
        flexDirection: 'column',
    },
    fixedHeight: {
        height: 240,
    },
}));

export default function MenuBar(props) {
    const classes = useStyles();
    const [open, setOpen] = React.useState(true);
    const handleDrawerOpen = () => {
        setOpen(true);
    };
    const handleDrawerClose = () => {
        setOpen(false);
    };
    const drawToolbox = () => {
        if (props.item === 'Credential Management') {
            return (
                <div>
                    <Tooltip title="Add">
                        <IconButton aria-label="add" onClick = {()=>props.clickAction('Add')}>
                            <AddIcon className={classes.addIcon} style={{ color: green[200]}}/>
                        </IconButton>
                    </Tooltip>

                    <Tooltip title="Edit">
                        <IconButton aria-label="edit" onClick = {()=>props.clickAction('Edit')}>
                            <EditIcon color="secondary" style={{ color: green[200]}}/>
                        </IconButton>
                    </Tooltip>
                    <Tooltip title="Delete">
                        <IconButton aria-label="delete" onClick = {()=>props.clickAction('Delete')}>
                            <DeleteIcon color="action" style={{ color: red[500]}}/>
                        </IconButton>
                    </Tooltip>

                </div>
            )
        } else if (props.item === 'API Key') {
            return (
                <div>
                    <Tooltip title="Add">
                        <IconButton aria-label="add" onClick = {()=>props.clickAction('Add')}>
                            <AddIcon className={classes.addIcon} style={{ color: green[200]}}/>
                        </IconButton>
                    </Tooltip>
                </div>
            )
        }
    }
    return (
        <div>
            <AppBar position="absolute" className={clsx(classes.appBar, open && classes.appBarShift)}>
                <Toolbar className={classes.toolbar}>
                    <IconButton
                        edge="start"
                        color="inherit"
                        aria-label="open drawer"
                        onClick={handleDrawerOpen}
                        className={clsx(classes.menuButton, open && classes.menuButtonHidden)}
                    >
                        <MenuIcon style={{ color: deepOrange[500]}}/>
                    </IconButton>
                    <Typography component="h1" variant="h6" color="inherit" noWrap className={classes.title}>
                        {props.item}
                    </Typography>
                    {drawToolbox()}
                </Toolbar>
            </AppBar>
            <Drawer
                variant="permanent"
                classes={{
                    paper: clsx(classes.drawerPaper, !open && classes.drawerPaperClose),
                }}
                open={open}
            >
                <div className={classes.toolbarIcon}>
                    <IconButton onClick={handleDrawerClose}>
                        <ChevronLeftIcon style={{ color: red[700]}}/>
                    </IconButton>
                </div>
                <Divider />
                <List>
                    <div>
                        {/*<ListItem button onClick={() => props.clickMenu('Credential Management')}>
                            <ListItemIcon>
                                <DashboardIcon style={{ color: blue[600]}}/>
                            </ListItemIcon>
                            <ListItemText primary="Credential Management" />
                        </ListItem>
                        <ListItem button onClick={() => props.clickMenu('API Key')}>
                            <ListItemIcon>
                                <VpnKeyIcon style={{ color: blue[600]}}/>
                            </ListItemIcon>
                            <ListItemText primary="API Key" />
                        </ListItem>
                        <ListItem button onClick={() => props.clickMenu('Attribute Mapping')}>
                            <ListItemIcon>
                                <SwapHorizontalCircleIcon style={{ color: blue[600]}}/>
                            </ListItemIcon>
                            <ListItemText primary="Attribute Mapping" />
                        </ListItem>
                        <ListItem button onClick={() => props.clickMenu('Custom Resource Mapping')}>
                            <ListItemIcon>
                                <StorageIcon style={{ color: blue[600]}}/>
                            </ListItemIcon>
                            <ListItemText primary="Custom Resource Mapping" />
            </ListItem>*/}
                        <ListItem button onClick={() => props.clickMenu('Demo')}>
                            <ListItemIcon>
                                <SpeakerNotesIcon style={{ color: blue[600]}}/>
                            </ListItemIcon>
                            <ListItemText primary="Demo" />
                        </ListItem>
                        <Divider />
                        <ListItem button onClick={() => props.clickMenu('Logout')}>
                            <ListItemIcon>
                                <ExitToAppIcon style={{ color: red[600]}}/>
                            </ListItemIcon>
                            <ListItemText primary="Logout" />
                        </ListItem>
                    </div>


                </List>
                <Divider />
            </Drawer>
        </div>
    )
}