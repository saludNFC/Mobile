package app.example.veuge.com.saludnfc;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by veuge on 28-05-16.
 */
public class GetAsyncTask extends AsyncTask<Void, Void, String> {

    public String url; // http://192.168.1.164:8000/
    public String path; // api/paciente
                        // api/paciente/xxx-123456
                        // api/paciente/xxx-123456/antecedentes
                        // api/paciente/xxx-123456/antecedentes/1
                        // api/paciente/xxx-123456/controles
                        // api/paciente/xxx-123456/controles/1
                        // api/paciente/xxx-123456/consultas
                        // api/paciente/xxx-123456/consultas/2
    public String token;

    public GetAsyncTask(String urlParam, String pathParam, String tokenParam){
        this.url = urlParam;
        this.path = pathParam;
        this.token = tokenParam;
    }

    public HttpClient httpClient;
    public HttpResponse httpResponse;
    public HttpGet httpGet;
    public HttpEntity httpEntity;

    public InputStream inputStream;

    public String response;
    public String message;
    public int responseCode;

    @Override
    protected String doInBackground(Void... params) {
        httpClient = new DefaultHttpClient();
        httpGet = new HttpGet(url + path);
        httpGet.setHeader("Authorization", "Bearer " + token);
        response = "";

        try{
            httpResponse = httpClient.execute(httpGet);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();

            httpEntity = httpResponse.getEntity();
            if(httpEntity != null){
                inputStream = httpEntity.getContent();
                response = convertStreamToString(inputStream);
                inputStream.close();
                return response;
            }
        }
        catch (ClientProtocolException e){
            httpClient.getConnectionManager().shutdown();
            e.printStackTrace();
            return "";
        }
        catch (IOException e){
            httpClient.getConnectionManager().shutdown();
            e.printStackTrace();
            return "";
        }

        return null;
    }

    public String convertStreamToString(InputStream instream){
        BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                instream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
