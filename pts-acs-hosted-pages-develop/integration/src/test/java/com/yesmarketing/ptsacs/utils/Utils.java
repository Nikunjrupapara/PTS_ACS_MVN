package com.yesmarketing.ptsacs.utils;

import com.yesmarketing.ptsacs.model.TestResponse;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.CloseableHttpResponse;

import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Utils {

    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

    public static final String ARG_BASE_URL = "test.hostname";
    public static final String DEFAULT_BASE_URL = "http://localhost:8080";

    public static String getBaseUrl() {
        return System.getProperty(ARG_BASE_URL, DEFAULT_BASE_URL);
    }

    public static TestResponse submitGetRequest(String uri, Header[] headers) throws Exception {
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeaders(headers);
        return submitRequest(httpGet);
    }

    public static TestResponse submitPostRequest(String uri, Header[] headers, String body) throws Exception {
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeaders(headers);

        StringEntity searchFieldsEntity = new StringEntity(body);
        httpPost.setEntity(searchFieldsEntity);

        LOG.info("POST BODY: {}", body);
        return submitRequest(httpPost);
    }

    public static TestResponse submitPutRequest(String uri, Header[] headers, String body) throws Exception {
        HttpPut httpPut = new HttpPut(uri);
        httpPut.setHeaders(headers);

        StringEntity putRequestEntity = new StringEntity(body);
        httpPut.setEntity(putRequestEntity);

        LOG.info("PUT BODY: {}", body);
        return submitRequest(httpPut);
    }

    private static TestResponse submitRequest(HttpRequestBase request) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        LOG.info("URI: {}", request.getURI());
        LOG.info("REQUEST: {}",request);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            StatusLine statusLine = response.getStatusLine();
            int status = statusLine.getStatusCode();
            Header[] responseHeaders = response.getAllHeaders();
            HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity, "UTF-8");
            TestResponse testResponse = new TestResponse(status, body, responseHeaders);
            LOG.info("Response: {}", testResponse);
            return testResponse;
        }
    }

    public static Header[] defaultHeaders() {
        List<Header> headers = new LinkedList<>();
        headers.add(new BasicHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE));
        return headers.toArray(new Header[]{});
    }

    public static Header[] defaultPostHeaders() {
        List<Header> headers = new LinkedList<>();
        headers.add(new BasicHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE));
        headers.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
        return headers.toArray(new Header[]{});
    }

    public static Header[] appendDefaultPutHeaders(List<Header> reqHeaders) {
        reqHeaders.add(new BasicHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE));
        reqHeaders.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
        reqHeaders.add(new BasicHeader("company", "ymnewsolutions"));
        return reqHeaders.toArray(new Header[]{});
    }

    public static Header[] appendDefaultPostHeaders(List<Header> reqHeaders) {
        reqHeaders.add(new BasicHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE));
        reqHeaders.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
        reqHeaders.add(new BasicHeader("company", "ymnewsolutions"));
        return reqHeaders.toArray(new Header[]{});
    }


    private Utils() {

    }
}
