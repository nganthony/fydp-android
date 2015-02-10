package com.uwaterloo.fydp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

public class HttpRequest {
    /**
     * Sends an HTTP GET request to the specified URL
     * @param url
     * @return
     * @throws IOException
     */
    public static String sendGet(String url) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        HttpResponse response = client.execute(request);

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }

    /**
     * Sends an HTTP POST request to the specified URL with JSON formatted data
     * @param url
     * @param jsonObject
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String sendJsonPost(String url, Object jsonObject) throws ClientProtocolException, IOException {
        HttpClient client = new DefaultHttpClient();
        Gson gson = new Gson();

        HttpPost post = new HttpPost(url);
        post.addHeader("Content-Type", "application/json");

        String json = gson.toJson(jsonObject);
        post.setEntity(new StringEntity(json, "UTF8"));

        HttpResponse response = client.execute(post);

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }

    /**
     * Sends an HTTP POST request to the specified URL with URL encoded formatted data
     * @param url
     * @param urlParameters
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String sendUrlEncodedPost(String url, List<NameValuePair> urlParameters) throws ClientProtocolException, IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }
}
