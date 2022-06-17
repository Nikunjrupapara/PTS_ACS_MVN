import React, { useState, useEffect } from 'react';
import Snackbar from '@material-ui/core/Snackbar';
import MuiAlert from '@material-ui/lab/Alert';
import IconButton from '@material-ui/core/IconButton';
import CloseIcon from '@material-ui/icons/Close';

function Alert(props) {
    return <MuiAlert elevation={6} variant="filled" {...props} />;
}

export default function MessageBar(props) {
    const [message, setMessage] = useState(null);
    const [autoHideDuration, setAutoHideDuration] = useState(6000);
    const [msgType, setMsgType] = useState('info');

    const handleClose = (event, reason) => {
        props.messageDetails.closeAction();
    };

    console.log(`MessageBar: props=${JSON.stringify(props)}`);

    const setProperties = () => {
        const messageDetails = props.messageDetails || {};
        setMessage(messageDetails.message || null);
        setAutoHideDuration(messageDetails.autoHideDuration || 6000);
        setMsgType(messageDetails.msgType || 'info');
    };

    useEffect(setProperties, []);

    console.log(`MessageBar: message=${message}, msgType=${msgType}, autoHideDuration=${autoHideDuration}`);
    return (
        <Snackbar
            open={message !== null}
            autoHideDuration={autoHideDuration}
            onClose={handleClose}
            anchorOrigin={{
                vertical: 'bottom',
                horizontal: 'left'
            }}
        >
            <Alert
                severity={msgType}
                action={
                    <IconButton
                        aria-label="close"
                        color="inherit"
                        size="small"
                        onClick={handleClose}
                    >
                        <CloseIcon fontSize="inherit" />
                    </IconButton>
                }
            >
                {message}
            </Alert>
        </Snackbar>
    );
};
