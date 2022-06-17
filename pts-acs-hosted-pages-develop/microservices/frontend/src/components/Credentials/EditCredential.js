import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import Axios from 'axios';
import { CREDENTIAL_MANAGEMENT_URL } from '../Utils';
import MessageBar from '../MessageBar/MessageBar';
import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Container from '@material-ui/core/Container';
import Grid from '@material-ui/core/Grid';
import TextField from '@material-ui/core/TextField'
import Tooltip from '@material-ui/core/Tooltip';
import Typography from '@material-ui/core/Typography';

const useStyles = makeStyles({
    container: {
        maxWidth: '700px'
    },
    monoTextField: {
        fontFamily:'Courier New'
    },
    textField: {
        margin: 10,
        spacing: 10,
        width: 700
    }
});

export default function EditCredential({ match }) {
    const classes = useStyles();
    const history = useHistory();
    const [createMode, setCreateMode] = useState(false);
    const [submitLabel, setSubmitLabel] = useState('');
    const [displayMessage, setDisplayMessage] = useState(false);

    const [company, setCompany] = useState('');
    const [clientId, setClientId] = useState('');
    const [organization, setOrganization] = useState('');
    const [organizationId, setOrganizationId] = useState('');
    const [privateKey, setPrivateKey] = useState('');
    const [secret, setSecret] = useState('');
    const [transactionalApi, setTransactionalApi] = useState('');
    const [username, setUsername] = useState('');

    const defaultErrors = {
        company: false,
        clientId: false,
        organization: false,
        organizationId: false,
        privateKey: false,
        secret: false,
        transactionalApi: false,
        username: false
    };
    const [errors, setErrors] = useState(defaultErrors);

    const [messageDetails, setMessageDetails] = useState({});

    const companyParam = match.params.company;

    useEffect(() => {
        console.log("Inside useEffect...");
        if (companyParam === 'new') {
            setCreateMode(true);
            setSubmitLabel("CREATE");
        } else {
            getCredential();
        }
    }, []);

    const messageDisplayed = () => { setDisplayMessage(false); };

    const getCredential = async() => {
        console.log("Inside getCredential...");
        try {
            const url = `${CREDENTIAL_MANAGEMENT_URL}/${companyParam}`;
            const response = await Axios.get(url, { withCredentials: true});
            const data = await response.data;
            setCompany(data.company);
            setClientId(data.clientId);
            setOrganization(data.organization);
            setOrganizationId(data.organizationId);
            setPrivateKey(data.privateKey);
            setSecret(data.secret);
            setTransactionalApi(data.transactionalApi);
            setUsername(data.username);

            setCreateMode(false);
            setSubmitLabel("UPDATE");
        } catch (err) {
            console.log(JSON.stringify(err));
            if (err.response.status === 401) {
                alert("You must log in as a user with the Admin role to edit credentials.");
                history.push("/login");
            }
        }
    };

    const validateForm = () => {
        const valErrors = {
            company: company.length === 0,
            clientId: clientId.length === 0,
            organization: organization.length === 0,
            organizationId: organizationId.length === 0,
            privateKey: privateKey.length === 0,
            secret: secret.length === 0,
            transactionalApi: transactionalApi.length === 0,
            username: username.length === 0
        };
        const errorsFound = Object.keys(valErrors).some(x => valErrors[x]);
        setErrors(valErrors);
        if (errorsFound) {
            setMessageDetails({
                message: 'Please enter a value for mandatory fields',
                msgType: 'error'
            });
            setDisplayMessage(true);
        }
        return !errorsFound;
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        if (!validateForm()) {
            return;
        }
        let data = {
            clientId: clientId,
            organization: organization,
            organizationId: organizationId,
            privateKey: privateKey,
            secret: secret,
            transactionalApi: transactionalApi,
            username: username
        };
        if (createMode) {
            data.company = company;
        }
        const reqUrl = createMode ? CREDENTIAL_MANAGEMENT_URL : `${CREDENTIAL_MANAGEMENT_URL}/${company}`;
        let axiosConfig = {
            method: createMode ? 'post' : 'put',
            data: data,
            withCredentials: true,
            validateStatus: function(status) {
                return status < 300;
            }
        };
        console.log(`Submitting request to ${reqUrl} with config ${JSON.stringify(axiosConfig)}`);
        Axios(reqUrl, axiosConfig)
        .then(response => {
            console.log('Axios request succeeded');
            const responseStatus = response.status;
            console.log(`Status ${responseStatus} received from ${reqUrl}`);
            console.log(data);
            console.log(response.status);
            console.log(response.statusText);
            console.log(response.headers);
            console.log(response.config);
            if (responseStatus === 200 || responseStatus === 201) {
                history.push("/credentials");
            } else {
                setMessageDetails({
                    message: 'An error occurred',
                    msgType: 'error'
                });
                setDisplayMessage(true);
            }
        })
        .catch(err => {
            console.log(`Request errored with ${err.toJSON()}`);
            setMessageDetails({
                message: 'An error occurred',
                msgType: 'error'
            });
            setDisplayMessage(true);
        });
    };

    const handleTest = (event) => {
        event.preventDefault();
        if (!validateForm()) {
            return;
        }
        let data = {
            clientId: clientId,
            company: company,
            organization: organization,
            organizationId: organizationId,
            privateKey: privateKey,
            secret: secret,
            transactionalApi: transactionalApi,
            username: username
        };
        const reqUrl = `${CREDENTIAL_MANAGEMENT_URL}/test`;
        let axiosConfig = {
            method: 'post',
            data: data,
            withCredentials: true,
            validateStatus: function(status) {
                return status < 300 || status === 400;
            }
        };
        console.log(`Submitting request to ${reqUrl} with config ${JSON.stringify(axiosConfig)}`);
        Axios(reqUrl, axiosConfig)
        .then(response => {
            const responseStatus = response.status;
            console.log(`Status ${responseStatus} received from ${reqUrl}`);
            console.log(`response.data: ${JSON.stringify(response.data)}`);
            console.log(`response.status: ${response.status}`);
            if (responseStatus === 200 || responseStatus === 201) {
                setMessageDetails({
                    message: 'The ACS Credential was tested successfully.',
                    msgType: 'success',
                    autoHideDuration: 7500
                });
                setDisplayMessage(true);
            } else {
                const moreInfoStr = response.data.moreInfo;
                const moreInfo = JSON.parse(moreInfoStr);
                setMessageDetails({
                    message: `The ACS credential test failed with the error: ${moreInfo.error_description}`,
                    msgType: 'error'
                });
                setDisplayMessage(true);
            }
        })
        .catch(err => {
            console.log(`Request errored with ${JSON.stringify(err)}`);
            alert('Catch: An error occurred!');
            setMessageDetails({
                message: err.message,
                msgType: 'error'
            });
            setDisplayMessage(true);
        });
    };

    const handleCancel = (event) => {
        event.preventDefault();
        history.push("/credentials");
    };
    
    const pageHeading = createMode ? 'Add new ACS Credential' : `Edit ACS Credential for ${company}`;

    console.log(`companyParam: ${companyParam}, createMode: ${createMode}`);

    if (displayMessage) {
        messageDetails.closeAction = messageDisplayed;
    }
    return (
        <Container fixed className={classes.container}>
            <form>
                <Grid container direction="row" justify="center">
                    <Typography component="h1" variant="h5">{pageHeading}</Typography>
                </Grid>
                <Tooltip
                    arrow
                    placement="bottom"
                    title="The name of the company integrating with ACS."
                >
                    <TextField
                        required
                        error={errors.company}
                        fullWidth
                        id="company"
                        variant="outlined"
                        label="Company"
                        margin="normal"
                        value={company}
                        disabled={!createMode}
                        onChange={(event) => setCompany(event.target.value)}
                        className={classes.textField}
                    />
                </Tooltip>
                <Tooltip
                    arrow
                    placement="bottom"
                    title={<span>The tenant id for the ACS instance, which is the bold part of the Adobe Campaign URL
                        shown below<br/><br/>https://<strong>yesmarketing-mkt-stage3</strong>.campaign.adobe.com</span>}
                >
                    <TextField
                        required
                        error={errors.organization}
                        fullWidth
                        id="tenantId"
                        variant="outlined"
                        label="ACS Tenant Id"
                        margin="normal"
                        value={organization}
                        onChange={(event) => setOrganization(event.target.value)}
                        className={classes.textField}
                    />
                </Tooltip>
                <Tooltip
                    arrow
                    placement="bottom"
                    title={
                        <span>
                            The Transactional Api value can be found in the URL shown in the API Preview of a 
                            transactional event<br/><br/>https://mc.adobe.io/<>&#123;</>organization<>&#125;</>/campaign/
                            <strong>mcyesmarketing</strong>/&#123;eventId&#125;
                        </span>
                    }
                >
                    <TextField
                        required
                        error={errors.transactionalApi}
                        fullWidth
                        id="transactionalApi"
                        variant="outlined"
                        label="ACS Transactional API"
                        margin="normal"
                        value={transactionalApi}
                        onChange={(event) => setTransactionalApi(event.target.value)}
                        className={classes.textField}
                    />
                </Tooltip>
                <Tooltip
                    arrow
                    placement="bottom"
                    title="The Technical Account Email from the Adobe I/O Console integration."
                >
                    <TextField
                        required
                        error={errors.username}
                        fullWidth
                        id="username"
                        variant="outlined"
                        label="Technical Account Email"
                        margin="normal"
                        value={username}
                        onChange={(event) => setUsername(event.target.value)}
                        className={classes.textField}
                    />
                </Tooltip>
                <Tooltip
                    arrow
                    placement="bottom"
                    title="The Organization ID from the Adobe I/O Console integration."
                >
                    <TextField
                        required
                        error={errors.organizationId}
                        fullWidth
                        id="organizationId"
                        variant="outlined"
                        label="ACS Organization Id"
                        margin="normal"
                        value={organizationId}
                        onChange={(event) => setOrganizationId(event.target.value)}
                        className={classes.textField}
                    />
                </Tooltip>
                <Tooltip
                    arrow
                    placement="bottom"
                    title="The Client ID from the Adobe I/O Console integration."
                >
                    <TextField
                        required
                        error={errors.clientId}
                        fullWidth
                        id="clientId"
                        variant="outlined"
                        label="Client Id"
                        margin="normal"
                        value={clientId}
                        onChange={(event) => setClientId(event.target.value)}
                        className={classes.textField}
                    />
                </Tooltip>
                <Tooltip
                    arrow
                    placement="bottom"
                    title="The Client Secret from the Adobe I/O Console integration."
                >
                    <TextField
                        required
                        error={errors.secret}
                        fullWidth
                        id="secret"
                        variant="outlined"
                        label="Secret"
                        margin="normal"
                        value={secret}
                        onChange={(event) => setSecret(event.target.value)}
                        className={classes.textField}
                    />
                </Tooltip>
                <Tooltip
                    arrow
                    placement="bottom"
                    title="The Private Key used to create the Adobe I/O Console integration."
                >
                    <TextField
                        required
                        error={errors.privateKey}
                        fullWidth
                        id="privateKey"
                        label="Private Key"
                        variant="outlined"
                        multiline
                        rows="20"
                        value={privateKey}
                        onChange={(event) => setPrivateKey(event.target.value)}
                        className={classes.textField}
                        InputProps={{
                            classes: {
                                input: classes.monoTextField
                            }
                        }}
                    />
                </Tooltip>
                <br /><br />
                <Button
                    type="submit"
                    variant="contained"
                    color="primary"
                    onClick={(event) => handleSubmit(event)}
                >{submitLabel}</Button>
                <Button
                    variant="contained"
                    color="primary"
                    onClick={(event) => handleTest(event)}
                >TEST</Button>
                <Button
                    variant="contained"
                    color="primary"
                    onClick={(event) => handleCancel(event)}
                >CANCEL</Button>
            </form>
            { displayMessage &&
                <MessageBar messageDetails={messageDetails} />
            }
        </Container>
    );
};
