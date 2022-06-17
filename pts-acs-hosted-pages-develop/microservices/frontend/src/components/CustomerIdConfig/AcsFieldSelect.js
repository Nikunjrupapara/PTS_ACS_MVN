import React, { useState, useEffect } from 'react';

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

export default function AcsFieldSelect(props) {
    const classes = useStyles();
    const [selectedFieldName, setSelectedFieldName] = useState(props.selectedFieldName);

    useEffect(() => { setSelectedFieldName(props.selectedFieldName); }, [props]);

    const menuItems = () => {
        let menuItems = [];
        props.fieldNames.forEach(field => menuItems.push(<MenuItem key={field} value={field}>{field}</MenuItem>));
        return menuItems;
    }

    const onChange = (event, index) => {
        const fieldName = event.target.value;
        setSelectedFieldName(fieldName);
        props.selectAction(index, fieldName);
    }

    const labelId=`selectAcsFieldLabel-${props.index}`;
    const selectId = `selectAcsField-${props.index}`;

    return (
        <FormControl className={classes.formControl} style={{ minWidth: props.minWidth, maxWidth: props.maxWidth }}>
            <InputLabel id={labelId}>Select a field</InputLabel>
            <Select
                id={selectId}
                labelId={labelId}
                value={selectedFieldName}
                onChange={(event) => onChange(event, props.index)}
            >
            {menuItems()}
            </Select>
        </FormControl>
    );
};
