import React, { useState, useEffect } from 'react';
import { DragDropContext, Draggable, Droppable } from 'react-beautiful-dnd';
import Axios from 'axios';
import AcsFieldSelect from './AcsFieldSelect';
import ListCompanies from '../ListCompanies/ListCompanies';
import LoadingSpinner from '../LoadingSpinner/LoadingSpinner';
import MessageBar from '../MessageBar/MessageBar';
import { ACS_METADATA_URL, CUSTOMERID_URL, HASH_FUNCTIONS } from '../Utils';

import { makeStyles } from '@material-ui/core/styles';
import AddCircleIcon from '@material-ui/icons/AddCircle';
import Box from '@material-ui/core/Box';
import Button from '@material-ui/core/Button';
import Checkbox from '@material-ui/core/Checkbox';
import Container from '@material-ui/core/Container';
import DeleteIcon from '@material-ui/icons/Delete';
import FormControl from '@material-ui/core/FormControl';
import Grid from '@material-ui/core/Grid';
import IconButton from '@material-ui/core/IconButton';
import InputLabel from '@material-ui/core/InputLabel';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemSecondaryAction from '@material-ui/core/ListItemSecondaryAction';
import ListSubHeader from '@material-ui/core/ListSubheader';
import MenuItem from '@material-ui/core/MenuItem';
import Select from '@material-ui/core/Select';
import TextField from '@material-ui/core/TextField';
import Tooltip from '@material-ui/core/Tooltip';
import Typography from '@material-ui/core/Typography';

const useStyles = makeStyles({
    container: {
        maxWidth: '700px'
    },
    textField: {
        margin: 10,
        spacing: 10,
        width: 500
    },
    box: {
        borderRadius: '5px',
        width: '500px'
    },
    errMsg: {
        paddingLeft: '16px',
        marginTop: '8px'
    }
});

