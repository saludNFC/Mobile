package app.example.veuge.com.saludnfc.network;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by veuge on 29-05-16.
 */
public class PostAsyncTask extends AsyncTask<List<NameValuePair>, Void, String> {
    /**
     * TODO
     * Error handling!!!!
     */

    public String url;
    public String path;
    public String token;

    public PostAsyncTask(String urlParam, String pathParam, String tokenParam){
        this.url = urlParam;
        this.path = pathParam;
        this.token = tokenParam;
    }

    public HttpClient httpClient;
    public HttpPost httpPost;
    public HttpResponse httpResponse;
    public HttpEntity httpEntity;

    public InputStream inputStream;

    public String response;
    public String message;
    public int responseCode;

    @Override
    protected String doInBackground(List... params){
        httpClient = new DefaultHttpClient();
        httpPost = new HttpPost(this.url + this.path);
        if(token != ""){
            httpPost.setHeader("Authorization", "Bearer " + token);
        }
        response = "";

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params[0]));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params[0]));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            httpResponse = httpClient.execute(httpPost);
            // write response to log
            Log.d("Http Post Response:", httpResponse.toString());
            response = EntityUtils.toString(httpResponse.getEntity());
            return response;
        } catch (ClientProtocolException e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            return errors.toString();
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
        }
        return null;
    }
}
