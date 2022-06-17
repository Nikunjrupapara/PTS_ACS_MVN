import React, { useState } from 'react';

import AddJwt from './AddJwt';
import { DATE_TIME_FMT as dateTimeFmt } from '../Utils';

import { makeStyles } from '@material-ui/core/styles';

import Grid from '@material-ui/core/Grid';
import IconButton from '@material-ui/core/IconButton';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListSubHeader from '@material-ui/core/ListSubheader';
import Popper from '@material-ui/core/Popper';
import Switch from '@material-ui/core/Switch';
import Typography from '@material-ui/core/Typography';

import AddCircleIcon from '@material-ui/icons/AddCircle';
import Assignment from '@material-ui/icons/Assignment';
import Visibility from '@material-ui/icons/Visibility';
import VisibilityOff from '@material-ui/icons/VisibilityOff';

const useStyles = makeStyles((theme) => ({
    centeredItem: {
        display: 'grid',
        alignItems: 'center'
    },
    listHeader: {
        fontSize: '0.75em',
        fontWeight: 'bold',
        wordWrap: 'break-word'
    },
    listItem: {
        fontSize: '0.75em',
        wordWrap: 'break-word'
    },
    popper: {
        border: '1px solid',
        padding: theme.spacing(1),
        maxWidth: '500px',
        wordWrap: 'break-word',
        backgroundColor: theme.palette.background.paper
    }
}));

function JwtPopper(props) {
    const classes = useStyles();
    const [anchorEl, setAnchorEl] = useState(null);

    const handleClick = (event) => {
        setAnchorEl(anchorEl ? null : event.currentTarget);
    };

    const open = Boolean(anchorEl);
    const id = open ? 'jwt-popper' : undefined;
    const jwtStr = props.tokenExists ? props.jwtStr : 'JWT will be generated when form is submitted.'

    return (
        <Grid item xs={2} className={classes.centeredItem}>
            <IconButton
                edge="start"
                aria-label="Show/hide JWT"
                onClick={handleClick}
            >
                { open ? (<VisibilityOff />) : (<Visibility />) }
            </IconButton>
            <Popper
                id={id}
                open={open}
                anchorEl={anchorEl}
                placement='bottom'
            >
                <div className={classes.popper}>
                    <Typography variant="caption">
                        {jwtStr}
                    </Typography>
                </div>
            </Popper>
        </Grid>
    );
};

function JwtRow(props) {
    const classes = useStyles();
    const { jwt } = props;
    const tokenExists = jwt.token.length > 0;

    const handleJwtEnabledClicked = (index) => {
        props.jwtEnabled(props.index);
    };

    return (
        <ListItem key={jwt.uuid} className={classes.listItem}>
            <Grid container direction="row">
                <Grid item xs={3}>{jwt.tokenId}</Grid>
                <Grid item xs={2}>{dateTimeFmt.format(new Date(jwt.notBefore))}</Grid>
                <Grid item xs={2}>{dateTimeFmt.format(new Date(jwt.expirationTime))}</Grid>
                <Grid item xs={2} className={classes.centeredItem}>
                    <Switch
                        checked={jwt.enabled}
                        name="jwtEnabled"
                        color="primary"
                        inputProps={{ 'aria-label': 'jwt-enabled' }}
                        onClick={() => handleJwtEnabledClicked(props.index)}
                    />
                </Grid>
                <JwtPopper jwtStr={jwt.token} tokenExists={tokenExists} />
                <Grid item xs={1} className={classes.centeredItem}>
                { tokenExists &&
                    <IconButton
                        edge="start"
                        aria-label="copy-jwt"
                        onClick={() => navigator.clipboard.writeText(jwt.token)}
                    >
                        <Assignment />
                    </IconButton>
                }
                </Grid>
            </Grid>
        </ListItem>
    );
}

export default function JwtList(props) {
    const classes = useStyles();
    const [openAddDlg, setOpenAddDlg] = useState(false);

    const onAddJwtClicked = () => {
        setOpenAddDlg(true);
    }

    const onAddJwtCancelClicked = () => {
        setOpenAddDlg(false);
    }

    const onAddJwtSaveClicked = (newJwt) => {
        setOpenAddDlg(false);
        props.addJwt(newJwt);
    };

    return (
        <List
            className={classes.list}
            aria-label="JWT List"
            subheader={
                <ListSubHeader>
                    <Grid container direction="column>">
                        <Grid container direction="row">
                            <Typography variant="subtitle1">JSON Web Token configuration</Typography>
                        </Grid>
                        <Grid container direction="row">
                            <Typography variant="caption">
                                Microservices API requests are authenticated using a JSON Web Token (JWT), of which each
                                form must have at least one.  The JWT encodes information about the form which is used to
                                determine if an incoming request can be authorized or not.  The list below shows the JWTs
                                defined for this form and allows the user to add new ones and disable existing ones.  Note
                                that it may be necessary to update the form pages with a new token if replacing an existing
                                JWT with a new one.
                            </Typography>
                        </Grid>
                        <Grid container direction="row">
                            <Grid item xs={12}>
                                <IconButton
                                    edge="start"
                                    aria-label="add-jwt"
                                    onClick={onAddJwtClicked}
                                >
                                    <AddCircleIcon />
                                </IconButton>
                                <AddJwt
                                    open={openAddDlg}
                                    minDate={props.minDate}
                                    maxDate={props.maxDate}
                                    closeAction={onAddJwtCancelClicked}
                                    saveAction={onAddJwtSaveClicked}
                                />
                            </Grid>
                        </Grid>
                    </Grid>
                </ListSubHeader>
            }
        >
            <ListItem>
                <Grid container direction="row">
                    <Grid item xs={3}><Typography component="subtitle1" variant="subtitle1">Token Id</Typography></Grid>
                    <Grid item xs={2}><Typography component="subtitle1" variant="subtitle1">Valid From</Typography></Grid>
                    <Grid item xs={2}><Typography component="subtitle1" variant="subtitle1">Expires</Typography></Grid>
                    <Grid item xs={2} className={classes.centeredItem}><Typography component="subtitle1" variant="subtitle1">Enabled</Typography></Grid>
                    <Grid item xs={2} className={classes.centeredItem}><Typography component="subtitle1" variant="subtitle1">Show/Hide</Typography></Grid>
                    <Grid item xs={1} className={classes.centeredItem}><Typography component="subtitle1" variant="subtitle1">Copy</Typography></Grid>
                </Grid>
            </ListItem>
            { props.jwts.length > 0 ? 
                props.jwts.map((jwt, index) => (
                    <JwtRow
                        jwt={jwt}
                        jwtEnabled={props.jwtEnabled}
                        index={index}
                    />
                )) : (
                    <ListItem>
                        <Grid container direction="row">
                            <Grid item xs={12}>
                                <Typography variant="caption" color="error">
                                    Please add at least one JSON Web Token to this form.
                                </Typography>
                            </Grid>
                        </Grid>
                    </ListItem>
                )
            }
        </List>
    );
};
