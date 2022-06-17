import React from 'react';

import { makeStyles } from '@material-ui/core/styles';

import FormControl from '@material-ui/core/FormControl';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import Select from '@material-ui/core/Select';

const useStyles = makeStyles({
    formControl: {
        backgroundColor: 'white'
    }
});

export default function AuthoritySelect(props) {
    const classes = useStyles();

    const menuItems = () => {
        let menuItems = [];
        props.availableAuthorities.forEach(val =>
            menuItems.push(<MenuItem key={val.authority} value={val.authority}>{val.description}</MenuItem>));
        return menuItems;
    };

    const onChange = (event, index) => {
        const authority = event.target.value;
        props.selectAction(index, authority);
    };

    const labelId = `selectedAuthorityLabel-${props.index}`;
    const selectId = `selectAuthorityField-${props.index}`;

    return (
        <FormControl
            className={classes.formControl}
            style={{ minWidth: props.minWidth, maxWidth: props.maxWidth }}
            error={props.error}
        >
            <InputLabel id={labelId}>Select a permission</InputLabel>
            <Select
                id={selectId}
                labelId={labelId}
                value={props.selectedAuthority}
                onChange={(event) => onChange(event, props.index)}
            >
                {menuItems()}
            </Select>
        </FormControl>
    );
};
