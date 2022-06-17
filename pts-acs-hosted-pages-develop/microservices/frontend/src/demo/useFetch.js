import { useState, useEffect } from "react";
import Axios from 'axios';

export default function useFetch(url) {
    const [ data, setData ] = useState([]);

    async function getData() {
        const response = await Axios.get(url, { withCredentials: true });
        const data = await response.data;
        setData(data);
    }

    useEffect(() => {
        getData();
    }, []);

    return data;
}