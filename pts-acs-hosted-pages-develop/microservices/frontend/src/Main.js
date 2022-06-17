import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import { makeStyles } from '@material-ui/core/styles';
import CssBaseLine from '@material-ui/core/CssBaseLine';
import Container from '@material-ui/core/Container';
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import { Snackbar, IconButton } from '@material-ui/core';
import CloseIcon from '@material-ui/icons/Close';
import ErrorIcon from '@material-ui/icons/Error';
import clsx from 'clsx';
import MenuBar from './components/MenuBar';
import Demo from './demo/Demo';


const useStyles = makeStyles(theme => ({
    root: {
        display: 'flex',
    },
    appBarSpacer: theme.mixins.toolbar,
    content: {
        flexGrow: 1,
        height: '100vh',
        overflow: 'auto',
    },
    container: {
        paddingTop: theme.spacing(4),
        paddingBottom: theme.spacing(4),
    },
    paper: {
        padding: theme.spacing(2),
        display: 'flex',
        overflow: 'auto',
        flexDirection: 'column',
    },
    addIcon: {
        color: 'green'
    },
    error: {
        backgroundColor: theme.palette.error.dark,
      },
      message: {
        display: 'flex',
        alignItems: 'center'
    }
}));

export default function Main(props) {
    const classes = useStyles();
    const [item, setItem] = useState('Demo');
    const [action, setAction] = useState('');
    const [errMsg, setErrMsg] = useState(null);
    let history = useHistory();

    const { className } = props;

    const clickMenu = (menu) => {
        setItem(menu);
    };

    const clickAction = (action) => {
        setAction(action);
    }

    const editorClosed = () => {
        setAction('');
        //setCompanies([]);
        //setSelectedCompany({});
    }

    const handleClose = (event, reason) => {
        setErrMsg(null);
    };

    const mainArea = () => {
        switch(item) {
            case "Logout":
                sessionStorage.removeItem('userName');
                sessionStorage.removeItem("accessToken");
                history.push('/login');
                break;
            case 'Demo':
            default:
                return (<Demo />);
        }
        //return (<Demo />);
    }

    if (!sessionStorage.getItem('accessToken')) {
        history.push('/login');
        return (<div></div>);
    }
    return (
        <div className={classes.root}>
            <CssBaseLine />
            <MenuBar item={item} action={action} clickAction={clickAction} clickMenu={clickMenu} />
            <main className={classes.content}>
                <div className={classes.appBarSpacer} />
                <Container maxWidth="xl" className={classes.container}>
                    <Grid container spacing={3}>
                        <Grid item xs={12}>
                            <Paper className={classes.paper}>
                                {mainArea()}
                            </Paper>
                        </Grid>
                    </Grid>
                </Container>
            </main>
            <Snackbar
                anchorOrigin={{
                    vertical: 'bottom',
                    horizontal: 'left'
                }}
                open={errMsg !== null}
                autoHideDuration={6000}
                onClose={() => {
                    history.push('/login')
                }}
                className={clsx(classes['error'], className)}
                message={
                    <span id="client-snackbar" className={classes.message}>
                        <ErrorIcon className={classes.icon} />
                        {errMsg}
                    </span>
                }
                action={[
                    <IconButton key="close" aria-label="close" color="inherit" onClick={handleClose}>
                        <CloseIcon className={classes.icon} />
                    </IconButton>
                ]}
                >
                </Snackbar>
        </div>
    );
};
