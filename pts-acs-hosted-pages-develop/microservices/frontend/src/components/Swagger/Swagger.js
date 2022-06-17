import React from 'react';
import SwaggerUI from 'swagger-ui-react';
import "swagger-ui-react/swagger-ui.css"
import { BASE_URL } from '../Utils';

export default function Swagger() {
    const url=`${BASE_URL}/v3/api-docs`;
    return <SwaggerUI url={url} />
}
