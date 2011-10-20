package com.foobnix.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.foobnix.util.LOG;

/**
 * @author iivanenko
 * 
 */
public class RequestHelper {
	protected final String apiUrl;
	protected DefaultHttpClient client;
	private CookieStore cookieStore;
	private BasicNameValuePair defaultParam;

	public RequestHelper(String apiUrl) {
		cookieStore = new BasicCookieStore();
		client = createNewClient();
		this.apiUrl = apiUrl;
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

	public void setCookie(Cookie cookie) {
		client.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2109);

		client.setCookieStore(cookieStore);
		if (!cookieStore.getCookies().contains(cookie)) {
			LOG.d("Set Cookie");
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
			LOG.d("GET reqUrl", reqUrl);
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
		LOG.d("GET reqUrl", reqUrl);
		request = new HttpGet(reqUrl);

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
				LOG.e("POST exception", e);
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
			LOG.d("Http Response", strResponse);
		} catch (ClientProtocolException e) {
			LOG.e(e);
		} catch (IOException e) {
			LOG.e(e);
		} catch (Exception e) {
			LOG.e(e);
		}

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
