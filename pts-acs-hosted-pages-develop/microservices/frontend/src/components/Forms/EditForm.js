import React, { useEffect, useState} from 'react';
import { useHistory } from 'react-router-dom';
import Axios from 'axios';
import { subQuarters, setHours, setMinutes, setSeconds } from 'date-fns';
import { v4 as uuidv4 } from 'uuid';

import { DATEPICKER_FMT, formatDate, FORM_MANAGEMENT_URL } from '../Utils';
import DomainField from './DomainField';
import JwtList from './JwtList';
import AuthoritySelect from './AuthoritySelect';
import MessageBar from '../MessageBar/MessageBar';
import PtsDateTimePicker from '../PtsDateTimePicker/PtsDateTimePicker';

import { makeStyles } from '@material-ui/core/styles';

import Box from '@material-ui/core/Box';
import Button from '@material-ui/core/Button';
import Checkbox from '@material-ui/core/Checkbox';
import Container from '@material-ui/core/Container';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Grid from '@material-ui/core/Grid';
import IconButton from '@material-ui/core/IconButton';
import InputAdornment from '@material-ui/core/InputAdornment';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemSecondaryAction from '@material-ui/core/ListItemSecondaryAction';
import ListSubHeader from '@material-ui/core/ListSubheader';
import TextField from '@material-ui/core/TextField';
import Tooltip from '@material-ui/core/Tooltip';
import Typography from '@material-ui/core/Typography';

import AddCircleIcon from '@material-ui/icons/AddCircle';
import DeleteIcon from '@material-ui/icons/Delete';
import EventAvailableIcon from '@material-ui/icons/EventAvailable';
import EventBusyIcon from '@material-ui/icons/EventBusy';

const useStyles = makeStyles({
    container: {
        maxWidth: '700px'
    },
    textField: {
        margin: 10,
        spacing: 10,
        minWidth: 300,
        width: 550
    },
    datePicker: {
        margin: 10,
        spacing: 10,
        minWidth: 300
    },
    box: {
        borderRadius: '5px',
        width: '500px'
    }
});

