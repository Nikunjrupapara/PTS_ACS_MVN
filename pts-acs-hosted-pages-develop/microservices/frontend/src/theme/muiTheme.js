import { createMuiTheme } from '@material-ui/core/styles';

const theme = createMuiTheme({
    props: {
        MuiPaper: {
            display: 'flex',
            overflow: 'auto',
            flexDirection: 'column',
            elevation: 0,
            square: true
        }
    },
    root: {
        display: 'flex'
    },
    spacing: 8,
    typography: {
        useNextVariants: true
    },
    palette : {
        primary: {
            light:'#b73c07',
            main: '#ed4d07',
            dark: '#fa6b07',
            contrastText: '#fff',
            background: 'white'
        }
    },
    overrides: {
        MuiAvatar: {
            root: {
                background: 'linear-gradient(to top,#b73c07 0,#ed4d07 50%,#fa6b07 100%)'
            }
        },
        MuiButton: {
            root: {
                margin: '16px',
                background: 'linear-gradient(to top,#b73c07 0,#ed4d07 50%,#fa6b07 100%)'
            }
        },
        MuiContainer: {
            root: {
                alignItems: 'center',
                display: 'flex',
                flexDirection: 'column'
            }
        },
        MuiGrid: {
            root: {
                alignItems: 'center'
            }
        },
        /*MuiPickersToolbarButton: {
            toolbarBtn: {
                margin: '0px',
                color: 'theme.palette.main'
            }
        },*/
        MuiTableCell: {
            head: {
                fontWeight: 'bold'
            }
        },
        MuiTooltip: {
            tooltip: {
                fontSize: '.8em',
                color: 'orange',
                backgroundColor: 'black',
                maxWidth: 500
            }
        }
    }
});

export default theme;
