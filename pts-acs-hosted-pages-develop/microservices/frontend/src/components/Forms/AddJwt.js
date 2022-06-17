import React, { useState } from 'react';

import { v4 as uuidv4 } from 'uuid';

import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import Grid from '@material-ui/core/Grid';
import IconButton from '@material-ui/core/IconButton';
import InputAdornment from '@material-ui/core/InputAdornment'
import Typography from '@material-ui/core/Typography';

import { DateTimePicker } from '@material-ui/pickers';

import EventAvailableIcon from '@material-ui/icons/EventAvailable';
import EventBusyIcon from '@material-ui/icons/EventBusy';

import PtsDateTimePicker from '../PtsDateTimePicker/PtsDateTimePicker';
import { DATEPICKER_FMT } from '../Utils';

const useStyles = makeStyles({
    paper: {
        minWidth: '500px'
    },
    /*textField: {
        margin: 10,
        spacing: 10,
        width: 500
    },*/
    datePicker: {
        margin: 10,
        spacing: 10,
        minWidth: 300
    }
});

export default function AddJwt(props) {
    const classes = useStyles();
    const tokenId = uuidv4();
    const [notBefore, setNotBefore] = useState(props.minDate);
    const [expirationTime, setExpirationTime] = useState(props.maxDate);

    const handleSaveClicked = (event) => {
        props.saveAction({
            tokenId: tokenId,
            notBefore: notBefore,
            expirationTime: expirationTime,
            enabled: false,
            token: '',
            created: null,
            lastUpdated: null
        });
    }

    return (
        <Dialog
            open={props.open}
            aria-labelledby="add-jwt-dialog"
            className={classes.paper}
        >
            <DialogTitle id="add-jwt-title">Add JSON Web Token</DialogTitle>
            <DialogContent>
                <DialogContentText>
                    Use this dialog to add a JWT to this form.
                </DialogContentText>
                <Grid container direction="column">
                    <Grid container direction="row">
                        <Grid item xs={1}>
                            <Typography variant="body2"><strong>Id</strong></Typography>
                        </Grid>
                        <Grid item xs={11}>
                            <Typography variant="body2">{tokenId}</Typography>
                        </Grid>
                    </Grid>
                    <Grid container direction="row">
                        <Grid item xs={12}>
                            <PtsDateTimePicker
                                id="notBefore"
                                label="Token Start Time"
                                value={notBefore}
                                minDate={props.minDate}
                                maxDate={props.maxDate}
                                ampm={false}
                                onChange={setNotBefore}
                                inputVariant="outlined"
                                format={DATEPICKER_FMT}
                                className={classes.datePicker}
                                showTodayButton
                                InputProps={{
                                    endAdornment: (
                                        <InputAdornment position="end">
                                            <IconButton>
                                                <EventAvailableIcon />
                                            </IconButton>
                                        </InputAdornment>
                                    )
                                }}
                            />
                        </Grid>
                    </Grid>
                    <Grid container direction="row">
                        <Grid item xs={12}>
                            <PtsDateTimePicker
                                id="expirationTime"
                                label="Token Expiry Time"
                                value={expirationTime}
                                minDate={notBefore}
                                maxDate={props.maxDate}
                                ampm={false}
                                onChange={setExpirationTime}
                                inputVariant="outlined"
                                format={DATEPICKER_FMT}
                                showTodayButton
                                className={classes.datePicker}
                                InputProps={{
                                    endAdornment: (
                                        <InputAdornment position="end">
                                            <IconButton>
                                                <EventBusyIcon />
                                            </IconButton>
                                        </InputAdornment>
                                    )
                                }}
                            />
                        </Grid>
                    </Grid>
                </Grid>
            </DialogContent>
            <DialogActions>
                <Button
                    onClick={props.closeAction}
                >
                    Cancel
                </Button>
                <Button
                    onClick={handleSaveClicked}
                >
                    Save
                </Button>
            </DialogActions>
        </Dialog>
    );
};
