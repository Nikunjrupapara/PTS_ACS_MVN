import React, { useState, useEffect } from 'react';
import { Link, useHistory } from 'react-router-dom';
import Axios from 'axios';
import { CREDENTIAL_MANAGEMENT_URL } from '../Utils';
import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Grid from '@material-ui/core/Grid';
import Switch from '@material-ui/core/Switch';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Typography from '@material-ui/core/Typography';
import Paper from '@material-ui/core/Paper';
import MessageBar from '../MessageBar/MessageBar';

const useStyles = makeStyles({
    table: {
        minWidth: 650
    }
});

export default function ListCredentials() {
    const classes = useStyles();
    const history = useHistory();
    const [credentials, setCredentials] = useState([]);
    const [displayMessage, setDisplayMessage] = useState(false);
    const [messageDetails, setMessageDetails] = useState({});

    const messageDisplayed = () => { setDisplayMessage(false); };

    useEffect(() => {
        getCredentials();
    }, []);

    const getCredentials = async() => {
        try {
            const response = await Axios.get(CREDENTIAL_MANAGEMENT_URL, { withCredentials: true});
            const data = await response.data;
            setCredentials(data);
        } catch (err) {
            console.log(JSON.stringify(err));
            if (err.response.status === 401) {
                alert("You must log in as a user with the Admin role to edit credentials.");
                history.push("/login");
            }
        }
    };

    const handleAddCredential = (event) => {
        history.push("/credentials/new");
    };

    const enabledClicked = (company) => {
        let credentialArray = credentials;
        const rowIndex = credentialArray.findIndex((element) => element.company === company);
        let credential = credentialArray[rowIndex];
        credential.enabled = !credential.enabled;
        let axiosConfig = {
            method: 'post',
            data: {
                enabled: credential.enabled
            },
            withCredentials: true,
            validateStatus: function(status) {
                return status < 300;
            }
        };
        const reqUrl = `${CREDENTIAL_MANAGEMENT_URL}/${credential.company}/enabled`;
        Axios(reqUrl, axiosConfig)
        .then(response => {
            credential.enabled = response.data.enabled;
            credential.lastUpdated = response.data.lastUpdated;
            credentialArray[rowIndex] = credential;
            setCredentials(credentialArray);
            setMessageDetails({
                message: 'The Credential was updated.',
                msgType: 'success'
            });
            setDisplayMessage(true);
        })
        .catch(err => {
            setMessageDetails({
                message: err.data.message,
                msgType: 'error'
            })
            setDisplayMessage(true);
        });
    };

    if (displayMessage) {
        messageDetails.closeAction = messageDisplayed;
    }

    return (
        <Grid container>
            <Grid container direction="row" justify="center">
                <Typography component="h1" variant="h5">Credentials</Typography>
                <Button
                    variant="contained"
                    color="primary"
                    onClick={(event) => handleAddCredential(event)}
                >
                    Add Credential
                </Button>
            </Grid>
            <TableContainer component={Paper}>
                <Table className={classes.table} aria-label="simple table">
                    <TableHead>
                        <TableRow>
                            <TableCell>Company</TableCell>
                            <TableCell>ACS Tenant Id</TableCell>
                            <TableCell align="center">Enabled</TableCell>
                            <TableCell>Created</TableCell>
                            <TableCell>Last Modified</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                    {
                        credentials.length > 0 ? 
                        credentials.map(row => (
                            <TableRow key={row.company}>
                                <TableCell component="th" scope="row">
                                    <Link to={`credentials/${row.company}`}>
                                        {row.company}
                                    </Link>
                                </TableCell>
                                <TableCell>{row.organization}</TableCell>
                                <TableCell align="center">
                                    <Switch
                                        checked={row.enabled}
                                        onChange={enabledClicked.bind(this,row.company)}
                                        name="credentialEnabled"
                                        color="primary"
                                        inputProps={{ 'aria-label': 'primary-checkbox' }}
                                    />
                                </TableCell>
                                <TableCell>{row.created}</TableCell>
                                <TableCell>{row.lastUpdated}</TableCell>
                            </TableRow>
                        )) : 
                        <TableRow>
                            <TableCell>
                                <Typography>No credentials found</Typography>
                            </TableCell>
                        </TableRow>
                    }
                    </TableBody>
                </Table>
            </TableContainer>
            {
                displayMessage && <MessageBar messageDetails={messageDetails} />
            }
        </Grid>
    );
};
