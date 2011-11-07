package com.foobnix.commons.http;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * @author iivanenko
 * 
 */
public class RequestHelper {
    private Log log = LogFactory.getLog(RequestHelper.class);

    protected String apiUrl;
    protected DefaultHttpClient client;
    private CookieStore cookieStore;
    private BasicNameValuePair defaultParam;

    public RequestHelper(String apiUrl) {
        cookieStore = new BasicCookieStore();
        client = createNewClient();
        this.apiUrl = apiUrl;
    }

    public RequestHelper() {
        cookieStore = new BasicCookieStore();
        client = createNewClient();
    }

    public DefaultHttpClient createNewClient() {
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, HTTP.UTF_8);
        params.setParameter(CoreProtocolPNames.USER_AGENT, "Apache-HttpClient/Android");
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);
        params.setParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false);
        return new DefaultHttpClient(params);
    }

    public String upload(String method, final ByteArrayOutputStream file, String fileName, ProgressListener lisner) {
        HttpPost request = null;

        CountingMultipartRequestEntity mpEntity = new CountingMultipartRequestEntity(lisner);
        ContentBody cbFile = new ByteArrayBody(file.toByteArray(), "image/jpg", fileName);

        mpEntity.addPart("paramName1", cbFile);

        try {
            mpEntity.addPart("paramName2", new StringBody(fileName, Charset.forName("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        request = new HttpPost(apiUrl + method);
        request.setEntity(mpEntity);

        return httpRequest(request);

    }

    public void setCookie(Cookie cookie) {
        client.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BEST_MATCH);

        client.setCookieStore(cookieStore);
        if (!cookieStore.getCookies().contains(cookie)) {
            cookieStore.addCookie(cookie);
        }
    }

    public List<Cookie> getCookies() {
        return client.getCookieStore().getCookies();
    }

    public String getContent(String url) {
        HttpGet request = new HttpGet(url);
        return httpRequest(request);
    }

    public String get(String method, NameValuePair... params) {
        return get(method, Arrays.asList(params));
    }

    public String get(String method, List<NameValuePair> params) {
        HttpGet request = null;
        if (params != null) {

            if (defaultParam != null) {
                params = new ArrayList<NameValuePair>(params);
                params.add(defaultParam);
            }

            String paramsList = URLEncodedUtils.format(params, "UTF-8");
            String reqUrl = apiUrl + method + "?" + paramsList;
            request = new HttpGet(reqUrl);
        } else {
            request = new HttpGet(apiUrl + method);
        }

        return httpRequest(request);
    }

    public String get(List<BasicNameValuePair> params) {
        HttpGet request = null;

        if (defaultParam != null) {
            params = new ArrayList<BasicNameValuePair>(params);
            params.add(defaultParam);
        }

        String paramsList = URLEncodedUtils.format(params, "UTF-8");
        String reqUrl = apiUrl + "?" + paramsList;
        request = new HttpGet(reqUrl);

        return httpRequest(request);
    }

    public String getPostUrl(String url) {
        HttpPost request = new HttpPost(url);
        log.debug("getPostUrl" + url);
        return httpRequest(request);
    }

    public String post(String method, NameValuePair... params) {
        HttpPost request = null;
        if (params != null) {
            String url = apiUrl + method;// + "?" + paramsList;
            request = new HttpPost(url);
            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(Arrays.asList(params), "UTF-8");
                request.setEntity(entity);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            request = new HttpPost(apiUrl + method);
        }
        return httpRequest(request);
    }

    private synchronized String httpRequest(HttpRequestBase request) {
        client = null;
        client = createNewClient();
        String strResponse = "";
        try {
            HttpResponse response = client.execute(request);

            HttpEntity entity = response.getEntity();
            strResponse = EntityUtils.toString(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.debug("Http Response" + strResponse);
        return strResponse;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setDefaultParam(BasicNameValuePair defaultParam) {
        this.defaultParam = defaultParam;
    }

    public BasicNameValuePair getDefaultParam() {
        return defaultParam;
    }

}