export default function CompanyIdConfig(props) {
    const classes = useStyles();
    const [companySelected, setCompanySelected] = useState(false);
    const [company, setCompany] = useState('');
    const [loading, setLoading] = useState(false);
    const [selectedFields, setSelectedFields] = useState([]);
    const [delimiter, setDelimiter] = useState('');
    const [displayDelimiter, setDisplayDelimiter] = useState(false);
    const [hashFunction, setHashFunction] = useState('');
    const [allFieldNames, setAllFieldNames] = useState([]);

    const defaultErrors = {
        fields: false,
        fieldsMsg: '',
        delimiter: false,
        hashFunction: false
    };

    const fieldsToExclude = [
        'PKey',
        'acsId',
        'cusCustomerIdHash',
        'geoUnit',
        'location',
        'orgUnit',
        'postalAddress',
        'title'
    ];

    const defaultField = {
        name: '',
        required: true
    };

    const [errors, setErrors] = useState(defaultErrors);
    const [messageDetails, setMessageDetails] = useState({});
    const [displayMessage, setDisplayMessage] = useState(false);

    const onCompanySelect = (companyArg) => {
        setCompany(companyArg);
        setCompanySelected(true);
    }

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            await getCompanyData();
        }
        console.log(`useEffect load data for company ${company}`);
        if (!companySelected) {
            return;
        }
        fetchData();
    }, [company]);

    useEffect(() => {
        setDisplayDelimiter(selectedFields && selectedFields.length > 1);
    }, [selectedFields]);

    const messageDisplayed = () => { setDisplayMessage(false); };

    const getCompanyData = async () => {
        console.log(`getCompanyData for ${company}`);
        Promise.all([
            getCompanyMetadata(),
            getCompanyIdConfig()
        ])
        .then(results => {
            // results[0] is for company metadata
            let result = results[0];
            if (!result.success) {
                setMessageDetails({
                    message: result.message,
                    msgType: 'error'
                });
                return;
            }
            let fieldNames = [];
            const acsProfileResponse = result.data.content;
            Object.keys(acsProfileResponse)
            .map(name => acsProfileResponse[name])
            .filter(fieldObj => fieldObj.resType !== 'link')
            .filter(fieldObj => !fieldsToExclude.includes(fieldObj.apiName))
            .forEach(fieldObj => fieldNames.push(fieldObj.apiName));
            setAllFieldNames(fieldNames);
    
            // result[1] is for company Id config
            result = results[1];
            if (!result.success) {
                setMessageDetails({
                    message: result.message,
                    msgType: 'error'
                });
                setSelectedFields([]);
                setDelimiter("");
                setHashFunction("");
                return;
            }
            const companyIdConfigResponse = result.data;
            setSelectedFields(companyIdConfigResponse.fields);
            setDelimiter(companyIdConfigResponse.delimiter);
            setHashFunction(companyIdConfigResponse.hashFunction);
        })
        .finally(() => setLoading(false));
    }

    const getCompanyMetadata = async () => {
        const reqUrl = `${ACS_METADATA_URL}/${company}/profile`;
        console.log(`getCompanyMetadata: ${reqUrl}`);
        return Axios.get(reqUrl, { withCredentials: true })
        .then(result => {
            const data = result.data;
            return new Promise((res, rej) => {
                res({
                    success: true,
                    data: data,
                    message: ''
                });
            })
        })
        .catch(err => {
            return new Promise((res, rej) => {
                res({
                    success: false,
                    data: {},
                    message: err.response.data.message
                });
            });
        });
    };

    const getCompanyIdConfig = async () => {
        const reqUrl = `${CUSTOMERID_URL}/${company}`;
        return Axios.get(reqUrl, { withCredentials: true})
        .then(result => {
            const data = result.data;
            return new Promise((res, rej) => {
                res({
                    success: true,
                    data: data,
                    message: ''
                });
            })
        })
        .catch(err => {
            return new Promise((res, rej) => {
                res({
                    success: false,
                    data: {},
                    message: err.response.data.message
                });
            });
        });
    };

    const onFieldSelected = (index, fieldName) => {
        const selectedFieldsTmp = [...selectedFields];
        if (selectedFieldsTmp[index]){
            selectedFieldsTmp[index].name = fieldName;
        } else {
            selectedFieldsTmp.push({
                name: fieldName,
                required: true
            });
        }
        setSelectedFields(selectedFieldsTmp);
    };

    const onRequiredClicked = (index) => {
        let selectedFieldsTmp = [...selectedFields];
        selectedFieldsTmp[index].required = !selectedFieldsTmp[index].required;
        setSelectedFields(selectedFieldsTmp);
    }

    const onAddFieldClicked = (event) => {
        const newField = {...defaultField};
        setSelectedFields([...selectedFields, newField]);
    };

    const onDeleteFieldClicked = (index) => {
        let arr = [...selectedFields];
        arr.splice(index, 1);
        setSelectedFields([...arr]);
    };

    const buildFieldSelect = (index, selectedObj) => {
        // exclude already selected fields, apart from the selected value for this select field,
        // from this select.
        const selectedIndex = selectedFields.indexOf(selectedObj);
        let selectedFieldsTmp = [...selectedFields];
        const selectedFieldNamesTmp = selectedFieldsTmp.map(field => field.name);
        selectedFieldNamesTmp.splice(selectedIndex, 1);
        let allowedFields = allFieldNames.filter(field => !selectedFieldNamesTmp.includes(field));
        return (
            <Draggable draggableId={selectedObj.name} index={index} key={selectedObj.name}>
                {(provided) => (
                    <ListItem
                        ref={provided.innerRef}
                        {...provided.draggableProps}
                        {...provided.dragHandleProps}
                        key={selectedObj.name}
                    >
                        <ListItemIcon>
                            <Tooltip
                                arrow
                                placement="bottom"
                                title="Check this box if the field is mandatory in the Customer Id, otherwise uncheck it."
                            >
                                <Checkbox
                                    edge="start"
                                    checked={selectedObj.required}
                                    color="primary"
                                    onChange={() => onRequiredClicked(index)}
                                />
                            </Tooltip>
                        </ListItemIcon>
                        <AcsFieldSelect
                            index={index}
                            selectedFieldName={selectedObj.name}
                            fieldNames={allowedFields}
                            selectedFieldNames={selectedFieldNamesTmp}
                            selectAction={onFieldSelected}
                            minWidth={370}
                            maxWidth={370}
                        />
                        { selectedFields.length > 1 &&
                            <ListItemSecondaryAction>
                                <IconButton
                                    edge="end"
                                    aria-label="delete"
                                    onClick={() => onDeleteFieldClicked(index)}
                                >
                                    <DeleteIcon />
                                </IconButton>
                            </ListItemSecondaryAction>
                        }
                    </ListItem>
                )}
            </Draggable>
        );
}

    const displayFieldSelects = () => {
        let fieldSelects = [];
        if (selectedFields.length === 0) {
            fieldSelects.push(buildFieldSelect(0, {...defaultField}));
        } else {
            selectedFields.forEach((field, index) => fieldSelects.push(buildFieldSelect(index, field)));
        }
        fieldSelects.push(
            <ListItem key="add">
                <IconButton
                    edge="start"
                    aria-label="add-field"
                    onClick={onAddFieldClicked}
                >
                    <AddCircleIcon />
                </IconButton>
            </ListItem>);
        return fieldSelects;
    };

    const onDragEnd = (result) => {
        console.log(`onDragEnd: result=${JSON.stringify(result)}`);
        const { source, destination, draggableId } = result;
        // item dragged outside droppable area - do nothing
        if (!destination) {
            return;
        }

        // item dropped at its starting position - do nothing
        if (
            destination.droppableId === source.droppableId &&
            destination.index === source.index
        ) {
            return;
        }

        const newSelectedFields = [...selectedFields];
        newSelectedFields.splice(source.index, 1);
        newSelectedFields.splice(destination.index, 0, draggableId);
        setSelectedFields(newSelectedFields);
    }

    const validateForm = () => {
        let valErrors = {...defaultErrors};
        if (selectedFields.length === 0) {
            valErrors.fields = true;
            valErrors.fieldsMsg = 'Please select at least one field to comprise the Customer Id.';
        } else  {
            const emptyNames = selectedFields.filter(val => val.name.length === 0);
            if (emptyNames.length > 0) {
                valErrors.fields = true;
                valErrors.fieldsMsg = 'Please select a value for each field drop down, or delete the empty field(s).';
            } else {
                if (selectedFields.length > 1) {
                    const ind = selectedFields.findIndex(field => field.name === 'cusCustomerId');
                    if (ind > -1) {
                        valErrors.fields = true;
                        valErrors.fieldsMsg = 'Field "cusCustomerId" cannot be part of a composite Customer Id definition.';
                    }
                }
            }
        }
        valErrors.delimiter = selectedFields.length > 1 && !delimiter;
        valErrors.hashFunction = !hashFunction;
        setErrors(valErrors);
        return !Object.keys(valErrors).some(x => valErrors[x]);
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        if (!validateForm()) {
            setMessageDetails({
                message: 'Please correct the highlighted validation errors and resubmit the form.',
                msgType: 'error'
            });
            setDisplayMessage(true);
            return;
        }
        const reqUrl = `${CUSTOMERID_URL}/${company}`;
        const axiosConfig = {
            method: 'put',
            data: {
                fields: selectedFields,
                delimiter: delimiter,
                hashFunction: hashFunction
            },
            withCredentials: true,
            validateStatus: function(status) {
                return status < 300;
            }
        };
        Axios(reqUrl, axiosConfig)
        .then(resp => {
            setSelectedFields(resp.data.fields);
            setDelimiter(resp.data.delimiter);
            setHashFunction(resp.data.hashFunction);
            setMessageDetails({
                message: 'The update was successful.',
                msgType: 'success'
            });
        })
        .catch(err => {
            setMessageDetails({
                message: 'An error occurred',
                msgType: 'error'
            });
        })
        .finally(() => setDisplayMessage(true));
    };

    if (displayMessage) {
        messageDetails.closeAction = messageDisplayed;
    }

    return (
        <Container fixed className={classes.container}>
            <form>
                <Grid container direction="row" justify="center">
                    <Typography component="h1" variant="h5">Configure Company Id</Typography>
                </Grid>
                <ListCompanies maxWidth="700" selectAction={onCompanySelect} />
                <LoadingSpinner loading={loading} />
                { companySelected && !loading &&
                    <Grid container direction="column">
                        <Grid container direction="row">
                            <Box border={1} mt={2} mb={2} className={classes.box}>
                                <DragDropContext onDragEnd={onDragEnd}>
                                    <Droppable droppableId="custIdFields">
                                        {(provided) => (
                                            <List
                                                className={classes.list}
                                                aria-label="Customer Id Fields"
                                                subheader={
                                                    <ListSubHeader>
                                                        <Grid container direction="column">
                                                            <Grid container direction="row">
                                                                <Typography variant="subtitle1">Customer Id Fields</Typography>
                                                            </Grid>
                                                            <Grid container direction="row">
                                                                <Typography variant="caption">
                                                                    Choose the ACS fields that comprise the Customer Id
                                                                    for the company.  Use the '+' button to add additional
                                                                    fields.  You can use drag and drop to reorder the
                                                                    selected fields if required.  The checkbox indicates
                                                                    if the field is mandatory in the Customer Id or not.
                                                                </Typography>
                                                            </Grid>
                                                        </Grid>
                                                    </ListSubHeader>
                                                }
                                                {...provided.droppableProps}
                                                ref={provided.innerRef}
                                            >
                                                { errors.fields && 
                                                    <Grid container direction="row">
                                                        <Typography variant="caption" color="error" className={classes.errMsg}>
                                                            {errors.fieldsMsg}
                                                        </Typography>
                                                    </Grid>
                                                }
                                                {displayFieldSelects()}
                                                {provided.placeholder}
                                            </List>
                                        )}
                                    </Droppable>
                                </DragDropContext>
                            </Box>
                        </Grid>
                        <Grid container direction="column">
                            { displayDelimiter &&
                                <Grid container direction="row">
                                    <Tooltip
                                        arrow
                                        placement="bottom"
                                        title="The character used to delimit the fields in the Customer Id string"
                                    >
                                        <TextField
                                            id="delimiter"
                                            label="Delimiter"
                                            variant="outlined"
                                            margin="normal"
                                            fullWidth
                                            error={errors.delimiter}
                                            value={delimiter}
                                            onChange={(event) => setDelimiter(event.target.value)}
                                            className={classes.textField}
                                        />
                                    </Tooltip>
                                </Grid>
                            }
                            <Grid container direction="row">
                                <Tooltip
                                    arrow
                                    placement="bottom"
                                    title="The function to use to create a Hash of the Customer Id."
                                >
                                    <FormControl className={classes.textField}>
                                        <InputLabel id="selectHashFunction-label">Select a Hash Function</InputLabel>
                                        <Select
                                            id="selectHashFunction"
                                            labelId="selectHashFunction-label"
                                            value={hashFunction}
                                            onChange={(event) => setHashFunction(event.target.value)}
                                            error={errors.hashFunction}
                                        >
                                            {
                                                HASH_FUNCTIONS.map(obj => <MenuItem key={obj.value} value={obj.value}>{obj.display}</MenuItem>)
                                            }
                                        </Select>
                                    </FormControl>
                                </Tooltip>
                            </Grid>
                            <Grid container direction="row">
                                <Button
                                    variant="contained"
                                    color="primary"
                                    onClick={(event) => handleSubmit(event)}
                                >
                                    SUBMIT
                                </Button>
                            </Grid>
                        </Grid>
                    </Grid>
                }
            </form>
            {
                displayMessage &&
                <MessageBar messageDetails={messageDetails} />
            }
</Container>
    );
}
