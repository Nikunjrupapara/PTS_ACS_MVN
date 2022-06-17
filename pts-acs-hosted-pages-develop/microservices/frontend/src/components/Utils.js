const ACCESS_TOKEN_NAME = "accessToken";

// For local development using a local npm server uncomment this line and comment out the default setting for BASE_URL
//const BASE_URL = "http://localhost:3000";
const BASE_URL = "";

const ACS_METADATA_URL = `${BASE_URL}/api/acs`;

const CREDENTIAL_MANAGEMENT_URL = `${BASE_URL}/api/credentials`;

const CUSTOMERID_URL = `${BASE_URL}/api/customerId`;

const FORM_MANAGEMENT_URL = `${BASE_URL}/api/formConfigs`;

const HASH_FUNCTIONS = [ 
    {
        display: 'MD5',
        value: 'md5'
     }, {
         display: 'SHA256',
         value: 'sha256'
    }, {
        display: 'SHA512',
        value: 'sha512'
    }
];

const LOCALE = Intl.DateTimeFormat().resolvedOptions().locale;
const DATE_TIME_FMT = new Intl.DateTimeFormat(LOCALE, {
    year: "numeric",
    month: "long",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
    second: "2-digit"
});

const DATEPICKER_FMT="dd MMMM yyyy HH:mm";

const formatDate = (dateVal) => {
    return dateVal.toISOString().replace('T',' ');
    //return dateVal.toISOString().slice(0,-5)+"Z";
}

export {
    ACCESS_TOKEN_NAME,
    ACS_METADATA_URL,
    BASE_URL,
    CREDENTIAL_MANAGEMENT_URL,
    CUSTOMERID_URL,
    DATE_TIME_FMT,
    DATEPICKER_FMT,
    FORM_MANAGEMENT_URL,
    formatDate,
    HASH_FUNCTIONS,
    LOCALE
};
