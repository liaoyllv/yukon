package com.lyl.yukon.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>http客户端工具类</p>
 *
 * @author liaoyl
 * @version 1.0 2018/10/24 09:37
 **/
public class HttpClientUtils {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    private static CloseableHttpClient httpClient;
    private static final String CHARSET = "UTF-8";

    // 初始化超时时间配置，再根据配置生成默认httpClient对象
    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    public static String doGet(String url, Map<String, String> params) throws IOException {
        return doGet(url, params, CHARSET);
    }

    public static String doGetSSL(String url, Map<String, String> params) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return doGetSSL(url, params, CHARSET);
    }

    public static String doPost(String url, Map<String, String> params) throws IOException {
        return doPostJson(url, params, CHARSET);
    }

    /**
     * HTTP Get 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     */
    public static String doGet(String url, Map<String, String> params, String charset) throws IOException {
        if (StringUtils.isBlank(url)) {
            throw new RuntimeException("url is null");
        }
        url = getUrlString(url, params, charset);
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            httpGet.abort();
            throw new RuntimeException("HttpClient,error status code :" + statusCode);
        }
        HttpEntity entity = response.getEntity();
        String result = null;
        if (entity != null) {
            result = EntityUtils.toString(entity, "utf-8");
        }
        EntityUtils.consume(entity);
        response.close();
        return result;
    }

    /**
     * 获取urlString
     */
    public static String getUrlString(String url, Map<String, String> params, String charset) throws IOException {
        if (params != null && !params.isEmpty()) {
            List<NameValuePair> pairs = new ArrayList<>(params.size());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String value = entry.getValue();
                if (value != null) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), value));
                }
            }
            // 将请求参数和url进行拼接
            url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
        }
        return url;
    }

    /**
     * HTTP Post 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @throws IOException
     */
    public static String doPostForm(String url, Map<String, String> params, String charset)
            throws IOException {
        if (StringUtils.isBlank(url)) {
            throw new RuntimeException("url is null");
        }
        List<NameValuePair> pairs = null;
        if (params != null && !params.isEmpty()) {
            pairs = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String value = entry.getValue();
                if (value != null) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), value));
                }
            }
        }
        HttpPost httpPost = new HttpPost(url);
        if (pairs != null && pairs.size() > 0) {
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, charset));
        }
        CloseableHttpResponse response;
        response = httpClient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            httpPost.abort();
            throw new RuntimeException("HttpClient,error status code :" + statusCode);
        }
        HttpEntity entity = response.getEntity();
        String result = null;
        if (entity != null) {
            result = EntityUtils.toString(entity, "utf-8");
        }
        EntityUtils.consume(entity);
        response.close();
        return result;
    }

    /**
     * HTTP Post 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @throws IOException
     */
    public static String doPostJson(String url, Map<String, String> params, String charset)
            throws IOException {
        if (StringUtils.isBlank(url)) {
            throw new RuntimeException("url is null");
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        // 创建Http Post请求
        HttpPost httpPost = new HttpPost(url);
        // 创建请求内容
        StringEntity entity = new StringEntity(JsonUtils.toJson(params), ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);
        // 执行http请求
        response = httpClient.execute(httpPost);
        String res = EntityUtils.toString(response.getEntity(), "utf-8");
        response.close();
        return res;
    }


    /**
     * HTTPS Get 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     */
    public static String doGetSSL(String url, Map<String, String> params, String charset) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        if (StringUtils.isBlank(url)) {
            throw new RuntimeException("url is null");
        }
        url = getUrlString(url, params, charset);
        HttpGet httpGet = new HttpGet(url);

        // https  注意这里获取https内容，使用了忽略证书的方式，当然还有其他的方式来获取https内容
        CloseableHttpClient httpsClient = HttpClientUtils.createSSLClientDefault();
        CloseableHttpResponse response = httpsClient.execute(httpGet);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            httpGet.abort();
            throw new RuntimeException("HttpClient,error status code :" + statusCode);
        }
        HttpEntity entity = response.getEntity();
        String result = null;
        if (entity != null) {
            result = EntityUtils.toString(entity, "utf-8");
        }
        EntityUtils.consume(entity);
        response.close();
        return result;
    }

    /**
     * HTTPS Post 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     * @throws IOException
     */
    public static String doPostSSL(String url, Map<String, String> params, String charset)
            throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        if (StringUtils.isBlank(url)) {
            throw new RuntimeException("url is null");
        }
        List<NameValuePair> pairs = null;
        if (params != null && !params.isEmpty()) {
            pairs = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String value = entry.getValue();
                if (value != null) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), value));
                }
            }
        }
        HttpPost httpPost = new HttpPost(url);
        if (pairs != null && pairs.size() > 0) {
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, charset));
        }
        CloseableHttpResponse response;
        response = HttpClientUtils.createSSLClientDefault().execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            httpPost.abort();
            throw new RuntimeException("HttpClient,error status code :" + statusCode);
        }
        HttpEntity entity = response.getEntity();
        String result = null;
        if (entity != null) {
            result = EntityUtils.toString(entity, "utf-8");
        }
        EntityUtils.consume(entity);
        response.close();
        return result;
    }

    /**
     * 这里创建了忽略整数验证的CloseableHttpClient对象
     */
    private static CloseableHttpClient createSSLClientDefault() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        // 信任所有
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (TrustStrategy) (chain, authType) -> true).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
        return HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }


}
