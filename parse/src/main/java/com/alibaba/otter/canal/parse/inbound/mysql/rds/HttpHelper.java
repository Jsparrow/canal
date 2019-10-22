package com.alibaba.otter.canal.parse.inbound.mysql.rds;

import static org.apache.http.client.config.RequestConfig.custom;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class HttpHelper {

    protected static final Logger logger = LoggerFactory.getLogger(HttpHelper.class);

    public static byte[] getBytes(String url, int timeout) throws Exception {
        long start = System.currentTimeMillis();
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setMaxConnPerRoute(50);
        builder.setMaxConnTotal(100);
        CloseableHttpClient httpclient = builder.build();
        URI uri = new URIBuilder(url).build();
        RequestConfig config = custom().setConnectTimeout(timeout)
            .setConnectionRequestTimeout(timeout)
            .setSocketTimeout(timeout)
            .build();
        HttpGet httpGet = new HttpGet(uri);
        HttpClientContext context = HttpClientContext.create();
        context.setRequestConfig(config);
        CloseableHttpResponse response = httpclient.execute(httpGet, context);
        try {
            int statusCode = response.getStatusLine().getStatusCode();
            long end = System.currentTimeMillis();
            long cost = end - start;
            if (logger.isWarnEnabled()) {
                logger.warn(new StringBuilder().append("post ").append(url).append(", cost : ").append(cost).toString());
            }
            if (statusCode == HttpStatus.SC_OK) {
                return EntityUtils.toByteArray(response.getEntity());
            } else {
                String errorMsg = EntityUtils.toString(response.getEntity());
                throw new RuntimeException(new StringBuilder().append("requestGet remote error, url=").append(uri.toString()).append(", code=").append(statusCode).append(", error msg=").append(errorMsg)
						.toString());
            }
        } finally {
            response.close();
            httpGet.releaseConnection();
        }
    }

    public static String get(String url, int timeout) {
        // logger.info("get url is :" + url);
        // 支持采用https协议，忽略证书
        url = url.trim();
        if (url.startsWith("https")) {
            return getIgnoreCerf(url, null, null, timeout);
        }
        long start = System.currentTimeMillis();
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setMaxConnPerRoute(50);
        builder.setMaxConnTotal(100);
        CloseableHttpClient httpclient = builder.build();
        CloseableHttpResponse response = null;
        HttpGet httpGet = null;
        try {
            URI uri = new URIBuilder(url).build();
            RequestConfig config = custom().setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout)
                .build();
            httpGet = new HttpGet(uri);
            HttpClientContext context = HttpClientContext.create();
            context.setRequestConfig(config);
            response = httpclient.execute(httpGet, context);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity());
            } else {
                String errorMsg = EntityUtils.toString(response.getEntity());
                throw new RuntimeException(new StringBuilder().append("requestGet remote error, url=").append(uri.toString()).append(", code=").append(statusCode).append(", error msg=").append(errorMsg)
						.toString());
            }
        } catch (Throwable t) {
            long end = System.currentTimeMillis();
            long cost = end - start;
            String curlRequest = getCurlRequest(url, null, null, cost);
            throw new RuntimeException("requestGet remote error, request : " + curlRequest, t);
        } finally {
            long end = System.currentTimeMillis();
            long cost = end - start;
            printCurlRequest(url, null, null, cost);
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
					logger.error(e.getMessage(), e);
                }
            }
            httpGet.releaseConnection();
        }
    }

    private static String getIgnoreCerf(String url, CookieStore cookieStore, Map<String, String> params, int timeout) {
        long start = System.currentTimeMillis();
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setMaxConnPerRoute(50);
        builder.setMaxConnTotal(100);
        HttpGet httpGet = null;
        CloseableHttpResponse response = null;
        try {
            // 创建支持忽略证书的https
            final SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (X509Certificate[] x509Certificates, String s) -> true).build();

            CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setSSLContext(sslContext)
                .setConnectionManager(new PoolingHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory> create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE))
                    .build()))
                .build();

            // ---------------- 创建支持https 的client成功---------

            URI uri = new URIBuilder(url).build();
            RequestConfig config = custom().setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout)
                .build();
            httpGet = new HttpGet(uri);
            HttpClientContext context = HttpClientContext.create();
            context.setRequestConfig(config);
            response = httpClient.execute(httpGet, context);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity());
            } else {
                String errorMsg = EntityUtils.toString(response.getEntity());
                throw new RuntimeException(new StringBuilder().append("requestGet remote error, url=").append(uri.toString()).append(", code=").append(statusCode).append(", error msg=").append(errorMsg)
						.toString());
            }
        } catch (Throwable t) {
            long end = System.currentTimeMillis();
            long cost = end - start;
            String curlRequest = getCurlRequest(url, cookieStore, params, cost);
            throw new RuntimeException("requestPost(Https) remote error, request : " + curlRequest, t);
        } finally {
            long end = System.currentTimeMillis();
            long cost = end - start;
            printCurlRequest(url, null, null, cost);
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
					logger.error(e.getMessage(), e);
                }
            }
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
    }

    private static String postIgnoreCerf(String url, CookieStore cookieStore, Map<String, String> params, int timeout) {
        long start = System.currentTimeMillis();
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setMaxConnPerRoute(50);
        builder.setMaxConnTotal(100);
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        try {
            // 创建支持忽略证书的https
            final SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (X509Certificate[] x509Certificates, String s) -> true).build();

            CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setSSLContext(sslContext)
                .setConnectionManager(new PoolingHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory> create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE))
                    .build()))
                .build();
            // ---------------- 创建支持https 的client成功---------

            URI uri = new URIBuilder(url).build();
            RequestConfig config = custom().setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout)
                .build();
            httpPost = new HttpPost(uri);
            List<NameValuePair> parameters = Lists.newArrayList();
            params.keySet().stream().map(key -> new BasicNameValuePair(key, params.get(key))).forEach(parameters::add);
            httpPost.setEntity(new UrlEncodedFormEntity(parameters, Charset.forName("UTF-8")));
            HttpClientContext context = HttpClientContext.create();
            context.setRequestConfig(config);
            context.setCookieStore(cookieStore);

            response = httpClient.execute(httpPost, context);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                long end = System.currentTimeMillis();
                long cost = end - start;
                printCurlRequest(url, cookieStore, params, cost);
                return EntityUtils.toString(response.getEntity());
            } else {
                long end = System.currentTimeMillis();
                long cost = end - start;
                String curlRequest = getCurlRequest(url, cookieStore, params, cost);
                throw new RuntimeException(new StringBuilder().append("requestPost(Https) remote error, request : ").append(curlRequest).append(", statusCode=").append(statusCode).append("").toString());
            }
        } catch (Throwable t) {
            long end = System.currentTimeMillis();
            long cost = end - start;
            String curlRequest = getCurlRequest(url, cookieStore, params, cost);
            throw new RuntimeException("requestPost(Https) remote error, request : " + curlRequest, t);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
					logger.error(e.getMessage(), e);
                }
            }
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
        }
    }

    public static String post(String url, CookieStore cookieStore, Map<String, String> params, int timeout) {
        url = url.trim();
        // 支持采用https协议，忽略证书
        if (url.startsWith("https")) {
            return postIgnoreCerf(url, cookieStore, params, timeout);
        }
        long start = System.currentTimeMillis();
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setMaxConnPerRoute(50);
        builder.setMaxConnTotal(100);
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient httpclient = builder.build();
            URI uri = new URIBuilder(url).build();
            RequestConfig config = custom().setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout)
                .build();
            httpPost = new HttpPost(uri);
            List<NameValuePair> parameters = Lists.newArrayList();
            params.keySet().stream().map(key -> new BasicNameValuePair(key, params.get(key))).forEach(parameters::add);
            httpPost.setEntity(new UrlEncodedFormEntity(parameters, Charset.forName("UTF-8")));
            HttpClientContext context = HttpClientContext.create();
            context.setRequestConfig(config);
            context.setCookieStore(cookieStore);

            response = httpclient.execute(httpPost, context);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                long end = System.currentTimeMillis();
                long cost = end - start;
                printCurlRequest(url, cookieStore, params, cost);
                return EntityUtils.toString(response.getEntity());
            } else {
                long end = System.currentTimeMillis();
                long cost = end - start;
                String curlRequest = getCurlRequest(url, cookieStore, params, cost);
                throw new RuntimeException(new StringBuilder().append("requestPost remote error, request : ").append(curlRequest).append(", statusCode=").append(statusCode).append(";").append(EntityUtils.toString(response.getEntity()))
						.toString());
            }
        } catch (Throwable t) {
            long end = System.currentTimeMillis();
            long cost = end - start;
            String curlRequest = getCurlRequest(url, cookieStore, params, cost);
            throw new RuntimeException("requestPost remote error, request : " + curlRequest, t);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
					logger.error(e.getMessage(), e);
                }
            }
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
        }
    }

    public static void printCurlRequest(String url, CookieStore cookieStore, Map<String, String> params, long cost) {
        logger.warn(getCurlRequest(url, cookieStore, params, cost));
    }

    private static String getCurlRequest(String url, CookieStore cookieStore, Map<String, String> params, long cost) {
        if (params == null) {
            return new StringBuilder().append("curl '").append(url).append("'\ncost : ").append(cost).toString();
        } else {
            StringBuilder paramsStr = new StringBuilder();
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                paramsStr.append(new StringBuilder().append(entry.getKey()).append("=").append(entry.getValue()).toString());
                if (iterator.hasNext()) {
                    paramsStr.append("&");
                }
            }
            if (cookieStore == null) {
                return new StringBuilder().append("curl '").append(url).append("' -d '").append(paramsStr.toString()).append("'\ncost : ").append(cost)
						.toString();
            } else {
                StringBuilder cookieStr = new StringBuilder();
                List<Cookie> cookies = cookieStore.getCookies();
                Iterator<Cookie> iteratorCookie = cookies.iterator();
                while (iteratorCookie.hasNext()) {
                    Cookie cookie = iteratorCookie.next();
                    cookieStr.append(new StringBuilder().append(cookie.getName()).append("=").append(cookie.getValue()).toString());
                    if (iteratorCookie.hasNext()) {
                        cookieStr.append(";");
                    }
                }
                return new StringBuilder().append("curl '").append(url).append("' -b '").append(cookieStr).append("' -d '").append(paramsStr.toString())
						.append("'\ncost : ").append(cost).toString();
            }
        }
    }
}
