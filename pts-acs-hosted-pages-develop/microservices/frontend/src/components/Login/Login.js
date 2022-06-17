import React, {useState} from 'react';
import { Redirect } from 'react-router-dom';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Box from '@material-ui/core/Box';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import Copyright from "../Copyright"
import Axios from 'axios';
import { BASE_URL } from '../Utils';
import { useAuthContext } from '../../context/AuthContext';



const useStyles = makeStyles(theme => ({
    '@global': {
        body: {
            backgroundColor: theme.palette.common.white,
        },
    },
    paper: {
        marginTop: theme.spacing(8),
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center'
    },
    avatar: {
        margin: theme.spacing(1)
    },
    form: {
        width: '100%', // Fix IE 11 issue.
        marginTop: theme.spacing(1),
    },
    submit: {
        margin: theme.spacing(3, 0, 2)
    },
    error: {
        color: theme.palette.error.dark
    }
}));

export default function SignIn(props) {
    const classes = useStyles();
    const [userName, setUserName] = useState("");
    const [password, setPassword] = useState("");
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [isError, setIsError] = useState(false);
    const { userHasAuthenticated } = useAuthContext();
    let referer;
    try {
        referer = props.location.state.referer.pathname;
    } catch (e) {
        referer = '/';
    }

    const handleLogin = (event) => {
        event.preventDefault();
        console.log("username: " + userName + ", password: " + password);
        if (password && userName) {
            console.log("Authenticating...");
            let data = {
                "usernameOrEmail": userName,
                "password": password
            };
            let config = {
                headers: {
                    "Content-Type": "application/json"
                }
            }
            Axios.post(`${BASE_URL}/authenticate`, data, config)
            .then(res => {
                if (res.status === 200) {
                    userHasAuthenticated(true);
                    setIsAuthenticated(true);
                } else {
                    setIsError(true);
                }
            })
            .catch(err => {
                setIsError(true);
            });
        }
        console.log("After if check...");
    }

    const validateForm = () => {
        return userName.length > 0 && password.length > 0;
    }

    if (isAuthenticated) {
        return <Redirect to={referer} />;
    }

    return (
        <Container maxWidth="xs">
            <Avatar className={classes.avatar}>
                <LockOutlinedIcon />
            </Avatar>
            <Typography component="h1" variant="h5">
                Log in
            </Typography>
            <form className={classes.form} onSubmit={handleLogin}>
                <TextField
                    variant="outlined"
                    margin="normal"
                    required
                    fullWidth
                    id="username"
                    label="Username"
                    name="username"
                    autoComplete="username"
                    onChange= {(event)=>{
                        setUserName(event.target.value);
                    }}
                    autoFocus
                />
                <TextField
                    variant="outlined"
                    margin="normal"
                    required
                    fullWidth
                    name="password"
                    label="Password"
                    type="password"
                    id="password"
                    onChange= {(event)=>{
                        setPassword(event.target.value);
                    }}
                    autoComplete="current-password"
                />
                {
                    isError &&
                    <div className={classes.error}>
                        <Typography >Invalid username or password!</Typography>
                    </div>
                }
                <Button
                    type="submit"
                    disabled={!validateForm()}
                    fullWidth
                    variant="contained"
                    className={classes.submit}
                >
                    Log In
                </Button>
            </form>
            <Box mt={8}>
                <Copyright />
            </Box>
        </Container>
    );
}
