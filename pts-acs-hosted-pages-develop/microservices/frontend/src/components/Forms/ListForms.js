import React, { useState, useEffect } from 'react';
import { Link, useHistory, useLocation } from 'react-router-dom';
import Axios from 'axios';
import qs from 'qs';
import ListCompanies from '../ListCompanies/ListCompanies';
import LoadingSpinner from '../LoadingSpinner/LoadingSpinner';
import MessageBar from '../MessageBar/MessageBar';
import { FORM_MANAGEMENT_URL } from '../Utils';
import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Container from '@material-ui/core/Container';
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import Switch from '@material-ui/core/Switch';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer'
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Typography from '@material-ui/core/Typography';

const useStyles = makeStyles({
    table: {
        minWidth: '650px'
    }
});

export default function ListForms() {
    const classes = useStyles();
    const history = useHistory();
    const [ company, setCompany ] = useState("");
    const [ companySelected, setCompanySelected ] = useState(false);
    const [ loading, setLoading ] = useState(false);
    const [ forms, setForms ] = useState([]);
    const [ displayMessage, setDisplayMessage ] = useState(false);
    const [messageDetails, setMessageDetails ] = useState({});

    const messageDisplayed = () => { setDisplayMessage(false); };

    const onCompanySelect = (companyArg) => {
        setCompany(companyArg);
        setCompanySelected(true);
    }

    const queryStr = useLocation().search;
    useEffect(() => {
        if (queryStr) {
            const queryParams = qs.parse(queryStr, { ignoreQueryPrefix: true });
            if (queryParams.company) {
                setCompany(queryParams.company);
                setCompanySelected(true);
            }
            const location = Object.assign({}, history.location);
            location.search = '';
            history.push(location);
        }
    }, []);

    useEffect(() => {
        const fetchdata = async () => {
            setLoading(true);
            try {
                const url = `${FORM_MANAGEMENT_URL}/${company}`;
                const response = await Axios.get(url, { withCredentials: true });
                const data = await response.data;
                setForms(data.forms);
                setLoading(false)
            } catch (err) {
                if (err.response.status === 401) {
                    alert("You must login as a user with the Admin role to edit forms");
                    history.push("/login");
                }
            }
            }
        if (!companySelected) {
            return;
        }
        fetchdata();
    }, [ company ]);

    const handleAddForm = (event) => {
        history.push(`forms/${company}/new`);
    };

    const enabledClicked = (uuid) => {
        let formArray = forms;
        const rowIndex = formArray.findIndex((element) => element.uuid === uuid);
        let form = formArray[rowIndex];
        form.enabled = !form.enabled;
        const axiosConfig = {
            method: 'post',
            data: {
                enabled: form.enabled
            },
            withCredentials: true,
            validateStatus: function (status) {
                return status < 300;
            }
        };
        const reqUrl = `${FORM_MANAGEMENT_URL}/${company}/form/${uuid}/enabled`;
        Axios(reqUrl, axiosConfig)
        .then(response => {
            form.enabled = response.data.enabled;
            form.lastUpdated = response.data.lastUpdated;
            formArray[rowIndex] = form;
            setForms(formArray);
            setMessageDetails({
                message: 'The form was updated.',
                msgType: 'success'
            });
        })
        .catch(err => {
            setMessageDetails({
                message: err.data.message,
                msgType: 'error'
            });
        })
        .finally(() => setDisplayMessage(true));
    };

    if (displayMessage) {
        messageDetails.closeAction = messageDisplayed;
    }

    return (
        <Container fixed>
            <Grid container direction="row" justify="center">
                <Typography component="h1" variant="h5">Forms</Typography>
            </Grid>
            <Grid container direction="row">
                <ListCompanies
                    company={company}
                    maxWidth="700"
                    selectAction={onCompanySelect}
                />
                <Button
                    variant="contained"
                    color="primary"
                    onClick={(event) => handleAddForm(event)}
                    disabled={!companySelected}
                >
                    Add Form
                </Button>
            </Grid>
            <LoadingSpinner loading={loading} />
            {
                companySelected && !loading && (
                    forms.length > 0 ? (
                    <TableContainer component={Paper}>
                        <Table className={classes.table} aria-label="simple table">
                            <TableHead>
                                <TableRow>
                                    <TableCell>UUID</TableCell>
                                    <TableCell>Code</TableCell>
                                    <TableCell>Enabled</TableCell>
                                    <TableCell>Description</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                            {
                                forms.map(row => (
                                    <TableRow key={row.uuid}>
                                        <TableCell component="th" scope="row">
                                            <Link to={`forms/${company}/${row.uuid}`}>{row.uuid}</Link>
                                        </TableCell>
                                        <TableCell>{row.code}</TableCell>
                                        <TableCell align="center">
                                            <Switch
                                                checked={row.enabled}
                                                onChange={enabledClicked.bind(this, row.uuid)}
                                                name="formEnabled"
                                                color="primary"
                                                inputProps={{ 'aria-label': 'primary-checkbox'}}
                                            />
                                        </TableCell>
                                        <TableCell>{row.description}</TableCell>
                                    </TableRow>
                                ))
                            }
                            </TableBody>
                        </Table>
                    </TableContainer>
                    ) : <Typography>No forms found for company.</Typography>
                )
            }
            {
                displayMessage &&
                <MessageBar messageDetails={messageDetails} />
             }
        </Container>
    );
}
