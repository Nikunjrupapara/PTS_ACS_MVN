import React, { Component } from "react";
import { Grid} from "@material-ui/core";

import CircularProgress from '@material-ui/core/CircularProgress';

export default function LoadingSpinner(props) {
    const { loading } = props;
    return (
        loading && 
        <Grid container spacing={0} direction="row" justify="center" alignItems="center">
            <Grid item xs={12}>
                <CircularProgress />
            </Grid>
        </Grid>
    );

};
