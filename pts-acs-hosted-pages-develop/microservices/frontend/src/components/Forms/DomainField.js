import React from 'react';

import { makeStyles } from '@material-ui/core/styles';

import IconButton from '@material-ui/core/IconButton';
import ListItem from '@material-ui/core/ListItem'
import ListItemSecondaryAction from '@material-ui/core/ListItemSecondaryAction';
import TextField from '@material-ui/core/TextField';
import Tooltip from '@material-ui/core/Tooltip';

import DeleteIcon from '@material-ui/icons/Delete';

const useStyles = makeStyles({
    textField: {
        margin: 5,
        spacing: 5,
        width: 450
    }
});

export default function DomainField(props) {
    const classes = useStyles();
    const onChange = (event, index) => {
        const domain = event.target.value;
        props.onDomainChange(index, domain);
    };

    const onDeleteDomainClicked = (index) => {
        console.log(`Deleting index ${index}, domain: ${props.domain}`);
        props.deleteDomain(index);
    };

    const key=`item-${props.index}`;

    return (
        <ListItem key={key}>
            <Tooltip
                arrow
                placement="bottom"
                title="Enter a domain upon which pages for this form will be hosted."
            >
                <TextField
                    required
                    error={props.error}
                    value={props.domain}
                    fullWidth
                    id="domain"
                    label="Domain"
                    variant="outlined"
                    onChange={(event) => onChange(event, props.index)}
                    className={classes.textField}
                />
            </Tooltip>
            { props.displayDeleteButton &&
                <ListItemSecondaryAction>
                    <IconButton
                        edge="end"
                        aria-label="delete"
                        onClick={() => onDeleteDomainClicked(props.index)}
                    >
                        <DeleteIcon />
                    </IconButton>
                </ListItemSecondaryAction>
            }
        </ListItem>
    );
};
