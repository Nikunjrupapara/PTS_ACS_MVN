import React, { useState, useEffect } from 'react';
import Axios from 'axios';

import { makeStyles } from '@material-ui/core/styles';
import FormControl from '@material-ui/core/FormControl';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import Select from '@material-ui/core/Select';

import { CREDENTIAL_MANAGEMENT_URL } from '../Utils';
import { Typography } from '@material-ui/core';

const useStyles = makeStyles({
    formControl: {
        minWidth: props => props.minWidth || 500,
        maxWidth: props => props.maxWidth || 700
    }
});

export default function ListCompanies(props) {
    const classes = useStyles();
    const [companies, setCompanies] = useState([]);
    const [company, setCompany] = useState('');
    const [displaySelect, setDisplaySelect] = useState(false);
    const [message, setMessage] = useState('');

    const getCompanies = async() => {
        Axios.get(CREDENTIAL_MANAGEMENT_URL, { withCredentials: true})
        .then(response => {
            const companies = response.data.map(row => row.company);
            if (companies.length > 0) {
                setCompanies(companies);
                setDisplaySelect(true);
            } else {
                setDisplaySelect(false);
                setMessage("No companies were found!");
            }
        })
        .catch(err => {
            console.log(JSON.stringify(err));
            if (err.response.status === 401) {
                setMessage("You must log in as a user with the Admin role to use this page.");
            } else {
                setMessage("An error occurred!");
            }
            setDisplaySelect(false);
        });
    };

    const menuItems = () => {
        let menuItems = [];
        companies.forEach(item => menuItems.push(<MenuItem key={item} value={item}>{item}</MenuItem>));
        return menuItems;
    };

    const onChange = (event) => {
        const company = event.target.value;
        setCompany(company);
        props.selectAction(company);
    }

    useEffect(() => { getCompanies() }, []);

    useEffect(() => {
        if (props.company) {
            setCompany(props.company);
        }
    }, [props.company]);

    return (
        <>
        {
            displaySelect && 
            <FormControl className={classes.formControl}>
                <InputLabel id="selectCompanyLabel">Select a company</InputLabel>
                <Select
                    id="selectCompany"
                    labelId="selectCompanyLabel"
                    value={company}
                    onChange={onChange}
                >
                {menuItems()}
                </Select>
            </FormControl>
        }
        {
            !displaySelect &&
            <Typography>{message}</Typography>
        }
        </>
    );
}