export default function EditForm({ match }) {
    const classes = useStyles();
    const history = useHistory();


    const [ createMode, setCreateMode ] = useState(false);
    const [ submitLabel, setSubmitLabel ] = useState('');
    const [ displayMessage, setDisplayMessage ] = useState(false);
    const [ messageDetails, setMessageDetails ] = useState({});

    const today = new Date();
    const defaultEffectiveFrom = setHours(setMinutes(setSeconds(subQuarters(today, 1), 0), 0), 0);
    const defaultEffectiveTo = new Date('2099-12-31T00:00:00');

    const [ authorityList, setAuthorityList ] = useState([]);
    const [ uuid, setUuid ] = useState('');
    const [ company, setCompany ] = useState('');
    const [ code, setCode ] = useState('');
    const [ description, setDescription ] = useState('');
    const [ domains, setDomains ] = useState([]);
    const [ useEffectiveFrom, setUseEffectiveFrom ] = useState(false);
    const [ effectiveFrom, setEffectiveFrom ] = useState(defaultEffectiveFrom);
    const [ useEffectiveTo, setUseEffectiveTo ] = useState(false);
    const [ effectiveTo, setEffectiveTo ] = useState(defaultEffectiveTo);
    const [ authorities, setAuthorities ] = useState([]);
    const [ jwts, setJwts ] = useState([]);

    const defaultErrors = {
        flags: {
            code: false,
            description: false,
            domains: false,
            effectiveFrom: false,
            effectiveTo: false,
            authorities: false,
            jwts: false,
        },
        arrays: {
            domainList: [],
            authorityList: [],
        },
        messages: {
            domainsMsg: '',
            authoritiesMsg: '',
            jwtsMsg: ''
        }
    };

    const [ errors, setErrors ] = useState(defaultErrors);

    const companyParam = match.params.company;
    const uuidParam = match.params.uuid;

    const messageDisplayed = () => { setDisplayMessage(false); };

    useEffect(() => {
        const fetchData = async () => {
            const reqUrl = `${FORM_MANAGEMENT_URL}/authorities`;
            try {
                const response = await Axios.get(reqUrl, { withCredentials: true});
                const data = await response.data;
                setAuthorityList(data);
            } catch(err) {
                if (err.response.status === 401) {
                    alert("You must log in as a user with the Admin role to edit a form.");
                    history.push("/login");
                }
            };
        };
        fetchData();
    }, []);

    useEffect(() => {
        if (uuidParam === 'new') {
            setCreateMode(true);
            setCompany(companyParam);
            setSubmitLabel('CREATE');
        } else {
            getForm();
        }
    }, [uuidParam]);

    const populateForm = (data) => {
        setCompany(data.company);
        setUuid(data.uuid);
        setCode(data.code);
        setDescription(data.description);
        setDomains(data.domains);
        const effectiveFromTemp = new Date(data.effectiveFrom);
        setEffectiveFrom(effectiveFromTemp);
        setUseEffectiveFrom(effectiveFromTemp !== defaultEffectiveFrom);
        const effectiveToTemp = new Date(data.effectiveTo);
        setEffectiveTo(effectiveToTemp);
        setUseEffectiveTo(effectiveToTemp !== defaultEffectiveTo);
        setAuthorities(data.authorities.map(item => item.authority));
        const jwtsTemp = data.jwts.map(jwt => {
            return {
                created: new Date(jwt.created),
                enabled: jwt.enabled,
                expirationTime: new Date(jwt.expirationTime),
                lastUpdated: new Date(jwt.lastUpdated),
                notBefore: new Date(jwt.notBefore),
                token: jwt.token,
                tokenId: jwt.tokenId
            }
        });
        setJwts(jwtsTemp);
        setCreateMode(false);
        setSubmitLabel('UPDATE');
}

    const getForm = async() => {
        try {
            const url = `${FORM_MANAGEMENT_URL}/${companyParam}/form/${uuidParam}`;
            const response = await Axios.get(url, { withCredentials: true });
            const data = await response.data;
            populateForm(data);
        } catch(err) {
            if (err.response && err.response.status === 401) {
                alert("You must log in as a user with the Admin role to edit a form.");
                history.push("/login");
            } else {
                alert("Something bad happened!");
            }
        }
    }

    const onAddDomainClicked = (index) => {
        setDomains([...domains, '']);
    };

    const deleteDomain = (index) => {
        let tempDomains = [...domains];
        tempDomains.splice(index, 1);
        setDomains(tempDomains);
    };

    const onDomainChanged = (index, domain) => {
        let tempDomains = [...domains];
        tempDomains[index] = domain;
        setDomains(tempDomains);
    }

    const buildDomainField = (index, domain) => {
        return (
            <DomainField
                index={index}
                domain={domain}
                displayDeleteButton={domains.length > 1}
                onDomainChange={onDomainChanged}
                deleteDomain={deleteDomain}
                error={errors.arrays.domainList[index]}
            />
        );
    };

    const displayDomains = () => {
        let domainFields = [];
        if (domains.length === 0) {
            domainFields.push(buildDomainField(0, ''));
        } else {
            domains.forEach((domain, index) => domainFields.push(buildDomainField(index, domain)));
        }
        return domainFields;
    };

    const onAddAuthorityClicked = (index) => {
        setAuthorities([...authorities, '']);
    };

    const onDeleteAuthorityClicked = (index) => {
        let tempAuthorities = [...authorities];
        tempAuthorities.splice(index, 1);
        setAuthorities(tempAuthorities);
    }

    const onAuthorityChanged = (index, authority) => {
        let tempAuthorities = [...authorities];
        tempAuthorities[index] = authority;
        setAuthorities(tempAuthorities);
    }

    const buildAuthorityField = (index, authority) => {
        // availableAuthorities is any Authority in authoritiesList that is not in the selectedAuthorities list, plus the
        // provided authority
        const availableAuthorities = authorityList.filter(val => {
            if (val.authority === authority) return true;
            return !authorities.includes(val.authority)
        });
        const key=`authority-${index}`;
        return (
            <ListItem key={key}>
                <AuthoritySelect
                    index={index}
                    selectedAuthority={authority}
                    availableAuthorities={availableAuthorities}
                    selectAction={onAuthorityChanged}
                    minWidth={370}
                    maxWidth={370}
                    error={errors.arrays.authorityList[index]}
                />
                { authorities.length > 1 &&
                    <ListItemSecondaryAction>
                        <IconButton
                            edge="end"
                            aria-label="delete-role"
                            onClick={() => onDeleteAuthorityClicked(index)}
                        >
                            <DeleteIcon />
                        </IconButton>
                    </ListItemSecondaryAction>
                }
            </ListItem>
        );
    };

    const displayAuthorities = () => {
        let authorityFields = [];
        if (authorities.length === 0) {
            authorityFields.push(buildAuthorityField(0, ''));
        } else {
            authorities.forEach((value, index) => authorityFields.push(buildAuthorityField(index, value)));
        }
        return authorityFields;
    };

    const jwtEnabled = (index) => {
        let jwtsTemp = [...jwts];
        jwtsTemp[index].enabled = !jwtsTemp[index].enabled;
        setJwts(jwtsTemp);
    };

    const addJwt = (jwt) => {
        setJwts([...jwts, jwt]);
    }

    const validateForm = () => {
        let valErrors = {...defaultErrors};
        // jwtExpiry should not be after effectiveTo date
        if (!code) {
            valErrors.flags.code = true;
        }
        if (!description) {
            valErrors.flags.description = true;
        }
        if (domains.length === 0) {
            valErrors.flags.domains = true;
            valErrors.messages.domainsMsg = 'Please specify at least one domain for this form.';
        } else {
            domains.forEach((domain, index) => {
                const validateDomain = (domain) => {
                    if (!domain) { return true; }
                    if (domain === 'localhost') { return false; }
                    return !domain.match(/^[a-z0-9]+([-.][a-z0-9]+)*\.[a-z]{2,}$/i);
                };
                valErrors.arrays.domainList[index] = validateDomain(domain);
            });
            if (Object.keys(valErrors.arrays.domainList).some(x => valErrors.arrays.domainList[x])) {
                valErrors.flags.domains = true;
                valErrors.messages.domainsMsg = 'Please specify a valid value for the highlighted fields, or remove them.';
            }
        }
        if (authorities.length === 0) {
            valErrors.flags.authorities = true;
            valErrors.messages.authoritiesMsg = 'Please specify at least one role for this form.';
        } else {
            authorities.forEach((role, index) => valErrors.arrays.authorityList[index] = !role);
            if (Object.keys(valErrors.arrays.authorityList).some(x => valErrors.arrays.authorityList[x])) {
                valErrors.flags.authorities = true;
                valErrors.messages.authoritiesMsg = 'Please specify a value for the highlighted fields, or remove them.';
            }
        }
        if (jwts.length === 0) {
            valErrors.flags.jwts = true;
            valErrors.messages.jwtsMsg = 'Please create at least one JWT for this form.';
        }
        setErrors(valErrors);
        return !Object.keys(valErrors.flags).some(x => valErrors.flags[x]);
    };

    const formatJwts = () => {
        return jwts.map(jwt => {
            return {
                tokenId: jwt.tokenId,
                notBefore: formatDate(jwt.notBefore),
                expirationTime: formatDate(jwt.expirationTime),
                token: jwt.token,
                enabled: jwt.enabled,
                created: jwt.created !== null ? formatDate(jwt.created) : null,
                lastUpdated: jwt.lastUpdated !== null ? formatDate(jwt.lastUpdated) : null
            }
        });
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        if (!validateForm()) {
            setMessageDetails({
                message: 'Please correct the highlighted errors and resubmit the form.',
                msgType: 'error'
            });
            setDisplayMessage(true);
            return;
        }
        let data = {
            code: code,
            description: description,
            effectiveFrom: formatDate(effectiveFrom),
            effectiveTo: formatDate(effectiveTo),
            domains: domains,
            authorities: authorities.map(item => authorityList.find(auth => auth.authority === item)),
            jwts: formatJwts()
        };
        const reqUrl = createMode ? `${FORM_MANAGEMENT_URL}/${company}` : `${FORM_MANAGEMENT_URL}/${company}/form/${uuid}`;
        let axiosConfig = {
            method: createMode ? 'post' : 'put',
            data: data,
            withCredentials: true,
            validateStatus: function(status) { return status < 300; }
        };
        Axios(reqUrl, axiosConfig)
        .then(response => {
            const responseStatus = response.status;
            if (responseStatus === 200 || responseStatus === 201) {
                populateForm(response.data);
                setMessageDetails({
                    message: `The form was ${createMode ? 'created' : 'updated'} successfully.`,
                    msgType: 'success'
                });
                if (createMode) {
                    history.push(`/forms/${company}/${response.data.uuid}`);
                }
            } else {
                setMessageDetails({
                    message: 'An error occurred.',
                    msgType: 'error'
                });
            }
            setDisplayMessage(true);
        })
        .catch(err => {
            setMessageDetails({
                message: 'An error occurred.',
                msgType: 'error'
            });
            setDisplayMessage(true);
        });
    };

    const handleCancel = (event) => {
        event.preventDefault();
        history.push(`/forms?company=${company}`);
    }

    const pageHeading  = `${createMode ? 'Add new form' : `Edit form ${code}`} for ${company}`;

    if (displayMessage) {
        messageDetails.closeAction = messageDisplayed;
    }

    return (
        <Container fixed className={classes.container}>
            <form>
                <Grid container direction="row" justify="center">
                    <Typography component="h1" variant="h5">{pageHeading}</Typography>
                </Grid>
                {
                    !createMode && (
                        <Grid container direction="column" justify="flex-start">
                            <Grid container direction="row">
                                <Grid item xs={1}>&nbsp;</Grid>
                            </Grid>
                            <Grid container direction="row">
                                <Grid item xs={2}>
                                    <Typography component="h1" variant="body1"><strong>UUID:</strong></Typography>
                                </Grid>
                                <Grid item xs={1}>&nbsp;</Grid>
                                <Grid item>
                                    <Typography component="h1" variant="body1">{uuid}</Typography>
                                </Grid>
                            </Grid>
                        </Grid>
                    )
                }
                <Tooltip
                    arrow
                    placement="bottom"
                    title="The unique identifier for this form for this company."
                >
                    <TextField
                        required
                        error={errors.flags.code}
                        fullWidth
                        id="code"
                        variant="outlined"
                        label="Code"
                        margin="normal"
                        value={code}
                        onChange={(event) => setCode(event.target.value)}
                        className={classes.textField}
                    />
                </Tooltip>
                <Tooltip
                    arrow
                    placement="bottom"
                    title="The description of the form."
                >
                    <TextField
                        required
                        error={errors.flags.description}
                        fullWidth
                        id="description"
                        label="Description"
                        variant="outlined"
                        multiline
                        rows="4"
                        value={description}
                        onChange={(event) => {setDescription(event.target.value)}}
                        className={classes.textField}
                    />
                </Tooltip>
                <Grid container direction="column">
                    <Grid container direction="row">
                        <Tooltip
                            arrow
                            placement="bottom"
                            title="Check this box to if you wish to specify a date from which the form is available."
                        >
                            <Grid item xs={4}>
                                <FormControlLabel
                                    control ={
                                        <Checkbox
                                            edge="start"
                                            checked={useEffectiveFrom}
                                            color="primary"
                                            onChange={() => setUseEffectiveFrom(!useEffectiveFrom)}
                                        />
                                    }
                                    label="Use Effective From Date"
                                />
                            </Grid>
                        </Tooltip>
                        <Tooltip
                            arrow
                            placement="right"
                            title="Use the date picker to choose the effective from date for the form."
                        >
                            <Grid item xs={7}>
                                { useEffectiveFrom &&
                                <PtsDateTimePicker
                                    value={effectiveFrom}
                                    onChange={setEffectiveFrom}
                                    inputVariant="outlined"
                                    error={errors.flags.effectiveFrom}
                                    id="effectiveFrom"
                                    label="Effective From Date"
                                    ampm={false}
                                    format={DATEPICKER_FMT}
                                    showTodayButton
                                    className={classes.datePicker}
                                    InputProps={{
                                        endAdornment: (
                                            <InputAdornment position="end">
                                                <IconButton>
                                                    <EventAvailableIcon />
                                                </IconButton>
                                            </InputAdornment>
                                        )
                                    }}
                                />}
                            </Grid>
                        </Tooltip>
                    </Grid>
                    <Grid container direction="row">
                        <Tooltip
                            arrow
                            placement="bottom"
                            title="Check this box to if you wish to specify a date until which the form is available."
                        >
                            <Grid item xs={4}>
                                <FormControlLabel
                                    control ={
                                        <Checkbox
                                            edge="start"
                                            checked={useEffectiveTo}
                                            color="primary"
                                            onChange={() => setUseEffectiveTo(!useEffectiveTo)}
                                        />
                                    }
                                    label="Use Effective To Date"
                                />
                            </Grid>
                        </Tooltip>
                        <Tooltip
                            arrow
                            placement="bottom"
                            title="Use the date picker to choose the effective to date for the form."
                        >
                            <Grid item xs={7}>
                                { useEffectiveTo &&
                                <PtsDateTimePicker
                                    value={effectiveTo}
                                    minDate={effectiveFrom}
                                    onChange={setEffectiveTo}
                                    inputVariant="outlined"
                                    error={errors.flags.effectiveTo}
                                    id="effectiveTo"
                                    label="Effective To Date"
                                    ampm={false}
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
                                />}
                            </Grid>
                        </Tooltip>
                    </Grid>
                </Grid>
                <Box border={1} mt={2} mb={2} className={classes.box}>
                    <List
                        className={classes.list}
                        aria-label="Domains"
                        subheader={
                            <ListSubHeader>
                                <Grid container direction="column">
                                    <Grid container direction="row">
                                        <Typography variant="subtitle1">Permitted Domains</Typography>
                                    </Grid>
                                    <Grid container direction="row">
                                        <Typography variant="caption">
                                            Add the domain(s) that the HTML pages that use this form will be hosted on.
                                            The protocol prefix (http/https) is not required.<br />
                                            For example: email.ymnewsolutions.com.<br />This is equivalent to a
                                            Subscription URL in Yesmail 360.
                                        </Typography>
                                    </Grid>
                                </Grid>
                            </ListSubHeader>
                        }
                    >
                        { errors.flags.domains &&
                            <Grid container direction="row">
                                <Typography variant="caption" color="error" className={classes.errMsg}>
                                    {errors.messages.domainsMsg}
                                </Typography>
                            </Grid>
                        }
                        {displayDomains()}
                        <ListItem key="add">
                            <IconButton
                                edge="start"
                                aria-label="add-domain"
                                onClick={onAddDomainClicked}
                            >
                                <AddCircleIcon />
                            </IconButton>
                        </ListItem>
                    </List>
                </Box>
                <Box border={1} mt={2} className={classes.box}>
                    <List
                        className={classes.list}
                        aria-label="Roles"
                        subheader={
                            <ListSubHeader>
                                <Grid container direction="column">
                                    <Grid container direction="row">
                                        <Typography variant="subtitle1">App Permissions</Typography>
                                    </Grid>
                                    <Grid container direction="row">
                                        <Typography variant="caption">
                                            Select the permissions that this app is granted.  If an app attempts to use a microservice
                                            endpoint for which it does not have the required permission the request will be rejected
                                            with a 401 Unauthorized error.
                                        </Typography>
                                    </Grid>
                                </Grid>
                            </ListSubHeader>
                        }
                    >
                        { errors.flags.authorities &&
                            <Grid container direction="row">
                                <Typography variant="caption" color="error" classNAme={classes.errMsg}>
                                    {errors.messages.authoritiesMsg}
                                </Typography>
                            </Grid>

                        }
                        {displayAuthorities()}
                        { authorities.length < authorityList.length &&
                            <ListItem key="add">
                                <IconButton
                                    edge="start"
                                    aria-label="add-role"
                                    onClick={onAddAuthorityClicked}
                                >
                                    <AddCircleIcon />
                                </IconButton>
                            </ListItem>
                        }
                    </List>
                </Box>
                <JwtList
                    jwts={jwts}
                    minDate={effectiveFrom}
                    maxDate={effectiveTo}
                    addJwt={addJwt}
                    jwtEnabled={jwtEnabled}
                />
                <Button
                    type="submit"
                    variant="contained"
                    color="primary"
                    onClick={(event) => handleSubmit(event)}
                >
                    {submitLabel}
                </Button>
                <Button
                    variant="contained"
                    color="primary"
                    onClick={(event) => handleCancel(event)}
                >CANCEL</Button>
            </form>
            {
                displayMessage &&
                <MessageBar messageDetails={messageDetails} />
            }
        </Container>
    );
};
