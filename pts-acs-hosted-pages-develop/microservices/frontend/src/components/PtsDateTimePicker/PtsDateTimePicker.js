import React from 'react';

import DateFnsUtils from '@date-io/date-fns';

import { createMuiTheme, makeStyles, ThemeProvider } from '@material-ui/core/styles';

import { DateTimePicker, MuiPickersUtilsProvider } from '@material-ui/pickers';

const dateTimePickerMuiTheme = createMuiTheme({
    palette : {
        primary: {
            light:'#b73c07',
            main: '#ed4d07',
            dark: '#fa6b07',
            contrastText: '#fff',
            background: 'white'
        }
    }
});

const useStyles = makeStyles({
    datePicker: {
        margin: 10,
        spacing: 10,
        minWidth: 300
    }
});

export default function PtsDateTimePicker(props) {
    const classes = useStyles();
    const {
        value,
        onChange,
        variant,
        inputVariant,
        error,
        id,
        label,
        ampm,
        format,
        showTodayButton,
        InputProps
    } = props;
    return (
        <ThemeProvider theme={dateTimePickerMuiTheme}>
            <MuiPickersUtilsProvider utils={DateFnsUtils}>
                <DateTimePicker
                    variant={variant}
                    value={value}
                    onChange={onChange}
                    inputVariant={inputVariant}
                    error={error}
                    id={id}
                    label={label}
                    ampm={ampm}
                    format={format}
                    showTodayButton={showTodayButton}
                    InputProps={InputProps}
                    className={classes.datePicker}
                />
            </MuiPickersUtilsProvider>
        </ThemeProvider>
    );
};
